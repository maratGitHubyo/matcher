package ru.sloggers.matcher.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.sloggers.matcher.repositories.MeteringDeviceJpaRepository;
import ru.sloggers.matcher.services.client.RecognitionClient;
import ru.sloggers.matcher.services.client.dto.RecognitionResponse;
import ru.sloggers.matcher.services.minio.MinioService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.*;
import static java.lang.String.format;
import static java.nio.file.Files.*;
import static java.nio.file.Files.list;
import static java.nio.file.Files.walk;
import static java.util.Objects.nonNull;

/**
 * Сервис по поиску логических пар счетчиков(новый/старый)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoFolderScanner {

    private static final String WALK_ERROR_MSG = "Ошибка обхода директории c корнем: [%s]";
    private static final String FOLDER_STRUCTURE_ERROR_MSG = "Нарушена требуемая структура папок, папки \"Фото старого ПУ\", \"Фото нового ПУ\" не найдены";
    private static final String MISSING_PHOTO_ERROR_MSG = "Нарушена требуемая структура, в некоторых папках отсутствуют фотографии";
    private static final String RECOGNITION_PHOTO_ERROR_MSG = "Ошибка при распознавании фото: [%s]";
    private static final String REJECTION_PHOTO_ERROR_MSG = "Ошибка отбраковки CV, данные адреса не совпадают с номерами счетчиков, проверьте вручную";

    // TODO : сделать root не через абсолютный адрес
    private static final String ROOT = "D:\\work-station\\projects\\IDEA\\matcher\\src\\main\\resources\\photos";
    private static final Integer PARTS_OF_ADDRESS = 5; // классический адрес по формализации состоит из пяти частей и находится в пути файла
    private static final String WATER_METER_OLD_PHOTOS = "Фото старого ПУ"; // формализованное название папки для хранения фото old, находится по пути полного адреса
    private static final String WATER_METER_NEW_PHOTOS = "Фото нового ПУ"; // формализованное название папки для хранения фото new, находится по пути полного адреса
    private static final String BINDING_FOLDER = "photos";
    // TODO : только ли jpeg?
    private static final String JPG = ".jpg";
    private static final String JPEG = ".jpeg";

    private final RecognitionClient client;
    private final MeteringDeviceService meteringDeviceService;
    private final MinioService minioService;
    private final MeteringDeviceJpaRepository meteringDeviceJpaRepository;

    public void main() {
        var root = Paths.get(ROOT);

        try (var dirsWalker = walk(root)) {
            // Обход всей структуры для указанной директории
            dirsWalker
                .filter(Files::isDirectory)
                .forEach(this::processDirectory);
        } catch (Exception ex) {
            log.warn(format(WALK_ERROR_MSG, ROOT), ex);
        }
    }

    private void processDirectory(Path directory) {
        // Если в ссылке не содержится полного адреса, сразу скипаем
        var address = extractAddress(directory);
        if (address.length != PARTS_OF_ADDRESS) {
            return;
        }

        final var ROOT = directory.toAbsolutePath();
        try (var subDirsWalker = list(directory)) {
            // Файл проверятся на валидность под капотом
            // Собираем все подпапки текущей директории
            var subDirs = subDirsWalker
                .filter(Files::isDirectory)
                .toList();

            if (subDirs.isEmpty()) {
                throw new Exception(format(FOLDER_STRUCTURE_ERROR_MSG, ROOT));
            }

            // Проверяем, содержится ли нужные файлы в собранных подпапках
            Path oldPhotosDir = null;
            Path newPhotosDir = null;
            for (var subDir : subDirs) {
                var name = subDir.getFileName().toString();
                if (name.contains(WATER_METER_OLD_PHOTOS)) {
                    oldPhotosDir = subDir;
                } else if (name.contains(WATER_METER_NEW_PHOTOS)) {
                    newPhotosDir = subDir;
                }
            }

            // Обязательно должны существовать обе папки
            if (nonNull(oldPhotosDir) && nonNull(newPhotosDir)) {

                // Получаем наборы фото для двух счетчиков
                var oldPhotos = getPhotos(oldPhotosDir);
                var newPhotos = getPhotos(newPhotosDir);

                // Обязательно должно существовать какое-то кол-во фото в обеих
                if (!oldPhotos.isEmpty() || !newPhotos.isEmpty()) {

                    // TODO : лог для Марата = проверка работы обхода, остальное внутри блока комментишь
                    log.info("===========================================================");
                    log.info("Директория: " + directory);
                    log.info("Старые фото: " + Arrays.toString(oldPhotos.toArray()));
                    log.info("Новые фото: " + Arrays.toString(newPhotos.toArray()));
                    log.info("===========================================================");

                    /*// TODO : лог для Марата = заглушка для одного адреса + поменяй руты
                    // Отправляем на распознавание
                    //var response = send(oldPhotos, newPhotos);
                    var response = new RecognitionResponse("10028323", "10964490", "");
                    var error = response.errorMessage();
                    if (error.isEmpty()) {

                        // Если ответ без ошибки, отправляем данные на отбраковку CV
                        // Сравниваем полученный адрес из названия папки с распознанными номерами в реестре
                        boolean isConfirmed = isConfirmed(address, response);
                        if (isConfirmed) {
                            // Если брака CV нет, записываем фотографии в хранилище MinIO/БД
                            log.info("СV верно отработал!!!");

                            saveData(ROOT, address, response, oldPhotos, newPhotos);
                        } else {
                            throw new Exception(REJECTION_PHOTO_ERROR_MSG);
                        }
                    } else {
                        throw new Exception(format(RECOGNITION_PHOTO_ERROR_MSG, error));
                    }*/
                } else {
                    throw new Exception(MISSING_PHOTO_ERROR_MSG);
                }
            } else {
                throw new Exception(FOLDER_STRUCTURE_ERROR_MSG);
            }
        } catch (Exception ex) {
            log.warn(format(WALK_ERROR_MSG, ROOT), ex);
        }
    }

    // TODO : проверить все корнер кейсы с неймингом файлов
    private String[] extractAddress(Path directory) {
        var name = directory.toAbsolutePath().toString();
        int bindingIndex = name.indexOf(BINDING_FOLDER);

        var nameWithoutRootPart = name.substring(bindingIndex + BINDING_FOLDER.length());
        if (nameWithoutRootPart.isEmpty()) {
            return new String[0];
        }
        var address = nameWithoutRootPart
            .replaceFirst("^[\\\\/]+", "")
            .split("[/\\\\]+");

        return Arrays.stream(address)
            .map(String::trim)
            .toArray(String[]::new);
    }

    private List<Path> getPhotos(Path directory) throws Exception {
        try (var photos = list(directory)) {
            return photos
                .filter(Files::isRegularFile)
                .filter(path -> {
                    var name = path.getFileName().toString();
                    return name.endsWith(JPG) || name.endsWith(JPEG);
                })
                .toList();
        }
    }

    private RecognitionResponse send(List<Path> oldPhotos, List<Path> newPhotos) {
        var oldPhotosForAPI = oldPhotos.stream()
            .map(path -> (Resource) new FileSystemResource(path.toFile())).toList();
        var newPhotosForAPI = newPhotos.stream()
            .map(path -> (Resource) new FileSystemResource(path.toFile())).toList();

        // TODO : корнер кейсы с ошибкой/пустотой/маппингом фейна
        return client.recognizePhotos(oldPhotosForAPI, newPhotosForAPI);
    }

    private boolean isConfirmed(String[] address, RecognitionResponse response) {
        var oldNumber = response.oldNumber();
        var newNumber = response.newNumber();
        return meteringDeviceService.checkIfExistSetRecognizeFlag(oldNumber, newNumber, address);
    }

    // TODO : корнер кейсы с ошибкой загрузки/сохранения/пустотой
    private void saveData(Path directory,
                          String[] address,
                          RecognitionResponse response,
                          List<Path> oldPhotos,
                          List<Path> newPhotos) throws Exception {

        var addressName = join("_", address);
        var oldNumber = response.oldNumber();
        var newNumber = response.newNumber();
        var meteringDevice = meteringDeviceJpaRepository.getByOldNumberAndNewNumber(oldNumber, newNumber);
        meteringDevice.setCanonicalPhotoPath(directory.toString());

        var count = 0;
        for (var photo : oldPhotos) {
            try (var bytes = newInputStream(photo)) {
                var uniqueName = addressName + "_" + response.oldNumber() + "_V" + count;

                minioService.uploadFile(uniqueName, bytes);
                var renaimingOldPhotos = meteringDevice.getRenaimingOldPhotos();
                meteringDevice.setRenaimingOldPhotos(renaimingOldPhotos + ", " + uniqueName);

                count++;
            }
        }

        count = 0;
        for (var photo : newPhotos) {
            try (var bytes = newInputStream(photo)) {
                var uniqueName = addressName + "_" + response.newNumber() + "_V" + count;

                minioService.uploadFile(uniqueName, bytes);
                var renaimingNewPhotos = meteringDevice.getRenaimingNewPhotos();
                meteringDevice.setRenaimingNewPhotos(renaimingNewPhotos + ", " + uniqueName);

                count++;
            }
        }
    }
}

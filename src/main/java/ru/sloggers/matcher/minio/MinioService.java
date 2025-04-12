package ru.sloggers.matcher.minio;

import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.sloggers.matcher.dto.ItemDto;
import ru.sloggers.matcher.mappers.ItemMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucket-name}")
    private String bucketName;

    private MinioClient minioClient;

    private final ItemMapper itemMapper;

    @PostConstruct
    public void init() {
        minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();

        createBuckets();
    }

    private void createBuckets() {
        try {
            boolean isExist = minioClient.bucketExists(
                    io.minio.BucketExistsArgs.builder().bucket(bucketName).build()
            );

            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Бакет '{}' успешно создан!", bucketName);
            } else {
                log.info("Бакет '{}' уже существует.", bucketName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании бакета в MinIO", e);
        }
    }

    public ObjectWriteResponse uploadFile(String objectName, InputStream inputStream, String contentType) throws MinioException {
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, -1, 10485760)
                    .contentType(contentType)
                    .build();
            return minioClient.putObject(objectArgs);
        } catch (Exception e) {
            throw new MinioException("Ошибка при загрузке файла в MinIO");
        }
    }

    public List<ItemDto> listObjects() {
        List<ItemDto> itemList = new ArrayList<>();
        try {
            ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .recursive(true)
                    .build();
            Iterable<Result<Item>> listObjects = minioClient.listObjects(listObjectsArgs);
            for (Result<Item> result : listObjects) {
                itemList.add(itemMapper.toDto(result.get()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении списка объектов из MinIO", e);
        }
        return itemList;
    }

    public InputStream downloadFile(String objectName) throws MinioException {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            throw new MinioException("Ошибка при скачивании файла из MinIO");
        }
    }
}


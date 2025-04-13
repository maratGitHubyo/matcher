package ru.sloggers.matcher.services;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.sloggers.matcher.entities.MeteringDevice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final MeteringDeviceService meteringDeviceService;

    public static final int CITY_CELL_INDEX = 3;
    public static final int STREET_CELL_INDEX = 4;
    public static final int HOUSE_CELL_INDEX = 5;
    public static final int APARTMENT_CELL_INDEX = 6;
    public static final int OLD_TYPE_CELL_INDEX = 10;
    public static final int OLD_NUMBER_CELL_INDEX = 11;
    public static final int NEW_TYPE_CELL_INDEX = 13;
    public static final int NEW_NUMBER_CELL_INDEX = 14;

    public static final int CAP_END = 9;

    public Integer parseRegistryAndSaveCounters(InputStream inputStream) throws IOException {
        List<MeteringDevice> meteringDevices = parseRegistryFile(inputStream);
        meteringDeviceService.saveAll(meteringDevices);
        return meteringDevices.size();
    }

    // Номера колонок согласно структуре файла (индексация с 0)
    private List<MeteringDevice> parseRegistryFile(InputStream inputStream) throws IOException {


        List<MeteringDevice> countries = new ArrayList<>();

        Workbook workbook = new XSSFWorkbook(inputStream);

        Sheet sheet = workbook.getSheetAt(0); // Первый лист
        for (Row row : sheet) {
            MeteringDevice.MeteringDeviceBuilder communalCounterBuilder = MeteringDevice.builder();
            if (row.getRowNum() < CAP_END) {
                continue;
            }
            for (Cell cell : row) {
                if (cell.getColumnIndex() == CITY_CELL_INDEX) {
                    communalCounterBuilder.city(getStringCellValue(cell));
                } else if (cell.getColumnIndex() == STREET_CELL_INDEX) {
                    communalCounterBuilder.street(getStringCellValue(cell));
                } else if (cell.getColumnIndex() == HOUSE_CELL_INDEX) {
                    communalCounterBuilder.houseNumber(getStringCellValue(cell));
                } else if (cell.getColumnIndex() == APARTMENT_CELL_INDEX) {
                    communalCounterBuilder.apartmentNumber(getStringCellValue(cell));
                } else if (cell.getColumnIndex() == OLD_TYPE_CELL_INDEX) {
                    communalCounterBuilder.oldType(getStringCellValue(cell));
                } else if (cell.getColumnIndex() == OLD_NUMBER_CELL_INDEX) {
                    communalCounterBuilder.oldNumber(getStringCellValue(cell));
                } else if (cell.getColumnIndex() == NEW_TYPE_CELL_INDEX) {
                    communalCounterBuilder.newType(getStringCellValue(cell));
                } else if (cell.getColumnIndex() == NEW_NUMBER_CELL_INDEX) {
                    communalCounterBuilder.newNumber(getStringCellValue(cell));
                }
            }

            MeteringDevice meteringDevice = communalCounterBuilder.build();
            countries.add(meteringDevice);
        }

        inputStream.close();

        return countries;
    }

    @SuppressWarnings("deprecation")
    public static String getStringCellValue(Cell cell) {
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    public byte[] generateMatchingResult(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);

        Sheet sheet = workbook.getSheetAt(0); // Первый лист
        for (Row row : sheet) {
            if (row.getRowNum() < CAP_END) {
                continue;
            }
            Cell oldNumberCell = row.getCell(OLD_NUMBER_CELL_INDEX);
            Cell newNumberCell = row.getCell(NEW_NUMBER_CELL_INDEX);

            boolean isRecognized = meteringDeviceService.existsByOldNumberAndNewNumberAndRecognized(getStringCellValue(oldNumberCell), getStringCellValue(newNumberCell));

            CellStyle style = workbook.createCellStyle();
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            if (isRecognized) {
                style.setFillForegroundColor(IndexedColors.GREEN.getIndex());

            } else {
                style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
            }


            for (Cell cell : row) {
                cell.setCellStyle(style);
            }


        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        byte[] outputStreamByteArray = byteArrayOutputStream.toByteArray();
        inputStream.close();
        return outputStreamByteArray;
    }
}


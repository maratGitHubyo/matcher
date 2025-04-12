package ru.sloggers.matcher.services;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.sloggers.matcher.entities.CommunalCounter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistryParser {

    private final CommunalCountService communalCountService;

    public static final int CAP_END = 9;

    public Integer parse(InputStream inputStream) throws IOException {
        List<CommunalCounter> communalCounters = parseFile(inputStream);
        communalCountService.saveAll(communalCounters);
        return communalCounters.size();
    }

    // Номера колонок согласно структуре файла (индексация с 0)
    private List<CommunalCounter> parseFile(InputStream inputStream) throws IOException {

        int cityCellIndex = 3;
        int streetCellIndex = 4;
        int houseCellIndex = 5;
        int apartmentCellIndex = 6;
        int oldTypeCellIndex = 10;
        int oldNumberCellIndex = 11;
        int newTypeCellIndex = 13;
        int newNumberCellIndex = 14;

        List<CommunalCounter> countries = new ArrayList<>();

        Workbook workbook = new XSSFWorkbook(inputStream);

        Sheet sheet = workbook.getSheetAt(0); // Первый лист
        for (Row row : sheet) {
            CommunalCounter.CommunalCounterBuilder communalCounterBuilder = CommunalCounter.builder();
            if (row.getRowNum() < CAP_END) {
                continue;
            }
            for (Cell cell : row) {
                if (cell.getColumnIndex() == cityCellIndex) {
                    communalCounterBuilder.city(getStringCellValue(cell));
                } else if (cell.getColumnIndex() == streetCellIndex) {
                    communalCounterBuilder.street(getStringCellValue(cell));
                } else if (cell.getColumnIndex() == houseCellIndex) {
                    communalCounterBuilder.houseNumber(getStringCellValue(cell));
                } else if (cell.getColumnIndex() == apartmentCellIndex) {
                    communalCounterBuilder.apartmentNumber(getStringCellValue(cell));
                } else if (cell.getColumnIndex() == oldTypeCellIndex) {
                    communalCounterBuilder.oldType(getStringCellValue(cell));
                } else if (cell.getColumnIndex() == oldNumberCellIndex) {
                    communalCounterBuilder.oldNumber(getStringCellValue(cell));
                } else if (cell.getColumnIndex() == newTypeCellIndex) {
                    communalCounterBuilder.newType(getStringCellValue(cell));
                } else if (cell.getColumnIndex() == newNumberCellIndex) {
                    communalCounterBuilder.newNumber(getStringCellValue(cell));
                }
            }

            CommunalCounter communalCounter = communalCounterBuilder.build();
            countries.add(communalCounter);
        }

        return countries;
    }

    @SuppressWarnings("deprecation")
    public static String getStringCellValue(Cell cell) {
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }
}


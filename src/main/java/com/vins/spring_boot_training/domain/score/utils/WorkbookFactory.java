package com.vins.spring_boot_training.domain.score.utils;

import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.ExportErrors;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class WorkbookFactory {

  private static CellStyle createHeaderStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBold(true);
    style.setFont(font);
    return style;
  }

  private static void addHeaderCell(Row headerRow, int columnIndex, String label, CellStyle headerStyle) {
    Cell cell = headerRow.createCell(columnIndex);
    cell.setCellValue(label);
    cell.setCellStyle(headerStyle);
  }

  private static void setCellValue(Cell cell, Object value) {
    if (value instanceof String string && string.startsWith("=")) {
      cell.setCellFormula(string.substring(1));
    } else if (value instanceof Number number) {
      cell.setCellValue(number.doubleValue());
    } else if (value != null) {
      cell.setCellValue(value.toString());
    }
  }

  private static void autoSizeColumns(Sheet sheet, int columnCount) {
    for (int i = 0; i < columnCount; i++) {
      sheet.autoSizeColumn(i);
    }
  }

  private static byte[] getBytesArray(Workbook workbook) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    workbook.write(outputStream);
    return outputStream.toByteArray();
  }

  public static <T> byte[] create(
      String title,
      List<T> data,
      Map<String, BiFunction<T, Integer, Object>> columnDefinitions) {

    try (Workbook workbook = new XSSFWorkbook()) {
      Sheet sheet = workbook.createSheet(title);
      CellStyle headerStyle = createHeaderStyle(workbook);

      Row headerRow = sheet.createRow(0);
      List<String> columnNames = new ArrayList<>(columnDefinitions.keySet());
      IntStream.range(0, columnNames.size())
          .forEach(i -> addHeaderCell(headerRow, i, columnNames.get(i), headerStyle));

      List<BiFunction<T, Integer, Object>> cellFunctions = new ArrayList<>(columnDefinitions.values());

      IntStream.range(0, data.size()).forEach(dataIndex -> {
        T item = data.get(dataIndex);
        Row row = sheet.createRow(dataIndex + 1);

        IntStream.range(0, cellFunctions.size()).forEach(columnIndex -> {
          BiFunction<T, Integer, Object> cellValueFunction = cellFunctions.get(columnIndex);
          Object value = cellValueFunction.apply(item, dataIndex);
          Cell cell = row.createCell(columnIndex);
          setCellValue(cell, value);
        });
      });

      autoSizeColumns(sheet, columnDefinitions.size());
      return getBytesArray(workbook);

    } catch (IOException e) {
      throw new CustomException(ExportErrors.WORKBOOK_ERROR);
    }
  }
}
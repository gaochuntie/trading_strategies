package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class Main {
    static String excelFilePath = "/home/jackmaxpale/IdeaProjects/MCM_Trading_strategies/src/main/java/org/example/bchain.xlsx";
    static String goldFilePath = "/home/jackmaxpale/IdeaProjects/MCM_Trading_strategies/src/main/java/org/example/gold.xlsx";
    static int bchain_break_point = 1827;
    static int gold_break_point = 1266;
    static DayMarketState head = null, tail = null;

    public static void main(String[] args) {
        loadBchainData();
        loadGoldData();
        printLinkedList(head);

    }

    public static void printLinkedList(DayMarketState head) {
        DayMarketState current = head;
        while (current != null) {
            System.out.println(current.date + " " + current.bchain_real + " " + current.bchain_guess + " " + current.gold_real + " " + current.gold_guess);
            current = current.getNext();
        }
    }

    public static void loadBchainData() {
        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> iterator = sheet.iterator();

            // Skip the header row
            if (iterator.hasNext()) {
                iterator.next();
            }
            if (iterator.hasNext()) {
                Row nextRow = iterator.next();
                System.out.println(nextRow.getCell(0).getCellType().name() + " " + nextRow.getRowNum());
                Cell dateCell = nextRow.getCell(0); //A
                String date = dateCell.getStringCellValue();

                DayMarketState current = new DayMarketState(date, nextRow.getRowNum(), -1, -1, -1, -1);
                head = current;
                tail = current;
            }

            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                int index = nextRow.getRowNum();
                if (index >= bchain_break_point) {
                    break;
                }
                System.out.println(nextRow.getCell(0).getCellType().name() + " " + index);
                Cell dateCell = nextRow.getCell(0); //A
                String date = dateCell.getStringCellValue();

                Cell bchainRealCell = nextRow.getCell(1); // Column B
                Cell bchainGuessCell = nextRow.getCell(4); // Column E

                double bchainReal = bchainRealCell.getNumericCellValue();
                double bchainGuess = bchainGuessCell.getNumericCellValue();

                DayMarketState current = new DayMarketState(date, nextRow.getRowNum(), -1, -1, bchainReal, bchainGuess);

                if (head == null) {
                    head = tail = current;
                } else {
                    tail.setNext(current);
                    current.setPrevious(tail);
                    tail = current;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Now, the 'head' variable points to the head of the linked list of DayMarketState objects
    }

    public static void loadGoldData() {
        try (FileInputStream fis = new FileInputStream(goldFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> iterator = sheet.iterator();

            // Skip the header row
            if (iterator.hasNext()) {
                iterator.next();
            }
            if (iterator.hasNext()) {
                Row nextRow = iterator.next();
                int index = nextRow.getRowNum();
                System.out.println(nextRow.getCell(0).getCellType().name() + " " + index);
                Cell dateCell = nextRow.getCell(0); //A
                String date = dateCell.getStringCellValue();

                // Search the linked list for a DayMarketState object with the same date
                DayMarketState current = head;
                while (current != null) {
                    if (current.date.equals(date)) {
                        // If such an object is found, set its gold_real and gold_guess fields
                        current.gold_real = -1;
                        current.gold_guess = -1;
                        break;
                    }
                    current = current.getNext();
                }
            }

            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                int index = nextRow.getRowNum();
                if (index >= gold_break_point) {
                    break;
                }
                System.out.println(nextRow.getCell(0).getCellType().name() + " " + index);
                Cell dateCell = nextRow.getCell(0); //A
                String date = dateCell.getStringCellValue();

                Cell goldRealCell = nextRow.getCell(1); // Column B
                Cell goldGuessCell = nextRow.getCell(4); // Column E
                double goldReal = -1;
                double goldGuess = -1;
                if (goldRealCell.getCellType() != CellType.ERROR) {
                    goldReal = goldRealCell.getNumericCellValue();
                }
                if (goldGuessCell.getCellType() != CellType.ERROR) {
                    goldGuess = goldGuessCell.getNumericCellValue();
                }

                // Search the linked list for a DayMarketState object with the same date
                DayMarketState current = head;
                while (current != null) {
                    if (current.date.equals(date)) {
                        // If such an object is found, set its gold_real and gold_guess fields
                        current.gold_real = goldReal;
                        current.gold_guess = goldGuess;
                        break;
                    }
                    current = current.getNext();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
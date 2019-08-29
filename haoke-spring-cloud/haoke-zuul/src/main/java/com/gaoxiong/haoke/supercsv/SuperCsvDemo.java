package com.gaoxiong.haoke.supercsv;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gaoxiong
 * @ClassName SuperCsvDemo
 * @Description TODO
 * @date 2019/8/28 12:01
 */
public class SuperCsvDemo {
    public static void main ( String[] args ) throws IOException {
//        readWithListReader();
        readWithCsvBeanReander();
    }

    private static void readWithListReader () throws IOException {
        ICsvListReader listReader = null;
        try {
            listReader = new CsvListReader(new FileReader("E:\\360MoveData\\Users\\Administrator\\Desktop\\super.csv"), CsvPreference.STANDARD_PREFERENCE);
            listReader.getHeader(true);

            final CellProcessor[]  processors = getProcessors();
            List<Object> objectList = null;
            while ((objectList = listReader.read(processors)) != null) {
                System.out.println("objectList = " + objectList);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (listReader != null) {
                listReader.close();
            }
        }
    }

    private static void readWithCsvBeanReander() throws IOException {
        ICsvBeanReader csvBeanReader = null;
        try {
            csvBeanReader = new CsvBeanReader(new FileReader(new File("E:\\360MoveData\\Users\\Administrator\\Desktop\\super.csv")), CsvPreference.STANDARD_PREFERENCE);

            final String[] header = csvBeanReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();
            List<CustomBean> list = new ArrayList<>();
            CustomBean customBean;
            while ((customBean = csvBeanReader.read(CustomBean.class, header, processors)) != null) {
                list.add(customBean);
            }
            System.out.println("list = " + list);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            csvBeanReader.close();
        }
    }


    private static CellProcessor[] getProcessors () {
        final String emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+"; // just an example, not very robust!
        StrRegEx.registerMessage(emailRegex, "must be a valid email address");

        final CellProcessor[] processors = new CellProcessor[] {
                new UniqueHashCode(new ParseInt()), // customerNo (must be unique)
                new NotNull(), // firstName
                new NotNull(), // lastName
                new ParseDate("dd/MM/yyyy"), // birthDate
                new NotNull(), // mailingAddress
                new Optional(new ParseBool()), // married
                new Optional(new ParseInt()), // numberOfKids
                new NotNull(), // favouriteQuote
                new StrRegEx(emailRegex), // email
                new LMinMax(0L, LMinMax.MAX_LONG) // loyaltyPoints
        };

        return processors;
    }

}

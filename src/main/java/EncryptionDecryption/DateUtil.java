package EncryptionDecryption;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Thomas Okonkwo on 26th April 2020
 */

public class DateUtil {

    private static ThreadLocal<SimpleDateFormat> inDateFormatHolder =  new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };


    private static ThreadLocal<SimpleDateFormat> dateTimeFormatHolder =  new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private static ThreadLocal<SimpleDateFormat> transactionDateFormatHolder =  new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public static String convertDateToString(Date date){

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String convertDateToString(Date date, String pattern){

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static Date convertStringToDate(String dateString) throws ParseException {
        if(dateString==null)
            return null;
        String pattern = "MM/dd/yyyy hh:mm:ss a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(dateString);
    }

    public static Date convertStringToDate(String dateString, String format) throws ParseException {
        if(dateString==null)
            return null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(dateString);
    }



    public static String formatDate(Date date) {
        inDateFormatHolder.get().setLenient(false);
        return inDateFormatHolder.get().format(date);
    }

    public static String getCurrentTimeString() {
        dateTimeFormatHolder.get().setLenient(false);
        return dateTimeFormatHolder.get().format(new Date());
    }

    public static String getTransactionDate(Date date) {
        transactionDateFormatHolder.get().setLenient(false);
        return transactionDateFormatHolder.get().format(date);
    }
}

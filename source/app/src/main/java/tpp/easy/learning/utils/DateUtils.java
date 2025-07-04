package tpp.easy.learning.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import timber.log.Timber;
import tpp.easy.learning.constant.Constants;

public class DateUtils {
    public static Date convertTimeFromUtcToLocal(String dateString){
        if(dateString == null){
            return null;
        }
        SimpleDateFormat inputFormat = new SimpleDateFormat(Constants.DATE_FORMAT_SERVER);
        inputFormat.setTimeZone(TimeZone.getTimeZone(Constants.TIMEZONE_SERVER));
        SimpleDateFormat outputFormat = new SimpleDateFormat(Constants.DATE_FORMAT_APP);
        outputFormat.setTimeZone(TimeZone.getDefault());
        try {
            Date utcDate = inputFormat.parse(dateString);
            String localDateString = outputFormat.format(utcDate);
            return outputFormat.parse(localDateString);
        } catch (ParseException e) {
            Timber.e(e);
        }
        return null;
    }

    public static String convertTimeFromUtcToLocalString(String dateString){
        if(dateString == null){
            return null;
        }
        SimpleDateFormat inputFormat = new SimpleDateFormat(Constants.DATE_FORMAT_SERVER);
        inputFormat.setTimeZone(TimeZone.getTimeZone(Constants.TIMEZONE_SERVER));
        SimpleDateFormat outputFormat = new SimpleDateFormat(Constants.DATE_FORMAT_APP);
        outputFormat.setTimeZone(TimeZone.getDefault());
        try {
            Date utcDate = inputFormat.parse(dateString);
            Date loacalDate = outputFormat.parse(outputFormat.format(utcDate));
            String localDateString = outputFormat.format(utcDate);
            return localDateString;
        } catch (ParseException e) {
            Timber.e(e);
        }
        return null;
    }

}

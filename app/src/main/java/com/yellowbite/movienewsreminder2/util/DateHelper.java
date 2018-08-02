package com.yellowbite.movienewsreminder2.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateHelper
{
    private static final DateFormat df;

    private static final String[] WEEKDAYS = {
            "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"
    };

    static
    {
        df = new SimpleDateFormat("dd.MM.yyyy");
    }

    // --- --- --- converting date to string --- --- ---
    public static Date toDate(String string)
    {
        try
        {
            return df.parse(string);
        } catch (ParseException ignored)
        {
            return null;
        }
    }

    public static String toString(Date date)
    {
        return df.format(date);
    }

    // --- --- --- getWeekday as message --- --- ---
    public static String[] getWeekdayAsMessage(Date date)
    {
        Calendar nowCalendar = Calendar.getInstance();
        Date nowDate = nowCalendar.getTime();

        Calendar dateCalendar = getCalendar(date);

        int weeksNowUntilDate = getWeeksNowUntilDate(date, nowCalendar, nowDate);
        String dateWeekday = getWeekday(dateCalendar);

        if(weeksNowUntilDate < -2)
        {
            return new String[]{"Vor mehr als 2 Wochen"};
        }

        if(weeksNowUntilDate == -2)
        {
            return new String[]{"Vor letzte Woche", dateWeekday};
        }

        if(weeksNowUntilDate == -1)
        {
            return new String[]{"Letzte Woche", dateWeekday};
        }

        if(isNow(dateWeekday, weeksNowUntilDate, nowCalendar))
        {
            return new String[]{"Heute"};
        }

        if(weeksNowUntilDate == 0)
        {
            return new String[]{dateWeekday};
        }

        if(weeksNowUntilDate == 1)
        {
            return new String[]{"Nächste Woche", dateWeekday};
        }

        if(weeksNowUntilDate == 2)
        {
            return new String[]{"Übernächste Woche", dateWeekday};
        }

        return new String[]{"mehr als 2 Wochen"};
    }

    private static boolean isNow(String dateWeekday, int weeksUntilDate, Calendar nowCalendar)
    {
        if(weeksUntilDate != 0)
        {
            return false;
        }

        String weekdayNow = getWeekday(nowCalendar);

        return weekdayNow.equals(dateWeekday);
    }

    private static int getWeeksNowUntilDate(Date date, Calendar nowCalendar, Date nowDate)
    {
        int nowWeek = nowCalendar.get(Calendar.WEEK_OF_YEAR);

        int dateWeek = getWeek(date);

        if(nowDate.compareTo(date) < 0 && nowWeek > dateWeek)
        {
            dateWeek += 52;
        }

        if(nowDate.compareTo(date) > 0 && nowWeek < dateWeek)
        {
            dateWeek -= 52;
        }

        return dateWeek - nowWeek;
    }

    private static int getWeek(Date date)
    {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        return dateCal.get(Calendar.WEEK_OF_YEAR);
    }

    private static String getWeekday(Calendar dateCalendar)
    {
        int weekday = dateCalendar.get(Calendar.DAY_OF_WEEK);
        return WEEKDAYS[weekday - 2];
    }

    private static Calendar getCalendar(Date date)
    {
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(date);
        return calDate;
    }
}

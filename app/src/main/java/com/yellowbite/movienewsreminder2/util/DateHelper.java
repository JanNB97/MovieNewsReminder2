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

    public static String getWeekdayAsMessage(Date date)
    {
        if(isNow(date))
        {
            return "heute";
        }

        int weeksNowUntilDate = getWeeksNowUntilDate(date);

        if(weeksNowUntilDate == 0)
        {
            return "Diesen " + getWeekday(date);
        }

        if(weeksNowUntilDate == 1)
        {
            return "Nächste Woche " + getWeekday(date);
        }

        return getWeekday(date) + " in " + weeksNowUntilDate + " Wochen";
    }

    private static boolean isNow(Date date)
    {
        Date now = Calendar.getInstance().getTime();
        return date.equals(now);
    }

    private static int getWeeksNowUntilDate(Date date)
    {
        Calendar now = Calendar.getInstance();
        int nowWeek = now.get(Calendar.WEEK_OF_YEAR);

        int dateWeek = getWeek(date);

        if(dateWeek < nowWeek)
        {
            dateWeek += 52;
        }

        return dateWeek - nowWeek;
    }

    private static int getWeek(Date date)
    {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        return dateCal.get(Calendar.WEEK_OF_YEAR);
    }

    private static String getWeekday(Date date)
    {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        int weekday = dateCal.get(Calendar.DAY_OF_WEEK);
        return WEEKDAYS[weekday - 1];
    }
}

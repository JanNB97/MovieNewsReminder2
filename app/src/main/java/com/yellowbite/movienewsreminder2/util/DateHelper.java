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

    public static String[] getWeekdayAsMessage(Date date)
    {
        if(isNow(date))
        {
            return new String[]{"heute"};
        }

        int weeksNowUntilDate = getWeeksNowUntilDate(date);

        if(weeksNowUntilDate < -2)
        {
            return new String[]{"Vor mehr als 2 Wochen"};
        }

        if(weeksNowUntilDate == -2)
        {
            return new String[]{"Vor letzte Woche", getWeekday(date)};
        }

        if(weeksNowUntilDate == -1)
        {
            return new String[]{"Letzte Woche", getWeekday(date)};
        }

        if(weeksNowUntilDate == 0)
        {
            return new String[]{getWeekday(date)};
        }

        if(weeksNowUntilDate == 1)
        {
            return new String[]{"Nächste Woche", getWeekday(date)};
        }

        if(weeksNowUntilDate == 2)
        {
            return new String[]{"Übernächste Woche", getWeekday(date)};
        }

        return new String[]{"mehr als 2 Wochen"};
    }

    private static boolean isNow(Date date)
    {
        Date now = Calendar.getInstance().getTime();
        return date.equals(now);
    }

    private static int getWeeksNowUntilDate(Date date)
    {
        Calendar now = Calendar.getInstance();
        Date nowDate = now.getTime();
        int nowWeek = now.get(Calendar.WEEK_OF_YEAR);

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

    private static String getWeekday(Date date)
    {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        int weekday = dateCal.get(Calendar.DAY_OF_WEEK);
        return WEEKDAYS[weekday - 2];
    }
}

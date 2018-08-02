package com.yellowbite.movienewsreminder2.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateHelper
{
    private static final DateFormat df;

    // date-message constants
    public static final String MORE_THAN_TWO_WEEKS_AGO = "Vor mehr als 2 Wochen";
    public static final String TWO_WEEKS_AGO = "Vorletzte Woche";
    public static final String ONE_WEEK_AGO = "Letzte Woche";
    public static final String TODAY = "Heute";
    public static final String TOMORROW = "Morgen";
    public static final String IN_ONE_WEEK = "Nächste Woche";
    public static final String IN_TWO_WEEKS = "Übernächste Woche";
    public static final String IN_MORE_THAN_TWO_WEEKS = "mehr als 2 Wochen";


    private enum Weekday {
        Montag, Dienstag, Mittwoch, Donnerstag, Freitag, Samstag, Sonntag
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
        return getWeekdayAsMessage(date, Calendar.getInstance());
    }

    protected static String[] getWeekdayAsMessage(Date date, Calendar nowCalendar)
    {
        Calendar dateCalendar = getCalendar(date);

        int weeksNowUntilDate = getWeeksNowUntilDate(date, nowCalendar);
        Weekday dateWeekday = getWeekday(dateCalendar);

        // Special days
        Weekday nowWeekday = getWeekday(nowCalendar);

        if(isNow(dateWeekday, nowWeekday, weeksNowUntilDate))
        {
            return new String[]{TODAY};
        }

        if(isTomorrow(dateWeekday, nowWeekday, weeksNowUntilDate))
        {
            return new String[]{TOMORROW};
        }

        // Normal days
        if(weeksNowUntilDate < -2)
        {
            return new String[]{MORE_THAN_TWO_WEEKS_AGO};
        }

        if(weeksNowUntilDate == -2)
        {
            return new String[]{TWO_WEEKS_AGO, dateWeekday.toString()};
        }

        if(weeksNowUntilDate == -1)
        {
            return new String[]{ONE_WEEK_AGO, dateWeekday.toString()};
        }

        if(weeksNowUntilDate == 0)
        {
            return new String[]{dateWeekday.toString()};
        }

        if(weeksNowUntilDate == 1)
        {
            return new String[]{IN_ONE_WEEK, dateWeekday.toString()};
        }

        if(weeksNowUntilDate == 2)
        {
            return new String[]{IN_TWO_WEEKS, dateWeekday.toString()};
        }

        return new String[]{IN_MORE_THAN_TWO_WEEKS};
    }

    private static boolean isNow(Weekday dateWeekday, Weekday nowWeekday, int weeksUntilDate)
    {
        if(weeksUntilDate != 0)
        {
            return false;
        }

        return dateWeekday == nowWeekday;
    }

    private static boolean isTomorrow(Weekday dateWeekday, Weekday nowWeekday, int weeksUnilDate)
    {
        if(weeksUnilDate == 1 && dateWeekday == Weekday.Montag && nowWeekday ==Weekday.Sonntag)
        {
            return true;
        }

        if(weeksUnilDate != 0)
        {
            return false;
        }

        return nowWeekday.ordinal() + 1 == dateWeekday.ordinal();
    }

    private static int getWeeksNowUntilDate(Date date, Calendar nowCalendar)
    {
        Date nowDate = nowCalendar.getTime();

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

    private static Weekday getWeekday(Calendar dateCalendar)
    {
        int weekday = dateCalendar.get(Calendar.DAY_OF_WEEK);
        return Weekday.values()[weekday - 2];
    }

    protected static Calendar getCalendar(Date date)
    {
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(date);
        return calDate;
    }
}

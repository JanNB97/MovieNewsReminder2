package com.yellowbite.movienewsreminder2.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateHelper
{
    private static final DateFormat df;

    // date-message constants
    public static final String MORE_THAN_TWO_WEEKS_AGO = "Vor mehr als 2 Wochen";
    public static final String TWO_WEEKS_AGO = "Vorletzte Woche";
    public static final String ONE_WEEK_AGO = "Letzte Woche";
    public static final String THE_DAY_BEFORE_YESTERDAY = "Vorgestern";
    public static final String YESTERDAY = "Gestern";

    public static final String TODAY = "Heute";

    public static final String TOMORROW = "Morgen";
    public static final String THE_DAY_AFTER_TOMORROW = "Übermorgen";
    public static final String IN_ONE_WEEK = "Nächste Woche";
    public static final String IN_TWO_WEEKS = "Übernächste Woche";
    public static final String IN_MORE_THAN_TWO_WEEKS = "mehr als 2 Wochen";

    public enum Weekday {
        Montag, Dienstag, Mittwoch, Donnerstag, Freitag, Samstag, Sonntag
    };

    static
    {
        df = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
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
        if(date == null)
        {
            return null;
        }

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
        Date nowDate = nowCalendar.getTime();

        int weeksNowUntilDate = getWeeksNowUntilDate(date, nowCalendar, nowDate);
        Weekday dateWeekday = getWeekday(dateCalendar);

        // Special days
        switch (getDistance(nowDate, date))
        {
            case -2:
                return new String[]{THE_DAY_BEFORE_YESTERDAY};
            case -1:
                return new String[]{YESTERDAY};
            case 0:
                return new String[]{TODAY};
            case 1:
                return new String[]{TOMORROW};
            case 2:
                return new String[]{THE_DAY_AFTER_TOMORROW};
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

    private static Weekday getWeekday(Calendar dateCalendar)
    {
        int weekday = dateCalendar.get(Calendar.DAY_OF_WEEK);
        if(weekday == 1)
        {
            return Weekday.Sonntag;
        }

        return Weekday.values()[weekday - 2];
    }

    protected static Calendar getCalendar(Date date)
    {
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(date);
        return calDate;
    }

    // --- --- --- distance between dates --- --- ---
    public static int getDistance(Date earlierDate, Date laterDate)
    {
        long earlierTime = earlierDate.getTime();
        long laterTime = laterDate.getTime();

        long distanceInMilliSeconds = laterTime - earlierTime;

        return toDays(distanceInMilliSeconds);
    }

    private static int toDays(long ms)
    {
        return (int) (Math.ceil((double)ms / 1000 / 60 / 60 / 24));
    }

    // --- --- --- get date in future --- --- ---
    public static Date incrementDays(Date date, int days)
    {
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        dateCalendar.add(Calendar.DATE, days);

        return dateCalendar.getTime();
    }
}

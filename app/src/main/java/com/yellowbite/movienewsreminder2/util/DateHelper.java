package com.yellowbite.movienewsreminder2.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper
{
    private static final DateFormat df;

    static
    {
        df = new SimpleDateFormat("dd.mm.yyyy");
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
}

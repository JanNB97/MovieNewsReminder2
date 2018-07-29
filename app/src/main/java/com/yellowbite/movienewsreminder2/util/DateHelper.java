package com.yellowbite.movienewsreminder2.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper
{
    public static Date toDate(String string)
    {
        DateFormat df = new SimpleDateFormat("dd.mm.yyyy");
        try
        {
            return df.parse(string);
        } catch (ParseException ignored)
        {
            return null;
        }
    }
}

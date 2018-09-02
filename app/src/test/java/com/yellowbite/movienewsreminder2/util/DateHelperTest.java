package com.yellowbite.movienewsreminder2.util;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class DateHelperTest
{
    private static final String nowString = "02.08.2018";    //Thursday
    private static final Calendar nowCalendar = DateHelper.getCalendar(DateHelper.toDate(nowString));

    @Test
    public void testGetWeekdayAsMessage()
    {
        // more than two weeks ago
        this.assertDateMessage(DateHelper.MORE_THAN_TWO_WEEKS_AGO, "11.01.2002");
        this.assertDateMessage(DateHelper.MORE_THAN_TWO_WEEKS_AGO, "15.07.2018");

        // two weeks ago
        this.assertDateMessage(DateHelper.TWO_WEEKS_AGO, DateHelper.Weekday.Montag, "16.07.2018");
        this.assertDateMessage(DateHelper.TWO_WEEKS_AGO, DateHelper.Weekday.Dienstag, "17.07.2018");
        this.assertDateMessage(DateHelper.TWO_WEEKS_AGO, DateHelper.Weekday.Mittwoch, "18.07.2018");
        this.assertDateMessage(DateHelper.TWO_WEEKS_AGO, DateHelper.Weekday.Donnerstag, "19.07.2018");
        this.assertDateMessage(DateHelper.TWO_WEEKS_AGO, DateHelper.Weekday.Freitag, "20.07.2018");
        this.assertDateMessage(DateHelper.TWO_WEEKS_AGO, DateHelper.Weekday.Samstag, "21.07.2018");
        this.assertDateMessage(DateHelper.TWO_WEEKS_AGO, DateHelper.Weekday.Sonntag, "22.07.2018");

        // one week ago
        this.assertDateMessage(DateHelper.ONE_WEEK_AGO, DateHelper.Weekday.Montag, "23.07.2018");
        this.assertDateMessage(DateHelper.ONE_WEEK_AGO, DateHelper.Weekday.Dienstag, "24.07.2018");
        this.assertDateMessage(DateHelper.ONE_WEEK_AGO, DateHelper.Weekday.Mittwoch, "25.07.2018");
        this.assertDateMessage(DateHelper.ONE_WEEK_AGO, DateHelper.Weekday.Donnerstag, "26.07.2018");
        this.assertDateMessage(DateHelper.ONE_WEEK_AGO, DateHelper.Weekday.Freitag, "27.07.2018");
        this.assertDateMessage(DateHelper.ONE_WEEK_AGO, DateHelper.Weekday.Samstag, "28.07.2018");
        this.assertDateMessage(DateHelper.ONE_WEEK_AGO, DateHelper.Weekday.Sonntag, "29.07.2018");

        // this week
        this.assertDateMessage(DateHelper.Weekday.Montag, "30.07.2018");
        this.assertDateMessage(DateHelper.THE_DAY_BEFORE_YESTERDAY, "31.07.2018");
        this.assertDateMessage(DateHelper.YESTERDAY, "01.08.2018");
        this.assertDateMessage(DateHelper.TODAY, "02.08.2018");
        this.assertDateMessage(DateHelper.TOMORROW, "03.08.2018");
        this.assertDateMessage(DateHelper.THE_DAY_AFTER_TOMORROW, "04.08.2018");
        this.assertDateMessage(DateHelper.Weekday.Sonntag, "05.08.2018");

        // in one week
        this.assertDateMessage(DateHelper.IN_ONE_WEEK, DateHelper.Weekday.Montag, "06.08.2018");
        this.assertDateMessage(DateHelper.IN_ONE_WEEK, DateHelper.Weekday.Dienstag, "07.08.2018");
        this.assertDateMessage(DateHelper.IN_ONE_WEEK, DateHelper.Weekday.Sonntag, "12.08.2018");

        // in two weeks
        this.assertDateMessage(DateHelper.IN_TWO_WEEKS, DateHelper.Weekday.Mittwoch, "15.08.2018");
        this.assertDateMessage(DateHelper.IN_TWO_WEEKS, DateHelper.Weekday.Donnerstag, "16.08.2018");

        // more than two weeks
        this.assertDateMessage(DateHelper.IN_MORE_THAN_TWO_WEEKS, "30.12.2019");
        this.assertDateMessage(DateHelper.IN_MORE_THAN_TWO_WEEKS, "20.08.2018");
    }

    private void assertDateMessage(String expected1, String expteced2, String dateString)
    {
        String[] actual = DateHelper.getWeekdayAsMessage(DateHelper.toDate(dateString), nowCalendar);

        Assert.assertEquals(expected1, actual[0]);

        if(!expteced2.equals(""))
        {
            Assert.assertEquals(expteced2, actual[1]);
        }
        else
        {
            Assert.assertEquals(actual.length, 1);
        }
    }

    private void assertDateMessage(String expected1, DateHelper.Weekday expteced2, String dateString)
    {
        this.assertDateMessage(expected1, expteced2.toString(), dateString);
    }

    private void assertDateMessage(String expected1, String dateString)
    {
        assertDateMessage(expected1, "", dateString);
    }

    private void assertDateMessage(DateHelper.Weekday expected1, String dateString)
    {
        assertDateMessage(expected1.toString(), dateString);
    }

    @Test
    public void testGetDateDistance()
    {
        this.assertDateDistanceIsRight("11.01.2000", "11.01.2000", 0);
        this.assertDateDistanceIsRight("03.09.2018", "02.09.2018", -1);
        this.assertDateDistanceIsRight("02.09.2018", "03.09.2018", 1);
        this.assertDateDistanceIsRight("04.09.2018", "28.08.2018", -7);
        this.assertDateDistanceIsRight("28.08.2018", "04.09.2018", 7);
        this.assertDateDistanceIsRight("04.09.2018", "04.09.2017", -365);
        this.assertDateDistanceIsRight("04.09.2017","04.09.2018",  365);
        this.assertDateDistanceIsRight("04.09.2018", "05.09.2017", -364);
        this.assertDateDistanceIsRight("05.09.2017", "04.09.2018", 364);
        this.assertDateDistanceIsRight("04.09.2018", "06.09.2017", -363);
        this.assertDateDistanceIsRight("04.09.2018", "07.09.2017", -362);
    }

    private void assertDateDistanceIsRight(String d1, String d2, int expectedDis)
    {
        Assert.assertEquals(expectedDis, DateHelper.getDistance(DateHelper.toDate(d1), DateHelper.toDate(d2)));
    }
}

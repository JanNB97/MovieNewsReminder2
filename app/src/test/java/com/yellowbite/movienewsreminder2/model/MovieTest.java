package com.yellowbite.movienewsreminder2.model;

import com.yellowbite.movienewsreminder2.model.enums.Status;
import com.yellowbite.movienewsreminder2.util.DateHelper;

import junit.framework.Assert;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieTest
{
    @Test
    public void testCompareTo()
    {
        Movie verfuegbarA = getStatusMovie("verfuegbarA", Status.VERFUEGBAR, 0, null, "11.01.2013");
        Movie verfuegbarB = getStatusMovie("verfuegbarB", Status.VERFUEGBAR, 0, null, "12.01.2013");
        Movie verfuegbarC = getStatusMovie("verfuegbarC", Status.VERFUEGBAR, 0, null, "12.01.2013");
        Movie verfuegbarNull = getStatusMovie(null, Status.VERFUEGBAR, 0, null, "12.01.2013");

        Movie entliehen_1Vor_11_07_2018 = getStatusMovie("entliehen_1Vor_11_07_2018", Status.ENTLIEHEN, 1, "11.07.2018", "01.01.2013");
        Movie entliehen_1Vor_13_07_2018 = getStatusMovie("entliehen_1Vor_13_07_2018", Status.ENTLIEHEN, 1, "13.07.2018", "01.01.2013");
        Movie entliehen_2Vor_10_07_2018 = getStatusMovie("entliehen_2Vor_10_07_2018", Status.ENTLIEHEN, 2, "10.07.2018", "01.01.2013");
        Movie entliehen_3Vor_12_07_2018A = getStatusMovie("entliehen_3Vor_12_07_2018A", Status.ENTLIEHEN, 3, "12.07.2018", "01.01.2013");
        Movie entliehen_3Vor_12_07_2018B = getStatusMovie("entliehen_3Vor_12_07_2018B", Status.ENTLIEHEN, 3, "12.07.2018", "01.01.2013");
        Movie entliehen_3Vor_12_07_2018Null = getStatusMovie(null, Status.ENTLIEHEN, 3, "12.07.2018","01.01.2013");

        Movie vorbestellt_1 = getStatusMovie("vorbestellt_1", Status.VORBESTELLT, 1, null, "01.01.2013");
        Movie vorbestellt_2A = getStatusMovie("vorbestellt_2A", Status.VORBESTELLT, 2, null, "01.01.2013");
        Movie vorbestellt_2B = getStatusMovie("vorbestellt_2B", Status.VORBESTELLT, 2, null, "02.01.2013");
        Movie vorbestellt_2C = getStatusMovie("vorbestellt_2C", Status.VORBESTELLT, 2, null, "02.01.2012");
        Movie vorbestellt_2Null = getStatusMovie(null, Status.VORBESTELLT, 2, null, "01.01.2013");

        // 'verfuegbar' and 'verfuegbar'
        this.assertEarlierThan(verfuegbarB, verfuegbarA);
        this.assertEarlierThan(verfuegbarNull, verfuegbarA);
        this.assertEarlierThan(verfuegbarB, verfuegbarNull);
        this.assertEarlierThan(verfuegbarB, verfuegbarC);

        // 'verfuegbar' and 'entliehen'
        this.assertEarlierThan(verfuegbarA, entliehen_1Vor_11_07_2018);

        // 'verfuegbar' and 'vorbestellt'
        this.assertEarlierThan(verfuegbarA, vorbestellt_1);

        // 'entliehen' and 'entliehen'
        this.assertEarlierThan(entliehen_1Vor_11_07_2018, entliehen_1Vor_13_07_2018);
        this.assertEarlierThan(entliehen_1Vor_11_07_2018, entliehen_2Vor_10_07_2018);
        this.assertEarlierThan(entliehen_1Vor_13_07_2018, entliehen_3Vor_12_07_2018A);
        this.assertEarlierThan(entliehen_3Vor_12_07_2018A, entliehen_3Vor_12_07_2018B);
        this.assertEarlierThan(entliehen_3Vor_12_07_2018A, entliehen_3Vor_12_07_2018Null);

        // 'entliehen' and 'vorbestellt'
        this.assertEarlierThan(entliehen_1Vor_11_07_2018, vorbestellt_1);
        this.assertEarlierThan(vorbestellt_1, entliehen_2Vor_10_07_2018);

        // 'vorbestellt' and 'vorbestellt'
        this.assertEarlierThan(vorbestellt_1, vorbestellt_2A);
        this.assertEarlierThan(vorbestellt_2B, vorbestellt_2A);
        this.assertEarlierThan(vorbestellt_2A, vorbestellt_2C);
        this.assertEarlierThan(vorbestellt_2A, vorbestellt_2Null);
        this.assertEarlierThan(vorbestellt_2Null, vorbestellt_2C);
    }

    private void assertEarlierThan(Movie m1, Movie m2)      // <
    {
        Assert.assertTrue(m1.getTitel() + " later than " + m2.getTitel(), m1.compareTo(m2) < 0);
        Assert.assertTrue(m2.getTitel() + " earlier than " + m1.getTitel(), m2.compareTo(m1) > 0);
    }

    private Movie getStatusMovie(String titel, Status status, int vorbestellungen, String entliehenBis, String zugang)
    {
        Date entliehenBisDate = null;

        if(status == Status.ENTLIEHEN)
        {
            entliehenBisDate = DateHelper.toDate(entliehenBis);
        }

        return new Movie(100000, "url",
                status, vorbestellungen, entliehenBisDate,
                "standort", DateHelper.toDate(zugang),
                titel);
    }
}

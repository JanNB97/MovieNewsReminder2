package com.yellowbite.movienewsreminder2.model;

import com.yellowbite.movienewsreminder2.model.enums.Status;

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
        Movie verfuegbarA = getStatusMovie("verfuegbarA", Status.VERFUEGBAR, 0, null);
        Movie verfuegbarB = getStatusMovie("verfuegbarB", Status.VERFUEGBAR, 0, null);

        Movie entliehen_1Vor_11_07_2018 = getStatusMovie("entliehen_1Vor_11_07_2018", Status.ENTLIEHEN, 1, "11.07.2018");
        Movie entliehen_1Vor_13_07_2018 = getStatusMovie("entliehen_1Vor_13_07_2018", Status.ENTLIEHEN, 1, "13.07.2018");
        Movie entliehen_2Vor_10_07_2018 = getStatusMovie("entliehen_2Vor_10_07_2018", Status.ENTLIEHEN, 2, "10.07.2018");
        Movie entliehen_3Vor_12_07_2018A = getStatusMovie("entliehen_3Vor_12_07_2018A", Status.ENTLIEHEN, 3, "12.07.2018");
        Movie entliehen_3Vor_12_07_2018B = getStatusMovie("entliehen_3Vor_12_07_2018B", Status.ENTLIEHEN, 3, "12.07.2018");

        Movie vorbestellt_1 = getStatusMovie("vorbestellt_1", Status.VORBESTELLT, 1, null);
        Movie vorbestellt_2A = getStatusMovie("vorbestellt_2A", Status.VORBESTELLT, 2, null);
        Movie vorbestellt_2B = getStatusMovie("vorbestellt_2B", Status.VORBESTELLT, 2, null);

        // 'verfuegbar' and 'verfuegbar'
        this.assertEarlierThan(verfuegbarA, verfuegbarB);

        // 'verfuegbar' and 'entliehen'
        this.assertEarlierThan(verfuegbarA, entliehen_1Vor_11_07_2018);

        // 'verfuegbar' and 'vorbestellt'
        this.assertEarlierThan(verfuegbarA, vorbestellt_1);

        // 'entliehen' and 'entliehen'
        this.assertEarlierThan(entliehen_1Vor_11_07_2018, entliehen_1Vor_13_07_2018);
        this.assertEarlierThan(entliehen_1Vor_11_07_2018, entliehen_2Vor_10_07_2018);
        this.assertEarlierThan(entliehen_1Vor_13_07_2018, entliehen_3Vor_12_07_2018A);
        this.assertEarlierThan(entliehen_3Vor_12_07_2018A, entliehen_3Vor_12_07_2018B);

        // 'entliehen' and 'vorbestellt'
        this.assertEarlierThan(entliehen_1Vor_11_07_2018, vorbestellt_1);
        this.assertEarlierThan(vorbestellt_1, entliehen_2Vor_10_07_2018);

        // 'vorbestellt' and 'vorbestellt'
        this.assertEarlierThan(vorbestellt_1, vorbestellt_2A);
        this.assertEarlierThan(vorbestellt_2A, vorbestellt_2B);
    }

    private void assertEarlierThan(Movie m1, Movie m2)      // <
    {
        Assert.assertTrue(m1.getTitel() + " later than " + m2.getTitel(), m1.compareTo(m2) < 0);
        Assert.assertTrue(m2.getTitel() + " earlier than " + m1.getTitel(), m2.compareTo(m1) > 0);
    }

    private Movie getStatusMovie(String titel, Status status, int vorbestellungen, String entliehenBis)
    {
        Date entliehenBisDate = null;

        if(status == Status.ENTLIEHEN)
        {
            DateFormat df = new SimpleDateFormat("dd.mm.yyyy");
            try
            {
                entliehenBisDate = df.parse(entliehenBis);
            } catch (ParseException ignored) {}
        }

        return new Movie(100000, "url",
                status, vorbestellungen, entliehenBisDate,
                "standort", "interessenkreis", "signatur",
                titel, "kurzbeschreibung");
    }
}

package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.data.OpeningHours;

import org.junit.Test;

import java.io.IOException;

public class MedZenOpeningHoursTest
{
    @Test
    public void testGetOpeningHours()
    {
        MedZenOpeningHours medZenOpeningHours = null;
        OpeningHours openingHours = null;
        try
        {
            medZenOpeningHours = new MedZenOpeningHours();
            openingHours = medZenOpeningHours.getOpeningHours();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

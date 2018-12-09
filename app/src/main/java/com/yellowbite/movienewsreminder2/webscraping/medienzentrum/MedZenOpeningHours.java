package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.data.OpeningHours;
import com.yellowbite.movienewsreminder2.util.DateHelper;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHelper;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public class MedZenOpeningHours
{
    public static final String OPENING_HOURS_URL = "https://opac.winbiap.net/mzhr/index.aspx?data=cGFnZUlkPTE2-K5D47jXuuW4=";

    private Document doc;

    public MedZenOpeningHours(String url) throws IOException
    {
        this.doc = WebscrapingHelper.getDoc(url);
    }

    public OpeningHours getOpeningHours()
    {
        List<String> tableData = WebscrapingHelper.getAllTexts(this.doc, "table > tbody > tr > td");
        return this.toOpeningHours(tableData);
    }

    private OpeningHours toOpeningHours(List<String> tableData)
    {
        OpeningHours result = new OpeningHours();
        DateHelper.Weekday currentWeekday = null;

        for(String element : tableData)
        {
            if(element.isEmpty())
            {
                continue;
            }

            DateHelper.Weekday elementWeekday = this.getWeekday(element);

            if(elementWeekday == null)
            {
                result.addWeekdayInfo(currentWeekday, element);
            }
            else
            {
                currentWeekday = elementWeekday;
            }
        }

        return result;
    }

    private DateHelper.Weekday getWeekday(String tableElement)
    {
        for(DateHelper.Weekday weekday : DateHelper.Weekday.values())
        {
            if(weekday.toString().equals(tableElement))
            {
                return weekday;
            }
        }

        return null;
    }
}

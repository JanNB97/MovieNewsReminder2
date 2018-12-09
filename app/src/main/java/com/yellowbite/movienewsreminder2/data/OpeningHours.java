package com.yellowbite.movienewsreminder2.data;

import com.yellowbite.movienewsreminder2.util.DateHelper;

import java.util.HashMap;
import java.util.Map;

public class OpeningHours
{
    private Map<DateHelper.Weekday, TimeInfo> openingHours = new HashMap<>();

    public void addWeekdayInfo(DateHelper.Weekday weekday, String information)
    {
        TimeInfo info = this.getWeekdayInfo(weekday);
        if(info == null)
        {
            info = new TimeInfo();
            info.setMorningInfo(information);
            openingHours.put(weekday, info);
        }
        else
        {
            info.setEveningInfo(information);
        }

        this.openingHours.put(weekday, info);
    }

    public TimeInfo getWeekdayInfo(DateHelper.Weekday weekday)
    {
        return this.openingHours.get(weekday);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof OpeningHours))
        {
            return false;
        }

        OpeningHours objHours = (OpeningHours)obj;

        for(DateHelper.Weekday weekday : DateHelper.Weekday.values())
        {
            TimeInfo thisTimeInfo = this.openingHours.get(weekday);
            TimeInfo objTimeInfo = objHours.openingHours.get(weekday);

            if(thisTimeInfo == null && objTimeInfo == null)
            {
                continue;
            }

            if(thisTimeInfo == null || objTimeInfo == null)
            {
                return false;
            }

            if(!thisTimeInfo.equals(objTimeInfo))
            {
                return false;
            }
        }

        return true;
    }
}

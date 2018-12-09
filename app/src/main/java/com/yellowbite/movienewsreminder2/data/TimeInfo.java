package com.yellowbite.movienewsreminder2.data;

public class TimeInfo
{
    private String morningInfo;
    private String eveningInfo;

    public String getMorningInfo()
    {
        return morningInfo;
    }

    public void setMorningInfo(String morningInfo)
    {
        this.morningInfo = morningInfo;
    }

    public String getEveningInfo()
    {
        return eveningInfo;
    }

    public void setEveningInfo(String eveningInfo)
    {
        this.eveningInfo = eveningInfo;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof TimeInfo))
        {
            return false;
        }

        TimeInfo objTimeInfo = (TimeInfo)obj;

        // TODO

        return this.morningInfo.equals(objTimeInfo.morningInfo)
                && this.eveningInfo.equals(objTimeInfo.eveningInfo);
    }
}

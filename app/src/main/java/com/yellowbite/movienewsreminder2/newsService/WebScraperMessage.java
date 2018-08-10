package com.yellowbite.movienewsreminder2.newsService;

public class WebScraperMessage
{
    private final String titel;
    private final String text;
    private final int icon;

    public WebScraperMessage(String titel, String text, int icon)
    {
        this.titel = titel;
        this.text = text;
        this.icon = icon;
    }

    public String getText()
    {
        return text;
    }

    public String getTitel()
    {
        return titel;
    }

    public int getIcon()
    {
        return icon;
    }
}

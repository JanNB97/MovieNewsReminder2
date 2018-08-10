package com.yellowbite.movienewsreminder2.newsService.messages;

public class WebScraperMessage
{
    private final String titel;
    private final String text;
    private final int icon;
    private final boolean showMultiLinedText;

    public WebScraperMessage(String titel, String text, int icon, boolean showMultiLinedText)
    {
        this.titel = titel;
        this.text = text;
        this.icon = icon;
        this.showMultiLinedText = showMultiLinedText;
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

    public boolean isShowMultiLinedText()
    {
        return showMultiLinedText;
    }
}
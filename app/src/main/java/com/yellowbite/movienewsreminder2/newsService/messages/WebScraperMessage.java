package com.yellowbite.movienewsreminder2.newsService.messages;

public class WebScraperMessage
{
    private final String title;
    private final String text;
    private final int icon;
    private final boolean showMultiLinedText;

    public WebScraperMessage(String title, String text, int icon, boolean showMultiLinedText)
    {
        this.title = title;
        this.text = text;
        this.icon = icon;
        this.showMultiLinedText = showMultiLinedText;
    }

    public String getText()
    {
        return text;
    }

    public String getTitle()
    {
        return title;
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

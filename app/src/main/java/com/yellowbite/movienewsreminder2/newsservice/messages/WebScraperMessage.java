package com.yellowbite.movienewsreminder2.newsservice.messages;

public class WebScraperMessage
{
    private final int id;

    private final Message message;

    private final String title;
    private final String text;
    private final int icon;
    private final boolean showMultiLinedText;

    public WebScraperMessage(Message message, String title, String text, int icon, boolean showMultiLinedText)
    {
        this.message = message;
        this.id = this.message.getId();

        this.title = title;
        this.text = text;
        this.icon = icon;
        this.showMultiLinedText = showMultiLinedText;
    }

    public int getId()
    {
        return id;
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

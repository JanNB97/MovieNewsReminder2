package com.yellowbite.movienewsreminder2.newsService.messages;

import com.yellowbite.movienewsreminder2.R;

public class HotMovieMessage extends WebScraperMessage
{
    public HotMovieMessage(String titel, String text, int icon)
    {
        super(titel, text, R.drawable.ic_launcher_foreground);
    }
}

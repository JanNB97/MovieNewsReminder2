package com.yellowbite.movienewsreminder2.newsService.messages;

public enum Message
{
    ADDED_MOVIE(0), BOOKED_MOVIES(1), HOT_MOVIE_AVAILABLE(2);

    private final int id;

    Message(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }
}

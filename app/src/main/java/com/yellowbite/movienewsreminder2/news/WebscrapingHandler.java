package com.yellowbite.movienewsreminder2.news;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public abstract class WebscrapingHandler
{
    private final String name;

    public WebscrapingHandler(String name)
    {
        this.name = name;
    }

    private static List<WebscrapingHandler> handlers;

    private static void registerHandlers()
    {
        //Register your handlers here
        registerHandler(new MedZenHandler());
    }

    public static ArrayList<WebscrapingHandler> getUpdatedWebscrapers(Context context)
    {
        ArrayList<WebscrapingHandler> updatedWebscrapers = new ArrayList<>();

        if(handlers == null)
        {
            handlers = new ArrayList<>();
            registerHandlers();
        }

        for(WebscrapingHandler webscrapingHandler : handlers)
        {
            if(webscrapingHandler.hasUpdated(context))
            {
                updatedWebscrapers.add(webscrapingHandler);
            }
        }

        return updatedWebscrapers;
    }

    public abstract boolean hasUpdated(Context context);

    private static void registerHandler(WebscrapingHandler handler)
    {
        handlers.add(handler);
    }

    public String getName() {
        return name;
    }
}

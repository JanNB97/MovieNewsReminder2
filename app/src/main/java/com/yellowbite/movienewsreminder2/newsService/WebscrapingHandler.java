package com.yellowbite.movienewsreminder2.newsService;

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

    public static List<WebScraperMessage> getWebScraperMessages(Context context)
    {
        ArrayList<WebScraperMessage> webScraperMessages = new ArrayList<>();

        if(handlers == null)
        {
            handlers = new ArrayList<>();
            registerHandlers();
        }

        for(WebscrapingHandler webscrapingHandler : handlers)
        {
            WebScraperMessage message = webscrapingHandler.checkForNews(context);
            if(message != null)
            {
                webScraperMessages.add(message);
            }
        }

        return webScraperMessages;
    }

    public abstract WebScraperMessage checkForNews(Context context);

    private static void registerHandler(WebscrapingHandler handler)
    {
        handlers.add(handler);
    }

    public String getName() {
        return name;
    }
}

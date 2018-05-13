package com.yellowbite.movienewsreminder2.webscraping;

import android.content.Context;

import com.yellowbite.movienewsreminder2.util.FileManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class MedienzentrumScraper extends WebscrapingHandler
{
    public MedienzentrumScraper() {
        super("Medienzentrum");
    }

    @Override
    public boolean hasUpdated(Context context) {
        try {
            Document doc = Jsoup.connect("https://opac.winbiap.net/mzhr/acquisitions.aspx?data=U29ydD1adWdhbmdzZGF0dW0gKEJpYmxpb3RoZWspJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD02MyUlb18wPTYlJXZfMD0yNS4wOC4yMDE2IDAwOjAwOjAwKytjXzE9MSUlbV8xPTElJWZfMT00MiUlb18xPTElJXZfMT00NlMtRFZEIChTcGllbGZpbG0pKytjXzI9MSUlbV8yPTElJWZfMj00OCUlb18yPTElJXZfMj1NZWRpZW56ZW50cnVtIEhlcnNmZWxkLVJvdGVuYnVyZyZhbXA7Y21kPTEmYW1wO3BJPTE=-MZcE9znO8Mk=").maxBodySize(0).get();

            int barcode = getMediaBarcode(doc.select("tr.ResultItem").first());

            List<String> barcodes = FileManager.readLines(context, "medienzentrum.txt");

            if(barcodes.size() == 0)
            {
                FileManager.writeLine(context, "medienzentrum.txt", Integer.toString(barcode), Context.MODE_PRIVATE);
                return true;
            }
            else
            {
                int barcodeBackup = Integer.parseInt(barcodes.get(0));

                if(barcode != barcodeBackup)
                {
                    FileManager.writeLine(context, "medienzentrum.txt", Integer.toString(barcode), Context.MODE_PRIVATE);
                    return true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void printMovieElement(Element element)
    {
        System.out.println("Titel: \t" + getTitel(element) + "\n" +
                "Erscheinungsjahr: \t" + getErscheinungsjahr(element) + "\n" +
                "Standort: \t" + getStandort(element) + "\n" +
                "Interessenkreis: \t" + getInteressenkreis(element) + "\n" +
                "Signatur: \t" + getSignatur(element) + "\n" +
                "Notation: \t" + getNotation(element) + "\n" +
                "Mediabarcode: \t" + getMediaBarcode(element) + "\n" +
                "--------------------------------\n");
    }

    private static String getTitel(Element element)
    {
        Elements elements = element.select("[title='Titel']");

        if(elements.size() == 0)
        {
            return null;
        }

        return elements.first().text();
    }

    private static String getStandort(Element element)
    {
        Elements standorte = element.select("span.location");

        if(standorte.size() == 0)
        {
            return null;
        }

        return standorte.first().text();
    }

    private static String getInteressenkreis(Element element)
    {
        Elements interessenkreis = element.select("span.topics");

        if(interessenkreis.size() == 0)
        {
            return null;
        }

        return interessenkreis.first().text();
    }

    private static String getSignatur(Element element)
    {
        Elements signaturen = element.select("span.systematik");

        if(signaturen.size() == 0)
        {
            return null;
        }

        return signaturen.first().text();
    }

    private static int getErscheinungsjahr(Element element)
    {
        Elements erscheinungsjahre = element.select("[title='Erscheinungsjahr']");

        if(erscheinungsjahre.size() == 0)
        {
            return -1;
        }

        return Integer.parseInt(erscheinungsjahre.first().text());
    }

    private static int getNotation(Element element)
    {
        Elements notationen = element.select("span.notation");

        if(notationen.size() == 0)
        {
            return -1;
        }

        return Integer.parseInt(notationen.first().text());
    }

    private static int getMediaBarcode(Element element)
    {
        Elements codes = element.select("span.mediaBarcode");

        if(codes.size() == 0)
        {
            return -1;
        }

        return Integer.parseInt(codes.first().text());
    }
}

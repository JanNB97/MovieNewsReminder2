package com.yellowbite.movienewsreminder2.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FileManager
{
    public static List<String> readLines(Context context, String filename)
    {
        ArrayList<String> readLines = new ArrayList<>();

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));

            String line;

            while ((line = reader.readLine()) != null)
            {
                readLines.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(reader != null)
            {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return readLines;
    }

    public static void writeLine(Context context, String filename, String line, int mode)
    {
        if(fileExists(context, filename) == false)
        {
            File file = new File(context.getFilesDir(), filename);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE)));
            writer.write(line);
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(writer != null)
            {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean fileExists(Context context, String filename)
    {
        File internalDir = context.getFilesDir();

        for(File file : internalDir.listFiles())
        {
            if(file.getName().equals(filename))
            {
                return true;
            }
        }

        return false;
    }
}

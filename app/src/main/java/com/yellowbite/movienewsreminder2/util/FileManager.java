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
    // --- --- --- Read --- --- ---
    public static List<String> read(Context context, String filename)
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

    // --- --- --- Write --- --- ---
    public static void insertFirst(Context context, String filename, String line)
    {
        insert(context, filename, line, 0);
    }

    public static void insert(Context context, String filename, String line, int index)
    {
        List<String> lines = read(context, filename);
        lines.add(index, line);
        write(context, filename, lines);
    }

    public static void write(Context context, String filename, String line)
    {
        List<String> lines = new ArrayList<>();
        lines.add(line);
        write(context, filename, lines);
    }

    public static void write(Context context, String filename, List<String> lines)
    {
        createFileIfNotExists(context, filename);

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE)));

            for(String line : lines)
            {
                writer.write(line);
                writer.newLine();
            }

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

    // --- --- --- Check for file existence --- --- ---
    private static void createFileIfNotExists(Context context, String filename)
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

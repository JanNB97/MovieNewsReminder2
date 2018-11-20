package com.yellowbite.movienewsreminder2.files;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileManager
{
    // --- --- --- Read --- --- ---
    public static String read(Context context, String filename)
    {
        String line = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
            line = reader.readLine();

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

        return line;
    }

    public static List<String> readAll(Context context, String filename)
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

    public static boolean isEmpty(Context context, String filename)
    {
        return read(context, filename) == null;
    }

    // --- --- --- Insert --- --- ---
    public static void insertFirst(Context context, String filename, String line)
    {
        insert(context, filename, line, 0);
    }

    public static void insertFirst(Context context, String filename, Collection<String> lines)
    {
        insert(context, filename, lines, 0);
    }

    public static void insert(Context context, String filename, String line, int index)
    {
        Collection<String> lines = new ArrayList<>();
        lines.add(line);
        insert(context, filename, lines, index);
    }

    public static void insert(Context context, String filename, Collection<String> lines, int index)
    {
        List<String> s = readAll(context, filename);
        s.addAll(index, lines);
        write(context, filename, s);
    }

    // --- --- --- Write --- --- ---
    public static void write(Context context, String filename, String line)
    {
        List<String> lines = new ArrayList<>();
        lines.add(line);
        write(context, filename, lines);
    }

    public static void write(Context context, String filename, Collection<String> lines)
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

    // --- --- --- Delete --- --- ---
    public static void delete(Context context, String filename, int index)
    {
        delete(context, filename, index, index + 1);
    }

    public static void deleteLast(Context context, String filename)
    {
        deleteLast(context, filename, 1);
    }

    public static void deleteLast(Context context, String filename, int numOfDelOperations)
    {
        List<String> lines = readAll(context, filename);

        for(int i = 0; i < numOfDelOperations; i++)
        {
            lines.remove(lines.size() - 1);
        }

        write(context, filename, lines);
    }

    public static void delete(Context context, String filename, int startInclusive, int endExclusive)
    {
        List<String> lines = readAll(context, filename);

        int removeOperations = endExclusive - startInclusive;

        for(int i = 0; i < removeOperations; i++)
        {
            lines.remove(startInclusive);
        }

        write(context, filename, lines);
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

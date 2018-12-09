package com.yellowbite.movienewsreminder2.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.data.OpeningHours;
import com.yellowbite.movienewsreminder2.tasks.AsyncTaskReturnValue;
import com.yellowbite.movienewsreminder2.tasks.SimpleAsyncTask;
import com.yellowbite.movienewsreminder2.util.DateHelper;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenOpeningHours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpeningHoursFragment extends ToolbarFragment
{
    public static final int FRAGMENT_ID = 4;

    public OpeningHoursFragment()
    {
        super(FRAGMENT_ID, R.layout.fragment_opening_hours, 2, "Öffnungszeiten");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.showOpeningHours(view);
    }

    private void showOpeningHours(View view)
    {
        List<TableRow> tableRows = new ArrayList<>();

        TableLayout tableLayout = view.findViewById(R.id.openingHoursTableLayout);

        for(int i = 0; i < tableLayout.getChildCount(); i++)
        {
            View child = tableLayout.getChildAt(i);
            if(child instanceof TableRow)
            {
                tableRows.add((TableRow)child);
            }
        }

        AsyncTaskReturnValue.executeReturnValue(this::getOpeningHours,
                openingHours -> this.showOpeningHours(openingHours, tableRows));
    }

    private OpeningHours getOpeningHours()
    {
        try
        {
            MedZenOpeningHours medZenOpeningHours = new MedZenOpeningHours();
            return medZenOpeningHours.getOpeningHours();
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private void showOpeningHours(OpeningHours openingHours, List<TableRow> tableRows)
    {
        DateHelper.Weekday[] weekdays = DateHelper.Weekday.values();

        for(int i = 0; i < weekdays.length && i < tableRows.size(); i++)
        {
            DateHelper.Weekday weekday = weekdays[i];

            TextView weekdayTextView = (TextView)tableRows.get(i).getChildAt(0);
            weekdayTextView.setText(weekday.toString());

            TextView morningTextView = (TextView)tableRows.get(i).getChildAt(1);
            morningTextView.setText(openingHours.getWeekdayInfo(weekday).getMorningInfo());

            TextView eveningTextView = (TextView)tableRows.get(i).getChildAt(2);
            eveningTextView.setText(openingHours.getWeekdayInfo(weekday).getEveningInfo());
        }
    }
}

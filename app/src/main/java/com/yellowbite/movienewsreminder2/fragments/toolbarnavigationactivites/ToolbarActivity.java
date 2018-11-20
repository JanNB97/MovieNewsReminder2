package com.yellowbite.movienewsreminder2.fragments.toolbarnavigationactivites;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.MovieFileHelper;

public abstract class ToolbarActivity extends AppCompatActivity
{
    protected void setContentViewWithoutTitleBar(@LayoutRes int layoutResID)
    {
        this.setContentView(layoutResID);
        this.addToolbar();
        this.modifyToolbar();
    }

    private void addToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    protected void modifyToolbar(){}

    @Override
    protected void onPause()
    {
        MovieFileHelper.startSaveAllThread();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = super.getMenuInflater();
        inflater.inflate(R.menu.toolbar_items, menu);

        return super.onCreateOptionsMenu(menu);
    }
}

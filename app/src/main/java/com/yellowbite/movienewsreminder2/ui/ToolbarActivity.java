package com.yellowbite.movienewsreminder2.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import com.yellowbite.movienewsreminder2.R;

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
}
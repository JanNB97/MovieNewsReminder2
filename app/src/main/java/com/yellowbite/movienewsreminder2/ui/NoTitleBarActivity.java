package com.yellowbite.movienewsreminder2.ui;

import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

public abstract class NoTitleBarActivity extends AppCompatActivity
{
    protected void setContentViewWithoutTitleBar(@LayoutRes int layoutResID)
    {
        this.removeTitleBar();
        this.setContentView(layoutResID);
    }

    private void removeTitleBar()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        ActionBar actionBar = getSupportActionBar(); //hide the title bar
        if(actionBar != null)
        {
            actionBar.hide();
        }
    }
}

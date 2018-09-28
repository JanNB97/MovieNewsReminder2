package com.yellowbite.movienewsreminder2.ui;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.MenuItem;

import com.yellowbite.movienewsreminder2.R;

public abstract class NavigationDrawerActivity extends ToolbarActivity
{
    private DrawerLayout drawerLayout;

    @Override
    protected void modifyToolbar()
    {
        this.drawerLayout = this.findViewById(R.id.drawer_layout);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

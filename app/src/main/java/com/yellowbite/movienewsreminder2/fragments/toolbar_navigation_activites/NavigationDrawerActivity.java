package com.yellowbite.movienewsreminder2.fragments.toolbar_navigation_activites;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.yellowbite.movienewsreminder2.R;

public abstract class NavigationDrawerActivity extends ToolbarActivity
{
    public static final int NO_ID = -1;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void modifyToolbar()
    {
        this.drawerLayout = this.findViewById(R.id.drawer_layout);
        this.navigationView = this.findViewById(R.id.nav_view);

        this.initNavigationView();

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    private void initNavigationView()
    {
        this.setNavDrawerItemChecked(0);

        this.navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawers();
            item.setChecked(true);
            return handleOnNavItemClicked(item);
        });
    }

    protected void setNavDrawerItemChecked(int i)
    {
        if(i == NO_ID)
        {
            // do nothing
            return;
        }

        this.navigationView.getMenu().getItem(i).setChecked(true);
    }

    abstract protected boolean handleOnNavItemClicked(@NonNull MenuItem item);

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

package com.yellowbite.movienewsreminder2.ui.activites;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.yellowbite.movienewsreminder2.MainActivity;
import com.yellowbite.movienewsreminder2.R;

public abstract class NavigationDrawerActivity extends ToolbarActivity
{
    private DrawerLayout drawerLayout;

    @Override
    protected void modifyToolbar()
    {
        this.drawerLayout = this.findViewById(R.id.drawer_layout);
        this.initNavigationView(this.findViewById(R.id.nav_view));

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    private void initNavigationView(NavigationView navigationView)
    {
        //navigationView.getMenu().getItem(0).setChecked(true);

        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawers();
            return handleOnNavItemClicked(item);
        });
    }

    private boolean handleOnNavItemClicked(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.home:
                if(!(this instanceof MainActivity))
                {
                    this.finish();
                    return true;
                }
                break;
            case R.id.wished_movies:
                WishlistActivity.start(this);
                return true;
        }
        return false;
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

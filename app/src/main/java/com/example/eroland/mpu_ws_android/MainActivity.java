package com.example.eroland.mpu_ws_android;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.example.eroland.mpu_ws_android.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity implements Settings.OnFragmentInteractionListener, PlotFragment.OnFragmentInteractionListener {
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Set a Toolbar to replace the ActionBar.
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        mDrawer = binding.drawerLayout;

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );

        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView nvDrawer = binding.nvView;
        setupDrawerContent(nvDrawer);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.flContent, new PlotFragment());
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                item -> {
                    selectDrawerItem(item);
                    return true;
                }
        );
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.plot_fragment:
                fragmentClass = PlotFragment.class;
                break;
            case R.id.settings_fragment:
                fragmentClass = Settings.class;
                break;
            default:
                //TODO
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("uri", uri.toString());
    }
}

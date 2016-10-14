package com.example.eroland.mpu_ws_android;

import android.content.Context;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.databinding.DataBindingUtil;

import com.github.mikephil.charting.charts.LineChart;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.eroland.mpu_ws_android.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements Settings.OnFragmentInteractionListener, Plot.OnFragmentInteractionListener{
    private DrawerLayout mDrawer;
    private Toolbar myToolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Set a Toolbar to replace the ActionBar.
        myToolbar = binding.toolbar;
        setSupportActionBar(myToolbar);
        mDrawer = binding.drawerLayout;
        drawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                myToolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        nvDrawer = binding.nvView;
        setupDrawerContent(nvDrawer);
        //connectServer();

//        LineChart chart = new LineChart(getBaseContext());
//        chart.setDescription("");
//        chart.setNoDataTextDescription("You need to provide data for the chart.");
//        chart.setTouchEnabled(true);
//        RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_main);
//        rl.addView(chart);
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
    }

    public void selectDrawerItem(MenuItem menuItem){
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()){
            case R.id.plot_fragment:
                fragmentClass = Plot.class;
                Log.d("plot","plot");
                break;
            case R.id.settings_fragment:
                Log.d("settings","settings");
                fragmentClass = Settings.class;
                break;
            default:
                Log.d("plotdefault","plotdefault");
        }
        try{
            fragment = (Fragment) fragmentClass.newInstance();
        }catch(Exception e){
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent,fragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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

    public void connectServer(){
        AsyncHttpClient.getDefaultInstance().websocket("http://148.226.154.198:3000", "client", new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {
                if(ex != null){
                    System.out.println("error");
                    ex.printStackTrace();
                    return;
                }
                System.out.println("conectado");
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String s) {
                        try{
                            JSONObject jObject = new JSONObject(s);
                            JSONArray jLectures = jObject.getJSONArray("lectures");
                            for (int i = 0; i < jLectures.length(); i++){
                                System.out.println(jLectures.get(i));
                            }
                        }catch (JSONException e){
                            //TODO
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("uri",uri.toString());
    }
}

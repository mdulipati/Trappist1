package trappist.aa.com.aa_facet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageSwitcher rowSwitcher;

    Integer[] images = {R.drawable.row31_v4, R.drawable.row32_v2, R.drawable.row33_v2};

    // Need to change the rowSwitcher view every time I received an event from him.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        createImageSwitcher();

        //Execute async task that connects to Database:
        ShowDialogAsyncTask aTask = new ShowDialogAsyncTask();
        aTask.execute();

    }

    /**
     * Create the image switcher objec that will switch between rows when the beacon triggers a
     * row change event.
     */
    private void createImageSwitcher() {
        //instantiate rowSwitcher (image switcher)
        rowSwitcher = (ImageSwitcher) findViewById(R.id.RowSwitcher);
        rowSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView view = new ImageView(getApplicationContext());
                view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                view.setLayoutParams(new ImageSwitcher.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return view;
            }
        });
        /**
         * Set current view to first row. This will be changed as we receive event from change from beacon tech.
         */
        rowSwitcher.setImageResource(images[0]);
    }

    /**
     * Takes in process change event and a) changes the image that is displayed in the image switcher,
     * and b) updates user preferences based upon what is chosen
     */
    private void processRowChangeEvent(RowChangeEvent event){
        rowSwitcher.setImageResource(images[event.getRowNumber() - 1]);
    }

    /**
     * Method designed to connect to the SAP Database. It should retrieve the values to store into
     * each row field for passengers.
     */
    private String connectToSAPDatabase() {
        String urlString = "https://trappist1p1942636699trial.hanatrial.ondemand.com/AmericanAirlines/services/flights.xsodata/passengers?$format=json";
        try {
            System.out.println("Attempting to connect to SAP Database for Passenger preferences...");
            String databaseResponse = "Hi";
            URL databaseURL = new URL(urlString);
            URLConnection conxToDB;
            conxToDB = databaseURL.openConnection();
            System.out.println("DB Conx Opened.");
            conxToDB.setReadTimeout(10000);
            conxToDB.setConnectTimeout(10000);
            BufferedReader in = new BufferedReader(new InputStreamReader(conxToDB.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) databaseResponse += inputLine;
            in.close();
            System.out.println("Received data from database:");
            System.out.println(databaseResponse);
            return databaseResponse;
        } catch (java.io.IOException e) {
            System.out.println("Exception connecting to Database");
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class ShowDialogAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

           String result = connectToSAPDatabase();return result;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("Response from DB: /n"+result);
//            TextView tv = (TextView) findViewById(R.id.MyTextResponse);
//            tv.setText(result);
        }
    }
}


package com.example.sahu.addloc;


import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class MyActivity extends Activity
        implements  AdapterView.OnItemClickListener,NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;


    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */

    private CharSequence mTitle;


    ListView l;
    View view1;
    int val=0;
    TextView temp;
    DbHelperAdapter dbo = new DbHelperAdapter(this);
    ArrayAdapter<String> adapter;
    ArrayList<String> mnames = new ArrayList<String>();
    String[] Lnames;
    String[] lNumber;
    GoogleMap mMap;
    //String[] names1={"Abhilash1","Archana","Noor","Sir Geeta","Manish Kumar","Anand","Chetan","Abhilash","Archana","Noor","Sir Geeta","Manish Kumar","Anand","Chetan"};
    //String[] names2={"Abhilash2","Archana","Noor","Sir Geeta","Manish Kumar","Anand","Chetan","Abhilash","Archana"," Noor","Sir Geeta","Manish Kumar","Anand","Chetan"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        l= (ListView) findViewById(R.id.listView);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        registerForContextMenu(l);


        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        getAll();




    }

    private void getAll() {
        if(val==1){
            Toast.makeText(this,"Deleted Successfully",Toast.LENGTH_SHORT).show();
        }
        Cursor cursor = dbo.all();
        cursor.moveToLast();
        Lnames = new String[cursor.getCount()];
        lNumber = new String[cursor.getCount()];
        int i=0;
        while (!cursor.isBeforeFirst()) {
            String name = cursor.getString(0); //Usn field
            String num = cursor.getString(1);
            double lat=cursor.getDouble(2);
            double lon=cursor.getDouble(3);
            mnames.add(name);
            Lnames[i] = name;
            lNumber[i] = num;
            cursor.moveToPrevious();
            i++;
        }

        CustomListView adapter1 = new CustomListView(this, Lnames,lNumber);
        l.setAdapter(adapter1);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }




    public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v,menuInfo);

        Vibrator v1= (Vibrator) MyActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        v1.vibrate(150);
        if(v.getId()==R.id.listView){

            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.up_del,menu);
            view1= ((AdapterView.AdapterContextMenuInfo)menuInfo).targetView;
            TextView a = (TextView)view1.findViewById(R.id.ContactName);
            //Toast.makeText(MyActivity.this,a.getText().toString(),Toast.LENGTH_SHORT).show();
            temp= a;



        }
    }

    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.action_edit:
                String name= (String) temp.getText();
                Cursor cursor = dbo.query(name);
                cursor.moveToLast();
                String name1,num;
                double lon,lat;
                while (!cursor.isAfterLast()) {
                    name1 = cursor.getString(0);
                    num = cursor.getString(1);
                    lat = cursor.getDouble(2);
                    lon = cursor.getDouble(3);
                    cursor.moveToNext();

                    Intent intent=new Intent(this,AddNew.class);
                    intent.putExtra("Name",name1);
                    intent.putExtra("Num",num);
                    intent.putExtra("lat",lat);
                    intent.putExtra("lon",lon);
                    startActivity(intent);
                    this.finish();
                }



                return true;
            case R.id.action_delete:
                final String string1= (String) temp.getText();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                alertDialogBuilder.setTitle("DELETE!!!");

                alertDialogBuilder
                        .setMessage("Are you Sure Want to Delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                               dbo.Delete(string1);
                                val=1;
                                getAll();

                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();



                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }



    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();



    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);

                l.setOnItemClickListener(this);
                l.setTextFilterEnabled(true);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                l.setOnItemClickListener(this);
                l.setTextFilterEnabled(true);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                l.setOnItemClickListener(this);
                l.setTextFilterEnabled(true);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.my, menu);
            restoreActionBar();
            return true;
        }

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.menu_new) {

            Intent intent = new Intent(this, AddNew.class);
            intent.putExtra("Name","ABC");

            startActivity(intent);
            this.finish();
            return true;
        }




        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView temp = (TextView)view.findViewById(R.id.ContactName);
        String name= (String) temp.getText();
        Cursor cursor = dbo.query(name);
        cursor.moveToLast();
        while (!cursor.isAfterLast()) {
            String num = cursor.getString(0);
            double lat = cursor.getDouble(2);
            double lon = cursor.getDouble(3);
            cursor.moveToNext();


            double lati = 13.0299765, langi = 77.5640579;


            StringBuilder uri = new StringBuilder("http://maps.google.com/maps?saddr=");
            uri.append(lati);
            uri.append(",");
            uri.append(langi);

            if ("myLocatio" != null) {
                uri.append("&daddr=");
                uri.append(Double.toString(lat));
                uri.append(",");
                uri.append(Double.toString(lon));

            }
            //Dialog progress = ProgressDialog.show(this,"","Getting Location",true);
            for (int i1 = 0; i1 < 4; i1++){

                Toast.makeText(MyActivity.this,"Please Wait !! Loading Map ", Toast.LENGTH_LONG).show();
        }
                Intent startMap = new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()));
                startActivity(startMap);

        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MyActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}

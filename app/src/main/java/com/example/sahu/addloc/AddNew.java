package com.example.sahu.addloc;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar;
public class AddNew extends Activity {
    DbHelperAdapter dbHelper;
    EditText name,number;
    TextView textView,textView1;
    double lat=0.0,lon=0.0;
    String mame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        name=(EditText)findViewById(R.id.editText4);
        number=(EditText)findViewById(R.id.Number);
        textView=(TextView)findViewById(R.id.textView3);
        textView1= (TextView) findViewById(R.id.textView);
        dbHelper=new DbHelperAdapter(this);
        mame=getIntent().getStringExtra("Name").toString();
        if(!mame.equals("ABC")) {
            String mnum = getIntent().getStringExtra("Num").toString();
            double lat1 = getIntent().getDoubleExtra("lat", 0.22);
            double lon1 = getIntent().getDoubleExtra("lon", 0.11);
            name.setText(mame);
            number.setText(mnum);
            lat=lat1;
            lon=lon1;
            //Toast.makeText(this, mame + ":" + mnum + ":" + lat1 + ":" + lon1, Toast.LENGTH_LONG).show();
                onSetName();
                textView1.setText("Click on map to Update location");
                textView1.setTextColor(Color.BLUE);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if(requestCode==2 && resultCode == 2) {
           // double lat = Double.parseDouble(data.getStringExtra("Lat"));
            //double lon = Double.parseDouble(data.getStringExtra("Lon"));
               lat=data.getDoubleExtra("Lat",0.11);
               lon=data.getDoubleExtra("Lon",0.22);
               onSetMap();
            //Toast.makeText(this,lat+"and "+lon,Toast.LENGTH_LONG).show();
        }
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                String[] projection1 = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                cursor.moveToFirst();
                Cursor cursor1 = getContentResolver().query(contactUri, projection1, null, null, null);
                cursor1.moveToFirst();
                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name1 = cursor.getString(column);
                int column1 = cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number1 = cursor1.getString(column1);
                name.setText(name1);
                number.setText(number1);
                onSetName();
            }
        }
    }
    public void loc(View view) {
        String Name=name.getText().toString();
        String Number=number.getText().toString();
        if(Name.isEmpty())
        {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            name.startAnimation(shake);
            Vibrator v1= (Vibrator) AddNew.this.getSystemService(Context.VIBRATOR_SERVICE);
            v1.vibrate(150);
            textView.setText("Name is Required Field");
            textView.setTextColor(Color.RED);
        }
        else{
            textView1.setText("");
        }
        if(lat==0.0 && lon==0.0) {
            // textView1.setTextColor();
            ImageButton img = (ImageButton) findViewById(R.id.imageButton);
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            img.startAnimation(shake);
            Vibrator v1 = (Vibrator) AddNew.this.getSystemService(Context.VIBRATOR_SERVICE);
            v1.vibrate(150);
            textView1.setText("Map Must be added");
            textView1.setTextColor(Color.RED);
        }
        else{
            onSetMap();
        }
        if(!Name.isEmpty() && !(lat==0.0 && lon==0.0 ))
        {
            textView1.setText("");
            textView.setText("");
            if(!mame.equals("ABC"))
            {
                dbHelper.Delete(mame);
            }
            long id=dbHelper.insertData(Name,Number,lat,lon);
            if(id<0){
                MyMessage.message(this,"Unsuccessful");
            }
            else{
                Toast.makeText(this,Name+" is added Successfully",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MyActivity.class);
                startActivity(intent);
                this.finish();
            }
        }
    }
    public void onSetMap(){
        textView1.setText("Map has been Added");
        textView1.setTextColor(Color.BLUE);
    }
    public void onSetName(){
        textView.setText("Name has been Successfully Added");
        textView.setTextColor(Color.BLUE);
    }
    public void reset(View v){
        name.setText("");
        number.setText("");
        textView.setText("");
        textView1.setText("");
        lat=0.0;
        lon=0.0;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new, menu);
        return true;
    }
    public void map(View v){
        Intent intent1 = new Intent(this, Map.class);
        startActivityForResult(intent1, 2);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MyActivity.class);
                startActivity(intent);
                this.finish();
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                return true;
            case R.id.action_settings:
                return true;
            case R.id.menu_cont:
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
                startActivityForResult(pickContactIntent, 1);
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
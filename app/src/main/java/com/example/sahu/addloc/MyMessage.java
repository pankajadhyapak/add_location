package com.example.sahu.addloc;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Sahu on 10/30/2014.
 */
public class MyMessage {
    public static void message(Context context,String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}

package com.rishabkanwal.ppicalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.editHorizontal).requestFocus();//Makes sure that the keyboard launches on the Edit text for horizontal pixels
    }
    //If handling orientation change is needed
   /*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {//Handles configuration change
        super.onConfigurationChanged(newConfig);
        TextView p = (TextView) findViewById(R.id.textPPI);
        TextView d = (TextView) findViewById(R.id.textDp);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//Handles orientation change to landscape
            if(p.getText().length() == 0){
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){//Handles orientation change to portrait
            if(p.getText().length() == 0){
            }
        }
    }*/

    //Handles button click for Calculate PPI
    public void onButtonClick(View PPI){
        int horizontal = 0 , vertical= 0;
        float size = 0, ppi;
        double dp;
        boolean DidItWork = true;
        EditText h = (EditText) findViewById(R.id.editHorizontal);
        EditText v = (EditText) findViewById(R.id.editVertical);
        EditText s = (EditText) findViewById(R.id.editSize);
        TextView p = (TextView) findViewById(R.id.textPPI);
        TextView d = (TextView) findViewById(R.id.textDp);
        TextView pi = (TextView) findViewById(R.id.textPixels);
        //Try catch to make sure that the values entered in the text fields are valid
        try{
            horizontal = Integer.parseInt(h.getText().toString());
            vertical = Integer.parseInt(v.getText().toString());
            size = Float.parseFloat(s.getText().toString());
        }catch(Exception e){
            DidItWork = false;
        }finally {
            if(DidItWork){
                ppi = ((float) Math.hypot(vertical, horizontal) / size);
                int ppir = Math.round(ppi);
                p.setText(((Math.round(ppir)) + " PPI"));
                dp = 25.4/(ppi);
                d.setText((double)(Math.round(dp*1000))/1000+" mm dot pitch");
                pi.setText(Integer.toString(horizontal*vertical) + " pixels");
            }
            if (!DidItWork) {
                Toast.makeText(getApplicationContext(), "Please enter values for all fields or click auto detect", Toast.LENGTH_SHORT).show();
            }
        }

    }
    //Handles button clicks for the Clear text button
    public void onButtonClick2(View Clear){
        EditText h = (EditText) findViewById(R.id.editHorizontal);
        EditText v = (EditText) findViewById(R.id.editVertical);
        EditText s = (EditText) findViewById(R.id.editSize);
        TextView p = (TextView) findViewById(R.id.textPPI);
        TextView d = (TextView) findViewById(R.id.textDp);
        TextView pi = (TextView) findViewById(R.id.textPixels);
        d.setText("");
        p.setText("");
        h.setText("");
        v.setText("");
        s.setText("");
        p.setText("");
        pi.setText("");
    }
    //Handles button click for the preset button
    public void onButtonClick3(View preset){
        final String[] presets = {"480p SD(DVD)","720p HD","768p HD", "900p HD","1080p HD","1600p(Quad) HD","2160p(4K) HD"};
        final EditText h = (EditText) findViewById(R.id.editHorizontal);
        final EditText v = (EditText) findViewById(R.id.editVertical);
        AlertDialog.Builder dialogBuilder;
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Choose from presets:");
        dialogBuilder.setItems(presets, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        h.setText("640");
                        v.setText("480");
                        break;
                    case 1:
                        h.setText("1280");
                        v.setText("720");
                        break;
                    case 2:
                        h.setText("1366");
                        v.setText("768");
                        break;
                    case 3:
                        h.setText("1600");
                        v.setText("900");
                        break;
                    case 4:
                        h.setText("1920");
                        v.setText("1080");
                        break;
                    case 5:
                        h.setText("2560");
                        v.setText("1600");
                        break;
                    case 6:
                        h.setText("3840");
                        v.setText("2160");
                        break;
                    default:
                        h.setText("");
                        v.setText("");
                        break;
                }
            }

        });
        AlertDialog dialogPresets = dialogBuilder.create();
        dialogPresets.show();

    }
    //Handles the button click for auto detect
    public void onButtonClick4(View Auto){
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        double size = 0, ppi = 0;
        double dp;
        double nv = 0;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        TextView p = (TextView) findViewById(R.id.textPPI);
        TextView d = (TextView) findViewById(R.id.textDp);
        EditText h = (EditText) findViewById(R.id.editHorizontal);
        EditText v = (EditText) findViewById(R.id.editVertical);
        EditText s = (EditText) findViewById(R.id.editSize);
        TextView pi = (TextView) findViewById(R.id.textPixels);
        h.setText(Integer.toString(width));
        double x = Math.pow(width/metrics.xdpi,2);
        double y = Math.pow(height/metrics.ydpi,2);
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            nv = resources.getDimensionPixelSize(resourceId);
        }
        if (!hasBackKey && !hasHomeKey) {
            height = height + (int)(Math.round(nv));
            y = Math.pow(height/metrics.ydpi,2);
            size = Math.sqrt(x + y);
        }
        else {
            size = Math.sqrt(x+y);
        }
        v.setText(Integer.toString(height));
        ppi = ((float) Math.hypot(width, height) / size);
        int ppir = (int)Math.round(ppi);
        p.setText(((Math.round(ppir))) + " PPI");
        dp = 25.4/(ppi);
        d.setText((double) (Math.round(dp * 1000)) / 1000 + " mm dot pitch");
        s.setText(Double.toString((double)(Math.round(size*100))/100));
        pi.setText(Integer.toString(height*width) + " pixels");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Creates a dialog that shows information about the application.
        if (id == R.id.about) {
            AlertDialog.Builder dialogBuilder;
            dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("About");
            dialogBuilder.setMessage("Made by: Rishab Kanwal\nCurrent release: Version 1.2.1\nRelease date: Dec 25th 2014");
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog about = dialogBuilder.create();
            about.show();
            return true;
        }
        //Creates a dialog with an accuracy disclaimer
        if (id == R.id.accuracy) {
            AlertDialog.Builder dialogBuilder;
            dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Accuracy Disclaimer");
            dialogBuilder.setMessage("This application assumes that pixels are square, which in most cases they are not." +
                    "\nAlso depending on your device auto detect may not be accurate.");
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog accuracy = dialogBuilder.create();
            accuracy.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    

}

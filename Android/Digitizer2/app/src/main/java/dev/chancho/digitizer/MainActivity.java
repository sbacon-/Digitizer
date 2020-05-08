package dev.chancho.digitizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    public final String TAG = "DIGITIZER";

    public DisplayMetrics displayMetrics = new DisplayMetrics();
    public int heightDevice, widthDevice;
    public int widthMonitor, heightMonitor, widthOffset;
    public int canvasWidth, canvasHeight;
    public String mode;
    public boolean settingsShow=false;

    public View[] settings = new View[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        //Derive the Device Resolution
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightDevice = displayMetrics.heightPixels;
        widthDevice = displayMetrics.widthPixels;

        initializeSettings();

        clearView();

        mode = "Draw";
    }
    public boolean onGenericMotionEvent(MotionEvent event){
        if (event.getTouchMajor() > 0) return false; //Return non Stylus Objects
        clearView();
        Log.i(TAG, buildOutput(event.getAxisValue(MotionEvent.AXIS_X),event.getAxisValue(MotionEvent.AXIS_Y),event.getButtonState(),-1));
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getTouchMajor() > 0) return false;
        clearView();
        int action = event.getActionMasked();
        int downState = -1;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downState=0;
                break;
            case MotionEvent.ACTION_UP:
                downState=1;
                break;
        }
        Log.i(TAG, buildOutput(event.getAxisValue(MotionEvent.AXIS_X),event.getAxisValue(MotionEvent.AXIS_Y),event.getButtonState(),downState));

        return true;
    }
    public String buildOutput(float x, float y, int button,int mouseDown){
        StringBuilder builder = new StringBuilder();
        builder.append("X:");
        builder.append((x/widthDevice)*widthMonitor+(widthOffset*widthMonitor));
        builder.append(" Y:");
        builder.append((y/heightDevice)*heightMonitor);
        builder.append(" B:");
        builder.append(button);
        builder.append(" D:");
        builder.append(mouseDown);
        return builder.toString();
    }
    public void clearView(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
    public void initializeSettings(){
        settings[0] = findViewById(R.id.settingsToggle);
        settings[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsToggle();
            }
        });
        settings[1] = findViewById(R.id.sMonitor);
        settings[1].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                widthOffset=widthOffset==0?1:0;
            }
        });
        settings[2] = findViewById(R.id.sWidth);
        settings[3] = findViewById(R.id.sHeight);
        settings[4] = findViewById(R.id.leftRight);
    }
    public void settingsToggle(){
        settingsShow=!settingsShow;
        Switch lrSwitch = (Switch)settings[4];
        LinearLayout layout = findViewById(R.id.linearLayout);
        layout.setGravity(lrSwitch.isChecked()?Gravity.LEFT:Gravity.RIGHT);
        widthMonitor = Integer.parseInt(((TextView)settings[2]).getText().toString());
        heightMonitor = Integer.parseInt(((TextView)settings[3]).getText().toString());
        for(int i=1; i<settings.length; i++) {
            settings[i].setVisibility(settingsShow ? View.INVISIBLE : View.VISIBLE);
        }
    }
}

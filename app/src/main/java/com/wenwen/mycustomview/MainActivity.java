package com.wenwen.mycustomview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    AttriToggleViewListener toggleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleView=findViewById(R.id.tv_toggle);
       /* toggleView.setSwitchBackgroudResource(R.drawable.switch_background);
        toggleView.setSlideButtonResource(R.drawable.slide_button);
        toggleView.setSwitchState(true);*/
        toggleView.setSwitchStateUpdateListener(new AttriToggleViewListener.SwitchStateUpdateListener() {
            @Override
            public void switchStateUpdate(boolean switchState) {
                if (switchState){
                    Toast.makeText(MainActivity.this,"设置为true",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"设置为false",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

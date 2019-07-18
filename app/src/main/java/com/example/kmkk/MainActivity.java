package com.example.kmkk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements IDataReciver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textCtrl = findViewById(R.id.TxtView);
        timer = new Timer();
    }

    private TextView textCtrl;
    private Timer timer;
    private int UpdatePeriod = 30000;

    public void Click3980(View view) {
        timer.cancel();
        timer = new Timer();
        TimerTask update = new WialonParser(this, "*3980*");
        timer.scheduleAtFixedRate(update, 0, UpdatePeriod);
    }

    public void Click3981(View view) {
        timer.cancel();
        timer = new Timer();
        TimerTask update = new WialonParser(this, "*3981*");
        timer.scheduleAtFixedRate(update, 0, UpdatePeriod);
    }

    public void Click3982(View view) {
        timer.cancel();
        timer = new Timer();
        TimerTask update = new WialonParser(this, "*3982*");
        timer.scheduleAtFixedRate(update, 0, UpdatePeriod);
    }

    public void Click3983(View view) {
        timer.cancel();
        timer = new Timer();
        TimerTask update = new WialonParser(this, "*3983*");
        timer.scheduleAtFixedRate(update, 0, UpdatePeriod);
    }

    @Override
    public void NewDataRecived(String data) {
        textCtrl.setText(data);
    }
}

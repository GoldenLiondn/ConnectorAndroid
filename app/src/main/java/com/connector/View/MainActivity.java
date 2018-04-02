package com.connector.View;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.connector.Model.ActivityHandler;
import com.connector.Model.Data;
import com.connector.Model.Player;
import com.connector.Model.Recorder;
import com.connector.Model.TransferedData;
import com.connector.R;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    public static ActivityHandler handler;

    Player p;
    Recorder r;

    TextView lb_phoneNumber;
    public static TextView lb_status;
    Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0, btn_bsp;
    public static Button btn_callEstablish, btn_callEnd;
    public static Button[] buttonsNum;

    private SensorManager mSensorManager;
    private Sensor mProximity;
    private static final int SENSOR_SENSITIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();

    }

    private void initComponents() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        handler = new ActivityHandler(this);
        lb_phoneNumber = (TextView) findViewById(R.id.tf_numberInMainView);
        lb_status = (TextView) findViewById(R.id.lb_status);
        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_bsp = (Button) findViewById(R.id.btn_backspace);
        btn_callEstablish = (Button) findViewById(R.id.btn_callEstablish);
        btn_callEnd = (Button) findViewById(R.id.btn_callEnd);
        btn_callEnd.setEnabled(false);

        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_bsp.setOnClickListener(this);

        buttonsNum = new Button[]{btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_bsp};

        new Thread(new StatusThread()).start();

    }

    public void callEstablish(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (lb_phoneNumber.getText().length() != 9) {
                    handler.sendMessage(handler.obtainMessage(0, getResources().getText(R.string.msg_invalidPhoneNumberQuant)));
                    return;
                }

                try {
                    TransferedData td = Data.serverRequest("request_call", new String[]{lb_phoneNumber.getText().toString()});
                    if (td.action.equals("Fail")) {
                        handler.sendMessage(handler.obtainMessage(0, td.data[0]));
                        setButtonsForCallEnable(true);
                    } else if (td.action.equals("Success")){
                        Data.calling = true;
                        setButtonsForCallEnable(false);
                        init_audio();
                    }

                } catch (Exception e) {
                    Data.isConnected = false;
                    e.printStackTrace();
                }


            }
        }).start();
    }

    public void endCall(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("trying to stop......................................................");
                try {
                    Data.calling = false;
                    setButtonsForCallEnable(true);
                    Data.serverRequest("end_call", new String[]{"end"});
                    System.out.println("end call request sended..........................................");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    Data.isConnected = false;
                    e.printStackTrace();
                }

            }
        }).start();



    }

    public void init_audio() {
        try {
            System.out.println("Trying to init audio..............................................................");
            Data.datagramSocket = new DatagramSocket(Data.myUDPport);

            p = new Player();
            p.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            r = new Recorder();
            p.start();
            r.start();
            System.out.println("audio initialized..............................................................");
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("Exception in init audio..............................................................");
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_0:
                lb_phoneNumber.setText(phoneNumberEdited("0"));
                break;
            case R.id.btn_1:
                lb_phoneNumber.setText(phoneNumberEdited("1"));
                break;
            case R.id.btn_2:
                lb_phoneNumber.setText(phoneNumberEdited("2"));
                break;
            case R.id.btn_3:
                lb_phoneNumber.setText(phoneNumberEdited("3"));
                break;
            case R.id.btn_4:
                lb_phoneNumber.setText(phoneNumberEdited("4"));
                break;
            case R.id.btn_5:
                lb_phoneNumber.setText(phoneNumberEdited("5"));
                break;
            case R.id.btn_6:
                lb_phoneNumber.setText(phoneNumberEdited("6"));
                break;
            case R.id.btn_7:
                lb_phoneNumber.setText(phoneNumberEdited("7"));
                break;
            case R.id.btn_8:
                lb_phoneNumber.setText(phoneNumberEdited("8"));
                break;
            case R.id.btn_9:
                lb_phoneNumber.setText(phoneNumberEdited("9"));
                break;
            case R.id.btn_backspace:
                lb_phoneNumber.setText(phoneNumberEdited("bsp"));
                break;
        }
    }

    public String phoneNumberEdited(String s) {
        String temp = lb_phoneNumber.getText().toString();
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < temp.length(); i++) {
            if (temp.charAt(i) != '-') {
                sb.append(temp.charAt(i));
            }
        }
        StringBuilder res = new StringBuilder("");
        if (sb.length() < 8 && !s.equals("bsp")) {
            sb.append(s);
        } else if (sb.length() > 0 && s.equals("bsp")) {
            sb.deleteCharAt(sb.length() - 1);
        }
        char[] chars = sb.toString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i == 3 || i == 5) res.append('-');
            res.append(chars[i]);
        }

        return res.toString();
    }

    public static void setButtonsForCallEnable(boolean enable) {
        handler.sendMessage(handler.obtainMessage(5, enable));
    }

    public static void setStatus(String status) {
        lb_status.setText(status);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                //near
                Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();

            } else {
                //far
                Toast.makeText(getApplicationContext(), "far", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    class StatusThread implements Runnable {

        @Override
        public void run() {
            TransferedData td = null;
            while (true) {


                while (true) {
                    try {
                        System.out.println("обновление статуса");
                        td = Data.serverRequest("request_status", new String[]{});
                        Data.calling = td.calling;
                        if (td.action.equals("Линия занята") || td.action.equals("Ошибка сервера")) {
                            handler.sendMessage(handler.obtainMessage(3, false));
                            handler.sendMessage(handler.obtainMessage(4, td.action));
                        } else {
                            handler.sendMessage(handler.obtainMessage(3, true));
                            handler.sendMessage(handler.obtainMessage(4, td.action));
                        }
                    } catch (Exception e) {
                        Data.isConnected = false;
                        e.printStackTrace();
                        handler.sendMessage(handler.obtainMessage(4, getResources().getText(R.string.msg_netFail)));
                    } finally {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }



}

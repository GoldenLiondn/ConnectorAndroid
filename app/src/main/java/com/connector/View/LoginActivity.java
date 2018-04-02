package com.connector.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.connector.Model.ActivityHandler;
import com.connector.Model.Data;
import com.connector.Model.TransferedData;
import com.connector.R;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 123;

    public static EditText tf_login, tf_password;
    ActivityHandler handler;
    private Context ctx;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(hasPermissions()) {

            System.out.println("has permissions");
            initComponents();
            new Thread(new ConnectThread()).start();

        }
        else {
            System.out.println("permission haven't had");
            requestPerms();
        }



    }


    public void login(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String login = tf_login.getText().toString();
                String password = tf_password.getText().toString();
                System.out.println("Мы тут......................................................................");
                if(login.isEmpty()) {
                    handler.sendMessage(handler.obtainMessage(2,0));
                    return;
                }
                if(login.length()!=10){
                    handler.sendMessage(handler.obtainMessage(0,getResources().getText(R.string.msg_invalidPhoneNumberQuant10)));
                    return;
                }
                if(!Data.isOnlyNumbers(login)){
                    handler.sendMessage(handler.obtainMessage(0,getResources().getText(R.string.msg_invalidPhoneNumberSimbols)));
                    return;
                }
                if(password.isEmpty()) {
                    handler.sendMessage(handler.obtainMessage(2,1));
                    return;
                }
                try {
                    TransferedData td = Data.serverRequest("login", new String[]{login,password});
                    if (td.action.equals("Success")) {
                        Intent intent = new Intent(ctx, MainActivity.class);
                        startActivity(intent);

                    }
                    if (td.action.equals("Fail")) {
                        handler.sendMessage(handler.obtainMessage(0,td.data[0]));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Data.isConnected = false;
                    handler.sendMessage(handler.obtainMessage(0,"Ошибка сети"));
                    System.out.println("Ошибка сети......................................................................");
                    return;
                }

            }
        }).start();


    }

    public void registration(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void initComponents() {
        ctx = getApplicationContext();
        handler = new ActivityHandler(this);
        tf_login = (EditText) findViewById(R.id.tf_login);
        tf_password = (EditText) findViewById(R.id.tf_passwordInLogin);
        tf_login.setText("0663895768");
        tf_password.setText("222");

    }



    class ConnectThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    if(!Data.isConnected) {
                        InetAddress serverAddr = InetAddress.getByName(Data.SERVER_IP);
                        Data.socket = new Socket(serverAddr, Data.SERVERPORT);
                        Data.oos = new ObjectOutputStream(Data.socket.getOutputStream());
                        Data.ois = new ObjectInputStream(Data.socket.getInputStream());
                        Data.isConnected = true;
                        System.out.println("Сщздан новый сокет.....................................................новый сокет");
                    } else {
                        System.out.println("Сокет уже существует...состояние - "+Data.isConnected);
                    }
                } catch (IOException e1) {
                    System.out.println("Сокет не создан, нет связи с сервером.............................ошибка");
                    Data.isConnected = false;
                    e1.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    private boolean hasPermissions(){
        int res;
        //string array of permissions,
        String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO};

        for (String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode){
            case PERMISSION_REQUEST_CODE:

                for (int res : grantResults){
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }

                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }

        if (allowed){
            //user granted all permissions we can perform our task.
            initComponents();
            new Thread(new ConnectThread()).start();
            System.out.println("ALLOWED...............................................................");
        }
        else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)){
                    System.out.println("sssssssss>>>>>>>>...................................................");
                    Toast.makeText(this, "Storage Permissions denied.", Toast.LENGTH_SHORT).show();

                } else {
                    System.out.println(".......................................................................");
                    //  showNoStoragePermissionSnackbar();
                }
            }
        }

    }


}

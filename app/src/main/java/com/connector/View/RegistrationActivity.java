package com.connector.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.connector.Model.ActivityHandler;
import com.connector.Model.Data;
import com.connector.Model.TransferedData;
import com.connector.R;

public class RegistrationActivity extends AppCompatActivity {

    public static ActivityHandler handler;
    public static EditText[] textFields;
    public String[] tfStrings;


    EditText tf_phoneNumber, tf_name, tf_referal, tf_passwordInReg, tf_passwordConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initComponents();
    }

    private void initComponents() {
        handler = new ActivityHandler(this);
        tf_phoneNumber = (EditText) findViewById(R.id.tf_phoneNumber);
        tf_name = (EditText) findViewById(R.id.tf_name);
        tf_referal = (EditText) findViewById(R.id.tf_referal);
        tf_passwordInReg = (EditText) findViewById(R.id.tf_passwordInReg);
        tf_passwordConfirm = (EditText) findViewById(R.id.tf_passwordConfirm);
        textFields = new EditText[]{tf_phoneNumber, tf_name, tf_referal, tf_passwordInReg, tf_passwordConfirm};
        tfStrings = new String[textFields.length];

    }

    public void registration(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < textFields.length; i++) {
                    tfStrings[i] = textFields[i].getText().toString();
                }
                for (int i = 0; i < tfStrings.length; i++) {
                    if (tfStrings[i].equalsIgnoreCase("")) {
                        handler.sendMessage(handler.obtainMessage(1, i));
                        return;
                    }
                }

                if (tfStrings[0].length() != 10) {
                    handler.sendMessage(handler.obtainMessage(0, getResources().getText(R.string.msg_invalidPhoneNumberQuant10)));
                    return;
                }

                if (!Data.isOnlyNumbers(tfStrings[0])) {
                    handler.sendMessage(handler.obtainMessage(0, getResources().getText(R.string.msg_invalidPhoneNumberSimbols)));
                    return;
                }

                if (!String.valueOf(tfStrings[3]).equalsIgnoreCase(String.valueOf(tfStrings[4]))) {
                    handler.sendMessage(handler.obtainMessage(0, getResources().getText(R.string.msg_invalidConfirm)));
                    return;
                }

                try {
                    System.out.println("tut......................................................");
                    TransferedData td = Data.serverRequest("registration", tfStrings);
                    System.out.println("tut2......................................................");
                    if (td.action.equals("Success")) {
                        System.out.println("tut3......................................................");
                        handler.sendMessage(handler.obtainMessage(0, "Success"));
                        finish();
                    }
                    if (td.action.equals("Fail")) {
                        System.out.println("tut4......................................................");
                        handler.sendMessage(handler.obtainMessage(0, getResources().getText(R.string.msg_regFail) + "\n" + td.data[0]));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Data.isConnected = false;
                    handler.sendMessage(handler.obtainMessage(0, "Ошибка сети"));
                    System.out.println("Ошибка сети......................................................................");
                    return;
                }
            }
        }).start();

    }


}

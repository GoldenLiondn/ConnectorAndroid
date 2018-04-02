package com.connector.Model;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.Toast;

import com.connector.View.LoginActivity;
import com.connector.View.MainActivity;
import com.connector.View.RegistrationActivity;

/**
 * Created by Maks on 31.03.2018.
 */

public class ActivityHandler extends Handler {
    public static final int DIALOG_MSG = 0;
    public static final int SETFOCUSREG = 1;
    public static final int SETFOCUSLOG = 2;
    public static final int CHANGECALLSTATE = 3;
    public static final int SETSTATUS = 4;
    public static final int SETBUTTONENABLED = 5;

    private Activity parent;

    public ActivityHandler(Activity parent) {
        super();
        this.parent = parent;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case DIALOG_MSG:
                Toast toast = Toast.makeText(parent.getApplicationContext(),
                        (String) msg.obj, Toast.LENGTH_SHORT);
                toast.show();
                break;
            case SETFOCUSREG:
                for (int i = 0; i < RegistrationActivity.textFields.length; i++) {
                    if (i == ((int) msg.obj)) {
                        RegistrationActivity.textFields[i].requestFocus();
                    }
                }
                break;
            case SETFOCUSLOG:

                if ((int) msg.obj == 0) {
                    LoginActivity.tf_login.requestFocus();
                }
                if ((int) msg.obj == 1) {
                    LoginActivity.tf_password.requestFocus();
                }

                break;
            case CHANGECALLSTATE:
                if((boolean) msg.obj){
                    MainActivity.setButtonsForCallEnable(true);
                } else{
                    MainActivity.setButtonsForCallEnable(false);
                }

                break;

            case SETSTATUS:
                MainActivity.setStatus((String) msg.obj);
                break;
            case SETBUTTONENABLED:
                if ((boolean) msg.obj) {
                    for (Button btn : MainActivity.buttonsNum)
                        btn.setEnabled(true);
                    MainActivity.btn_callEstablish.setEnabled(true);
                    //  btn_callEstablish.setImageResource(R.drawable.call);
                    MainActivity.btn_callEnd.setEnabled(false);
                    //   btn_callEnd.setImageResource(R.drawable.end_disable);

                } else {
                    for (Button btn : MainActivity.buttonsNum)
                        btn.setEnabled(false);
                    MainActivity.btn_callEstablish.setEnabled(false);
                    //      btn_callEstablish.setImageResource(R.drawable.call_disable);
                    MainActivity.btn_callEnd.setEnabled(true);
                    //     btn_callEnd.setImageResource(R.drawable.end);
                }



                break;

            default:
                super.handleMessage(msg);
        }

    }
}
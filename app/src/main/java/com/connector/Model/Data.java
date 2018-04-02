package com.connector.Model;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Maks on 31.03.2018.
 */

public class Data {

    public static boolean calling = false;
    public static boolean isConnected = false;

    public static volatile Socket socket;
    public static volatile ObjectInputStream ois;
    public static volatile ObjectOutputStream oos;
    public static final int SERVERPORT = 1010;
    public static final String SERVER_IP = "192.168.0.88";

    public static DatagramSocket datagramSocket;

    public static final int myUDPport = 50003;
    public static final int portUDPToSend = 50002;


    public static synchronized TransferedData serverRequest(String request, String[] data) throws IOException, ClassNotFoundException, NullPointerException {
        Data.oos.writeObject(new TransferedData(request, data));
        Data.oos.flush();
        return (TransferedData) Data.ois.readObject();
    }

    public static boolean isOnlyNumbers(String text) {
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] < 48 || chars[i] > 58)
                return false;
        }
        return true;
    }

}





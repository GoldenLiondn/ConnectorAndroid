package com.connector.Model;

/**
 * Created by Maks on 31.03.2018.
 */

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;

import java.net.DatagramPacket;
import java.net.InetAddress;


public class Recorder extends Thread {

    int internalBufferSize;
    byte buff[] = new byte[2200];
    public InetAddress serverInetAddress;
    private AudioRecord audioRecord;

    @Override
    public void run() {
        try {
            System.out.println("Recorder created................................................................");
            Long pack = 0L;
            serverInetAddress = InetAddress.getByName(Data.SERVER_IP);
            createAudioRecorder();
            audioRecord.startRecording();
            System.out.println("recording started................................................................");
            while (Data.calling) {
                int read = audioRecord.read(buff, 0, buff.length);
                DatagramPacket data = new DatagramPacket(buff, buff.length, serverInetAddress, Data.portUDPToSend);
                System.out.println("send: #" + (pack++) + "ip " + Data.SERVER_IP + " port" + Data.portUDPToSend);
                Data.datagramSocket.send(data);
            }
            System.out.println("recording stopped................................................................");
            audioRecord.stop();
            audioRecord.release();
            Data.datagramSocket.close();
            System.out.println("Socket closed");
            System.out.println("Recorder closed...............................................................");
        } catch (Exception e) {
            System.out.println("some recording exception.....................................................");
            Data.datagramSocket.close();
            e.printStackTrace();
        }
    }


    void createAudioRecorder() {

        int sampleRate = 44100;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int minInternalBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        internalBufferSize = minInternalBufferSize * 4;
        System.out.println(Build.VERSION.SDK_INT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, internalBufferSize);

    }

}
package com.connector.Model;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.connector.View.MainActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author duong
 */
public class Player extends Thread {
	public AudioManager audioManager;
	byte[] buffer = new byte[2200];


	@Override
	public void run() {
		try {
			System.out.println("Player created................................................................");
			audioManager.setSpeakerphoneOn(false);
			audioManager.setMicrophoneMute(false);

		//	System.out.println(audioManager.isSpeakerphoneOn());
			DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
			System.out.println("Server socket created. Waiting for incoming data...");
			Long pack = 0L;
			AudioTrack aTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL,
					44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
					buffer.length, AudioTrack.MODE_STREAM);
			if(aTrack!=null) {
				System.out.println("Audiotrack created.................................................");

			}
			else System.out.println("Audiotrack DON'T created.................................................");
			aTrack.play();
			if(aTrack!=null) {
				System.out.println("Audiotrack created.................................................");

			}
			else System.out.println("Audiotrack DON'T created.................................................");
			System.out.println("player started................................................................");
			while (Data.calling) {
				System.out.println("1.....................................");
				Data.datagramSocket.receive(incoming);
				System.out.println("2.....................................");
				buffer = incoming.getData();
				System.out.println("3.....................................");
				aTrack.write(buffer, 0, buffer.length);
				System.out.println("receive from: #" + (pack++) + "ip " + incoming.getSocketAddress() + " port"
						+ incoming.getPort());
			}
			System.out.println("playing stopped................................................................");
			aTrack.stop();
			System.out.println("player closed...............................................................");
		} catch (IOException e) {
			System.out.println("some playing exception.....................................................");
			Data.calling = false;
		}

	}


}
package com.bornbytes.socketconnectionclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Client extends Activity {
 
    private Socket socket;
 
    private static int SERVERPORT = 5733;
    private static String SERVER_IP = "192.168.6.1";

    Thread thread;

    TextView tvMessages;
    EditText et;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        tvMessages = findViewById(R.id.tvMessages);
        et = (EditText) findViewById(R.id.EditText01);
        String ip = getIntent().getStringExtra("ip");

        if(!TextUtils.isEmpty(ip)){
            SERVER_IP = ip;
        }
        SERVERPORT = getIntent().getIntExtra("port", SERVERPORT);


        Log.e("find",""+SERVER_IP+">>>>"+SERVERPORT);
        new Thread(new Thread1()).start();


    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if(socket!=null){
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        try {

            String str = et.getText().toString();
            new Thread(new Thread3(str)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PrintWriter output;
    private BufferedReader input;
    class Thread1 implements Runnable {
        public void run() {
            Socket socket;
            try {
                socket = new Socket(SERVER_IP, SERVERPORT);
                output = new PrintWriter(socket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvMessages.setText("Connected\n");
                    }
                });
                new Thread(new Thread2()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Thread2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    final String message = input.readLine();
                    if (message!=null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvMessages.append("server: " + message + "\n");
                            }
                        });
                    } else {
                         new Thread(new Thread1()).start();

                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Thread3 implements Runnable {
        private String message;
        Thread3(String message) {
            this.message = message;
        }
        @Override
        public void run() {
            output.write(message);
            output.flush();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvMessages.append("server: " + message + "\n");
                    et.setText("");
                }
            });
        }
    }
 

}
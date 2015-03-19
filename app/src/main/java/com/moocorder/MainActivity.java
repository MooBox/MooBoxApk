package com.moocorder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;


public class MainActivity extends ActionBarActivity {

    private ImageButton imageButton;
    private ImageButton greenButton;
    private ImageButton redButton;
    private static Socket socket;
    private String host = "192.168.0.10";
    private int port = 80;
    private String username = "Anonymous";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.imageButton = (ImageButton) this.findViewById(R.id.mooButton);
        this.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand("1&2");
            }
        });
        this.greenButton = (ImageButton) this.findViewById(R.id.greenButton);
        this.greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand("1");
            }
        });
        this.redButton = (ImageButton) this.findViewById(R.id.redButton);
        this.redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand("2");
            }
        });
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = defaultSharedPreferences.getString("username", username);

        host = defaultSharedPreferences.getString("url", host);
        port = Integer.valueOf(defaultSharedPreferences.getString("port", port + ""));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendCommand(final String param) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {

                    InetAddress address = InetAddress.getByName(host);
                    InetSocketAddress inetSocketAddress = new InetSocketAddress(address, port);

                    socket = new Socket();
                    socket.connect(inetSocketAddress, 4000);

                    //Send the message to the server
                    OutputStream os = socket.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    BufferedWriter bw = new BufferedWriter(osw);


                    String sendMessage = param;
                    bw.write(sendMessage);
                    bw.newLine();
                    bw.flush();
                    BufferedReader in =
                            new BufferedReader(
                                    new InputStreamReader(socket.getInputStream()));
                    String response = in.readLine();

                    System.out.println("Message sent to the server : " + sendMessage + " Response : "+response);
                    Toast toast = Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG);
                    toast.show();


                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                finally {
                    //Closing the socket
                    try {
                        if (socket != null && socket.isConnected()) {
                            socket.close();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Looper.loop();
            }
        });
        thread.start();
    }
}

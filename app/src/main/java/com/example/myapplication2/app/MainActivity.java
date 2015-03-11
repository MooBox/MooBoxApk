package com.example.myapplication2.app;

import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class MainActivity extends ActionBarActivity {
    
    ImageButton imageButton;
    private static Socket socket;

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

                try
                {
                    String host = "192.168.0.10";
                    int port = 8091;
                    InetAddress address = InetAddress.getByName(host);
                     socket = new Socket(address, port);

                    //Send the message to the server
                    OutputStream os = socket.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    BufferedWriter bw = new BufferedWriter(osw);

                    String number = "2";

                    String sendMessage = number + "\n";
                    bw.write(sendMessage);
                    bw.flush();
                    System.out.println("Message sent to the server : "+sendMessage);

                    //Get the return message from the server
                    InputStream is = socket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String message = br.readLine();
                    System.out.println("Message received from the server : " +message);
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
                finally
                {
                    //Closing the socket
                    try
                    {
                        socket.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

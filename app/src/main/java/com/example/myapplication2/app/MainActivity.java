package com.example.myapplication2.app;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;


public class MainActivity extends ActionBarActivity {

    private ImageButton imageButton;
    private ImageButton greenButton;
    private ImageButton redButton;
    private static Socket socket;
    private String host = "192.168.0.10";
    private int port = 8091;

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

    public void sendCommand(String param)
    {
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
            bw.flush();
            System.out.println("Message sent to the server : " + sendMessage);

        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            //Closing the socket
            try {
                if(socket!=null && socket.isConnected()) {
                    socket.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

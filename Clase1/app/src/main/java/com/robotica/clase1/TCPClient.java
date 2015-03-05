package com.robotica.clase1;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {

    private String serverMessage;
    private String ipAddress;
    public static final int SERVERPORT = 2000;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    public boolean con;
    public boolean con2;

    //TextView textViewc = (TextView) findViewById(R.id.textView5);



    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(String ipAddress, OnMessageReceived listener) {
        this.ipAddress = ipAddress;
        mMessageListener = listener;
    }


    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }

    public String stopClient() throws Throwable{
        mRun = false;
        socket.close();
        con2=socket.isClosed();
        if(con2==true){
            con=false;
            return "*desconectado*";

        }
        return null;
    }

    public String conection() {

        if(con==true){
            return "¡conectado OK¡";
        }
        return "error en la conexion";
    }

    public void IP(String ip){
        ipAddress =ip;
        Log.e("TCP Client", ipAddress);
    }


    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(ipAddress);

            Log.e("TCP Client", "C: Connecting...");
            //create a socket to make the connection with the server
            socket = new Socket(serverAddr, SERVERPORT);

            try {

                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                Log.e("TCP Client", "C: Sent.");
                //_tv_estado.setText("Estado: Conectado");
                Log.e("TCP Client", "C: Done.");
                //textViewc.setText("Done.");
                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                if(socket.isConnected()==true){
                    con=true;
                }
                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage = in.readLine();

                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;

                }


                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");


            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();

            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }


    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}
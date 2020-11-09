package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/* Iterative version of TCP Server, after accepting connection server will listen for clients messaged.
   After client disconnects server will ask if it should wait for next client.
   Only 1 client at a time may be connected to server.
 */

public class Iterative_TCP_Server {
    ServerSocket myServer = null;
    Socket myClient = null;
    int port;

    public Iterative_TCP_Server(int port) {
        this.port = port;
    }

    private void serverStart(int port) {
        try {
            if(this.myServer != null) {
                this.myServer.close();
            }
            this.myServer = new ServerSocket(port);
        } catch(Exception e){
            this.myServer = null;
            System.out.println(e.getMessage());
        }
    }

    private void serverStop() {
        try {
            this.myServer.close();
            System.out.println("Server disconnected!");
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void serverRun() {
        serverStart(this.port);
        System.out.println("Server started on port: " + this.myServer.getLocalPort());
        try {
            System.out.println("waiting for connection...");
            boolean flag = true;
            while(true) {
                try {
                    while (true) {
                        if (myClient == null) {
                            myClient = this.myServer.accept();
                        } else if (myClient.isConnected()) {
                            //Type "connected" message once.
                            if (flag) {
                                System.out.println(this.myClient.getInetAddress().getHostName() + " connected from port " + this.myClient.getPort());
                                flag = false;
                            }

                            byte[] message = new byte[1024];
                            DataInputStream toReceive = new DataInputStream(this.myClient.getInputStream());
                            DataOutputStream toSend = new DataOutputStream(this.myClient.getOutputStream());
                            int bytesReceived = toReceive.read(message);
                            String messageString = new String(message, 0, bytesReceived);
                            System.out.println(this.myClient.getInetAddress().getHostName() + " - "
                                    + this.myClient.getInetAddress().getHostAddress() + " on Port " +
                                    +this.myClient.getPort() + " - " + messageString + " (" + bytesReceived + " bytes)");
                            if (messageString.contains("closeConnection")) {
                                System.out.println("Disconnecting " + this.myClient.getInetAddress().getHostName());
                                myClient.close();
                                myClient = null;
                                flag = true;
                                break;
                            }

                            toSend.write(message, 0, bytesReceived);
                            toSend.flush();
                        }
                    }
                } catch(Exception e) {
                    myClient = null;
                    flag = true;
                    System.out.println(e.getMessage());
                    String nextClient;
                    Scanner myScanner = new Scanner(System.in);
                    System.out.println("Wait for next client? Y/N");
                    nextClient = myScanner.nextLine();
                    if(!(nextClient.equalsIgnoreCase("y"))){
                        break;
                    }
                }

                String nextClient;
                Scanner myScanner = new Scanner(System.in);
                System.out.println("Wait for next client? Y/N");
                nextClient = myScanner.nextLine();
                flag = true;
                if(!(nextClient.equalsIgnoreCase("y"))){
                    break;
                } else {
                    System.out.println("waiting for connection...");
                }

            }

            serverStop();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


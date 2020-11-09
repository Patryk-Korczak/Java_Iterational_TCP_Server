package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Enter port: ");
        int port = myScanner.nextInt();
        Iterative_TCP_Server myServer = new Iterative_TCP_Server(port);
        myServer.serverRun();
    }
}


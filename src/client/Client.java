package client;
/*
Client Class
 */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {

    public static void main(String args[]) {

        DatagramSocket socket;
        DatagramPacket packet;
        InetAddress address;

        String input = "test message input";
        byte[] message = input.getBytes();
        int port = 6788;

        try {
            socket = new DatagramSocket();
            InetAddress host = InetAddress.getByName("localhost");

            // create request and send packet
            DatagramPacket request = new DatagramPacket(message, message.length, host, port);
            socket.send(request);

            // store reply in buffer and receive reply
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            socket.receive(reply);
            System.out.println("Client Received: " + new String(reply.getData()));

            socket.close();
        }
        catch(Exception e){

        }
    }
}
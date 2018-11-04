package server;
/*
Client Class
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Server {

    public static void main(String args[]) {
        DatagramSocket socket;
        int clientPort = 6788;
        try{
            socket = new DatagramSocket(clientPort);
            byte[] data = new byte[1000];

            System.out.println("Server Running...");

            while(true) {
                DatagramPacket request = new DatagramPacket(data, data.length);

                socket.receive(request);
                String [] arrayMsg = (new String(request.getData())).split(" ");

                byte[] replyMsg = ("Server Received at index 0: " + arrayMsg[0]).getBytes();
                InetAddress address = request.getAddress();
                int port = request.getPort();
                DatagramPacket reply = new DatagramPacket(replyMsg, replyMsg.length, address, port);
                socket.send(reply);

            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
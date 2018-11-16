/*
Client Class
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;

public class Server {

    public static void main(String args[]) throws IOException, ClassNotFoundException {

        // TCP
        ServerSocket servSocket;
        Socket fromClientSocket;

        int  tcpPort = 8888;
        String str;
        Item item;

        DatagramSocket socket;
        int clientPort = 6788;

        servSocket = new ServerSocket(tcpPort);
        System.out.println("Waiting for connection on port " + tcpPort);

        fromClientSocket = servSocket.accept();

        ObjectOutputStream out = new ObjectOutputStream(fromClientSocket.getOutputStream());

        ObjectInputStream in = new ObjectInputStream(fromClientSocket.getInputStream());

        while ((item = (Item) in.readObject()) != null) {
            item.printInfo();

            out.writeObject("bye bye");
            break;
        }
        out.close();

        fromClientSocket.close();

//        try{
//            socket = new DatagramSocket(clientPort);
//            byte[] data = new byte[1000];
//
//            System.out.println("Server Running...");
//
//            while(true) {
//                DatagramPacket request = new DatagramPacket(data, data.length);
//
//                socket.receive(request);
//                String [] arrayMsg = (new String(request.getData())).split(" ");
//
//                byte[] replyMsg = ("Server Received at index 0: " + arrayMsg[0]).getBytes();
//                InetAddress address = request.getAddress();
//                int port = request.getPort();
//                DatagramPacket reply = new DatagramPacket(replyMsg, replyMsg.length, address, port);
//                socket.send(reply);
//
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
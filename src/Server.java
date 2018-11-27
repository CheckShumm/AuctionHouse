/*
Client Class
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;

@SuppressWarnings("ALL")
public class Server {

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        int  tcpPort = 8888;
        ServerSocket ss = new ServerSocket(tcpPort);
        Socket socket = null;
        while(true)
        try {
            socket = ss.accept();
            ClientHandler clientHandler = new ClientHandler(socket);
            Thread thread = new ClientHandler(socket);
        } catch (Exception e) {
            socket.close();
            e.printStackTrace();
        }



    }

    public void udpServer() {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
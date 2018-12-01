/*
Client Class
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("ALL")
public class Server {

    private static int  tcpPort = 3333;

    private  ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private  ServerSocket ss;
    private  Socket socket = null;
    private  Boolean isRunning = true;
    public  AuctionTimer auctionTimer;

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        new Server();
    }

    public Server() throws IOException {
        ss = new ServerSocket(tcpPort);
        System.out.println("Server is running");
        auctionTimer = new AuctionTimer();

            while(isRunning)
                try {
                socket = ss.accept();
                ClientHandler clientHandler = new ClientHandler(socket,this);
                clientHandler.start();
                auctionTimer.start();
                clientHandlers.add(clientHandler);
            } catch (Exception e) {
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

    public  ArrayList<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

}
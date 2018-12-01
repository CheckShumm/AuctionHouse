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

    //gets configuration from config.properties file
    private Environment env = new Environment();

    InetAddress serverAddress;

    private int serverUDPPort;
    private int serverTCPPort;

    public static ClientHandlers clientHandlers;

    private  ServerSocket ss;
    private  Socket socket = null;
    private  Boolean isRunning = true;
    public  static AuctionTimer auctionTimer;

    DatagramSocket udpSocket;

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        new Server();
    }

    public Server() throws IOException {

        serverUDPPort = Integer.parseInt(env.get("SERVER_PORT_UDP", "3332"));
        serverTCPPort = Integer.parseInt(env.get("SERVER_PORT_TCP", "3333"));

        udpSocket = new DatagramSocket(serverUDPPort, serverAddress);
        ss = new ServerSocket(serverTCPPort);

        clientHandlers = ClientHandlers.getInstance();

        auctionTimer = new AuctionTimer();
        auctionTimer.run();

        System.out.println("Server is running");

        try {
            Thread tcpSockets = new Thread () {
                public void run () {
                    try {
                        socket = ss.accept();
                        ClientHandler clientHandler = new ClientHandler(socket);
                        clientHandlers.add(clientHandler);
                        clientHandler.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            tcpSockets.start();
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

}
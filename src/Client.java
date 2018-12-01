/*
Client Class
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Logger;

public class Client {

    //gets configuration from config.properties file
    private Environment env = new Environment();
    private User user;
    private Message msg = new Message();
    
    private ObjectOutputStream oos;
    private DataOutputStream dos;

    private ObjectInputStream ois;
    private DataInputStream dis;

    InetAddress serverAddress;
    InetAddress clientAddress;

    private int serverUDPPort;
    private int serverTCPPort;

    private int clientUDPPort;
    private int clientTCPPort;

    private byte[] buf = new byte[256];

    private boolean isOn = true;

    public Client() {

        try {
            //set client configuration
            clientAddress = InetAddress.getByName("localhost");
            serverAddress = InetAddress.getByName(env.get("SERVER_ADDRESS", "localhost"));
//            serverUDPPort = Integer.parseInt(env.get("SERVER_PORT_UDP", "3332"));
//            serverTCPPort = Integer.parseInt(env.get("SERVER_PORT_TCP", "3333"));

            clientUDPPort = Integer.parseInt(env.get("SERVER_PORT_UDP", "3332"));
            clientTCPPort = Integer.parseInt(env.get("SERVER_PORT_TCP", "3333"));

            this.user = new User();

            //create and initialize sockets
            load();
        } catch(Exception e) {
            System.out.println("Server is not found");
            e.printStackTrace();
        }

    }

    private void load() {

        try {
            Scanner in = new Scanner(System.in);
            System.out.println("Client IP: " + clientAddress);

            DatagramSocket udpSocket = new DatagramSocket();

            Socket socket = new Socket(serverAddress, clientTCPPort);

            oos = new ObjectOutputStream(socket.getOutputStream());
//            dos = new DataOutputStream(socket.getOutputStream());

            // let server know client is connected
            msg.setType("CONNECT");
            // oos.writeObject(msg);
            oos.flush(); // flush stream

            ois = new ObjectInputStream(socket.getInputStream());
//            dis = new DataInputStream(socket.getInputStream());
            System.out.println("here client");
            DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, serverUDPPort);

            while (true) {
                // asks for user input
                System.out.println(printOptions());

                // reads user input
                String option = in.nextLine();
                option = option.toUpperCase();

                // decides what to do based on user input
                switch(option) {
                    case MessageType.REGISTER:
                        msg.setType(MessageType.OFFER);
                        msg.setUser(registerForm());

                        InetAddress address = packet.getAddress();
                        int port = packet.getPort();

                        byte[] msgByte = Help.serialize(msg);
                        packet = new DatagramPacket(msgByte, msgByte.length, serverAddress, serverUDPPort);
                        udpSocket.send(packet);

                        packet = new DatagramPacket(buf, buf.length);
                        udpSocket.receive(packet);
                        msg = (Message)Help.deserialize(packet.getData());

                        System.out.println(msg + "\n");
                        break;
                    case MessageType.OFFER:
                        msg.setItem(offer());
                        msg.setType(MessageType.OFFER);
                        oos.writeObject(msg);
                        oos.flush();

                        msg = (Message) ois.readObject();
                        System.out.println(msg + "\n");
                        System.out.println(msg.getItem().getStartTime());
                        break;
                    case "exit":
                        System.out.println("Exiting the auction!");
                        ois.close();
                        oos.close();
                        dis.close();
                        dos.close();
                        socket.close();
                        break;
                }
            }
        }
        catch (ConnectException e) {
            System.out.println("Unable to connect to server with tcp");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private Item offer() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter name of the item?");
        String itemName = in.nextLine();

        System.out.println("Please give a short description of your item.");
        String itemDescription = in.nextLine();

        System.out.println("What is the starting bid for " + itemName);
        double minPrice = in.nextDouble();

        Item item = new Item(itemName, user, itemDescription, minPrice);

        return item;
    }

    private User registerForm() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter username");
        String username = in.nextLine();

        User user = new User();
        user.setUsername(username);
        return user;
    }
    
    private String printOptions() {

        System.out.println("Welcome to Auction House!");
        if (this.user.isAuth())
        {
            return "Enter 'register' to get access to auction";
        } else {
            return ("Enter 'offer' to put an item up for auction\n"+
                    "Enter 'bid' if you would like to bid on an item\n"+
                    "Enter 'exit' if you would like to exit");
        }

    }

    public static void main(String args[]) {
        Client client = new Client();
    }
    
   
}
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

    public static User user;

    private Message msg = new Message();
    private Message inputMessage = new Message();

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ObjectInputStream globalInputStream;

    InetAddress serverAddress;
    InetAddress clientAddress;

    DatagramPacket packet;

    private int serverUDPPort;
    private int serverTCPPort;

    private int clientUDPPort;
    private int clientTCPPort;

    //UDP buffer
    private byte[] buf = new byte[256];

    public static boolean isOn = true;

    public static boolean isAuth = false;
    public static boolean wait = false;

    public Client() {

        try {
            //set client configuration
            clientAddress = InetAddress.getByName("localhost");
            serverAddress = InetAddress.getByName(env.get("SERVER_ADDRESS", "localhost"));
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

            DatagramSocket udpSocket = new DatagramSocket();
            Socket socket = new Socket(serverAddress, clientTCPPort);

            oos = new ObjectOutputStream(socket.getOutputStream());

            // let server know client is connected
            msg.setType("CONNECT");
            oos.flush(); // flush stream
            System.out.println("Welcome to Auction House!");

            Listener listener = new Listener(socket,this);
            UDPListener udpListener = new UDPListener(udpSocket);

            Thread menu = new Thread(() -> {

                while (true) {
                    if(!Client.wait || true) {
//                        System.out.println("client wait:" + Client.wait);
                        try {
                            Client.wait = true;

                            // asks for user input
                            System.out.println(printOptions());

                            // reads user input
                            String option = in.nextLine();
                            option = option.toUpperCase();
                            byte[] msgByte;

                            // decides what to do based on user input
                            switch (option) {
                                case MessageType.REGISTER:
                                    msg.setType(MessageType.REGISTER);
                                    msg.setUser(registerForm());

                                    msgByte = Help.serialize(msg);
                                    packet = new DatagramPacket(msgByte, msgByte.length, serverAddress, clientUDPPort);
                                    udpSocket.send(packet);
//                                    Client.wait = true;
                                    Thread.sleep(1000);

                                    break;
                                case MessageType.LOGIN:
                                    msg.setType(MessageType.LOGIN);
                                    msg.setUser(registerForm());
                                    msgByte = Help.serialize(msg);

                                    packet = new DatagramPacket(msgByte, msgByte.length, serverAddress, clientUDPPort);
                                    udpSocket.send(packet);
//                                    Client.wait = true;
                                    Thread.sleep(1000);

                                    break;
                                case MessageType.OFFER:
                                    int offerCount = 0;
                                    while(!inputMessage.getType().equals(MessageType.OFFER_CONFIRM) & offerCount < 3) {
                                        msg.setItem(offer());
                                        msg.setType(MessageType.OFFER);
                                        System.out.println("Offering " + msg.getItem().getName() + " to the Auction House");
                                        oos.writeUnshared(msg);
                                        oos.flush();
                                        offerCount++;
                                        Thread.sleep(1000);
                                    }
                                    inputMessage.setType(MessageType.NULL);
                                    break;
                                case MessageType.BID:
                                    msg.setType(MessageType.BID);
                                    bid();
                                    System.out.println("Bidding on " + msg.getItemName());
                                    oos.writeUnshared(msg);
                                    oos.flush();
                                    break;
                                case "exit":
                                    System.out.println("Exiting the auction!");

//                                    msg.setType(MessageType.DEREGISTER);
//                                    msgByte = Help.serialize(msg);
//                                    packet = new DatagramPacket(msgByte, msgByte.length, serverAddress, clientUDPPort);
//                                    udpSocket.send(packet);
//                                    Client.wait = true;
//                                    Thread.sleep(1000);

                                    ois.close();
                                    oos.close();
                                    socket.close();
                                    break;
                            }
//                            System.out.println("client wait:" + Client.wait);
                        } catch (IOException e) {
                            System.out.println("Unable to connect to server.");
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            listener.start();
            udpListener.start();
            menu.start();
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
        System.out.println("What is the name of your item?");
        String itemName = in.nextLine();

        System.out.println("Please give a short description of your item.");
        String itemDescription = in.nextLine();

        System.out.println("What is the starting price for " + itemName);
        double minPrice = in.nextDouble();

        Item item = new Item(itemName, user, itemDescription, minPrice);

        return item;
    }

    private void bid() {
        Scanner in = new Scanner(System.in);
        System.out.println("What item would you like to bid on?");
        String itemName = in.nextLine();
        msg.setItemName(itemName);

        System.out.println("How much would you like to offer?");
        double amount = in.nextDouble();
        msg.setAmount(amount);

    }

    private User registerForm() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter username");
        String username = in.nextLine();

        System.out.println("Enter password");
        String password = in.nextLine();

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }
    
    private String printOptions() {

        if (!user.isAuth())
        {
            return ("Enter 'register' to register an account\n"+
                    "Enter 'login' to login to your account\n");
        } else {
            return ("Enter 'offer' to put an item up for auction\n"+
                    "Enter 'bid' if you would like to bid on an item\n"+
                    "Enter 'exit' if you would like to exit\n");
        }

    }

    public void setInputMessage(Message inputMessage) {
        this.inputMessage = inputMessage;
    }

//    public static void setAuthenticated(boolean isAuth) {
//        isAuth = isAuth;
//    }

    public void setUser(User user) {
        this.user = user;
    }

    public static void main(String args[]) {
        Client client = new Client();
    }
    
   
}
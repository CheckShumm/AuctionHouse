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

    private boolean isOn = true;

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

            ois = new ObjectInputStream(socket.getInputStream());

            System.out.println("Welcome to Auction House!");

            Listener listener = new Listener(socket);
            listener.start();

            Thread menu = new Thread(() -> {
                while (true) {
                    try {
                        // asks for user input
                        System.out.println(printOptions());

                        // reads user input
                        String option = in.nextLine();
                        option = option.toUpperCase();

                        // decides what to do based on user input
                        switch (option) {
                            case MessageType.REGISTER:
                                msg.setType(MessageType.REGISTER);
                                msg.setUser(registerForm());
                                byte[] msgByte = Help.serialize(msg);
                                packet = new DatagramPacket(msgByte, msgByte.length, serverAddress, clientUDPPort);
                                udpSocket.send(packet);

                                packet = new DatagramPacket(buf, buf.length);
                                udpSocket.receive(packet);
                                msg = (Message) Help.deserialize(packet.getData());
                                user = msg.getUser();
                                System.out.println(msg + "\n");
                                break;
                            case MessageType.OFFER:
                                msg.setItem(offer());
                                msg.setType(MessageType.OFFER);
                                oos.writeObject(msg);
                                oos.flush();
                                System.out.println("Hanging");
                                msg = (Message) ois.readObject();
                                System.out.println("Still Hanging");
                                System.out.println(msg + "\n");
                                System.out.println(msg.getItem().getStartTime());
                                break;
                            case "exit":
                                System.out.println("Exiting the auction!");
                                ois.close();
                                oos.close();
                                socket.close();
                                break;
                        }
                    } catch (IOException e) {
                        System.out.println("Client menu IO exception!");
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        System.out.println("Client packet receive not a message type!");
                    }
                }
            });

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

    private User registerForm() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter username");
        String username = in.nextLine();

        User user = new User();
        user.setUsername(username);
        return user;
    }
    
    private String printOptions() {

        if (!this.user.isAuth())
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
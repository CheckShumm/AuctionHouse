/*
Client Class
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    //Logger
    private static Logger log = LogManager.getLogger("auctionhouse");

    //gets configuration from config.properties file
    private Environment env;

    public static User user;

    private Message msg = new Message();
    private Message inputMessage = new Message();

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ObjectInputStream globalInputStream;

    InetAddress serverAddress;
    InetAddress clientAddress;

    DatagramPacket packet;
    private Socket socket;

    private int serverUDPPort;
    private int serverTCPPort;

    private int clientUDPPort;
    private int clientTCPPort;

    //UDP buffer
    private byte[] buf = new byte[256];

    public static boolean isOn = true;

    public static boolean isAuth = false;
    public static boolean wait = false;

    public Client(String propertiesFilePath) {
            env = new Environment(propertiesFilePath);

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

            connect();

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
                                    log.trace(msg + " " + msg.getUser().getRequestCount() + " " + this.socket.getRemoteSocketAddress());
//                                    Client.wait = true;
                                    Thread.sleep(3000);

                                    break;
                                case MessageType.LOGIN:
                                    msg.setType(MessageType.LOGIN);
                                    msg.setUser(registerForm());
                                    msgByte = Help.serialize(msg);

                                    packet = new DatagramPacket(msgByte, msgByte.length, serverAddress, clientUDPPort);
                                    udpSocket.send(packet);
                                    log.trace(msg + " " + msg.getUser().getRequestCount() + " " + this.socket.getRemoteSocketAddress());
//                                    Client.wait = true;
                                    Thread.sleep(3000);

                                    break;
                                case MessageType.OFFER:

                                    int offerCount = 0;

                                    while (!inputMessage.getType().equals(MessageType.OFFER_CONFIRM) & offerCount < 3) {
                                        msg.setItem(offer());
                                        msg.setType(MessageType.OFFER);
                                        System.out.println("Offering " + msg.getItem().getName() + " to the Auction House");
                                        try {
                                            oos.writeUnshared(msg);
                                            log.trace(msg + " " + msg.getUser().getRequestCount() + " " + this.socket.getRemoteSocketAddress());
                                            oos.flush();
                                        } catch(Exception e) {
                                            System.out.println("Somethings Wrong. Please restart application");
                                            reconnect(3000);
                                        }

                                        offerCount++;
                                        Thread.sleep(1000);
                                    }
                                    inputMessage.setType(MessageType.NULL);
                                    break;
                                case MessageType.BID:
                                    msg.setType(MessageType.BID);
                                    msg.setUser(user);
                                    bid();
                                    System.out.println("Bidding on " + msg.getItemID());
                                    if(socket.isConnected()) {
                                        try {
                                            oos.writeUnshared(msg);
                                            log.trace(msg + " " + msg.getUser().getRequestCount() + " " + this.socket.getRemoteSocketAddress());
                                            oos.flush();
                                        } catch(Exception e) {
                                            System.out.println("somethings wrong");
                                            reconnect(3000);
                                        }
                                    }

                                    break;
                                case MessageType.DEREGISTER:
                                    msg.setType(MessageType.DEREGISTER);
                                    msg.setUser(Client.user);

                                    msgByte = Help.serialize(msg);
                                    packet = new DatagramPacket(msgByte, msgByte.length, serverAddress, clientUDPPort);
                                    udpSocket.send(packet);
                                    log.trace(msg + " " + msg.getUser().getRequestCount() + " " + this.socket.getRemoteSocketAddress());
                                    reconnect(3000);
//                                    System.out.println("Exiting the auction!");
//                                    ois.close();
//                                    oos.close();
//                                    socket.close();
                                    break;
                            }
                        } catch (IOException e) {
                            System.out.println("unable to connect to server!");
                            log.warn("unable to connect to server!" + " " + msg.getUser().getRequestCount() + " " + this.socket.getRemoteSocketAddress());
                            e.printStackTrace();
                        }
                         catch (InterruptedException e) {
                             try {
                                 reconnect(3000);
                             } catch (InterruptedException | IOException e1) {
                                 e1.printStackTrace();
                             }
                         }
                         catch(Exception e ){
                            System.out.println("somethings wrong");
                             try {
                                 reconnect(3000);
                             } catch (InterruptedException | IOException e1) {
//                                 e1.printStackTrace();
                             }
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
        System.out.println("Enter the id of the item you would like to bid on");
        int itemID = in.nextInt();
        msg.setItemID(itemID);

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
                    "Enter 'deregister' if you would like to exit\n");
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

    public void reconnect(int timeout) throws InterruptedException, IOException {
        int attempts = 0;
        while(attempts < 8 && !socket.isConnected()) {
            attempts++;
            Thread.sleep(timeout);
            System.out.println("Attempting to connect (" + attempts + ") ...");
            connect();
        }
    }

    public void connect() throws IOException {
        socket = new Socket(serverAddress, clientTCPPort);

        oos = new ObjectOutputStream(socket.getOutputStream());

        // let server know client is connected
        msg.setType("CONNECT");
        msg.setUser(user);
        oos.writeUnshared(msg);
        log.trace(msg + " " + msg.getUser().getRequestCount() + " " + this.socket.getRemoteSocketAddress());
        oos.flush(); // flush stream
    }

    public static void main(String args[]) {
        Client client = new Client(args[0]);
    }
    
   
}

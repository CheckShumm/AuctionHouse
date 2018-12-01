/*
Client Class
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    
    private static User user = new User();
    private static Message msg = new Message();
    
    private static ObjectOutputStream oos;
    private static DataOutputStream dos;

    private static ObjectInputStream ois;
    private static  DataInputStream dis;

    public static void main(String args[]) {
        tcpClient();
    }

    private static void tcpClient() {
        InetAddress address;
        int  tcpPort = 3333;

        try {
            Scanner in = new Scanner(System.in);
            InetAddress host = InetAddress.getByName("localhost");
            System.out.println("IP: " + host);
            Socket socket = new Socket(InetAddress.getLocalHost(), tcpPort);

            oos = new ObjectOutputStream(socket.getOutputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            // let server know client is connected
            msg.setType("CONNECT");
           // oos.writeObject(msg);
            oos.flush(); // flush stream

            ois = new ObjectInputStream(socket.getInputStream());
            dis = new DataInputStream(socket.getInputStream());

            while (true) {
                // asks for user input
                System.out.println(printOptions());

                // reads user input
                String option = in.nextLine();

                // decides what to do based on user input
                switch(option) {
                    case "offer":
                        msg.setItem(offer());
                        msg.setType("OFFER");
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
        catch(Exception e){
            e.printStackTrace();
        }
    }


    private static Item offer() {
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
    
    private static String printOptions() {
        return ("Enter 'offer' to put an item up for auction\n"+
                "Enter 'bid' if you would like to bid on an item\n"+
                "Enter 'exit' if you would like to exit");
    }
    
   
}
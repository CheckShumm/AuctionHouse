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
        tcpServer();
    }

    private static void tcpServer() {
        InetAddress address;
        int  tcpPort = 8888;

        try {
            Scanner in = new Scanner(System.in);
            InetAddress host = InetAddress.getByName("localhost");
            System.out.println("IP: " + host);
            Socket socket = new Socket(InetAddress.getLocalHost(), tcpPort);

            // TCP
            oos = new ObjectOutputStream(socket.getOutputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            ois = new ObjectInputStream(socket.getInputStream());
            dis = new DataInputStream(socket.getInputStream());
            
      
            ItemOffer itemOffer = new ItemOffer(user);
          
            
            while (true) {
                // asks for user input
                System.out.println(printOptions());
                
                // reads user input
                String option = in.nextLine();
                
                // decides what to do based on user input
                switch(option) {
                    case "offer":
                        Item item = itemOffer.offer();
                        msg.setItem(item);
                        msg.setType("OFFER");              
                        oos.writeObject(msg);
              
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
        catch(Exception ignored){

        }
    }

    public static void offer() throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println(dis.readUTF());
        String itemName  = in.nextLine();
        dos.writeUTF(itemName);

        System.out.println(dis.readUTF());
        String itemDescription  = in.nextLine();
        dos.writeUTF(itemDescription);

        System.out.println(dis.readUTF());
        if(in.hasNextDouble()) {
            double price = in.nextDouble();
            dos.writeDouble(price);
        } else {
            dos.writeDouble(-1);
        }


        String response = dis.readUTF();
        System.out.println(response);

        if(response.contains("OFFER-CONF")) {
            System.out.println("your item is now up for auction.");
        } else if (response.contains("OFFER-DENIED")) {
            // add switch case for all options
            System.out.println("the item you are trying to put up is not valid!");
        };
    }

    private static void udpServer() throws SocketException {

        DatagramSocket socket;
        DatagramPacket packet;
        InetAddress address;


        // UDP
        socket = new DatagramSocket();

         User user = new User();


        // create request and send packet
//            DatagramPacket request = new DatagramPacket(message, message.length, host, port);
//            socket.send(request);
//
//            // store reply in buffer and receive reply
//            byte[] buffer = new byte[1000];
//            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
//            socket.receive(reply);
//            System.out.println("Client Received: " + new String(reply.getData()));

            socket.close();
    }
    
    public static String printOptions() throws IOException {
        return ("Enter 'offer' to put an item up for auction\n"+
                "Enter 'bid' if you would like to bid on an item\n"+
                "Enter 'exit' if you would like to exit");
    }
    
   
}
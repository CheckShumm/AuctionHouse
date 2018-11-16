/*
Client Class
 */

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String args[]) {

        DatagramSocket socket;
        DatagramPacket packet;
        InetAddress address;

        System.out.println("Welcome to the Auction House.");


        int port = 6788;
        int  tcpPort = 8888;
        String str = "";
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        Socket client = null;

        try {
            InetAddress host = InetAddress.getByName("localhost");

            client = new Socket(InetAddress.getLocalHost(), tcpPort);

            // TCP
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

            // Gather item data
            Scanner sc = new Scanner(System.in);
            System.out.println("Please enter the name of the item.");
            String name = sc.nextLine(); // add error validations

            System.out.println("Please enter a description for the item.");
            String description = sc.nextLine();

            System.out.println("Please enter the starting price of the item.");
            double minPrice = sc.nextDouble();
            User owner = new User();
            Item item = new Item(name, owner, description, minPrice);

            out.writeObject(item);

            while ((str = (String) in.readObject()) != null) {
                System.out.println(str);
                out.writeObject("bye");

                if (str.equals("bye"))
                    break;
            }

            in.close();
            out.close();
            client.close();

            // UDP
            socket = new DatagramSocket();


            // byte[] message = input.getBytes();

            // create request and send packet
//            DatagramPacket request = new DatagramPacket(message, message.length, host, port);
//            socket.send(request);
//
//            // store reply in buffer and receive reply
//            byte[] buffer = new byte[1000];
//            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
//            socket.receive(reply);
//            System.out.println("Client Received: " + new String(reply.getData()));
//
//            socket.close();
        }
        catch(Exception e){

        }
    }
}
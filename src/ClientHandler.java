import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler extends Thread{

    // TCP
    private Socket socket;
    private Item item;
    private int  tcpPort = 8888;

    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private User user;
    private int requestCount;

    public ClientHandler(Socket socket) throws IOException {
        requestCount = 0;
        this.socket = socket;
        user = new User();

        System.out.println("Waiting for connection on port " + tcpPort);
        this.run();
    }
    @Override
    public void run() {
        System.out.print("Server is running");
        while (true) {
            try {

                oos = new ObjectOutputStream(socket.getOutputStream());
                dos = new DataOutputStream(socket.getOutputStream());

                ois = new ObjectInputStream(socket.getInputStream());
                dis = new DataInputStream(socket.getInputStream());


                dos.writeUTF("Welcome to the Auction House.\n"+
                        "What would you like to do?\n" + printOptions());
                String option = dis.readUTF();
                requestCount++;
                switch(option) {
                    case "offer":
                        offer();
                        if (item.getMinPrice() < 0 ) {
                            offerDenied("price not valid");
                        } else {
                            offerConfirm();
                        }
                        break;
                    case "bid":
                        break;
                    case"exit":
                        break;
                    default:
                        dos.writeUTF("Sorry that option is not available. \n Please select another\n" + printOptions());
                        break;
                }

//                while ((item = (Item) ois.readObject()) != null) {
//                    item.printInfo();
//
//                    oos.writeObject("bye bye");
//                    break;
//                }
//                oos.close();
//
//                fromClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void offer() throws IOException {
        dos.writeUTF("Enter name of the item.");
        String itemName = dis.readUTF();

        dos.writeUTF("Enter item description.");
        String itemDescription = dis.readUTF();

        dos.writeUTF("What is the starting bid for " + itemName);
        double minPrice = dis.readDouble();

        item = new Item(itemName, user, itemDescription, minPrice);
        System.out.println("OFFER " + requestCount +
                " " + item.getName() +
                " " + item.getDescription() +
                " " + item.getMinPrice());
    }

    public void offerConfirm() throws IOException {
        dos.writeUTF("OFFER-CONF " + requestCount +
                " " + item.getName() +
                " " + item.getDescription() +
                " " + item.getMinPrice());
        oos.writeObject(item);
    }

    public void offerDenied(String err) throws IOException {
        dos.writeUTF("OFFER-DENIED " + requestCount + " " + err);
    }

    public String printOptions() throws IOException {
        return ("Enter 'offer' to put an item up for auction\n"+
                "Enter 'bid' if you would like to bid on an item\n"+
                "Enter 'exit' if you would like to exit");
    }

}

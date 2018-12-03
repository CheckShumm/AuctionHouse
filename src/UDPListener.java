import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

public class UDPListener extends Thread{

    private DatagramSocket socket;
    private DatagramPacket packet;
    private byte[] buf = new byte[256];

    private Message message;
    private User user;

    public UDPListener(DatagramSocket socket) {
        this.socket = socket;
        this.message = new Message();
    }

    @Override
    public void run() {
        try {
            while(true) {
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
//                System.out.println("UDP Message receieve");
                message = (Message) Help.deserialize(packet.getData());
                user = message.getUser();
                process(message);
            }

        } catch (IOException e) {
            System.out.println("Unable to process packet from udp listener");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Packet receive is not a message type from udp listener");
            e.printStackTrace();
        }

    }

    //redirect incoming udp message type to their process function
    public void process(Message msg) {
//        System.out.println("Process Ran");
        switch(msg.getType()) {
            case MessageType.REGISTERED:
                registered(msg);
                break;
            case MessageType.UNREGISTERED:
                unregistered(msg);
                break;
            default:
                break;
        }

        Client.wait = false;
//        System.out.println("set client wait to false:" + Client.wait);
    }

    public void registered(Message msg) {
//        System.out.println("user authenticated: " + msg.getUser().isAuth());
        Client.isAuth = msg.getUser().isAuth();
        Client.user = msg.getUser();
        System.out.println(message + "\n");
        Client.wait = false;
    }

    public void unregistered(Message msg) {
        System.out.println(msg);
        Client.wait = false;
    }

    public void deregConfirm() {
        System.out.println("You are logged off now");
    }

}

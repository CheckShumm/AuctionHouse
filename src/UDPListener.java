import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

public class UDPListener extends Thread{

    //Logger
    private static Logger log = LogManager.getLogger("auctionhouse");

    private DatagramSocket socket;
    private DatagramPacket packet;
    private byte[] buf = new byte[999];

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
                log.trace(message + " " + message.getUser().getRequestCount() + " " + this.socket.getRemoteSocketAddress());
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
            case MessageType.DEREG_CONF:
                deregConfirm(msg);
                break;
            case MessageType.DEREG_DENIED:
                deregDenied(msg);
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
//        log.trace(message + " " + message.getUser().getRequestCount() + " " + this.socket.getRemoteSocketAddress());
        Client.wait = false;
    }

    public void unregistered(Message msg) {
//        log.trace(message + " " + message.getUser().getRequestCount() + " " + this.socket.getRemoteSocketAddress());

        Client.wait = false;
    }

    public void deregConfirm(Message msg) {
        Client.isAuth = msg.getUser().isAuth();
        Client.user = msg.getUser();
        Client.user.setAuth(false);
//        log.trace(message + " " + message.getUser().getRequestCount() + " " + this.socket.getRemoteSocketAddress());
        log.trace("You are logged off now" + " " + msg.getUser().getRequestCount() + " " + this.socket.getRemoteSocketAddress());
    }

    public void deregDenied(Message msg) {
        Client.isAuth = msg.getUser().isAuth();
        Client.user = msg.getUser();
//        log.trace("You are logged off now" + " " + message.getUser().getRequestCount() + " " + this.socket.getRemoteSocketAddress());

    }

}

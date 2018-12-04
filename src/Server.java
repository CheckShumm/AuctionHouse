/*
    Server processes UDP and TCP depending on the message type.
 */

import java.io.IOException;
import java.net.*;

public class Server {

    //gets configuration from config.properties file
    private Environment env = new Environment();

    InetAddress serverAddress;

    private int serverUDPPort;
    private int serverTCPPort;

    private ClientHandlers clientHandlers;
    private UDPHandlers udpHandlers;

    private ItemHandler itemHandlers;

    private ServerSocket ss;
    private Socket socket = null;
    private Boolean isRunning = true;
    public static AuctionTimer auctionTimer;

    //UDP
    DatagramSocket udpSocket;
    private byte[] buf = new byte[999];

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        new Server(args[0]);
    }

    public Server(String propertiesFilePath) throws IOException {
        env = new Environment(propertiesFilePath);

        serverUDPPort = Integer.parseInt(env.get("SERVER_PORT_UDP", "3332"));
        serverTCPPort = Integer.parseInt(env.get("SERVER_PORT_TCP", "3333"));

        udpSocket = new DatagramSocket(serverUDPPort, serverAddress);
        ss = new ServerSocket(serverTCPPort);

        clientHandlers = ClientHandlers.getInstance();
        udpHandlers = UDPHandlers.getInstance();
        itemHandlers = ItemHandler.getInstance();

        auctionTimer = new AuctionTimer();
        auctionTimer.run();

        System.out.println("Server is running");

        try {
            Thread tcpSockets = new Thread () {
                public void run () {
                    try {
                        while(true)
                        {
                            socket = ss.accept();
                            ClientHandler clientHandler = new ClientHandler(socket);
                            clientHandlers.add(clientHandler);
                            clientHandler.start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread udpSockets = new Thread () {
                public void run () {
                    try {
                        while(true)
                        {
                            //Receive UDP Packet
                            DatagramPacket packet
                                    = new DatagramPacket(buf, buf.length);
                            udpSocket.receive(packet);

                            //Output packet
                            Message incomingMsg = (Message) Help.deserialize(packet.getData());
                            System.out.println(incomingMsg);

                            UDPHandler udpHandler = new UDPHandler(udpSocket, packet);
                            udpHandlers.add(udpHandler);
                            udpHandler.start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            };
            udpSockets.start();
            tcpSockets.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
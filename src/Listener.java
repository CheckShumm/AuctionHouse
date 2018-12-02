import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Listener extends Thread{

    private Socket socket = null;
    private Message message;
    private ObjectInputStream ois;
    private Client client;

    public Listener(Socket socket, Client client) throws IOException {
        this.ois = new ObjectInputStream(socket.getInputStream());
        this.message = new Message();
        this.client = client;
    }

    @Override
    public void run() {
        try {
            while(true) {
                this.message = (Message) ois.readObject();
                System.out.println(message);
                client.setInputMessage(this.message);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}

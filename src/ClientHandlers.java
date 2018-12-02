import java.util.ArrayList;

public class ClientHandlers  {

    private static ClientHandlers mInstance;
    private ArrayList<ClientHandler> list = null;

    public static ClientHandlers getInstance() {
        if(mInstance == null)
            mInstance = new ClientHandlers();

        return mInstance;
    }

    private ClientHandlers() {

        list = new ArrayList<ClientHandler>();
    }

    // retrieve array from anywhere
    public ArrayList<ClientHandler> getArray() {

        return this.list;
    }

    //Add element to array
    public void add(ClientHandler clientHandler)
    {
        list.add(clientHandler);
    }
}
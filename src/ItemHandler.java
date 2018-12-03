import java.util.ArrayList;

public class ItemHandler  {

    private static ItemHandler mInstance;
    private ArrayList<Item> list = null;
    private int itemCount;

    public static ItemHandler getInstance() {
        if(mInstance == null)
            mInstance = new ItemHandler();

        return mInstance;
    }

    private ItemHandler() {
        this.itemCount = 0;
        this.list = new ArrayList<Item>();
    }

    // retrieve array from anywhere
    public ArrayList<Item> getArray() {

        return this.list;
    }

    //Add element to array
    public void add(Item item) {
        itemCount++;
        item.setItemNumber(itemCount);
        list.add(item);
    }
}
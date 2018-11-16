import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {

    private String name;
    private User owner;
    private String description;
    private double minPrice;
    private Boolean sold;
    private double startTime;
    private ArrayList<User> bidders = new ArrayList();

    public Item(String name, User owner, String description, double minPrice) {
        // set name
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.minPrice = minPrice;
        this.sold = false;
    }

    @Override
    public String toString() {
        return "Item [name=" + name + ", description=" + description
                + "]";
    }

    public void printInfo() {
        System.out.println("Item Name: "  + this.name);
        System.out.println("Description: "  + this.description);
    }

}

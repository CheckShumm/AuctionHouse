import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {

    private String name;
    private User owner;
    private int itemNumber;
    private String description;
    private double minPrice;
    private double currentBid;
    private User topBidder;
    private Boolean sold;
    private int startTime;
    private ArrayList<User> bidders = new ArrayList();

    public Item(String name, User owner, String description, double minPrice) {
        // set name
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.minPrice = minPrice;
        this.sold = false;
        this.currentBid = 0;
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


    public void addBidder(User user) {
        if(!bidders.contains(user)) {
            bidders.add(user);
        }
    }

    // GETTERS AMD SETTERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public ArrayList<User> getBidders() {
        return bidders;
    }

    public void setBidders(ArrayList<User> bidders) {
        this.bidders = bidders;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public User getTopBidder() {
        return topBidder;
    }

    public void setTopBidder(User topBidder) {
        this.topBidder = topBidder;
    }
}

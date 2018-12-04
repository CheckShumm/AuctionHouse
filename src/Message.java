
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author n_shumm
 */


public class Message implements Serializable{
    
    private Item item;
    private String type;
    private User user;
    private String reason;
    private double amount;
    private static int count;
    private int itemID; // For Bidding

    public Message(){
        this.type = "default";
        ++count;
    }
    
    public Item getItem() {
        return this.item;
    }
    
    public void setItem(Item item) {
        this.item = item;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        switch(type) {
            case MessageType.REGISTER:
                sb.append(type).append(" ")
                .append("Attempt to register ")
                .append(user.getUsername());
                break;
            case MessageType.REGISTERED:
                sb.append(type + " ");
                sb.append("Confirm ");;
                sb.append(user.getUsername() + " registration");
                break;
            case MessageType.UNREGISTERED:
                sb.append(count + " ");
                sb.append(type + " ");
                sb.append("Unable to register ");
                sb.append(user.getUsername() + " ");
                sb.append("due to " + this.reason );
                break;
            case MessageType.DEREGISTER:
                sb.append(type + " ");
                sb.append(user.getRequestCount());
                sb.append(user.getUsername() + " ");
                break;
            case MessageType.DEREG_CONF:
                sb.append(type + " ");
                sb.append(user.getRequestCount());
                break;
            case MessageType.DEREG_DENIED:
                sb.append(type + " ");
                sb.append(user.getRequestCount() + " ");
                sb.append("due to " + this.reason );
                break;
            case MessageType.OFFER:
                sb.append(type + " ");
                sb.append('1' + " "); // user.getRequestCount();
                sb.append(item.getName() + " ");
                sb.append(item.getDescription() + " ");
                sb.append(item.getMinPrice());
                break;
            case MessageType.OFFER_CONFIRM:
                sb.append(type + " 1 " + item.getName() + " " + item.getDescription() + " " + item.getMinPrice());
                break;
            case MessageType.OFFER_DENIED:
                sb.append(type + " 1 " + this.reason);
            case MessageType.NEW:
                sb.append(type + " ")
                        .append(item.getItemNumber() + " ")
                        .append(item.getName() + " ")
                        .append(item.getDescription() + " ")
                        .append(item.getMinPrice() + " ");
                break;
            case MessageType.BID:
                sb.append(MessageType.BID + " ")
                        .append(user.getRequestCount() + " ")
                        .append(item.getItemNumber() + " ")
                        .append(this.amount);
                break;
            case MessageType.HIGHEST:
                this.item.setCurrentBid(amount);
                sb.append(MessageType.HIGHEST + " ")
                        .append("#" + item.getItemNumber() + " ")
                        .append(item.getName() + " ")
                        .append(amount);
                break;
            case MessageType.WIN:
                sb.append(MessageType.WIN + " ")
                        .append(item.getItemNumber() + " ")
                        .append(item.getName() + " ")
                        .append(item.getCurrentBid() + " ");
                break;
            case MessageType.BID_OVER:
                sb.append(MessageType.BID_OVER + " ")
                        .append(item.getItemNumber() + " ")
                        .append(item.getName() + " ")
                        .append(item.getCurrentBid());
                break;
            case MessageType.SOLDTO:
                sb.append(MessageType.SOLDTO + " ")
                        .append(item.getItemNumber() + " ")
                        .append(item.getName() + " ")
                        .append(item.getCurrentBid());
                break;
            case MessageType.NOT_SOLD:
                sb.append(MessageType.NOT_SOLD + " ")
                        .append(item.getItemNumber() + " ")
                        .append(item.getName() + " ")
                        .append("no valid bids");
                break;
            default:
                break;

        }
        return sb.toString();
    }
    
}

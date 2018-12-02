
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
    private String itemName; // For Bidding

    public Message(){
        this.type = "default";
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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
                sb.append(type + " ");
                sb.append("Unable to register ");
                sb.append(user.getUsername() + " ");
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
                        .append("Request Count ")
                        .append(item.getItemNumber() + " ")
                        .append(this.amount);
                break;
            case MessageType.HIGHEST:
                sb.append(MessageType.HIGHEST + " ")
                        .append("#" + item.getItemNumber() + " ")
                        .append(item.getName() + " ")
                        .append(item.getCurrentBid());
            default:
                break;

        }
        return sb.toString();
    }
    
}

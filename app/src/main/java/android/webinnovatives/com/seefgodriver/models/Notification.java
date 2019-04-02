package android.webinnovatives.com.seefgodriver.models;

/**
 * Created by hp on 4/1/2019.
 */

public class Notification {
    String id;
    String source;
    String destination;
    String date;
    String status;
    String name;
    String price;


    public Notification() {
    }

    public Notification(String id, String source, String destination, String date, String status, String name, String price) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.date = date;
        this.status = status;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

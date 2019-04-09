package android.webinnovatives.com.seefgodriver.models;

public class Warehouse {
    public String warehouse_id ;
    public String warehouse_name ;
    public String warehouse_address ;
    public String warehouse_contact ;
    public String warehouse_lat ;
    public String warehouse_long ;

    public Warehouse() {
    }

    public Warehouse(String warehouse_id, String warehouse_name, String warehouse_address, String warehouse_contact, String warehouse_lat, String warehouse_long) {
        this.warehouse_id = warehouse_id;
        this.warehouse_name = warehouse_name;
        this.warehouse_address = warehouse_address;
        this.warehouse_contact = warehouse_contact;
        this.warehouse_lat = warehouse_lat;
        this.warehouse_long = warehouse_long;
    }

    public String getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(String warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public String getWarehouse_name() {
        return warehouse_name;
    }

    public void setWarehouse_name(String warehouse_name) {
        this.warehouse_name = warehouse_name;
    }

    public String getWarehouse_address() {
        return warehouse_address;
    }

    public void setWarehouse_address(String warehouse_address) {
        this.warehouse_address = warehouse_address;
    }

    public String getWarehouse_contact() {
        return warehouse_contact;
    }

    public void setWarehouse_contact(String warehouse_contact) {
        this.warehouse_contact = warehouse_contact;
    }

    public String getWarehouse_lat() {
        return warehouse_lat;
    }

    public void setWarehouse_lat(String warehouse_lat) {
        this.warehouse_lat = warehouse_lat;
    }

    public String getWarehouse_long() {
        return warehouse_long;
    }

    public void setWarehouse_long(String warehouse_long) {
        this.warehouse_long = warehouse_long;
    }
}

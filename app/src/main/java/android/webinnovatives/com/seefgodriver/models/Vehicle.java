package android.webinnovatives.com.seefgodriver.models;

public class Vehicle {
    String vehicle_id;
    String vehicle_name;
    String vehicle_size;

    public Vehicle() {
    }

    public Vehicle(String vehicle_id, String vehicle_name, String vehicle_size) {
        this.vehicle_id = vehicle_id;
        this.vehicle_name = vehicle_name;
        this.vehicle_size = vehicle_size;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getVehicle_size() {
        return vehicle_size;
    }

    public void setVehicle_size(String vehicle_size) {
        this.vehicle_size = vehicle_size;
    }
}

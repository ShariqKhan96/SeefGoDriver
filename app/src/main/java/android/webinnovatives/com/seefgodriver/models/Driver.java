package android.webinnovatives.com.seefgodriver.models;

public class Driver {
    String driver_id;
    String driver_name;
    String driver_password;
    String driver_cnic;
    String driver_license;
    String device_token;
    String driver_email;
    String vehicle_id;
    String status;
    public Driver() {
    }

    public Driver(String driver_id, String driver_name, String driver_password, String driver_cnic, String driver_license, String device_token, String driver_email, String vehicle_id, String status) {
        this.driver_id = driver_id;
        this.driver_name = driver_name;
        this.driver_password = driver_password;
        this.driver_cnic = driver_cnic;
        this.driver_license = driver_license;
        this.device_token = device_token;
        this.driver_email = driver_email;
        this.vehicle_id = vehicle_id;
        this.status = status;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_password() {
        return driver_password;
    }

    public void setDriver_password(String driver_password) {
        this.driver_password = driver_password;
    }

    public String getDriver_cnic() {
        return driver_cnic;
    }

    public void setDriver_cnic(String driver_cnic) {
        this.driver_cnic = driver_cnic;
    }

    public String getDriver_license() {
        return driver_license;
    }

    public void setDriver_license(String driver_license) {
        this.driver_license = driver_license;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getDriver_email() {
        return driver_email;
    }

    public void setDriver_email(String driver_email) {
        this.driver_email = driver_email;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

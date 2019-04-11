package android.webinnovatives.com.seefgodriver.models;

public class Task {
    public String package_name;
    public String package_width;
    public String package_height;
    public String date_time;
    public String start_point;
    public String package_comment;
    public String package_status;
    public String weight_range;
    public String price_range;
    public String end_point;
    public String status;
    public String price;

    public Task() {
    }

    public Task(String package_name, String package_width, String package_height, String date_time, String start_point, String package_comment, String package_status, String weight_range, String price_range, String end_point, String status, String price) {
        this.package_name = package_name;
        this.package_width = package_width;
        this.package_height = package_height;
        this.date_time = date_time;
        this.start_point = start_point;
        this.package_comment = package_comment;
        this.package_status = package_status;
        this.weight_range = weight_range;
        this.price_range = price_range;
        this.end_point = end_point;
        this.status = status;
        this.price = price;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPackage_width() {
        return package_width;
    }

    public void setPackage_width(String package_width) {
        this.package_width = package_width;
    }

    public String getPackage_height() {
        return package_height;
    }

    public void setPackage_height(String package_height) {
        this.package_height = package_height;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getStart_point() {
        return start_point;
    }

    public void setStart_point(String start_point) {
        this.start_point = start_point;
    }

    public String getPackage_comment() {
        return package_comment;
    }

    public void setPackage_comment(String package_comment) {
        this.package_comment = package_comment;
    }

    public String getPackage_status() {
        return package_status;
    }

    public void setPackage_status(String package_status) {
        this.package_status = package_status;
    }

    public String getWeight_range() {
        return weight_range;
    }

    public void setWeight_range(String weight_range) {
        this.weight_range = weight_range;
    }

    public String getPrice_range() {
        return price_range;
    }

    public void setPrice_range(String price_range) {
        this.price_range = price_range;
    }

    public String getEnd_point() {
        return end_point;
    }

    public void setEnd_point(String end_point) {
        this.end_point = end_point;
    }
}


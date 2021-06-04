package com.lightning.master.ledbulb.Complaint.fragfortabs.Complain_Content;

public class ComplainModel {

    String id,booking_code, user_id ,booking_lat ,booking_long ,booking_location ,booking_quantity ,booking_description,
            booking_img_path,booking_type,droppoint_id,booking_date,booking_status,deleted,pickup_otp;

    public String getId() {
        return id;
    }

    public String getPickup_otp() {
        return pickup_otp;
    }

    public void setPickup_otp(String pickup_otp) {
        this.pickup_otp = pickup_otp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBooking_code() {
        return booking_code;
    }

    public void setBooking_code(String booking_code) {
        this.booking_code = booking_code;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBooking_lat() {
        return booking_lat;
    }

    public void setBooking_lat(String booking_lat) {
        this.booking_lat = booking_lat;
    }

    public String getBooking_long() {
        return booking_long;
    }

    public void setBooking_long(String booking_long) {
        this.booking_long = booking_long;
    }

    public String getBooking_location() {
        return booking_location;
    }

    public void setBooking_location(String booking_location) {
        this.booking_location = booking_location;
    }

    public String getBooking_quantity() {
        return booking_quantity;
    }

    public void setBooking_quantity(String booking_quantity) {
        this.booking_quantity = booking_quantity;
    }

    public String getBooking_description() {
        return booking_description;
    }

    public void setBooking_description(String booking_description) {
        this.booking_description = booking_description;
    }

    public String getBooking_img_path() {
        return booking_img_path;
    }

    public void setBooking_img_path(String booking_img_path) {
        this.booking_img_path = booking_img_path;
    }

    public String getBooking_type() {
        return booking_type;
    }

    public void setBooking_type(String booking_type) {
        this.booking_type = booking_type;
    }

    public String getDroppoint_id() {
        return droppoint_id;
    }

    public void setDroppoint_id(String droppoint_id) {
        this.droppoint_id = droppoint_id;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}

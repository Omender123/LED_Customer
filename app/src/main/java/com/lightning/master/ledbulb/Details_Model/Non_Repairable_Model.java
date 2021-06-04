package com.lightning.master.ledbulb.Details_Model;

public class Non_Repairable_Model {
    public String quantity_non;
    public String non_replacement_cost;
    public String id;
    public String response_type,comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResponse_type() {
        return response_type;
    }

    public void setResponse_type(String response_type) {
        this.response_type = response_type;
    }

    public String getQuantity_non() {
        return quantity_non;
    }

    public void setQuantity_non(String quantity_non) {
        this.quantity_non = quantity_non;
    }

    public String getNon_replacement_cost() {
        return non_replacement_cost;
    }

    public void setNon_replacement_cost(String non_replacement_cost) {
        this.non_replacement_cost = non_replacement_cost;
    }

    public String getNon_sale_cost() {
        return non_sale_cost;
    }

    public void setNon_sale_cost(String non_sale_cost) {
        this.non_sale_cost = non_sale_cost;
    }

    public String getNon_return_cost() {
        return non_return_cost;
    }

    public void setNon_return_cost(String non_return_cost) {
        this.non_return_cost = non_return_cost;
    }

    public String non_sale_cost;
    public String non_return_cost;
}

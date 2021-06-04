package com.lightning.master.ledbulb.Details_Model;

public class Repairable_Model {
    public String quantity;
    public String repair_cost;
    public String replacement_cost;
    public String id,comments;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public String response_type;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRepair_cost() {
        return repair_cost;
    }

    public void setRepair_cost(String repair_cost) {
        this.repair_cost = repair_cost;
    }

    public String getReplacement_cost() {
        return replacement_cost;
    }

    public void setReplacement_cost(String replacement_cost) {
        this.replacement_cost = replacement_cost;
    }

    public String getSale_cost() {
        return sale_cost;
    }

    public void setSale_cost(String sale_cost) {
        this.sale_cost = sale_cost;
    }

    public String getReturn_cost() {
        return return_cost;
    }

    public void setReturn_cost(String return_cost) {
        this.return_cost = return_cost;
    }

    public String sale_cost;
    public String return_cost;
}

package com.myorderboss.app;

public class Order {
    private String cost_material;
    private String cust_name;
    private String roomL;
    private String roomW;
    private String order_date;
    private String room_area;
    private String total_cost;
    private String vat;
    private String set_cost;
    private String user_id;
    private String order_active;

    public Order() {
    }

    public Order(String cost_material, String cust_name, String roomL, String roomW, String order_date, String room_area, String total_cost, String vat, String set_cost, String user_id, String order_active) {
        this.cost_material = cost_material;
        this.cust_name = cust_name;
        this.roomL = roomL;
        this.roomW = roomW;
        this.order_date = order_date;
        this.room_area = room_area;
        this.total_cost = total_cost;
        this.vat = vat;
        this.set_cost = set_cost;
        this.user_id = user_id;
        this.order_active = order_active;
    }

    public String getCost_material() {
        return cost_material;
    }

    public void setCost_material(String cost_material) {
        this.cost_material = cost_material;
    }

    public String getRoomW() {
        return roomW;
    }

    public void setRoomW(String roomW) { this.roomW = roomW; }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getRoomL() {
        return roomL;
    }

    public void setRoomL(String roomL) { this.roomL = roomL; }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getRoom_area() {
        return room_area;
    }

    public void setRoom_area(String room_area) {
        this.room_area = room_area;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getSet_cost() {
        return set_cost;
    }

    public void setSet_cost(String set_cost) {
        this.set_cost = set_cost;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_active() {
        return order_active;
    }

    public void setOrder_active(String order_active) {
        this.order_active = order_active;
    }
}

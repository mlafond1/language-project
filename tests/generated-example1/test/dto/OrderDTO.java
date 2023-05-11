package test.dto;

import java.util.Date;

public class OrderDTO {

    private int id;
    private int customerId;
    private double total;
    private Date orderDate;
    private Date deliveryDate;


    public OrderDTO(){}

    public OrderDTO(int id) {
        this.id = id;
    }

    public OrderDTO(int id, int customerId, double total, Date orderDate, Date deliveryDate) {
        this.id = id;
        this.customerId = customerId;
        this.total = total;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
    }


    public void setId(int id){
        this.id = id;
    }

    public void setCustomerId(int customerId){
        this.customerId = customerId;
    }

    public void setTotal(double total){
        this.total = total;
    }

    public void setOrderDate(Date orderDate){
        this.orderDate = orderDate;
    }

    public void setDeliveryDate(Date deliveryDate){
        this.deliveryDate = deliveryDate;
    }

    public int getId(){
        return this.id;
    }

    public int getCustomerId(){
        return this.customerId;
    }

    public double getTotal(){
        return this.total;
    }

    public Date getOrderDate(){
        return this.orderDate;
    }

    public Date getDeliveryDate(){
        return this.deliveryDate;
    }

}


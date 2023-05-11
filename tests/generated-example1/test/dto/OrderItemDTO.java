package test.dto;


public class OrderItemDTO {

    private int orderId;
    private int productId;
    private int quantity;
    private double subTotal;
    private boolean hasBeenRefunded;


    public OrderItemDTO(){}

    public OrderItemDTO(int orderId, int productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    public OrderItemDTO(int orderId, int productId, int quantity, double subTotal, boolean hasBeenRefunded) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.subTotal = subTotal;
        this.hasBeenRefunded = hasBeenRefunded;
    }


    public void setOrderId(int orderId){
        this.orderId = orderId;
    }

    public void setProductId(int productId){
        this.productId = productId;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void setSubTotal(double subTotal){
        this.subTotal = subTotal;
    }

    public void setHasBeenRefunded(boolean hasBeenRefunded){
        this.hasBeenRefunded = hasBeenRefunded;
    }

    public int getOrderId(){
        return this.orderId;
    }

    public int getProductId(){
        return this.productId;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public double getSubTotal(){
        return this.subTotal;
    }

    public boolean getHasBeenRefunded(){
        return this.hasBeenRefunded;
    }

}


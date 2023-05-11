package test.dto;


public class StatusDTO {

    private char status;
    private String statusName;
    private float discountRate;


    public StatusDTO(){}

    public StatusDTO(char status) {
        this.status = status;
    }

    public StatusDTO(char status, String statusName, float discountRate) {
        this.status = status;
        this.statusName = statusName;
        this.discountRate = discountRate;
    }


    public void setStatus(char status){
        this.status = status;
    }

    public void setStatusName(String statusName){
        this.statusName = statusName;
    }

    public void setDiscountRate(float discountRate){
        this.discountRate = discountRate;
    }

    public char getStatus(){
        return this.status;
    }

    public String getStatusName(){
        return this.statusName;
    }

    public float getDiscountRate(){
        return this.discountRate;
    }

}


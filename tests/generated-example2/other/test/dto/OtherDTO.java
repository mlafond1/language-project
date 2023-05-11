package other.test.dto;


public class OtherDTO {

    private String name;
    private boolean isOther;
    private int something1;
    private int something2;
    private int favoriteNumber;


    public OtherDTO(){}

    public OtherDTO(String name, boolean isOther, int favoriteNumber) {
        this.name = name;
        this.isOther = isOther;
        this.favoriteNumber = favoriteNumber;
    }

    public OtherDTO(String name, boolean isOther, int something1, int something2, int favoriteNumber) {
        this.name = name;
        this.isOther = isOther;
        this.something1 = something1;
        this.something2 = something2;
        this.favoriteNumber = favoriteNumber;
    }


    public void setName(String name){
        this.name = name;
    }

    public void setIsOther(boolean isOther){
        this.isOther = isOther;
    }

    public void setSomething1(int something1){
        this.something1 = something1;
    }

    public void setSomething2(int something2){
        this.something2 = something2;
    }

    public void setFavoriteNumber(int favoriteNumber){
        this.favoriteNumber = favoriteNumber;
    }

    public String getName(){
        return this.name;
    }

    public boolean getIsOther(){
        return this.isOther;
    }

    public int getSomething1(){
        return this.something1;
    }

    public int getSomething2(){
        return this.something2;
    }

    public int getFavoriteNumber(){
        return this.favoriteNumber;
    }

}


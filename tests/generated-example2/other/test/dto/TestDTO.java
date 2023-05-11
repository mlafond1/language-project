package other.test.dto;

import java.util.Date;

public class TestDTO {

    private int id;
    private Date myDate;
    private Date myTime;
    private boolean myBool;
    private char myChar;
    private String myString;
    private String mySizedString;
    private int myInt;
    private int mySizedInt;
    private float myFloat;
    private double myDouble;


    public TestDTO(){}

    public TestDTO(int id) {
        this.id = id;
    }

    public TestDTO(int id, Date myDate, Date myTime, boolean myBool, char myChar, String myString, String mySizedString, int myInt, int mySizedInt, float myFloat, double myDouble) {
        this.id = id;
        this.myDate = myDate;
        this.myTime = myTime;
        this.myBool = myBool;
        this.myChar = myChar;
        this.myString = myString;
        this.mySizedString = mySizedString;
        this.myInt = myInt;
        this.mySizedInt = mySizedInt;
        this.myFloat = myFloat;
        this.myDouble = myDouble;
    }


    public void setId(int id){
        this.id = id;
    }

    public void setMyDate(Date myDate){
        this.myDate = myDate;
    }

    public void setMyTime(Date myTime){
        this.myTime = myTime;
    }

    public void setMyBool(boolean myBool){
        this.myBool = myBool;
    }

    public void setMyChar(char myChar){
        this.myChar = myChar;
    }

    public void setMyString(String myString){
        this.myString = myString;
    }

    public void setMySizedString(String mySizedString){
        this.mySizedString = mySizedString;
    }

    public void setMyInt(int myInt){
        this.myInt = myInt;
    }

    public void setMySizedInt(int mySizedInt){
        this.mySizedInt = mySizedInt;
    }

    public void setMyFloat(float myFloat){
        this.myFloat = myFloat;
    }

    public void setMyDouble(double myDouble){
        this.myDouble = myDouble;
    }

    public int getId(){
        return this.id;
    }

    public Date getMyDate(){
        return this.myDate;
    }

    public Date getMyTime(){
        return this.myTime;
    }

    public boolean getMyBool(){
        return this.myBool;
    }

    public char getMyChar(){
        return this.myChar;
    }

    public String getMyString(){
        return this.myString;
    }

    public String getMySizedString(){
        return this.mySizedString;
    }

    public int getMyInt(){
        return this.myInt;
    }

    public int getMySizedInt(){
        return this.mySizedInt;
    }

    public float getMyFloat(){
        return this.myFloat;
    }

    public double getMyDouble(){
        return this.myDouble;
    }

}


package test.dto;

import java.util.Date;

public class CustomerDTO {

    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private char status;
    private Date lastLogin;


    public CustomerDTO(){}

    public CustomerDTO(int id) {
        this.id = id;
    }

    public CustomerDTO(int id, String email, String firstName, String lastName, String password, char status, Date lastLogin) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.status = status;
        this.lastLogin = lastLogin;
    }


    public void setId(int id){
        this.id = id;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setStatus(char status){
        this.status = status;
    }

    public void setLastLogin(Date lastLogin){
        this.lastLogin = lastLogin;
    }

    public int getId(){
        return this.id;
    }

    public String getEmail(){
        return this.email;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public String getPassword(){
        return this.password;
    }

    public char getStatus(){
        return this.status;
    }

    public Date getLastLogin(){
        return this.lastLogin;
    }

}


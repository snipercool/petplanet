package be.example.petplanet.petplanet.Activities;

public class UserClass {
    private int id;
    private String username;
    private String password;

    //Default constructor

    public UserClass(){

    }

    //Constructor

    public UserClass(String username, String password){
        this.username = username;
        this.password = password;
    }

    //Getters


    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    //Setters


    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

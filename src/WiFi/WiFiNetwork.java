package WiFi;

public class WiFiNetwork {

    private String name;
    private String password;

    public WiFiNetwork(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void resetPassword(String password) {
//        this.password = password;
//    }

    public boolean validatePassword(String password){
        return this.password.equals(password);
    }
}

package Model;

/**
 * Created by Alican on 2016-12-15.
 * Good luck, Commander!
 */

public class Contact {
    private String number;
    private String name;

    public Contact(String number, String name){
        this.name = name;
        this.number = number;
    }//Contact

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}//class

/**
 * This class defines a Person object that stores details about
 * people who are involved with projects such as the architect,
 * engineer, manager, and customer.
 *
 * @author Daniel Nel
 * @version %i%
 */

public class Person {

    /**
     * String values for name, contact number, email, and address.
     */
    int id;
    String name;
    String contactNumber;
    String email;
    String address;

    /*
     *Constructor method
     * @param name full name value
     * @param contactNumber contact number value
     * @param email email value
     * @param address address value
     * @return Person object
     * */
    public Person(int id, String name, String contactNumber,
                  String email, String address) {
        this.id = id;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
    }

    /*
     *Method for generating a string of the Person object
     * @return string representation of Person object attributes.
     * */
    public String toString() {
        String output = "Name: " + name;
        output += "\nContact: " + contactNumber;
        output += "\nEmail: " + email;
        output += "\nAddress: " + address;

        return output;
    }
}

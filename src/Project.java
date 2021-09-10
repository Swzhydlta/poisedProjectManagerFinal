/**
 * This class defines a Project object that stores details about
 * the projects that this app is designed to manage.
 *
 * @author Daniel Nel
 * @version %i%
 */


public class Project {

    /**
     * String values for project name, building type, address, deadline, finalized.
     * Int values for project number, erf number, project fee, amount paid.
     * Person values for architect, customer, contractor.
     */
    int projectNumber;
    String projectName;
    String buildingType;
    String address;
    int erfNumber;
    int projectFee;
    int amountPaid;
    String deadline;
    String finalized;
    Person engineer;
    Person architect;
    Person manager;
    Person customer;


    /*
     *Constructor method
     * @param projectNumber project number value
     * @param projectName project name value
     * @param buildingType building type value
     * @param address address value
     * @param erfNumber ERF number value
     * @param projectFee project fee value
     * @param amountPaid amount paid value
     * @param deadline deadline value
     * @param finalized finalized value
     * @param architect architect details
     * @param contractor contractor details
     * @param customer customer details
     * @return Project object
     * */
    public Project(int projectNumber, String projectName, String buildingType,
                   String address, int erfNumber, int projectFee,
                   int amountPaid, String deadline, String finalized, Person engineer,
                   Person architect, Person manager, Person customer) {
        this.projectNumber = projectNumber;
        this.projectName = projectName;
        this.buildingType = buildingType;
        this.address = address;
        this.erfNumber = erfNumber;
        this.projectFee = projectFee;
        this.amountPaid = amountPaid;
        this.deadline = deadline;
        this.finalized = finalized;
        this.engineer = engineer;
        this.architect = architect;
        this.manager = manager;
        this.customer = customer;
    }

    /*
     *Method for generating a string of the Project object
     * @return string representation of Project object attributes.
     * */
    public String toString() {
        String output = "Project number: " + projectNumber;
        output += "\nProject Name: " + projectName;
        output += "\nBuilding type: " + buildingType;
        output += "\nAddress: " + address;
        output += "\nERF number: " + erfNumber;
        output += "\nProject fee: " + projectFee;
        output += "\nAmount paid: " + amountPaid;
        output += "\nDeadline: " + deadline;
        output += "\nFinalized: " + finalized;
        output += "\nEngineer: " + engineer.name;
        output += "\nManager: " + manager.name;
        output += "\nArchitect: " + architect.name;
        output += "\nCustomer: " + customer.name + "\n";

        return output;
    }
}



/*
 * This is the final deliverable for a project management system
 * designed for the structural engineering firm Poised.
 * <br>
 * This is the PoisedPMS class that hosts the main method for the program.
 * The main method generates a list of project objects after querying all columns from
 * all tables in the PoisedPMS SQL database. It then allows the user to view and make
 * changes to the project objects via input that directs the flow of logic accordingly.
 *
 * @author Daniel Nel
 * @version %I%
 */

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class PoisedPMS {
    public static void main(String[] args) {
        try {
            // Connect to the PoisedPMS database, via the jdbc:mysql: channel on localhost (this PC)
            // Use username "otheruser", password "swordfish".
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/PoisedPMS?useSSL=false",
                    "otheruser",
                    "swordfish"
            );

            // Set up Connection and ResultSet objects.
            Statement statement = connection.createStatement();

            // Declare a list and fill it with existing Projects from the database.
            List<Project> projects = createObjectList(statement);

            // A list for new projects added in this session.
            List<Project> newProjects = new ArrayList<>();

            // Booleans to keep track of any changes or project additions.
            boolean projectUpdates = false;
            boolean newProject = false;

            // Welcome menu to log, edit, or view projects.
            System.out.println("Welcome!");
            Scanner scan = new Scanner(System.in);
            boolean optionMenu1 = true;
            while (optionMenu1) {
                try {
                    System.out.println("Select: \n1 to log a new project " +
                            "\n2 to edit a project \n3 to search for a project" +
                            "\n4 to view all projects" +
                            "\n5 to see projects that still need to be completed" +
                            "\n6 to see projects that are past the due date" +
                            " \n7 to exit");
                    int logViewExit = scan.nextInt();

                    // Log a project.
                    if (logViewExit == 1) {
                        newProject = true;

                        // Project input.
                        int projectNumber = projects.size() + 1;
                        System.out.println("Project Name: ");
                        System.out.println("Select:" +
                                "\n1 to enter a project name " +
                                "\n2 to generate a project name");
                        String projectName = "";
                        int projectNamingOption = isInt();
                        if (projectNamingOption == 1) {
                            System.out.println("Enter a project name");
                            projectName = scan.next();
                            projectName += scan.nextLine();
                        } else if (projectNamingOption == 2) {
                            projectName = "";
                        }
                        System.out.println("Building type, e.g., Apartment, House, Shop: ");
                        String buildingType = scan.next();
                        System.out.println("Address: ");
                        String address = "";
                        address = scan.next();
                        address += scan.nextLine();
                        System.out.println("ERF number: ");
                        int erfNumber = isInt();
                        System.out.println("Project fee: ");
                        int projectFee = isInt();
                        System.out.println("Amount paid: ");
                        int amountPaid = isInt();
                        System.out.println("Deadline, e.g., 2021-02-23 16:30:00 ");
                        String deadline = scan.next();
                        deadline += scan.nextLine();
                        String finalized = "No";

                        // Engineer input.
                        int engineerId = projects.size() + 1;
                        System.out.println("Engineer name: ");
                        String engineerName = "";
                        engineerName = scan.next();
                        engineerName += scan.nextLine();
                        System.out.println("Engineer contact number: ");
                        String engineerNumber = scan.next();
                        System.out.println("Engineer email: ");
                        String engineerEmail = scan.next();
                        System.out.println("Engineer address: ");
                        String engineerAddress = "";
                        engineerAddress = scan.next();
                        engineerAddress += scan.nextLine();

                        // Architect input.
                        int architectId = projects.size() + 1;
                        System.out.println("Architect name: ");
                        String architectName = "";
                        architectName = scan.next();
                        architectName += scan.nextLine();
                        System.out.println("Architect contact number: ");
                        String architectNumber = scan.next();
                        System.out.println("Architect email: ");
                        String architectEmail = scan.next();
                        System.out.println("Architect address: ");
                        String architectAddress = "";
                        architectAddress = scan.next();
                        architectAddress += scan.nextLine();

                        // Manager input.
                        int managerId = projects.size() + 1;
                        System.out.println("Manager name: ");
                        String managerName = "";
                        managerName = scan.next();
                        managerName += scan.nextLine();
                        System.out.println("Manager contact number: ");
                        String managerNumber = scan.next();
                        System.out.println("Manager email: ");
                        String managerEmail = scan.next();
                        System.out.println("Manager address: ");
                        String managerAddress = "";
                        managerAddress = scan.next();
                        managerAddress += scan.nextLine();

                        // Customer input.
                        int customerId = projects.size() + 1;
                        System.out.println("Customer name: ");
                        String customerName = "";
                        customerName = scan.next();
                        customerName += scan.nextLine();
                        System.out.println("Customer contact number: ");
                        String customerNumber = scan.next();
                        System.out.println("Customer email: ");
                        String customerEmail = scan.next();
                        System.out.println("Customer address: ");
                        String customerAddress = "";
                        customerAddress = scan.next();
                        customerAddress += scan.nextLine();

                        // Autogenerate project name.
                        if (projectName.equals("")) {
                            projectName += buildingType + " ";
                            String surname = customerName.substring(customerName.lastIndexOf(" ") + 1);
                            projectName += surname;
                        }

                        // Instantiate Person and Project objects using input data.
                        Person engineer = new Person(engineerId, engineerName, engineerNumber, engineerEmail, engineerAddress);
                        Person architect = new Person(architectId, architectName, architectNumber, architectEmail, architectAddress);
                        Person manager = new Person(managerId, managerName, managerNumber, managerEmail, managerAddress);
                        Person customer = new Person(customerId, customerName, customerNumber, customerEmail, customerAddress);
                        Project project = new Project(projectNumber, projectName, buildingType, address, erfNumber, projectFee,
                                amountPaid, deadline, finalized, engineer, architect, manager, customer);

                        // Add logged project to list and display it to user.
                        projects.add(project);
                        newProjects.add(project);
                        System.out.println("\nProject logged. \n\nProject details:");
                        System.out.println("\n" + project);
                    }

                    // Edit a project.
                    else if (logViewExit == 2) {

                        // Look for any projects first.
                        if (projects.size() == 0) {
                            System.out.println("\nThere are no projects to edit\n");

                            // Print out available projects to edit.
                        } else {
                            projectUpdates = true;
                            System.out.println("\nCurrent projects:\n");
                            for (int i = 0; i < projects.size(); i++) {
                                System.out.println("Project number: " + projects.get(i).projectNumber +
                                        "\nProject Name: " + projects.get(i).projectName + "\n");
                            }

                            // Ask user to choose a project to edit.
                            boolean editingProject = true;
                            while (editingProject) {
                                try {
                                    System.out.println("Enter the number of the project you would like to edit " +
                                            "or 'b' to go back to the main menu: ");
                                    String projectEditSelector = scan.next();

                                    // If the user entered a valid digit, get the corresponding project from the list.
                                    if (Character.isDigit(projectEditSelector.charAt(0))) {
                                        int selectedProject = Integer.parseInt(projectEditSelector) - 1;
                                        Project projectToEdit = projects.get(selectedProject);
                                        System.out.println("\n" + projectToEdit);
                                        boolean editing = true;

                                        // Ask the user to choose what project details to edit.
                                        while (editing) {
                                            try {
                                                System.out.println("\nPress \n1 to change due date \n2 to update the fee paid to date" +
                                                        "\n3 to finalize the project \n4 to view project details" +
                                                        " \n5 to update the engineer/manager/customer/architect details \n6 to go back:");
                                                int editToMake = scan.nextInt();

                                                // Change date.
                                                if (editToMake == 1) {
                                                    System.out.println("Please enter a new date and time, e.g., 2021-01-01 17:00:00 ");
                                                    String newDeadline = scan.next();
                                                    newDeadline += scan.nextLine();
                                                    projectToEdit.deadline = newDeadline;
                                                }

                                                // Change fee paid so far.
                                                else if (editToMake == 2) {
                                                    System.out.println("Please enter the new amount: ");
                                                    projectToEdit.amountPaid = isInt();
                                                }

                                                // Finalize project if it is not finalized yet.
                                                else if (editToMake == 3) {
                                                    if (projectToEdit.finalized.toLowerCase().equals("no")) {

                                                        // Generate a date to go on the invoice
                                                        java.util.Date todayDate = Calendar.getInstance().getTime();
                                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                                        String strTodayDate = dateFormat.format(todayDate);

                                                        // If the customer has not paid the full amount, generate an invoice.
                                                        if (projectToEdit.amountPaid < projectToEdit.projectFee) {
                                                            System.out.println("Invoice generated");
                                                            String customerInvoice = strTodayDate;
                                                            customerInvoice += "\nPoised Contractors Invoice";
                                                            customerInvoice += "\n\nTo:";
                                                            customerInvoice += "\n" + projectToEdit.customer.name;
                                                            customerInvoice += "\n" + projectToEdit.customer.contactNumber;
                                                            customerInvoice += "\n" + projectToEdit.customer.email;
                                                            customerInvoice += "\n" + projectToEdit.customer.address;
                                                            customerInvoice += "\n\nBalance to be paid:\n\n";
                                                            customerInvoice += "R " + (projectToEdit.projectFee - projectToEdit.amountPaid) + ".00";
                                                            try {
                                                                Files.writeString(Path.of("./" + projectToEdit.projectName + "_invoice.txt"), customerInvoice, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                                                            } catch (Exception e) {
                                                                System.out.println(e);
                                                            }
                                                        }

                                                        // Generate a project completion document.
                                                        projectToEdit.finalized = "Yes";
                                                        System.out.println("Generating project completion document");
                                                        String completedProject = "Completed Project: ";
                                                        completedProject += "\n\n" + projectToEdit;
                                                        completedProject += "\nDate completed: " + strTodayDate;
                                                        try {
                                                            Files.writeString(Path.of("./" + projectToEdit.projectName + "_completed.txt"), completedProject, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                                                        } catch (Exception e) {
                                                            System.out.println(e);
                                                        }

                                                        // Notify user that project is already finalized if it is.
                                                    } else {
                                                        System.out.println("This project has already been finalized.");
                                                    }
                                                }

                                                // Show edited project.
                                                else if (editToMake == 4) {
                                                    System.out.println("\n" + projectToEdit);
                                                }

                                                // Change engineer/manager/architect/customer details.
                                                else if (editToMake == 5) {
                                                    Person personToEdit = null;
                                                    boolean personEdit = true;

                                                    // Choose which one to edit.
                                                    while (personEdit) {
                                                        try {
                                                            System.out.println("Press \n1 to edit the engineer \n2 to edit the manager " +
                                                                    "\n3 to edit the architect \n4 to edit the customer");
                                                            int personSelector = scan.nextInt();
                                                            if (personSelector == 1) {
                                                                personToEdit = projectToEdit.engineer;
                                                                personEdit = false;
                                                            } else if (personSelector == 2) {
                                                                personToEdit = projectToEdit.manager;
                                                                personEdit = false;
                                                            } else if (personSelector == 3) {
                                                                personToEdit = projectToEdit.architect;
                                                                personEdit = false;
                                                            } else if (personSelector == 4) {
                                                                personToEdit = projectToEdit.customer;
                                                                personEdit = false;
                                                            } else {
                                                                System.out.println("Only 1, 2, and 3 are valid entries.");
                                                            }
                                                        } catch (InputMismatchException e) {
                                                            System.out.println("You did not enter an integer.");
                                                            scan.next();
                                                        }
                                                    }

                                                    // Choose which attributes of the contractor/architect/customer to edit.
                                                    boolean whoToEdit = true;
                                                    while (whoToEdit) {
                                                        try {
                                                            System.out.println("Press \n1 to edit the name \n2 to edit the contact number" +
                                                                    "\n3 to edit the email address \n4 to edit the physical address" +
                                                                    "\n5 to stop editing the chosen person: ");
                                                            int attributeToEdit = scan.nextInt();

                                                            // Change name.
                                                            if (attributeToEdit == 1) {
                                                                System.out.println("Please enter a new name: ");
                                                                String newName = "";
                                                                newName = scan.next();
                                                                newName += scan.nextLine();
                                                                personToEdit.name = newName;
                                                            }

                                                            // Change contact number.
                                                            else if (attributeToEdit == 2) {
                                                                System.out.println("Please enter a new contact number: ");
                                                                personToEdit.contactNumber = scan.next();
                                                            }

                                                            // Change email address.
                                                            else if (attributeToEdit == 3) {
                                                                System.out.println("Please enter a new email: ");
                                                                personToEdit.email = scan.next();
                                                            }

                                                            // Change physical address.
                                                            else if (attributeToEdit == 4) {
                                                                System.out.println("Please enter a new address: ");
                                                                String newAddress = "";
                                                                newAddress = scan.next();
                                                                newAddress += scan.nextLine();
                                                                personToEdit.address = newAddress;
                                                            }

                                                            // Stop editing the engineer/manager/architect/customer.
                                                            else if (attributeToEdit == 5) {
                                                                whoToEdit = false;
                                                                System.out.println("\nUpdated details:\n" + personToEdit);
                                                            }

                                                            // Catch invalid entry errors.
                                                            else if (attributeToEdit == 0 || attributeToEdit > 5) {
                                                                System.out.println("Only 1, 2, 3, 4, and 5 are valid entries.");
                                                            }

                                                            // Catch input exceptions.
                                                        } catch (Exception e) {
                                                            System.out.println("Only 1, 2, 3, 4, and 5 are valid entries.");
                                                            scan.next();
                                                        }
                                                    }
                                                }

                                                // Exit project editing loop.
                                                else if (editToMake == 6) {
                                                    editing = false;
                                                }

                                                // Catch invalid int inputs.
                                                else if (editToMake == 0 || editToMake > 6) {
                                                    System.out.println("Only 1, 2, 3, 4, 5, 6 are valid entries.");
                                                }

                                                // Catch input exceptions.
                                            } catch (Exception e) {
                                                System.out.println("Only 1, 2, 3, 4, 5, 6 are valid entries.");
                                                scan.next();
                                            }
                                        }

                                        // Go back to previous menu.
                                    } else if (projectEditSelector.equals("b")) {
                                        editingProject = false;

                                        // Catch invalid letter entries.
                                    } else {
                                        System.out.println("Only b is a valid letter entry.");
                                    }

                                    // Catch invalid index entries.
                                } catch (IndexOutOfBoundsException e) {
                                    System.out.println("Please enter a valid project number.");
                                }
                            }
                        }
                    }

                    // Search for a project if any projects exist.
                    else if (logViewExit == 3) {

                        if (projects.size() == 0) {
                            System.out.println("\nThere are no projects to search for\n");

                            // Show limited details about all projects.
                        } else {
                            System.out.println("\nCurrent projects:\n");
                            for (int i = 0; i < projects.size(); i++) {
                                System.out.println("Project number: " + projects.get(i).projectNumber +
                                        "\nProject Name: " + projects.get(i).projectName);
                            }

                            // Choose to search by name or number.
                            System.out.println("Press \n1 to search by number " +
                                    "\n2 to search by name");
                            int searchType = isInt();

                            // If user chose to search by number.
                            if (searchType == 1) {
                                System.out.println("Enter the project number: ");
                                int projectSelectorInt = isInt();

                                // Check if a number within the range of projects was entered and display the project if so.
                                while (projectSelectorInt == 0 || projectSelectorInt > projects.size()) {
                                    System.out.println("No project matches that integer. Please enter a valid integer: ");
                                    projectSelectorInt = isInt();
                                }
                                for (int i = 0; i < projects.size(); i++) {
                                    if (projectSelectorInt == projects.get(i).projectNumber) {
                                        System.out.println(projects.get(i) + "\n");
                                    }
                                }

                                // If user chose to search by name.
                            } else if (searchType == 2) {
                                boolean nameSearch = true;
                                while (nameSearch) {
                                    System.out.println("Enter the name of the project.");
                                    String projectSelectorString = scan.next();
                                    projectSelectorString += scan.nextLine();
                                    boolean projectMatch = false;

                                    // Check if the name entered matches a project and display it if so.
                                    for (int i = 0; i < projects.size(); i++) {
                                        if (projectSelectorString.equals(projects.get(i).projectName)) {
                                            System.out.println(projects.get(i) + "\n");
                                            projectMatch = true;
                                            nameSearch = false;
                                        }
                                    }
                                    if (!projectMatch) {
                                        System.out.println("Your search did not match any projects.");
                                    }
                                }
                            }
                        }
                    }

                    // View projects if any projects exist.
                    else if (logViewExit == 4) {

                        if (projects.size() == 0) {
                            System.out.println("\nThere are no projects to view\n");

                        } else {
                            System.out.println("\n");
                            for (int i = 0; i < projects.size(); i++) {
                                System.out.println(projects.get(i).toString());
                            }
                        }
                    }

                    // View projects that still need to be completed if any projects exist.
                    else if (logViewExit == 5) {

                        if (projects.size() == 0) {
                            System.out.println("\nThere are no projects saved yet\n");

                        } else {
                            System.out.println("\nIncomplete projects:\n");
                            boolean incompleteProjects = false;
                            for (int i = 0; i < projects.size(); i++) {
                                String finalizedOrNot = projects.get(i).finalized.toLowerCase();
                                if (finalizedOrNot.equals("no")) {
                                    System.out.println(projects.get(i).toString());
                                    incompleteProjects = true;
                                }
                            }
                            if (!incompleteProjects) {
                                System.out.println("All projects are complete.");
                            }
                        }
                    }

                    // View projects that are past the due date if any projects exist.
                    else if (logViewExit == 6) {

                        if (projects.size() == 0) {
                            System.out.println("\nThere are no projects saved yet\n");

                        } else {
                            System.out.println("\nProjects past due date:\n");
                            boolean overdueProjects = false;
                            for (int i = 0; i < projects.size(); i++) {
                                String finalizedOrNot = projects.get(i).finalized.toLowerCase();
                                if (finalizedOrNot.equals("no")) {
                                    String stringDeadline = projects.get(i).deadline;
                                    SimpleDateFormat deadlineFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    java.util.Date deadline = deadlineFormatter.parse(stringDeadline);
                                    Date todayDate = Calendar.getInstance().getTime();
                                    if (todayDate.compareTo(deadline) > 0) {
                                        System.out.println(projects.get(i) + "\n");
                                        overdueProjects = true;
                                    }
                                }
                            }

                            if (!overdueProjects) {
                                System.out.println("All completed projects have been completed by the due date.");
                            }
                        }
                    }

                    // Exit program.
                    else if (logViewExit == 7) {
                        optionMenu1 = false;
                    }

                    // Catch invalid int inputs.
                    else if (logViewExit == 0 || logViewExit > 7) {
                        System.out.println("Only 1, 2, 3, 4, 5, 6, and 7 are valid entries.");
                    }

                    // Catch string inputs.
                } catch (InputMismatchException e) {
                    System.out.println("Only 1, 2, 3, 4, 5, 6 and 7 are valid entries.");
                    scan.next();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // Add any newly added projects to database.
            if (newProject) {
                for (int i = 0; i < newProjects.size(); i++) {
                    Project projectBeingWritten = newProjects.get(i);

                    // Insert into Engineers.
                    PreparedStatement pStatement = connection.prepareStatement(
                            "INSERT INTO Engineers VALUES (?, ?, ?, ?, ?)"
                    );
                    pStatement.setInt(1, projectBeingWritten.engineer.id);
                    pStatement.setString(2, projectBeingWritten.engineer.name);
                    pStatement.setString(3, projectBeingWritten.engineer.contactNumber);
                    pStatement.setString(4, projectBeingWritten.engineer.email);
                    pStatement.setString(5, projectBeingWritten.engineer.address);
                    pStatement.executeUpdate();

                    // Insert into Architects.
                    pStatement = connection.prepareStatement(
                            "INSERT INTO Architects VALUES (?, ?, ?, ?, ?)"
                    );
                    pStatement.setInt(1, projectBeingWritten.architect.id);
                    pStatement.setString(2, projectBeingWritten.architect.name);
                    pStatement.setString(3, projectBeingWritten.architect.contactNumber);
                    pStatement.setString(4, projectBeingWritten.architect.email);
                    pStatement.setString(5, projectBeingWritten.architect.address);
                    pStatement.executeUpdate();

                    // Insert into Customers.
                    pStatement = connection.prepareStatement(
                            "INSERT INTO Customers VALUES (?, ?, ?, ?, ?)"
                    );
                    pStatement.setInt(1, projectBeingWritten.customer.id);
                    pStatement.setString(2, projectBeingWritten.customer.name);
                    pStatement.setString(3, projectBeingWritten.customer.contactNumber);
                    pStatement.setString(4, projectBeingWritten.customer.email);
                    pStatement.setString(5, projectBeingWritten.customer.address);
                    pStatement.executeUpdate();

                    // Insert into Managers.
                    pStatement = connection.prepareStatement(
                            "INSERT INTO Managers VALUES (?, ?, ?, ?, ?)"
                    );
                    pStatement.setInt(1, projectBeingWritten.manager.id);
                    pStatement.setString(2, projectBeingWritten.manager.name);
                    pStatement.setString(3, projectBeingWritten.manager.contactNumber);
                    pStatement.setString(4, projectBeingWritten.manager.email);
                    pStatement.setString(5, projectBeingWritten.manager.address);
                    pStatement.executeUpdate();

                    // Insert into Projects.
                    pStatement = connection.prepareStatement(
                            "INSERT INTO Projects VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    );
                    pStatement.setInt(1, projectBeingWritten.projectNumber);
                    pStatement.setString(2, projectBeingWritten.projectName);
                    pStatement.setString(3, projectBeingWritten.buildingType);
                    pStatement.setString(4, projectBeingWritten.address);
                    pStatement.setInt(5, projectBeingWritten.erfNumber);
                    pStatement.setInt(6, projectBeingWritten.projectFee);
                    pStatement.setInt(7, projectBeingWritten.amountPaid);
                    pStatement.setString(8, projectBeingWritten.deadline);
                    pStatement.setString(9, projectBeingWritten.finalized);
                    pStatement.setInt(10, projectBeingWritten.engineer.id);
                    pStatement.setInt(11, projectBeingWritten.architect.id);
                    pStatement.setInt(12, projectBeingWritten.manager.id);
                    pStatement.setInt(13, projectBeingWritten.customer.id);
                    pStatement.executeUpdate();
                }
            }

            // Write changes to existing Project objects back to database.
            if (projectUpdates) {
                for (int i = 0; i < projects.size(); i++) {
                    Project projectBeingWritten = projects.get(i);

                    // Update Engineers.
                    PreparedStatement pStatement = connection.prepareStatement(
                            "UPDATE Engineers SET e_name=?, e_contactNumber=?, e_email=?, e_address=? WHERE engineerId=?"
                    );
                    pStatement.setString(1, projectBeingWritten.engineer.name);
                    pStatement.setString(2, projectBeingWritten.engineer.contactNumber);
                    pStatement.setString(3, projectBeingWritten.engineer.email);
                    pStatement.setString(4, projectBeingWritten.engineer.address);
                    pStatement.setInt(5, projectBeingWritten.engineer.id);
                    pStatement.executeUpdate();

                    // Update Architects.
                    pStatement = connection.prepareStatement(
                            "UPDATE Architects SET a_name=?, a_contactNumber=?, a_email=?, a_address=? WHERE architectId=?"
                    );
                    pStatement.setString(1, projectBeingWritten.architect.name);
                    pStatement.setString(2, projectBeingWritten.architect.contactNumber);
                    pStatement.setString(3, projectBeingWritten.architect.email);
                    pStatement.setString(4, projectBeingWritten.architect.address);
                    pStatement.setInt(5, projectBeingWritten.architect.id);
                    pStatement.executeUpdate();

                    // Update Customers.
                    pStatement = connection.prepareStatement(
                            "UPDATE Customers SET c_name=?, c_contactNumber=?, c_email=?, c_address=? WHERE customerId=?"
                    );
                    pStatement.setString(1, projectBeingWritten.customer.name);
                    pStatement.setString(2, projectBeingWritten.customer.contactNumber);
                    pStatement.setString(3, projectBeingWritten.customer.email);
                    pStatement.setString(4, projectBeingWritten.customer.address);
                    pStatement.setInt(5, projectBeingWritten.customer.id);
                    pStatement.executeUpdate();

                    // Update Managers.
                    pStatement = connection.prepareStatement(
                            "UPDATE Managers SET m_name=?, m_contactNumber=?, m_email=?, m_address=? WHERE managerId=?"
                    );
                    pStatement.setString(1, projectBeingWritten.manager.name);
                    pStatement.setString(2, projectBeingWritten.manager.contactNumber);
                    pStatement.setString(3, projectBeingWritten.manager.email);
                    pStatement.setString(4, projectBeingWritten.manager.address);
                    pStatement.setInt(5, projectBeingWritten.manager.id);
                    pStatement.executeUpdate();

                    // Update Projects.
                    pStatement = connection.prepareStatement(
                            "UPDATE Projects SET projectName=?, buildingType=?, address=?, erfNumber=?, projectFee=?, amountPaid=?, deadline=?, finalized=? WHERE projectNumber=?"
                    );
                    pStatement.setString(1, projectBeingWritten.projectName);
                    pStatement.setString(2, projectBeingWritten.buildingType);
                    pStatement.setString(3, projectBeingWritten.address);
                    pStatement.setInt(4, projectBeingWritten.erfNumber);
                    pStatement.setInt(5, projectBeingWritten.projectFee);
                    pStatement.setInt(6, projectBeingWritten.amountPaid);
                    pStatement.setString(7, projectBeingWritten.deadline);
                    pStatement.setString(8, projectBeingWritten.finalized);
                    pStatement.setInt(9, projectBeingWritten.projectNumber);

                    pStatement.executeUpdate();
                }
            }

            // Print all projects in full before closing
            printAllFromTables(statement);

            // Close up our connections
            System.out.println("\nGoodbye");
            statement.close();
            connection.close();

        } catch (SQLException e) {
            // We only want to catch a SQLException - anything else is off-limits for now.
            e.printStackTrace();
        }
    }

// Methods

    /**
     * Method for printing all project details out in full.
     * It does this by querying all values in all database rows.
     * It takes a statement to try to avoid spreading DB access too far.
     *
     * @param statement on an existing connection.
     * @throws SQLException
     */
    public static void printAllFromTables(Statement statement) throws SQLException {

        ResultSet results = statement.executeQuery("SELECT * FROM projects " +
                "JOIN architects ON projects.architectId = architects.architectId " +
                "JOIN managers ON projects.managerId = managers.managerId " +
                "JOIN engineers ON projects.engineerId = engineers.engineerId " +
                "JOIN customers ON projects.customerId = customers.customerId"
        );
        System.out.println("\n");
        while (results.next()) {
            System.out.println(
                    "\nProject number: " + results.getInt("projectNumber")
                            + "\nProject name: " + results.getString("projectName")
                            + "\nBuilding type: " + results.getString("buildingType")
                            + "\nAddress: " + results.getString("address")
                            + "\nERF number: " + results.getInt("erfNumber")
                            + "\nProject fee: " + results.getInt("projectFee")
                            + "\nAmount paid: " + results.getInt("amountPaid")
                            + "\nDeadline: " + results.getString("deadline")
                            + "\nFinalized: " + results.getString("finalized")
                            + "\nEngineer: " + results.getString("e_name")
                            + "\nEngineer contact number: " + results.getString("e_contactNumber")
                            + "\nEngineer email: " + results.getString("e_email")
                            + "\nEngineer address: " + results.getString("e_address")
                            + "\nArchitect: " + results.getString("a_name")
                            + "\nArchitect contact number: " + results.getString("a_contactNumber")
                            + "\nArchitect email: " + results.getString("a_email")
                            + "\nArchitect address: " + results.getString("a_address")
                            + "\nManager: " + results.getString("m_name")
                            + "\nManager contact number: " + results.getString("m_contactNumber")
                            + "\nManager email: " + results.getString("m_email")
                            + "\nManager address: " + results.getString("m_address")
                            + "\nCustomer: " + results.getString("c_name")
                            + "\nCustomer contact number: " + results.getString("c_contactNumber")
                            + "\nCustomer email: " + results.getString("c_email")
                            + "\nCustomer address: " + results.getString("c_address")
            );
        }
    }

    /**
     * Method creating a list of Project objects from a full database query.
     *
     * @param statement on an existing connection
     * @throws SQLException
     * @returns List of Projects
     */
    public static List createObjectList(Statement statement) throws SQLException {

        List<Project> projects = new ArrayList<>();

        ResultSet results = statement.executeQuery("SELECT * FROM projects " +
                "JOIN architects ON projects.architectId = architects.architectId " +
                "JOIN managers ON projects.managerId = managers.managerId " +
                "JOIN engineers ON projects.engineerId = engineers.engineerId " +
                "JOIN customers ON projects.customerId = customers.customerId"
        );

        System.out.println("\n");
        while (results.next()) {
            int projectNumber = results.getInt("projectNumber");
            String projectName = results.getString("projectName");
            String buildingType = results.getString("buildingType");
            String address = results.getString("address");
            int erfNumber = results.getInt("erfNumber");
            int projectFee = results.getInt("projectFee");
            int amountPaid = results.getInt("amountPaid");
            String deadline = results.getString("deadline");
            String finalized = results.getString("finalized");
            int eId = results.getInt("engineerId");
            String eName = results.getString("e_name");
            String eContactNumber = results.getString("e_contactNumber");
            String eEmail = results.getString("e_email");
            String eAddress = results.getString("e_address");
            int mId = results.getInt("managerId");
            String mName = results.getString("m_name");
            String mContactNumber = results.getString("m_contactNumber");
            String mEmail = results.getString("m_email");
            String mAddress = results.getString("m_address");
            int aId = results.getInt("architectId");
            String aName = results.getString("a_name");
            String aContactNumber = results.getString("a_contactNumber");
            String aEmail = results.getString("a_email");
            String aAddress = results.getString("a_address");
            int cId = results.getInt("customerId");
            String cName = results.getString("c_name");
            String cContactNumber = results.getString("c_contactNumber");
            String cEmail = results.getString("c_email");
            String cAddress = results.getString("c_address");

            Person engineer = new Person(eId, eName, eContactNumber, eEmail, eAddress);
            Person architect = new Person(aId, aName, aContactNumber, aEmail, aAddress);
            Person manager = new Person(mId, mName, mContactNumber, mEmail, mAddress);
            Person customer = new Person(cId, cName, cContactNumber, cEmail, cAddress);
            Project project = new Project(projectNumber, projectName, buildingType, address, erfNumber, projectFee, amountPaid, deadline, finalized, engineer, architect, manager, customer);
            projects.add(project);
        }
        return projects;
    }


    /*
     * This method will not accept user input as valid until it is an int.
     * @return int
     * */
    public static int isInt() {
        Scanner scan = new Scanner(System.in);
        int returnValue;
        while (!scan.hasNextInt()) {
            System.out.println("Please enter a valid integer...");
            scan.next();
        }
        returnValue = scan.nextInt();
        return returnValue;
    }
}

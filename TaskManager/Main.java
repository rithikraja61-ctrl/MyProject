import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserManagerInterface userManager = new UserManager();
        TaskManagerInterface taskManager = new TaskManager();
        while (true) {
            Scanner sc = new Scanner(System.in); // Creating new scanner obj For Getting value in runtime
            UserInterface currentUser;
            currentUser = userManager.login();
           taskManager.setCurrentUser(currentUser);
            Integer choice = 0;
            while (choice != 5) {
                try {
                    System.out.print("Welcome ");
                    System.out.println(currentUser.getUserName());
                    System.out.println("1.Add Task");
                    System.out.println("2.Update Task");
                    System.out.println("3.Delete Task");
                    System.out.println("4.List All  Task");
                    System.out.println("5.Exit");
                    System.out.println("Enter Your Choice");
                    choice = new Integer(sc.nextLine());
                    if (choice == 1) {
                        // For Adding new Task
                        taskManager.add();
                    } else if (choice == 2) {  //For Updating
                        taskManager.update();
                    } else if (choice == 3) {// For Deleting Task
                        taskManager.delete();
                    } else if (choice == 4)  //For Listing Task
                    {
                        taskManager.list();
                    } else if (choice == 5) {
                      taskManager.exit();
                    } else {
                        System.out.println("Invalid Choice");
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Invalid Choice");
                }
            }
        }
    }
}

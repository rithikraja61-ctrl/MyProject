import java.util.ArrayList;
import java.util.List;

public  class TaskManagerUtil {
    static void display(UserInterface currentUser) {
        for (int i = 0; i < currentUser.getTask().size(); i++) {
                System.out.print(i + 1);
                System.out.print(" : ");
                System.out.println(currentUser.getTask().get(i).getTitle());
        }
    }
    static boolean isTaskEmpty(UserInterface currentUser)
    {
         if(currentUser.getTask().isEmpty()){

               return true;
            }

        return false;
    }
    static void displayStatus(UserInterface currentUser, String status) {
        List<TaskInterface> taskStatus=new ArrayList();
        for (int i = 0; i < currentUser.getTask().size(); i++) {

                if (currentUser.getTask().get(i).getStatus().equals(status)) {
                            taskStatus.add(currentUser.getTask().get(i));
                        }
                    }



        if (isTaskEmpty(currentUser)) {
            System.out.println("Task is Empty.");
        } else {
            boolean isEmpty = true;
            System.out.println("===========================");
            System.out.println(status);
            for (int i = 0; i < taskStatus.size(); i++) {
                    System.out.print(i + 1);
                    System.out.println("]");
                    System.out.print("Title :");
                    System.out.println(taskStatus.get(i).getTitle());
                    if(taskStatus.get(i).getDescription()!=null) {
                        System.out.print("Description :");
                        System.out.println(taskStatus.get(i).getDescription());
                    }
                    else{
                        System.out.println(" Description Empty");
                    }
                    isEmpty = false;

            }
            if (isEmpty) {
                System.out.print(status);
                System.out.println(" : No Task ");
            }
        }


    }
}

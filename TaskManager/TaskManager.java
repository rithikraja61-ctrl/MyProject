import java.util.Scanner;

public class TaskManager implements TaskManagerInterface{
    private UserInterface currentUser;       //store user
    Scanner sc = new Scanner(System.in);

    public TaskManager(UserInterface currentUser) {
        this.currentUser = currentUser;
    }
    public TaskManager()
    {

    }
    public void setCurrentUser(UserInterface currentUser)
    {
        this.currentUser=currentUser;
    }

    public void add() {

        boolean exit = false;// exit when user what to leave
        while (!exit) {
                String addChoice = null;
                Task task = null;
                System.out.println("Add Task enter 'exit' for Exit");
                TaskManagerUtil.display(currentUser);
                if (TaskManagerUtil.isTaskEmpty(currentUser)) {
                    System.out.println("Task Is Empty");
                }

                try {
                        System.out.println("Enter Title Name:");
                        addChoice  = sc.nextLine();
                        if (addChoice.equals("exit")) {
                            throw new Exception();
                        }
                        task = new Task(addChoice);

                                currentUser.getTask().add(task);
                                System.out.println("Title added Successfully");

                        boolean validInput = false;
                        while (!validInput) {
                            System.out.println("Want to Create Description enter (yes/no).Default is Empty");
                            addChoice = sc.nextLine();
                            if (addChoice.equals("yes")) {
                                System.out.println("Enter Task description:");
                                addChoice = sc.nextLine();
                                if (addChoice.equals("exit")) {
                                    throw new Exception();
                                }
                                task.setDescription(addChoice);
                                validInput=true;
                                System.out.println("Description added Successfully");
                            } else if (addChoice.equals("no")) {
                                validInput=true;
                            }
                            else if (addChoice.equals("exit")) {
                                throw new Exception();
                            }
                            else
                            {
                                System.out.println("Enter A valid input (yes/no) only");
                            }
                        }

                }

            catch(Exception e){
                    System.out.println("Exiting..");
                    exit=true;
               }

        }


        }

  public  void update() {
        boolean exit = false;
        String updateChoice = null;
        while (!exit) {
            System.out.println("Update Task enter 'exit' for exit");
            TaskManagerUtil.display(currentUser);
            if (TaskManagerUtil.isTaskEmpty(currentUser)) {
                System.out.println("Task is Empty.For Updating You Need To Add Task First");
                break;
            } else {
                boolean validTask = false;
                while (!validTask) {
                    try {
                        System.out.println("Select Task");
                        updateChoice = sc.nextLine();
                        Integer ch = new Integer(updateChoice);
                        if (ch - 1 > currentUser.getTask().size()) {
                            throw new Exception();
                        }
                        System.out.print("Description :");
                        System.out.println(currentUser.getTask().get(ch-1).getDescription());
                        System.out.print("Status :");
                        System.out.println(currentUser.getTask().get(ch-1).getStatus());

                        boolean validInput = false;
                        while (!validInput) {
                            System.out.println("Enter Your Choice");
                            System.out.println("1.Update Description");
                            System.out.println("2.Update Status");
                            System.out.println("3.Edit Title");
                            updateChoice = sc.nextLine();
                            Integer choiceType = new Integer(updateChoice);
                            if (choiceType == 1) {
                                System.out.println("Enter new Description");
                                updateChoice = sc.nextLine();
                                if (updateChoice.equals("exit")) {
                                    throw new Exception();
                                }
                                currentUser.getTask().get(ch-1).setDescription(updateChoice);
                                validInput = true;
                                System.out.println("Description Updated Successfully");
                            } else if (choiceType == 2) {
                                System.out.println("Select Status  :");
                                System.out.println("1.TODO");
                                System.out.println("2.IN_PROGRESS");
                                System.out.println("3.DONE");
                                updateChoice = sc.nextLine();
                                Integer chStatus = new Integer(updateChoice);
                                if (chStatus == 1) {
                                    currentUser.getTask().get(ch-1).setStatus("TODO");
                                    validInput = true;
                                } else if (chStatus == 2) {
                                    currentUser.getTask().get(ch-1).setStatus("IN_PROGRESS");
                                    validInput = true;
                                } else if (chStatus == 3) {
                                    currentUser.getTask().get(ch-1).setStatus("DONE");
                                    validInput = true;
                                } else {
                                    System.out.println("Invalid Input");
                                }
                                if(validInput)
                                {
                                    System.out.print("Status Updated Successfully");
                                }

                            } else if (choiceType == 3) {
                                System.out.println("Enter new Title");
                                updateChoice = sc.nextLine();
                                if (updateChoice.equals("exit")) {
                                    throw new Exception();
                                }
                                currentUser.getTask().get(ch-1).setTitle(updateChoice);
                                validInput = true;
                                System.out.print("Title Edited Successfully");
                            } else {
                                System.out.println("Invalid Input");
                            }

                        }
                        validTask=true;
                    } catch (Exception e) {
                        if (updateChoice.equals("exit")) {
                            System.out.println("Exiting..");
                            exit = true;
                            validTask=true;
                        } else {
                            System.out.println("Invalid Input");
                        }
                    }
                }
            }
        }
    }
  public void list() {
           String listChoice=null;
           boolean exit=false;
        while (!exit) {
            try {
                if(TaskManagerUtil.isTaskEmpty(currentUser))
                {
                    System.out.println("Task is Empty you need to add task First");
                    break;
                }
                System.out.println("===========================");
                System.out.println("Enter The Choice Of List");
                System.out.println("1.List All Task ");
                System.out.println("2.List Task By TODO");
                System.out.println("3.List Task By IN_PROGRESS");
                System.out.println("4.List Task By DONE");
                System.out.println("===========================");
                listChoice = sc.nextLine();

                Integer listCh = new Integer(listChoice);
                if (listCh == 1) {
                    TaskManagerUtil.displayStatus(currentUser, "TODO");
                    TaskManagerUtil.displayStatus(currentUser, "IN_PROGRESS");
                    TaskManagerUtil.displayStatus(currentUser, "DONE");
                }
                else if(listCh==2)
                {
                    TaskManagerUtil.displayStatus(currentUser, "TODO");
                }
                else if(listCh==3)
                {
                    TaskManagerUtil.displayStatus(currentUser, "IN_PROGRESS");
                }
                else if(listCh==4)
                {
                    TaskManagerUtil.displayStatus(currentUser, "DONE");
                }
                else{
                    System.out.println("Invalid choice");
                }

            } catch (Exception e) {
                if (listChoice.equals("exit")) {
                    System.out.println("Exiting..");
                    exit=true;
                } else {
                    System.out.println("Invalid Choice");
                }
            }
        }
    }
   public void delete() {
        boolean exit = false;
        while (!exit) {
            System.out.println("Delete Task enter 'exit' for exit");
            TaskManagerUtil.display(currentUser);
            if (TaskManagerUtil.isTaskEmpty(currentUser)) {
                System.out.println("Task is Empty.You Need To Add Task For Deleting");
                break;
            } else {
                String deleteChoice = null;
                boolean validInput=false;
                while(!validInput) {
                    try {
                        System.out.println("Enter Choice for Delete ");
                        deleteChoice = sc.nextLine();
                        if(deleteChoice.equals("exit"))
                        {
                            throw new Exception();
                        }
                        Integer ch = new Integer(deleteChoice);
                        currentUser.getTask().remove(ch-1);
                        validInput=true;
                        System.out.print("Task Deleted Successfully");
                    } catch (Exception e) {
                        if (deleteChoice.equals("exit")) {
                            System.out.println("Exiting..");
                            exit = true;
                            validInput=true;
                        } else {
                            System.out.println("Invalid Choice");
                        }
                    }
                }
            }
        }
    }
    public void exit()
    {
        System.out.println("Exiting.....");
    }
    }

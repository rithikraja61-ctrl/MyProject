import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserManager implements UserManagerInterface{
    Scanner sc = new Scanner(System.in);
    private List<UserInterface> user=new ArrayList();
    public UserInterface login()
    {
        UserInterface currentUser = null;
        try {
            System.out.println("Enter  User Name ");
            String currentUsername = sc.nextLine();//getting username from terminal
            for (int i = 0; i <user.size(); i++)// loop until all the user array length
            {
             if (user.get(i).getUserName().equals(currentUsername))// checking the newly enter username is already stored in user
                {
                    currentUser = user.get(i);
                    break;
                }
            }

            if (currentUser == null) {
                currentUser = new User(currentUsername);
                        user.add(currentUser);
            }
        }catch(Exception e)
        {
          System.out.println("USER LIMIT REACH");
        }
        return currentUser;
    }
}

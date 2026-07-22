import java.util.ArrayList;
import java.util.List;

public class User implements UserInterface{
    private String userName;
    List<TaskInterface> task=new ArrayList();
    public User()
    {

    }
    public User(String name)
    {
         this.userName=name;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<TaskInterface> getTask() {
        return task;
    }

}

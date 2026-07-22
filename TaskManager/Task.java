public class Task implements TaskInterface{
    private String description=null,status="TODO";
    private String title;
    public Task()
    {

    }
    public Task(String s)
    {
        this.title=s;
    }
     public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

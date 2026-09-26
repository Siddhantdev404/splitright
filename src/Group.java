import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupId;
    private String groupName;
    private List<User> members;
    private List<Expense> expenses;

    public Group(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.members = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    public void addMember(User user) {
        this.members.add(user);
    }

    public void addExpense(Expense expense) {
        this.expenses.add(expense);
    }

    public List<User> getMembers() {
        return this.members;
    }

    public List<Expense> getExpenses() {
        return this.expenses;
    }

    public String getGroupName() {
        return this.groupName;
    }
}
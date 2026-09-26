import java.util.List;
import java.util.Map;

public class Account {
    private String accountId;
    private String ownerName;
    private double balance;

    public Account(String accountId, String ownerName) {
        this.accountId = accountId;
        this.ownerName = ownerName;
        this.balance = 0.0;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }
        this.balance += amount;
    }

    public double getBalance() {
        return this.balance;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public static void main(String[] args) {
    User u1 = new User("U1", "Rahul", "rahul@gmail.com");
    User u2 = new User("U2", "Priya", "priya@gmail.com");
    User u3 = new User("U3", "Arjun", "arjun@gmail.com");

    // Two expenses paid by different people — this is what makes
    // the aggregation interesting
    Expense e1 = new Expense("E1", "Dinner", 600.0, "U1");
    Expense e2 = new Expense("E2", "Auto ride", 150.0, "U2");

    Group group = new Group("G1", "Goa Trip");
    group.addMember(u1);
    group.addMember(u2);
    group.addMember(u3);
    group.addExpense(e1);
    group.addExpense(e2);

    SettlementEngine engine = new SettlementEngine();

    // Aggregate net balances across both expenses
    Map<String, Double> netBalances = engine.aggregateNetBalances(
            group.getExpenses(), group.getMembers());

    System.out.println("Net balances after all expenses:");
    for (Map.Entry<String, Double> entry : netBalances.entrySet()) {
        double bal = entry.getValue();
        if (bal < 0) {
            System.out.println("  " + entry.getKey() + " is owed Rs." + Math.abs(bal));
                            
        } else if (bal > 0.01) {
            System.out.println("  " + entry.getKey() + " owes Rs." + bal);
                            
        } else {
            System.out.println("  " + entry.getKey() + " is settled");
        }
    }

    // Settle the debts — get minimum transactions
    List<String> transactions = engine.settleDebts(netBalances);

    System.out.println("\nTo settle all debts:");
    for (String transaction : transactions) {
        System.out.println("  " + transaction);
    }
}
}
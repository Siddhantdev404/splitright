import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettlementEngine {

    // Step 1: Aggregate net balances across ALL expenses
    // Returns a single Map: userId -> net balance across every expense
    // Negative = this person is owed money (they overpaid)
    // Positive = this person owes money (others paid for them)
    public Map<String, Double> aggregateNetBalances(
            List<Expense> expenses, List<User> members) {

        // Start everyone at 0.0 — clean slate before processing
        Map<String, Double> netBalances = new HashMap<>();
        for (User member : members) {
            netBalances.put(member.getUserId(), 0.0);
        }

        SplitCalculator calculator = new SplitCalculator();

        // Process each expense and ADD its result to running totals
        // We use getOrDefault in case a userId appears unexpectedly
        for (Expense expense : expenses) {
            Map<String, Double> splitResult =
                calculator.calculateEqualSplit(expense, members);

            for (Map.Entry<String, Double> entry : splitResult.entrySet()) {
                String userId = entry.getKey();
                double amount = entry.getValue();

                // Add this expense's share to the user's running total
                // getOrDefault used defensively — key should exist but
                // this prevents a NullPointerException if it doesn't
                double currentBalance = netBalances.getOrDefault(userId, 0.0);
                netBalances.put(userId, currentBalance + amount);
            }
        }

        return netBalances;
    }

    // Step 2: Convert net balances into minimum settlement transactions
    // A "transaction" is simply: fromUserId pays toUserId this amount
    public List<String> settleDebts(Map<String, Double> netBalances) {

        // List chosen here because order matters —
        // we want to display transactions in the sequence they were created
        List<String> transactions = new ArrayList<>();

        // Separate into two lists — who owes (debtors) and who is owed (creditors)
        // We copy into new lists because we will modify the values as we settle
        List<String> debtors = new ArrayList<>();   // positive balance = owes money
        List<String> creditors = new ArrayList<>(); // negative balance = is owed money

        for (Map.Entry<String, Double> entry : netBalances.entrySet()) {
            // Small threshold to ignore floating point dust like 0.000001
            if (entry.getValue() > 0.01) {
                debtors.add(entry.getKey());
            } else if (entry.getValue() < -0.01) {
                creditors.add(entry.getKey());
            }
            // If balance is between -0.01 and 0.01, person is settled — skip them
        }

        // Greedy algorithm: match largest debtor to largest creditor
        // Keep going until everyone is settled
        // We use index pointers because we modify amounts in the map directly
        int d = 0; // pointer into debtors list
        int c = 0; // pointer into creditors list

        while (d < debtors.size() && c < creditors.size()) {
            String debtorId = debtors.get(d);
            String creditorId = creditors.get(c);

            double owedAmount = netBalances.get(debtorId);      // positive
            double owingAmount = netBalances.get(creditorId);   // negative

            // Math.min finds the smaller of the two amounts —
            // we can only settle up to what is owed or what is needed
            double settleAmount = Math.min(owedAmount, Math.abs(owingAmount));

            // Round to 2 decimal places for clean display
            settleAmount = Math.round(settleAmount * 100.0) / 100.0;

            transactions.add(debtorId + " pays " +  creditorId + " Rs." + settleAmount);
                            

            // Reduce both balances by the settled amount
            netBalances.put(debtorId, owedAmount - settleAmount);
            netBalances.put(creditorId, owingAmount + settleAmount);

            // If debtor is fully settled, move to next debtor
            if (netBalances.get(debtorId) <= 0.01) d++;

            // If creditor is fully repaid, move to next creditor
            if (netBalances.get(creditorId) >= -0.01) c++;
        }

        return transactions;
    }
}
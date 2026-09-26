import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplitCalculator {

    // This method takes ONE expense and the list of group members
    // and returns a Map: userId -> what they owe (or are owed)
    public Map<String, Double> calculateEqualSplit(
            Expense expense, List<User> members) {

        // HashMap chosen over TreeMap because we don't need
        // the results sorted — we just need fast key lookups
        Map<String, Double> owingMap = new HashMap<>();

        double totalAmount = expense.getTotalAmount();

        // members.size() gives us the count without us manually
        // tracking it — the List already knows how many it holds
        int numberOfMembers = members.size();

        // Integer division would lose the decimal — we need
        // double division so 600/3 = 200.0 not 200
        double sharePerPerson = totalAmount / numberOfMembers;

        for (User member : members) {

            // .equals() compares the actual string content
            // == would compare memory addresses and would fail here
            if (member.getUserId().equals(expense.getPaidByUserId())) {

                // Payer's net balance: their share MINUS what they
                // paid out of pocket. 200 - 600 = -400.
                // Negative = the group owes THEM this amount
                owingMap.put(member.getUserId(),sharePerPerson - totalAmount);
                            

            } else {
                // Everyone else simply owes their equal share
                owingMap.put(member.getUserId(), sharePerPerson);
            }
        }

        // Return the complete map — every member's net position
        return owingMap;
    }
}
public class Expense {
    private String expenseId;
    private String description;
    private Double totalAmount;
    private String paidByUserId;

    public Expense( String expenseId,String description, Double totalAmount,String paidByUserId){
        this.expenseId=expenseId;
        this.description=description;
        this.totalAmount=totalAmount;
        this.paidByUserId=paidByUserId;

    }
    
    public String getExpenseId(){
        return expenseId;
    }

    public String getDescription(){
        return description;
    }

    public Double getTotalAmount(){
        return totalAmount;
    }

    public String getPaidByUserId(){
        return paidByUserId;
    }

}

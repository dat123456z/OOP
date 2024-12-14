package  model;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import view.DBUTills;

public class ListExpense {
    public void displayExpenses() {
        List<String> expenses = getExpenses();
        for (String expense : expenses) {
            System.out.println(expense);
        }
    }

    private List<String> getExpenses() {
        List<String> expenses = new ArrayList<>();
        try (Connection conn = SQLConnection.getSQLServerConnection()) {
            List<ExpenseManagerModel> expensesList = DBUTills.getExpenses(conn);
            for (ExpenseManagerModel expense : expensesList) {
            expenses.add(expense.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expenses;
    }
}

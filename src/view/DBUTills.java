package view;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ExpenseManagerModel;
public class DBUTills {
    public static List<ExpenseManagerModel> getExpenses(Connection conn) throws SQLException {
        List<ExpenseManagerModel> list = new ArrayList<>();
        String sql = "SELECT category, description, date, amount FROM expenses";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            String category = rs.getString("category");
            String description = rs.getString("description");
            Date date = rs.getDate("date"); // Sử dụng kiểu Date của java.sql
            double amount = rs.getDouble("amount");
            
            // Tạo một đối tượng ExpenseManagerModel và thêm vào danh sách
            ExpenseManagerModel expense = new ExpenseManagerModel(category, description, amount, date);
            list.add(expense);
        }
        
        return list; // Trả về danh sách các khoản chi tiêu
    }
}

package dao.sqlite;

import dao.ExpenseDAO;
import database.ConnectionFactory;
import model.Expense;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteExpenseDAO implements ExpenseDAO {
    @Override
    public List<Expense> getAllExpenses(String budgetId) {
        Connection connection = ConnectionFactory.connection();

        boolean success = false;
        ResultSet rs;
        String sql = "select expenses.id, expenses.month, expenses.day, expenses.year, expenses.name, expenses.amount, expenses.budget_id "
                + "from expenses "
                + "inner join budgets on budgets.id = expenses.budget_id "
                + "where expenses.budget_id = ? ";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, budgetId);

            rs = stmt.executeQuery();
            List<Expense> expenses = new ArrayList<>();
            while (rs.next()) {
                Expense expense = new Expense(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getDouble(6),
                        rs.getString(7)
                );

                expenses.add(expense);
            }

            success = true;
            return expenses;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(success);
        }

        return null;
    }

    @Override
    public boolean createExpense(Expense expense) {
        Connection connection = ConnectionFactory.connection();

        boolean success = false;
        String sql = "insert or ignore into expenses values(?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, expense.id);
            stmt.setString(2, expense.month);
            stmt.setInt(3, expense.day);
            stmt.setInt(4, expense.year);
            stmt.setString(5, expense.name);
            stmt.setDouble(6, expense.amount);
            stmt.setString(7, expense.budgetId);

            success = true;
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(success);
        }

        return false;
    }

    @Override
    public boolean updateExpense(Expense expense) {
        Connection connection = ConnectionFactory.connection();

        boolean success = false;
        String sql = "update expenses set month = ?, "
                + "day = ?, "
                + "year = ?, "
                + "name = ?, "
                + "amount = ? "
                + "where id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, expense.month);
            stmt.setInt(2, expense.day);
            stmt.setInt(3, expense.year);
            stmt.setString(4, expense.name);
            stmt.setDouble(5, expense.amount);
            stmt.setString(6, expense.id);

            success = true;
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(success);
        }

        return false;
    }

    @Override
    public boolean deleteExpense(String id) {
        Connection connection = ConnectionFactory.connection();

        boolean success = false;
        String sql = "delete from expenses where id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);

            success = true;
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(success);
        }

        return false;
    }
}

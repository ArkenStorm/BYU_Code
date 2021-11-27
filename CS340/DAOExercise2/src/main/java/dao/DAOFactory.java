package dao;

public interface DAOFactory {
    DatabaseInitializer createInitializerDAO();
    ExpenseDAO createExpenseDAO();
}

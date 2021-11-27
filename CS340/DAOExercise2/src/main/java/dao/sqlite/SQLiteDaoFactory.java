package dao.sqlite;

import dao.DAOFactory;
import dao.DatabaseInitializer;
import dao.ExpenseDAO;

public class SQLiteDaoFactory implements DAOFactory {
    @Override
    public DatabaseInitializer createInitializerDAO() {
        return new SQLiteInitializer();
    }

    @Override
    public ExpenseDAO createExpenseDAO() {
        return new SQLiteExpenseDAO();
    }
}

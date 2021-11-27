import dao.DAOFactoryHolder;
import dao.sqlite.SQLiteDaoFactory;
import database.ConnectionFactory;
import view.Navigator;
import view.main.MainView;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        DAOFactoryHolder holder = DAOFactoryHolder.getInstance();
        holder.setFactory(new SQLiteDaoFactory());
        holder.getFactory().createInitializerDAO().initializeDatabase();
    }
}

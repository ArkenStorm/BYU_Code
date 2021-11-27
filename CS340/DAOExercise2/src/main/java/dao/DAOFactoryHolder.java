package dao;

public class DAOFactoryHolder {
    private static DAOFactoryHolder instance;
    private DAOFactory factory;

    public static DAOFactoryHolder getInstance() {
        if (instance == null) {
            instance = new DAOFactoryHolder();
        }
        return instance;
    }

    public DAOFactory getFactory() {
        return factory;
    }

    public void setFactory(DAOFactory factory) {
        this.factory = factory;
    }
}

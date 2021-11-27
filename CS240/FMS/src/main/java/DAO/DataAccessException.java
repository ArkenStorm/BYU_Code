package DAO;

public class DataAccessException extends Throwable {
	public DataAccessException(String errorMessage) {
		super(errorMessage);
	}
}

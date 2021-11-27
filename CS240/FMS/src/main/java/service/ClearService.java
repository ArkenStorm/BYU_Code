package service;

import DAO.*;
import result.MessageResult;

/**
 * Service for clearing the database.
 */
public class ClearService {
	/**
	 * Clears the Database.
	 *
	 * @return the message result
	 */
	public MessageResult clear() {
		Database db = Database.getInstance();
		AuthTokenDAO atdao = AuthTokenDAO.getInstance();
		EventDAO edao = EventDAO.getInstance();
		PersonDAO pdao = PersonDAO.getInstance();
		UserDAO udao = UserDAO.getInstance();
		try {
			atdao.clearAuthTokens();
			edao.clearEvents();
			pdao.clearPersons();
			udao.clearUsers();
			db.closeConnection(true);
		}
		catch (DataAccessException e) {
			return new MessageResult(e.getMessage());
		}
		return new MessageResult("Clear succeeded.");
	}
}

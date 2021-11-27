package service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import model.AuthToken;
import model.Person;
import result.MessageResult;
import result.PersonArrayResult;
import result.PersonResult;
import result.Result;

import java.util.List;

/**
 * Service for finding Persons
 */
public class PersonService {
	/**
	 * Gets person by id.
	 *
	 * @param authToken the authtoken
	 * @param personID the person id
	 * @return the person by id
	 */
	public Result getPersonByID(String authToken, String personID) {
		Database db = Database.getInstance();
		PersonDAO pdao = PersonDAO.getInstance();
		AuthTokenDAO atdao = AuthTokenDAO.getInstance();
		Person person;
		try {
			AuthToken token = atdao.getToken(authToken);
			if (token == null) {
				return new MessageResult("Error: The current user is unauthorized to make that request");
			}
			person = pdao.getPersonByID(personID);
			if (person == null) {
				return new MessageResult("Error: No Person with that personID exists");
			}
			else if (person.getUsername() == null || !person.getUsername().equals(token.getUsername())) {
				return new MessageResult("Error: That person does not belong to the current user");
			}
		}
		catch (DataAccessException e) {
			return new MessageResult(e.getMessage());
		}
		return new PersonResult(person);
	}

	/**
	 * Gets ALL the family members of the currentUser based on the provided AuthToken
	 *
	 * @param authToken the authToken
	 * @return the persons
	 */
	public Result getPersons(String authToken) {
		Database db = Database.getInstance();
		PersonDAO pdao = PersonDAO.getInstance();
		AuthTokenDAO atdao = AuthTokenDAO.getInstance();
		List<Person> personList;
		try {
			AuthToken token = atdao.getToken(authToken);
			if (token == null) {
				return new MessageResult("Error: The current user is not authorized to make this request");
			}
			personList = pdao.getUserPersons(token.getUsername());
			db.closeConnection(true);
		} catch (DataAccessException e) {
			return new MessageResult(e.getMessage());
		}
		return new PersonArrayResult(personList);
	}
}

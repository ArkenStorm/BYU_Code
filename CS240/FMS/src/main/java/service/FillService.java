package service;

import DAO.*;
import com.google.gson.Gson;
import model.Event;
import model.Person;
import model.User;
import result.MessageResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

/**
 * Service for filling the database.
 */
public class FillService {
	private Names femaleNames;
	private Names maleNames;
	private Names surnames;
	private LocationList locationList;
	private int numEvents;

	public FillService() {
		numEvents = 0;
		try {
			femaleNames = new Gson().fromJson(Files.readString(Paths.get("./json/fnames.json")), Names.class);
			maleNames = new Gson().fromJson(Files.readString(Paths.get("./json/mnames.json")), Names.class);
			surnames = new Gson().fromJson(Files.readString(Paths.get("./json/snames.json")), Names.class);
			locationList = new Gson().fromJson(Files.readString(Paths.get("./json/locations.json")), LocationList.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fills the database for the specified username.
	 *
	 * @param username    the username
	 * @param generations the generations
	 * @return the message result
	 */
	public MessageResult fill(String username, int generations) {
		Database db = Database.getInstance();
		PersonDAO pdao = PersonDAO.getInstance();
		UserDAO udao = UserDAO.getInstance();
		EventDAO edao = EventDAO.getInstance();
		try {
			User user = udao.getUserByUsername(username);
			if (user == null) {
				return new MessageResult("Error: No user exists with that username.");
			}
			pdao.deleteUserPersons(username);
			Person userPerson = new Person(user.getPersonID(), username, user.getFirstName(), user.getLastName(), user.getGender(), "", "", "");
			int birthYear = 1997;
			Location userBirthLocation = locationList.getRandomLocation();
			Event userBirth = new Event(UUID.randomUUID().toString(), username, userPerson.getPersonID(),
										userBirthLocation.latitude, userBirthLocation.longitude, userBirthLocation.country,
										userBirthLocation.city, "birth", birthYear);
			edao.insertEvent(userBirth);
			if (generations > 1) {
				Person userFather = generateData(username, generations - 1, birthYear - 25, user.getGender());
				Person userMother = generateData(username, generations - 1, birthYear - 25, user.getGender());
				userPerson.setFatherID(userFather.getPersonID());
				userPerson.setMotherID(userMother.getPersonID());
				userFather.setSpouseID(userMother.getPersonID());
				userMother.setSpouseID(userFather.getPersonID());
				pdao.insertPerson(userFather);
				pdao.insertPerson(userMother);
				tillDeathDoThemPart(username, userFather, userMother, birthYear);
			}
			pdao.insertPerson(userPerson);
			db.closeConnection(true);
		}
		catch (DataAccessException e) {
			return new MessageResult(e.getMessage());
		}
		int addedPersons = (int) Math.pow(2, generations + 1) - 1;
		return new MessageResult("Successfully added " + addedPersons + " persons and " + numEvents + " events to the database.");
	}

	private Person generateData(String username, int generations, int birthYear, String gender) throws DataAccessException {
		PersonDAO pdao = PersonDAO.getInstance();
		EventDAO edao = EventDAO.getInstance();
		String firstname;
		if (gender.equals("m")) {
			firstname = maleNames.getRandomName();
		}
		else {
			firstname = femaleNames.getRandomName();
		}
		String lastname = surnames.getRandomName();

		Person person = new Person(UUID.randomUUID().toString(), username, firstname, lastname, gender, "", "", "");
		Location location = locationList.getRandomLocation();
		Event birth = new Event(UUID.randomUUID().toString(), username, person.getPersonID(), location.latitude, location.longitude, location.country, location.city, "birth", birthYear);
		edao.insertEvent(birth);
		numEvents += 1;
		if (generations > 0) {
			Person father = generateData(username, generations - 1, birthYear - 25, "m");
			Person mother = generateData(username, generations - 1, birthYear - 25, "f");
			father.setSpouseID(mother.getPersonID());
			mother.setSpouseID(father.getPersonID());
			person.setFatherID(father.getPersonID());
			person.setMotherID(mother.getPersonID());
			pdao.insertPerson(father);
			pdao.insertPerson(mother);

			tillDeathDoThemPart(username, father, mother, birthYear);
		}
		return person;
	}

	private void tillDeathDoThemPart(String username, Person father, Person mother, int birthYear) throws DataAccessException {
		PersonDAO pdao = PersonDAO.getInstance();
		EventDAO edao = EventDAO.getInstance();

		Location marriageLocation = locationList.getRandomLocation();
		Event marriage = new Event(UUID.randomUUID().toString(), username, father.getPersonID(), marriageLocation.latitude, marriageLocation.longitude, marriageLocation.country, marriageLocation.city, "marriage", birthYear - 2);
		edao.insertEvent(marriage);
		numEvents += 1;
		marriage.setPersonID(mother.getPersonID());
		marriage.setEventID(UUID.randomUUID().toString());
		edao.insertEvent(marriage);
		numEvents += 1;

		Location deathLocation = locationList.getRandomLocation();
		Event death = new Event(UUID.randomUUID().toString(), username, father.getPersonID(), deathLocation.latitude, deathLocation.longitude, deathLocation.country, deathLocation.city, "death", birthYear + 45);
		edao.insertEvent(death);
		numEvents += 1;
		death.setPersonID(mother.getPersonID());
		death.setEventID(UUID.randomUUID().toString());
		edao.insertEvent(death);
		numEvents += 1;
	}

	private class Names {
		String[] data;
		public String getRandomName() {
			return data[new Random().nextInt(data.length)];
		}
	}

	private class Location {
		String country;
		String city;
		Double latitude;
		Double longitude;
	}

	private class LocationList {
		Location[] data;
		public Location getRandomLocation() {
			return data[new Random().nextInt(data.length)];
		}
	}
}

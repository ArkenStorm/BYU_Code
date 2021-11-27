package com.example.fmc;

import com.example.fmc.extendedModels.FullEvent;
import com.example.fmc.extendedModels.FullPerson;
import model.Event;
import model.Person;

import java.util.*;

public class Datacache {
	private static Datacache instance;
	private boolean loggedIn = false;
	private String token;
	private String userPersonID;
	private Person user;
	private List<Person> familyData;
	private Map<String, FullPerson> fullPersons = new HashMap<>();
	private List<FullEvent> fullEvents = new ArrayList<>();
	private Map<String, List<FullEvent>> personEvents = new HashMap<>();
	private List<FullEvent> fatherSideMale = new ArrayList<>();
	private List<FullEvent> fatherSideFemale = new ArrayList<>();
	private List<FullEvent> motherSideMale = new ArrayList<>();
	private List<FullEvent> motherSideFemale = new ArrayList<>();
	private boolean showLifeStoryLines = true;
	private boolean showFamilyTreeLines = true;
	private boolean showSpouseLines = true;
	private boolean showFatherSide = true;
	private boolean showMotherSide = true;
	private boolean showMale = true;
	private boolean showFemale = true;
	private Map<String, Float> eventColors = new TreeMap<>();
	private boolean settingsUpdated = false;

	public static Datacache getInstance() {
		if (instance == null) {
			instance = new Datacache();
		}
		return instance;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserPersonID() {
		return userPersonID;
	}

	public void setUserPersonID(String userPersonID) {
		this.userPersonID = userPersonID;
	}

	public void setUser(Person user) {
		this.user = user;
	}

	public Person getUser() {
		return user;
	}

	public List<Person> getFamilyData() {
		return familyData;
	}

	public Map<String, FullPerson> getFullPersons() {
		return fullPersons;
	}

	public void setFamilyData(List<Person> familyData) {
		this.familyData = familyData;
		for (Person person : familyData) {
			Person father = findPersonByID(person.getFatherID());
			Person mother = findPersonByID(person.getMotherID());
			Person spouse = findPersonByID(person.getSpouseID());
			this.fullPersons.put(person.getPersonID(), new FullPerson(person.getPersonID(), person.getUsername(), person.getFirstName(),
					person.getLastName(), person.getGender(), person.getFatherID(), person.getMotherID(),
					person.getSpouseID(), father, mother, spouse));
		}
	}

	public FullEvent getEventByID(String eventID) {
		if (eventID == null || eventID.isEmpty()) {
			return null;
		}
		for (FullEvent e : fullEvents) {
			if (e.getEventID().equals(eventID)) {
				return e;
			}
		}
		return null;
	}

	private Person findPersonByID(String personID) {
		if (personID == null || personID.isEmpty()) {
			return null;
		}
		for (Person p : familyData) {
			if (p.getPersonID().equals(personID)) {
				return p;
			}
		}
		return null;
	}

	public void setFullEvents(List<Event> eventData) {
		for (Event event : eventData) {
			Person person = findPersonByID(event.getPersonID());
			fullEvents.add(new FullEvent(event.getEventID(), event.getUsername(), event.getPersonID(),
					event.getLatitude(), event.getLongitude(), event.getCountry(), event.getCity(),
					event.getEventType(), event.getYear(), person));
		}
		setFatherSideMale();
		setFatherSideFemale();
		setMotherSideMale();
		setMotherSideFemale();
	}

	public List<FullEvent> getFullEvents() {
		return fullEvents;
	}

	private class SortByYear implements Comparator<FullEvent> {
		@Override
		public int compare(FullEvent o1, FullEvent o2) {
			return o1.getYear() - o2.getYear();
		}
	}

	public List<FullEvent> getPersonEvents(String personID) {
		if (personID == null || personID.isEmpty()) {
			return new ArrayList<>();
		}
		if (personEvents.containsKey(personID)) {
			return personEvents.get(personID);
		}
		List<FullEvent> eventList = new ArrayList<>();
		for (FullEvent event : fullEvents) {
			if (event.getPersonID().equals(personID)) {
				eventList.add(event);
			}
		}
		personEvents.put(personID, eventList);
		eventList.sort(new SortByYear());
		return eventList;
	}

	public List<FullEvent> getFilteredPersonEvents(String personID) {
		if (personID == null || personID.isEmpty()) {
			return new ArrayList<>();
		}
		List<FullEvent> eventList = new ArrayList<>();
		List<FullEvent> filteredEvents = getFilteredEvents();
		for (FullEvent event : filteredEvents) {
			if (event.getPersonID().equals(personID)) {
				eventList.add(event);
			}
		}
		return eventList;
	}

	private void setFatherSideMale() {
		if (user.getGender().equals("m")) {
			fatherSideMale.addAll(getPersonEvents(userPersonID));
		}
		fatherSideMaleHelper(fullPersons.get(user.getFatherID()));
	}

	private void fatherSideMaleHelper(Person next) {
		if (next == null) {
			return;
		}
		fatherSideMale.addAll(getPersonEvents(next.getPersonID()));
		if (next.getFatherID() == null || next.getFatherID().isEmpty()) {
			return;
		}
		fatherSideMaleHelper(fullPersons.get(next.getFatherID()));
	}

	private void setFatherSideFemale() {
		if (user.getGender().equals("f")) {
			fatherSideFemale.addAll(getPersonEvents(userPersonID));
		}
		fatherSideFemaleHelper(fullPersons.get(user.getFatherID()));
	}

	private void fatherSideFemaleHelper(Person next) {
		if (next == null) {
			return;
		}
		fatherSideFemale.addAll(getPersonEvents(next.getPersonID()));
		if (next.getMotherID() == null || next.getMotherID().isEmpty()) {
			return;
		}
		fatherSideFemaleHelper(fullPersons.get(next.getMotherID()));
	}

	private void setMotherSideMale() {
		if (user.getGender().equals("m")) {
			motherSideMale.addAll(getPersonEvents(userPersonID));
		}
		motherSideMaleHelper(fullPersons.get(user.getMotherID()));
	}

	private void motherSideMaleHelper(Person next) {
		if (next == null) {
			return;
		}
		motherSideMale.addAll(getPersonEvents(next.getPersonID()));
		if (next.getFatherID() == null || next.getFatherID().isEmpty()) {
			return;
		}
		motherSideMaleHelper(fullPersons.get(next.getFatherID()));
	}

	private void setMotherSideFemale() {
		if (user.getGender().equals("f")) {
			motherSideFemale.addAll(getPersonEvents(userPersonID));
		}
		motherSideFemaleHelper(fullPersons.get(user.getMotherID()));
	}

	private void motherSideFemaleHelper(Person next) {
		if (next == null) {
			return;
		}
		motherSideFemale.addAll(getPersonEvents(next.getPersonID()));
		if (next.getFatherID() == null || next.getFatherID().isEmpty()) {
			return;
		}
		motherSideFemaleHelper(fullPersons.get(next.getMotherID()));
	}

	public List<FullEvent> getFilteredEvents() {
		if (showFatherSide && showMotherSide && showMale && showFemale) {
			return fullEvents;
		}
		else if (showFatherSide && showMale && showFemale) {
			List<FullEvent> combinedList = new ArrayList<>();
			combinedList.addAll(fatherSideMale);
			combinedList.addAll(fatherSideFemale);
			return combinedList;
		}
		else if (showMotherSide && showMale && showFemale) {
			List<FullEvent> combinedList = new ArrayList<>();
			combinedList.addAll(motherSideMale);
			combinedList.addAll(motherSideFemale);
			return combinedList;
		}
		else if (!showFatherSide && !showMotherSide && showMale && showFemale) {
			List<FullEvent> combinedList = new ArrayList<>();
			combinedList.addAll(getPersonEvents(userPersonID));
			combinedList.addAll(getPersonEvents(user.getSpouseID()));
			return combinedList;
		}
		else if (!showFatherSide && !showMotherSide && showMale && user.getGender().equals("m")) {
			return getPersonEvents(userPersonID);
		}
		else if (!showFatherSide && !showMotherSide && showFemale && user.getGender().equals("f")) {
			return getPersonEvents(userPersonID);
		}
		else if (showFatherSide && showMale) {
			return fatherSideMale;
		}
		else if (showFatherSide && showFemale) {
			return fatherSideFemale;
		}
		else if (showMotherSide && showMale) {
			return motherSideMale;
		}
		else if (showMotherSide && showFemale) {
			return motherSideFemale;
		}
		return new ArrayList<>(); // return empty list instead?
	}

	public void setSettings(boolean lifeStory, boolean familyTree, boolean spouse, boolean father, boolean mother, boolean male, boolean female) {
		showLifeStoryLines = lifeStory;
		showFamilyTreeLines = familyTree;
		showSpouseLines = spouse;
		showFatherSide = father;
		showMotherSide = mother;
		showMale = male;
		showFemale = female;
	}

	public boolean getShowLifeStoryLines() {
		return showLifeStoryLines;
	}

	public boolean getShowFamilyTreeLines() {
		return showFamilyTreeLines;
	}

	public boolean getShowSpouseLines() {
		return showSpouseLines;
	}

	public boolean getShowFatherSide() {
		return showFatherSide;
	}

	public boolean getShowMotherSide() {
		return showMotherSide;
	}

	public boolean getShowMale() {
		return showMale;
	}

	public boolean getShowFemale() {
		return showFemale;
	}

	public List<Person> getPersonFamily(Person person) {
		List<Person> family = new ArrayList<>();
		if (person.getFatherID() != null && !person.getFatherID().isEmpty()) {
			family.add(fullPersons.get(person.getFatherID()));
		}
		if (person.getMotherID() != null && !person.getMotherID().isEmpty()) {
			family.add(fullPersons.get(person.getMotherID()));
		}
		if (person.getSpouseID() != null && !person.getSpouseID().isEmpty()) {
			family.add(fullPersons.get(person.getSpouseID()));
		}
		// now get le children
		for (Person human : familyData) {
			if ((human.getFatherID() != null && human.getFatherID().equals(person.getPersonID())) ||
					(human.getMotherID() != null && human.getMotherID().equals(person.getPersonID()))) {
				family.add(human);
			}
		}
		return family;
	}

	public Map<String, Float> getEventColors() {
		return eventColors;
	}

	public void setEventColors(Map<String, Float> eventColors) {
		this.eventColors = eventColors;
	}

	public boolean isSettingsUpdated() {
		return settingsUpdated;
	}

	public void setSettingsUpdated(boolean settingsUpdated) {
		this.settingsUpdated = settingsUpdated;
	}

	public ArrayList<Object> getSearchFilteredItems(String filter) {
		filter = filter.toLowerCase();
		ArrayList<Object> combinedList = new ArrayList<>();
		List<FullEvent> filteredEvents = getFilteredEvents();
		List<Person> allPeople = getFamilyData();
		for (FullEvent event : filteredEvents) {
			if (event.getCountry().toLowerCase().contains(filter) || event.getCity().toLowerCase().contains(filter) ||
			event.getEventType().toLowerCase().contains(filter) || Integer.toString(event.getYear()).contains(filter)) {
				combinedList.add(event);
			}
		}
		for (Person person : allPeople) {
			if (person.getFirstName().toLowerCase().contains(filter) || person.getLastName().toLowerCase().contains(filter)) {
				combinedList.add(person);
			}
		}

		return combinedList;
	}
}

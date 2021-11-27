package com.example.fmc;

import com.example.fmc.extendedModels.FullEvent;
import model.Event;
import model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class DatacacheTest {
	Datacache cache = Datacache.getInstance();
	static List<Person> persons;
	static List<Event> events;

	@BeforeAll
	static void setUp() {
		Person user = new Person("007", "username", "James", "Bond", "m", "2", "3", "");
		Datacache.getInstance().setUser(user);
		persons = new ArrayList<>();
		persons.add(new Person("1", "username", "Billy", "Joel", "m", "2", "3", ""));
		persons.add(new Person("2", "username", "Billy Sr.", "Joel", "m", "", "", "3"));
		persons.add(new Person("3", "username", "Hannah", "Joel", "f", "", "", "2"));
		persons.add(new Person("4", "username", "Billy III", "Joel", "m", "1", "", ""));
		Datacache.getInstance().setFamilyData(persons);
		events = new ArrayList<>();
		events.add(new Event("11", "username", "1", 40.8448, 73.8648, "USA", "The Bronx", "birth", 1949));
		events.add(new Event("12", "username", "1", 40.8448, 73.8648, "USA", "The Bronx", "death", 2024));
		events.add(new Event("13", "username", "2", 40.8448, 73.8648, "USA", "The Bronx", "birth", 1919));
		events.add(new Event("14", "username", "2", 40.8448, 73.8648, "USA", "The Bronx", "marriage", 1939));
		events.add(new Event("15", "username", "3", 40.8448, 73.8648, "USA", "The Bronx", "marriage", 1939));
		events.add(new Event("16", "username", "4", 40.8448, 73.8648, "USA", "The Bronx", "birth", 2009));
		events.add(new Event("17", "username", "2", 40.8448, 73.8648, "USA", "The Bronx", "death", 1989));
		events.add(new Event("18", "username", "3", 40.8448, 73.8648, "USA", "The Bronx", "death", 1989));
		events.add(new Event("19", "username", "3", 40.8448, 73.8648, "USA", "The Bronx", "birth", 1919));
		Datacache.getInstance().setFullEvents(events);
	}

	@Test
	void relationshipsSuccess() {
		List<Person> familyResult = cache.getPersonFamily(persons.get(0));
		Assertions.assertEquals(3, familyResult.size());
	}

	@Test
	void relationshipsAbnormal() {
		List<Person> familyResult = cache.getPersonFamily(new Person("420", "somedude", "Jerry", "Springer", "m", "", "", ""));
		Assertions.assertEquals(0, familyResult.size());
	}

	@Test
	void eventFilterSuccess() {
		cache.setSettings(true, true, true, false, true, true, true);
		List<FullEvent> filteredEvents = cache.getFilteredEvents();
		Assertions.assertEquals(6, filteredEvents.size());
	}

	@Test
	void eventFilterAbnormal() {
		cache.setSettings(true, true, true, false, false, false, false);
		List<FullEvent> filteredEvents = cache.getFilteredEvents();
		Assertions.assertEquals(0, filteredEvents.size());
	}

	@Test
	void eventSortSuccess() {
		List<FullEvent> sortedEvents = cache.getPersonEvents("2");
		Assertions.assertTrue(sortedEvents.get(0).getYear() < sortedEvents.get(1).getYear());
	}

	@Test
	void eventSortAbnormal() {
		List<FullEvent> sortedEvents = cache.getPersonEvents("");
		Assertions.assertEquals(0, sortedEvents.size());
	}

	@Test
	void searchSuccess() {
		ArrayList<Object> searchResults = cache.getSearchFilteredItems("birth");
		Assertions.assertEquals(4, searchResults.size());
	}

	@Test
	void searchAbnormal() {
		ArrayList<Object> searchResults = cache.getSearchFilteredItems("dontyoudaregivemeanything");
		Assertions.assertEquals(0, searchResults.size());
	}
}

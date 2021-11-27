package com.example.fmc.extendedModels;

import model.Event;
import model.Person;

public class FullEvent extends Event {
	Person person;

	public FullEvent(String eventID, String username, String personID, Double latitude, Double longitude, String country, String city, String eventType, int year, Person person) {
		super(eventID, username, personID, latitude, longitude, country, city, eventType, year);
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}

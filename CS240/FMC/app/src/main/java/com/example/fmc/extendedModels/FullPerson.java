package com.example.fmc.extendedModels;

import model.Person;

public class FullPerson extends Person {
	Person father;
	Person mother;
	Person spouse;

	public FullPerson(String personID, String username, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID, Person father, Person mother, Person spouse) {
		super(personID, username, firstName, lastName, gender, fatherID, motherID, spouseID);
		this.father = father;
		this.mother = mother;
		this.spouse = spouse;
	}

	public Person getFather() {
		return father;
	}

	public void setFather(Person father) {
		this.father = father;
	}

	public Person getMother() {
		return mother;
	}

	public void setMother(Person mother) {
		this.mother = mother;
	}

	public Person getSpouse() {
		return spouse;
	}

	public void setSpouse(Person spouse) {
		this.spouse = spouse;
	}
}

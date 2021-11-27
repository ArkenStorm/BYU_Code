create table if not EXISTS User
(
	Username varchar(255) primary key,
	Password varchar(255) not NULL,
	Email varchar(255) not NULL,
	FirstName varchar(255) not NULL,
	LastName varchar(255) not NULL,
	Gender varchar(1) not NULL,
	PersonID varchar(255) not NULL,
	foreign key(PersonID) references Person(PersonID)
);

create table if not EXISTS Person
(
	PersonID varchar(255) primary key,
	Username varchar(255),
	FirstName varchar(255) not NULL,
	LastName varchar(255) not NULL,
	Gender varchar(1) not NULL,
	FatherID varchar(255),
	MotherID varchar(255),
	SpouseID varchar(255),
	foreign key(Username) references User(Username),
	foreign key(FatherID) references Person(PersonID),
	foreign key(MotherID) references Person(PersonID),
	foreign key(SpouseID) references Person(PersonID)
);

create table if not EXISTS Event
(
	EventID varchar(255) primary key,
	Username varchar(255),
	PersonID varchar(255) not NULL,
	Latitude varchar(255),
	Longitude varchar(255),
	Country varchar(255),
	City varchar(255),
	EventType varchar(255),
	Year varchar(4),
	foreign key(Username) references User(Username),
	foreign key(PersonID) references Person(PersonID)
);

create table if not EXISTS AuthToken
(
	AuthToken varchar(255) primary KEY,
	Username varchar(255) not NULL,
	foreign key(Username) references User(Username)
);
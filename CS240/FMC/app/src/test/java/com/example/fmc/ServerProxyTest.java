package com.example.fmc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import result.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

public class ServerProxyTest {
	String baseURL = "http://192.168.100.217:8080";

	@Test
	void loginSuccess() throws MalformedURLException {
		URL url = new URL(baseURL + "/user/login");
		Result result = ServerProxy.authorizeUser(new LoginRequest("username", "password"), url);
		Assertions.assertTrue(result instanceof AuthorizationResult);
	}

	@Test
	void loginFail() throws MalformedURLException {
		URL url = new URL(baseURL + "/user/login");
		Result result = ServerProxy.authorizeUser(new LoginRequest("username", "thisisnotthepassword"), url);
		Assertions.assertTrue(result instanceof MessageResult);
	}

	@Test
	void registerSuccess() throws MalformedURLException {
		URL url = new URL(baseURL + "/user/register");
		Result result = ServerProxy.authorizeUser(new RegisterRequest("username" + LocalDateTime.now().toString(), "password",
				"email@email.com", "Joe", "Shmoe", "m"), url);
		Assertions.assertTrue(result instanceof AuthorizationResult);
	}

	@Test
	void registerFail() throws MalformedURLException {
		URL url = new URL(baseURL + "/user/register");
		Result result = ServerProxy.authorizeUser(new RegisterRequest("username", "password",
				"email@email.com", "Joe", "Shmoe", "Apache Attack Helicopter"), url);
		Assertions.assertTrue(result instanceof MessageResult);
	}

	@Test
	void getPeopleSuccess() throws MalformedURLException {
		URL loginUrl = new URL(baseURL + "/user/login");
		Result result = ServerProxy.authorizeUser(new LoginRequest("username", "password"), loginUrl);
		if (result instanceof AuthorizationResult) { // in this case, since that user is registered, it should always succeed
			Datacache.getInstance().setToken(((AuthorizationResult) result).getAuthToken());
			URL peopleURL = new URL(baseURL + "/person");
			result = ServerProxy.getPersons(peopleURL);
		}
		Assertions.assertTrue(result instanceof PersonArrayResult);
	}

	@Test
	void getPeopleFail() throws MalformedURLException {
		Datacache.getInstance().setToken("badtoken");
		URL peopleURL = new URL(baseURL + "/person");
		Result result = ServerProxy.getPersons(peopleURL);
		Assertions.assertTrue(result instanceof MessageResult);
	}

	@Test
	void getEventsSuccess() throws MalformedURLException {
		URL loginUrl = new URL(baseURL + "/user/login");
		Result result = ServerProxy.authorizeUser(new LoginRequest("username", "password"), loginUrl);
		if (result instanceof AuthorizationResult) { // in this case, since that user is registered, it should always succeed
			Datacache.getInstance().setToken(((AuthorizationResult) result).getAuthToken());
			URL eventURL = new URL(baseURL + "/event");
			result = ServerProxy.getEvents(eventURL);
		}
		Assertions.assertTrue(result instanceof EventArrayResult);
	}

	@Test
	void getEventsFail() throws MalformedURLException {
		Datacache.getInstance().setToken("badtoken");
		URL eventURL = new URL(baseURL + "/event");
		Result result = ServerProxy.getPersons(eventURL);
		Assertions.assertTrue(result instanceof MessageResult);
	}
}

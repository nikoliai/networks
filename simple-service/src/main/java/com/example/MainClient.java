package com.example;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.hm.taxi.Car;

public class MainClient {

	public static void main(String[] args) {
		String jasonString1 = "{\n\"id\":3,\n\"state\":\"" + Car.Status.Free.name() + "\"\n\"location\":[\"9\", \"Lindenweg\", \"Gilching\", \"germany\"]}";
		String jasonString2 = "{\n\"id\":3,\n\"state\":\"" + Car.Status.Inactive.name() + "\"\n\"location\":[\"9\", \"Lindenweg\", \"Gilching\", \"germany\"]}";
		String jasonString3 = "{\n\"id\":3,\n\"state\":\"" + Car.Status.Free.name() + "\"\n\"location\":[\"9\", \"Lindenweg\", \"Gilching\", \"germany\"]}";
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/myapp/driver/state/");
		String response = target.request().accept(MediaType.TEXT_PLAIN_TYPE)
				.post(Entity.entity(jasonString1, MediaType.APPLICATION_JSON), String.class);
		String response2 = target.request().accept(MediaType.TEXT_PLAIN_TYPE)
				.post(Entity.entity(jasonString2, MediaType.APPLICATION_JSON), String.class);
		String response3 = target.request().accept(MediaType.TEXT_PLAIN_TYPE)
				.post(Entity.entity(jasonString3, MediaType.APPLICATION_JSON), String.class);

	}

}

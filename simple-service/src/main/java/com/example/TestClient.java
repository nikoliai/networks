package com.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Path("mytest/{houseNr1}/{street1}/{city1}/{country1}/{houseNr2}/{street2}/{city2}/{country2}")
// @Path("mytest/{latStart}/{longStart}/{latFinish}/{longFinish}")

public class TestClient {

    String id = "app_id=lCp2io17mGlpmfZDl5jY&app_code=VW3eq70qhKoG9Qed65y9Uw";

    public void changeCoordinates(String latitude, String longitude) {

    }

    // first address
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String calculateCoordinates(@PathParam("houseNr1") String houseNr1, @PathParam("street1") String street1,
            @PathParam("city1") String city1, @PathParam("country1") String country1,
            @PathParam("houseNr2") String houseNr2, @PathParam("street2") String street2,
            @PathParam("city1") String city2, @PathParam("country1") String country2) {

        String requestGetCoordinates = "https://geocoder.api.here.com/6.2/geocode.json?app_id=lCp2io17mGlpmfZDl5jY&app_code=VW3eq70qhKoG9Qed65y9Uw&searchtext=";
        String request1 = requestGetCoordinates + houseNr1 + "+" + street1 + "+" + city1 + "+" + country1;
        String request2 = requestGetCoordinates + houseNr2 + "+" + street2 + "+" + city2 + "+" + country2;

        Client client = ClientBuilder.newClient();

        // System.out.println(request1);

        // first request
        WebTarget webTarget = client.target(request1);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        // get response
        String response = invocationBuilder.get(String.class);
        String lat1 = parseCoordinates(response)[0];
        String lon1 = parseCoordinates(response)[1];

        // second request

        WebTarget webTarget2 = client.target(request2);
        Invocation.Builder invocationBuilder2 = webTarget2.request(MediaType.APPLICATION_JSON);

        // get response
        String response2 = invocationBuilder2.get(String.class);
        String lat2 = parseCoordinates(response2)[0];
        String lon2 = parseCoordinates(response2)[1];


        String requestTravelTime = "http://localhost:8080/myapp/mytest/" + lat1 + "/" + lon1 + "/" + lat2 + "/" + lon2;
        WebTarget webTarget3 = client.target(requestTravelTime);
        System.out.println(requestTravelTime);
        Invocation.Builder invocationBuilder3 = webTarget3.request(MediaType.APPLICATION_JSON);
        String responseTT = invocationBuilder3.get(String.class);
        // parse response
        String trTime  = parseTravelTime(responseTT);
        System.out.println("summary travel time: " + trTime);
        return response;
    }
    
    
    public String parseTravelTime(String responseTT) {
        String trTime = "";
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(responseTT);
            JSONObject jsonObject = (JSONObject) obj;
            // jsonObject.values();
             trTime = ((JSONObject) ((JSONObject) ((JSONArray) ((JSONObject) jsonObject.get("response"))
                    .get("route")).get(0)).get("summary")).get("travelTime").toString();

           // System.out.println("summary travel time: " + trTime);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return trTime;
        
    }

    public String[] parseCoordinates(String response) {

        String[] requestedItems = new String[2];

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(response);
            JSONObject jsonObject = (JSONObject) obj;
            // jsonObject.values();
            JSONObject navPos = ((JSONObject) ((JSONArray) ((JSONObject) ((JSONObject) ((JSONArray) ((JSONObject) ((JSONArray) ((JSONObject) jsonObject
                    .get("Response")).get("View")).get(0)).get("Result")).get(0)).get("Location"))
                            .get("NavigationPosition")).get(0));
            String lat = navPos.get("Latitude").toString();
            requestedItems[0] = lat;
            String lon = navPos.get("Longitude").toString();
            requestedItems[1] = lon;
            // jsonObject.get("response")).get("route").toString();
            System.out.println("latitude: " + lat);
            System.out.println("longitude: " + lon);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return requestedItems;
    }

}

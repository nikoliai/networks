package com.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Path("mytest")
public class TestClient {
    String originalAddress;
    String targetAddress;
    String latitude;
    String longitude;
    String requestGetCoordinates;
    String requestCalculateRoute = "https://route.api.here.com/routing/7.2/calculateroute.json?";
    String id = "app_id=lCp2io17mGlpmfZDl5jY&app_code=VW3eq70qhKoG9Qed65y9Uw";
    
     public void changeCoordinates(String latitude, String longitude){
         this.latitude = latitude;
         this.longitude = longitude;
     }
     
    //public String getCoordinates();
    // public String get
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getIt() {
        Client client = ClientBuilder.newClient();
        String request = requestCalculateRoute + id + "&waypoint0=geo!" + "48.14329555,11.4982583511298&waypoint1=geo!48.1369135,11.5032263&mode=fastest;car;traffic:disabled";
        
        WebTarget webTarget = client.target(request );
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        String response = invocationBuilder.get(String.class);

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(response);
            JSONObject jsonObject = (JSONObject) obj;
    //       jsonObject.values();
            String trTime = ((JSONObject) ((JSONObject) ((JSONArray) ((JSONObject) jsonObject.get("response"))
                   .get("route")).get(0)).get("summary")).get("travelTime").toString();

            // jsonObject.get("response")).get("route").toString();
            System.out.println("summary travel time: " + trTime);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // try {
        // obj = new JSONObject(response);
        // String travelTime = obj.getString("travelTime");
        // System.out.println("travel time is:" + travelTime);
        // System.out.println(obj);

        // } catch (JSONException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        return response;
    }
}

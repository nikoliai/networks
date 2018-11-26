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

@Path("mytest/{latStart}/{longStart}/{latFinish}/{longFinish}")

public class TravelTime {

    String id = "app_id=lCp2io17mGlpmfZDl5jY&app_code=VW3eq70qhKoG9Qed65y9Uw";

    public void changeCoordinates(String latitude, String longitude) {

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String calculateTravelTime(@PathParam("latStart") String latStart, @PathParam("longStart") String longStart,
            @PathParam("latFinish") String latFinish, @PathParam("longFinish") String longFinish) { // , String

        String requestCalculateRoute = "https://route.api.here.com/routing/7.2/calculateroute.json?";

        latStart = "48.14329555";
        longStart = "11.4982583511298";
        latFinish = "48.1369135";
        longFinish = "11.5032263";

        Client client = ClientBuilder.newClient();
        // create request
        String request = requestCalculateRoute + id + "&waypoint0=geo!" + latStart + "," + longStart + "&waypoint1=geo!"
                + latFinish + "," + longFinish + "&mode=fastest;car;traffic:disabled";

        WebTarget webTarget = client.target(request);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        // get response
        String response = invocationBuilder.get(String.class);
        

        return response;

    }
}

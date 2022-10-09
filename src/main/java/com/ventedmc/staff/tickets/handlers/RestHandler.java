package com.ventedmc.staff.tickets.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ventedmc.staff.StaffPlugin;
import com.ventedmc.staff.tickets.TicketModule;
import com.ventedmc.staff.tickets.interfaces.Response;
import com.ventedmc.staff.tickets.interfaces.Ticket;
import com.ventedmc.staff.tickets.models.ResponseModel;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.Bukkit;
import org.eclipse.jetty.util.IO;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static spark.Spark.*;
import static spark.Spark.halt;

public class RestHandler {
    private TicketModule ticketModule;

    public RestHandler(TicketModule ticketModule) {
        this.ticketModule = ticketModule;

        registerRestAPI();
    }

    public void registerRestAPI() {
        StaffPlugin.getInstance().getLogger().log(Level.FINE, "Creating a POST /ticketResponse/:authKey on Port: 7879");
        port(7879); // Set the port to the port we want

        /**
         * Here we tell Spark (REST API) that we wish to register a POST web server with the params below.
         *
         * All these methods including port(), get(), halt() are static imports so we can access the method
         * without defining the class which holds these methods.
         *
         * The import statement should look like import static spark.Spark.*;
         *
         * @param authKey - This is the Auth Key we provide to the minecraft server from the discord bot.
         */
        post("/ticketResponse/:authKey", (request, response) -> { // Here you can see there's a function that returns a request, and response.
            String authKey = request.params(":authKey"); // Auth Key we pass in as a param :authKey
            if(!authKey.equals("2153ed78-4758-11ed-b878-0242ac120002")) { // Here we check whether the Auth Key is our value.
                return halt(401); // If it's not our Auth Key we return 401 - Unauthorized.
            }
            Map<String, String[]> queryParams = request.queryMap().toMap(); // Here we map out any other params they've passed in as JSON.
            if(!queryParams.containsKey("ticketResponder") || !queryParams.containsKey("ticketResponse") || !queryParams.containsKey("ticketID")) {
                return halt(400); // The data we need isn't provided, so we return 400 - Bad Request.
            }
            String ticketResponder = String.join(" ", queryParams.get("ticketResponder")); // Ticket Responder Field.
            String ticketResponse = String.join(" ", queryParams.get("ticketResponse")); // Ticket Response Field.
            long ticketID = Long.parseLong(String.join(" ", queryParams.get("ticketID"))); // Ticket ID.

            Response responseObj = new ResponseModel(ticketResponder, ticketResponse); // Create the new response object with the name of the responder/response

            Ticket ticket = ticketModule.getTicketDataSource().getTicketFromID(ticketID); // Get the Ticket that corresponds with the ID provided.
            if(ticket == null)return halt(400); // Ticket with that ID doesn't exist so we return 400.
            ticketModule.getResponseDataSource().insertResponse(ticket, responseObj); // Insert the response to the ticket.

            return halt(200); // Here we return 400 - OK as the data provided is correct.
        });
    }

    public void notifyTicketCreation(Ticket ticket) {
        HashMap<String, String> requestBody = new HashMap<>() {{ // Create the request body with the params
            put("ticketID", String.valueOf(ticket.getTicketID())); // Ticket ID Param
            put("ticketCreator", Bukkit.getOfflinePlayer(ticket.getTicketCreator()).getName()); // Ticket Creator Param
            put("ticketSubject", ticket.getSubject()); // Ticket Subject Param
        }};
        try {
            sendData(requestBody, "/notifyTicket"); // Send the Data
        }catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            StaffPlugin.getInstance().getLogger().log(Level.SEVERE, "Unable to contact the REST API for Discord Integration.");
        }
    }

    public void notifyTicketResponse(Ticket ticket, Response response) {
        HashMap<String, String> requestBody = new HashMap<>() {{ // Create the request body with the params
            put("ticketID", String.valueOf(ticket.getTicketID())); // Ticket ID Param
            put("ticketCreator", Bukkit.getOfflinePlayer(ticket.getTicketCreator()).getName()); // Ticket Creator Param
            put("ticketSubject", ticket.getSubject()); // Ticket Subject Param
            put("ticketResponder", response.getResponder()); // Responder from the response.
            put("ticketResponse", response.getResponse()); // Response from the response.
        }};
        try {
            sendData(requestBody, "/notifyResponse");
        }catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            StaffPlugin.getInstance().getLogger().log(Level.SEVERE, "Unable to contact the REST API for Discord Integration.");
        }
    }

    private void sendData(HashMap<String, String> requestBody, String requestPath) throws IOException, URISyntaxException {
        ObjectMapper objectMapper = new ObjectMapper(); // Initialize Object Mapper
        String requestJson = objectMapper.writeValueAsString(requestBody); // Write the to JSON using ObjectMapper
        URIBuilder builder = new URIBuilder(); // Initialize the URI Builder
        builder.setScheme("http").setHost("172.18.0.1:7878").setPath(requestPath).setParameter("authKey", "2153ed78-4758-11ed-b878-0242ac120002"); // Set the builder to use the PATH with the AUTH KEY.
        URI uri = builder.build(); // Build the URI.
        HttpPost httpPost = new HttpPost(uri); // Create a POST REQUEST using HTTP POST
        httpPost.setHeader("Content-Type", "application/json"); // Set the content type to JSON as above we convert to JSON.
        httpPost.setEntity(new StringEntity(requestJson)); // Set the body to the JSON we created above w/ object MAPPER.
        try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
            closeableHttpClient.execute(httpPost); // Execute the query.
        }
    }
}

package com.ventedmc.staff.tickets.models;

import com.ventedmc.staff.tickets.interfaces.Response;

import java.util.UUID;

public class ResponseModel implements Response {
    private Long responseID = null;
    private Long ticketID = null;
    private UUID responder;
    private String response;

    public ResponseModel(Long responseID, Long ticketID, UUID responder, String response) {
        this.responseID = responseID;
        this.ticketID = ticketID;
        this.responder = responder;
        this.response = response;
    }

    public ResponseModel(UUID responder, String response) {
        this.responder = responder;
        this.response = response;
    }

    @Override
    public long getResponseID() {
        return responseID;
    }

    @Override
    public long getTicketID() {
        return ticketID;
    }

    @Override
    public UUID getResponder() {
        return responder;
    }

    @Override
    public String getResponse() {
        return response;
    }
}
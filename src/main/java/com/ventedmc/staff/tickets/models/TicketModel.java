package com.ventedmc.staff.tickets.models;

import com.ventedmc.staff.tickets.interfaces.Response;
import com.ventedmc.staff.tickets.interfaces.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketModel implements Ticket {
    private Long ticketID = null;
    private UUID ticketCreator;
    private String subject;
    private List<Response> responses;

    public TicketModel(Long ticketID, UUID ticketCreator, String subject) {
        this.ticketID = ticketID;
        this.ticketCreator = ticketCreator;
        this.subject = subject;

        this.responses = new ArrayList<>();
    }

    public TicketModel(UUID ticketCreator, String subject) {
        this.ticketCreator = ticketCreator;
        this.subject = subject;

        this.responses = new ArrayList<>();
    }

    @Override
    public long getTicketID() {
        return ticketID;
    }

    @Override
    public void setTicketID(Long ticketID) {
        this.ticketID = ticketID;
    }

    @Override
    public UUID getTicketCreator() {
        return ticketCreator;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public List<Response> getResponses() {
        return responses;
    }

    @Override
    public void addResponse(Response response) {
        this.responses.add(response);
    }
}

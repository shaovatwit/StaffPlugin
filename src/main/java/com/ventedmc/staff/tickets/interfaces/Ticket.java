package com.ventedmc.staff.tickets.interfaces;

import java.util.List;
import java.util.UUID;

public interface Ticket {

    long getTicketID();

    void setTicketID(Long ticketID);

    UUID getTicketCreator();

    String getSubject();
    
    List<Response> getResponses();

    void addResponse(Response response);
}

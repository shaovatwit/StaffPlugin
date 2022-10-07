package com.ventedmc.staff.tickets.interfaces;

import java.util.UUID;

public interface Response {

    long getResponseID(); // getResponseID

    long getTicketID();

    UUID getResponder();

    String getResponse();

}

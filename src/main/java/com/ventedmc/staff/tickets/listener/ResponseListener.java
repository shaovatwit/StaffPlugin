package com.ventedmc.staff.tickets.listener;

import com.ventedmc.staff.tickets.TicketModule;
import com.ventedmc.staff.tickets.interfaces.Response;
import com.ventedmc.staff.tickets.interfaces.Ticket;
import com.ventedmc.staff.tickets.models.ResponseModel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ResponseListener implements Listener {

    private TicketModule ticketModule;

    public ResponseListener(TicketModule ticketModule) {
        this.ticketModule = ticketModule;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(ticketModule.getTicketHashMap().containsKey(e.getPlayer().getUniqueId())) {
            if(e.getMessage().equalsIgnoreCase("cancel")) {
                e.setCancelled(true);
                ticketModule.getTicketHashMap().remove(e.getPlayer().getUniqueId());
                return;
            }
            Ticket ticket = ticketModule.getTicketHashMap().get(e.getPlayer().getUniqueId());

            // Submit Response
            Response response = new ResponseModel(e.getPlayer().getUniqueId(), e.getMessage());
            ticketModule.getResponseDataSource().insertResponse(ticket, response);
            e.setCancelled(true);

            ticketModule.getTicketHashMap().remove(e.getPlayer().getUniqueId());
        }
    }
}

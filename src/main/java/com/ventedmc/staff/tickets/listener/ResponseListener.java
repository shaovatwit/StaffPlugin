package com.ventedmc.staff.tickets.listener;

import com.ventedmc.staff.tickets.TicketModule;
import com.ventedmc.staff.tickets.interfaces.Response;
import com.ventedmc.staff.tickets.interfaces.Ticket;
import com.ventedmc.staff.tickets.models.ResponseModel;
import com.ventedmc.staff.utils.ItemUtils;
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
                e.getPlayer().sendMessage(ItemUtils.colorize("&4&l[!] &cYou have cancelled the response."));
                return;
            }
            Ticket ticket = ticketModule.getTicketHashMap().get(e.getPlayer().getUniqueId());

            // Submit Response
            Response response = new ResponseModel(e.getPlayer().getName(), e.getMessage());
            ticketModule.getResponseDataSource().insertResponse(ticket, response);
            e.setCancelled(true);

            ticketModule.getTicketHashMap().remove(e.getPlayer().getUniqueId());

            e.getPlayer().sendMessage(ItemUtils.colorize("&4&l[!] &cYour response to the selected ticket has been submitted."));
        }
    }
}

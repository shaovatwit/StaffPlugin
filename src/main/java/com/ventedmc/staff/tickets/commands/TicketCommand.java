package com.ventedmc.staff.tickets.commands;

import com.ventedmc.staff.tickets.TicketModule;
import com.ventedmc.staff.tickets.interfaces.Ticket;
import com.ventedmc.staff.tickets.models.TicketModel;
import com.ventedmc.staff.tickets.ui.TicketListInterface;
import l2.envy.gui.L2UI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TicketCommand implements CommandExecutor {
    private TicketModule ticketModule;

    public TicketCommand(TicketModule ticketModule) {
        this.ticketModule = ticketModule;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player)commandSender;
            if(args.length == 0) {
                L2UI.getInstance().getRegistrar().getStaticL2UserInterface(new TicketListInterface(ticketModule)).join(player);
                return true;
            }
            String subject = String.join(" ", args);
            Ticket ticket = new TicketModel(player.getUniqueId(), subject);

            ticketModule.getTicketDataSource().insertTicket(ticket);
            return true;
        }
        return false;
    }
}

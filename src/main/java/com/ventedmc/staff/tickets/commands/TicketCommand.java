package com.ventedmc.staff.tickets.commands;

import com.ventedmc.staff.tickets.TicketModule;
import com.ventedmc.staff.tickets.interfaces.Ticket;
import com.ventedmc.staff.tickets.models.TicketModel;
import com.ventedmc.staff.tickets.ui.TicketListInterface;
import com.ventedmc.staff.tickets.ui.TicketMainMenu;
import com.ventedmc.staff.utils.ItemUtils;
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
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("list")) {
                    L2UI.getInstance().getRegistrar().getStaticL2UserInterface(new TicketMainMenu(ticketModule, player)).join(player);
                    player.sendMessage(ItemUtils.colorize("&4&l[!] &cOpening Ticket Main Menu."));
                    return true;
                }
            } else if(args.length > 1) {
                if(args[0].equalsIgnoreCase("create")) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    String subject = sb.toString();
                    Ticket ticket = new TicketModel(player.getUniqueId(), subject);

                    ticketModule.getTicketDataSource().insertTicket(ticket);
                    player.sendMessage(ItemUtils.colorize("&4&l[!] &cYour ticket has been created successfully."));
                    return true;
                }
            }
            player.sendMessage(ItemUtils.colorize("&4&l[!] &cIncorrect Usage: /ticket list OR /ticket create <subject>"));
            return false;
        }
        commandSender.sendMessage(ItemUtils.colorize("&4&l[!] &cYou must be a player to execute that command!"));
        return false;
    }
}

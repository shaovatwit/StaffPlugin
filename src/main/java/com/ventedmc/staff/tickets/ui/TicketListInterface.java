package com.ventedmc.staff.tickets.ui;

import com.ventedmc.staff.mode.StaffModeModule;
import com.ventedmc.staff.tickets.TicketModule;
import com.ventedmc.staff.tickets.interfaces.Ticket;
import com.ventedmc.staff.utils.ItemBuilder;
import com.ventedmc.staff.utils.ItemUtils;
import com.ventedmc.staff.utils.RomanNumeralUtils;
import l2.envy.gui.L2UI;
import l2.envy.gui.icon.DefaultIcon;
import l2.envy.gui.object.PaginatedMenu;
import l2.envy.gui.screen.DefaultScreen;
import l2.envy.gui.screen.Screen;
import l2.envy.gui.ui.UserInteface;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class TicketListInterface extends PaginatedMenu {
    private static final int[] responseSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
    private static final int[] fillerSlots = {45, 46, 47, 49, 51, 52, 53};

    private TicketModule ticketModule;

    public TicketListInterface(TicketModule ticketModule) {
        super(ItemUtils.colorize("&4&l[!] &cOpen Tickets"));
        this.ticketModule = ticketModule;
    }

    @Override
    public boolean onJoin(Player player) {
        return true; // Allow the player to join the interface.
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onLeave(OfflinePlayer offlinePlayer) {

    }

    @Override
    public void onInitialize() {
        getL2UI().setCanceledByDefault(true);
        setupScreen();
    }

    public void setupScreen() {
        int inventoryIndex = 0;
        for (Ticket ticket : ticketModule.getTicketDataSource().getAllTickets()) {
//            screen.addIcon(inventoryIndex, new DefaultIcon(new ItemBuilder(Material.PAPER)
//                    .setName("&4&l[!] &cTicket ID &f- " + ticket.getTicketID())
//                    .addLoreLine("&4&l * &cCreator: &f" + Bukkit.getOfflinePlayer(ticket.getTicketCreator()).getName())
//                    .addLoreLine("&4&l * &cSubject: &f" + ticket.getSubject())
//                    .addLoreLine("&4&l * &cResponses: &f" + ticket.getResponses().size())
//                    .addLoreLine(" ")
//                    .addLoreLine("&7&o((Left-Click to add a response))")
//                    .addLoreLine("&7&o((Right-Click to view responses))")
//                    .toItemStack()).setActionHandler(action -> {
//                if (action.getEvent() instanceof InventoryClickEvent) {
//                    InventoryClickEvent inventoryClickEvent = (InventoryClickEvent) action.getEvent();
//                    if (inventoryClickEvent.isLeftClick()) {
//                        ticketModule.getTicketHashMap().put(action.getPlayer().getUniqueId(), ticket);
//                        getL2UI().kick(action.getPlayer());
//                        action.getPlayer().sendMessage(ItemUtils.colorize("&4&l(!) &cPlease type a response for this ticket, if this was a mistake type cancel."));
//                    } else {
//                        L2UI.getInstance().getRegistrar().getStaticL2UserInterface(new TicketViewInterface(ticket, ticketModule)).join(action.getPlayer());
//                    }
//                }
//            }));
        }
    }
}

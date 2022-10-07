package com.ventedmc.staff.tickets.ui;

import com.ventedmc.staff.tickets.TicketModule;
import com.ventedmc.staff.tickets.interfaces.Response;
import com.ventedmc.staff.tickets.interfaces.Ticket;
import com.ventedmc.staff.utils.ItemBuilder;
import com.ventedmc.staff.utils.ItemUtils;
import l2.envy.gui.icon.DefaultIcon;
import l2.envy.gui.object.PaginatedMenu;
import l2.envy.gui.screen.DefaultScreen;
import l2.envy.gui.screen.Screen;
import l2.envy.gui.ui.UserInteface;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TicketViewInterface extends PaginatedMenu {
    private static final int[] responseSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
    private static final int[] fillerSlots = {45, 46, 47, 49, 51, 52, 53};

    private Ticket ticket;
    private TicketModule ticketModule;

    public TicketViewInterface(Ticket ticket, TicketModule ticketModule) {
        super(ItemUtils.colorize("&4&l[!] &cTicket Responses"));
        this.ticket = ticket;
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
        getScreen().setSize(54);
        // Set the item slot indexes
        getScreen().updateIndexes(responseSlots);
        // Setting the next/previous slots
        getScreen().setPreviousSlot(48);
        getScreen().setNextSlot(50);
        getL2UI().setCanceledByDefault(true);

        setupScreen();

        for (int fillerSlot : fillerSlots) {
            getScreen().addIcon(fillerSlot, new DefaultIcon(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDurability((short) 15).setName(" ").toItemStack()));
        }

        getScreen().addIcon(53, new DefaultIcon(new ItemBuilder(Material.PAPER).setName(ItemUtils.colorize("&4&l[!] &cRespond"))
                .addLoreLine(ItemUtils.colorize("&4&l * &cClick to add a response to this ticket.")).toItemStack()).setActionHandler(action -> {
            ticketModule.getTicketHashMap().put(action.getPlayer().getUniqueId(), ticket);
            getL2UI().kick(action.getPlayer());
            action.getPlayer().sendMessage(ItemUtils.colorize("&4&l(!) &cPlease type a response for this ticket, if this was a mistake type cancel."));
        }));
    }

    public void setupScreen() {
        for(Response response : ticket.getResponses()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(response.getResponder());
            getScreen().addToList(new DefaultIcon(new ItemBuilder(Material.PAPER)
                    .setName("&4&l[!] &cTicket ID &f- " + ticket.getTicketID())
                    .addLoreLine("&4&l * &cCreator: &f" + Bukkit.getOfflinePlayer(ticket.getTicketCreator()).getName())
                    .addLoreLine("&4&l * &cSubject: &f" + ticket.getSubject())
                    .addLoreLine("&4&l * &cResponses: &f" + ticket.getResponses().size())
                    .addLoreLine(" ")
                    .addLoreLine("&4&l[!] &cResponse ID &f-" + response.getResponseID())
                    .addLoreLine("&4&l * &cResponder: &f" + Bukkit.getOfflinePlayer(response.getResponder()).getName())
                    .addLoreLine("&4&l * &cResponse: &f" + response.getResponse())
                    .toItemStack()));
        }
    }
}

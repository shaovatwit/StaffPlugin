package com.ventedmc.staff.tickets.ui;

import com.ventedmc.staff.tickets.TicketModule;
import com.ventedmc.staff.utils.ItemUtils;
import l2.envy.gui.L2UI;
import l2.envy.gui.icon.DefaultIcon;
import l2.envy.gui.screen.DefaultScreen;
import l2.envy.gui.screen.Screen;
import l2.envy.gui.ui.UserInteface;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TicketMainMenu extends UserInteface {
    private Screen screen;
    private Player player;
    private TicketModule module;

    public TicketMainMenu(TicketModule module, Player player) {
        this.module = module;
        this.screen = new DefaultScreen()
                .setName(module.getConfig().getString("tickets.menus.ticketMainMenu.name"))
                .setSize(module.getConfig().getInt("tickets.menus.ticketMainMenu.size"));
        this.player = player;
    }

    @Override
    public Screen getScreen(Player player) {
        return screen;
    }

    @Override
    public boolean onJoin(Player player) {
        return true;
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
        setupSlots();
    }

    public void setupSlots() {
        ItemStack openTickets = ItemUtils.getItemStack(module.getConfig().getString("tickets.menus.ticketMainMenu.slots.openTickets.item"),
                module.getConfig().getString("tickets.menus.ticketMainMenu.slots.openTickets.name"),
                module.getConfig().getStringList("tickets.menus.ticketMainMenu.slots.openTickets.lore"), (short) 0);

        ItemStack closedTickets = ItemUtils.getItemStack(module.getConfig().getString("tickets.menus.ticketMainMenu.slots.closedTickets.item"),
                module.getConfig().getString("tickets.menus.ticketMainMenu.slots.closedTickets.name"),
                module.getConfig().getStringList("tickets.menus.ticketMainMenu.slots.closedTickets.lore"), (short) 0);

        ItemStack adminView = ItemUtils.getItemStack(module.getConfig().getString("tickets.menus.ticketMainMenu.slots.adminView.item"),
                module.getConfig().getString("tickets.menus.ticketMainMenu.slots.adminView.name"),
                module.getConfig().getStringList("tickets.menus.ticketMainMenu.slots.adminView.lore"), (short) 0);

        int openTicketSlot = module.getConfig().getInt("tickets.menus.ticketMainMenu.slots.openTickets.slot");
        int closedTicketSlot = module.getConfig().getInt("tickets.menus.ticketMainMenu.slots.closedTickets.slot");
        int adminViewTicketSlot = module.getConfig().getInt("tickets.menus.ticketMainMenu.slots.adminView.slot");
        String adminViewPermission = module.getConfig().getString("tickets.menus.ticketMainMenu.slots.adminView.requiredPermission");

        screen.addIcon(openTicketSlot, new DefaultIcon(openTickets).setActionHandler(action -> {
            L2UI.getInstance().getRegistrar().getStaticL2UserInterface(new TicketPlayerListInterface(module, player, true)).join(player);
        }));

        screen.addIcon(closedTicketSlot, new DefaultIcon(closedTickets).setActionHandler(action -> {

        }));

        if (!player.hasPermission(adminViewPermission)) return;
        screen.addIcon(adminViewTicketSlot, new DefaultIcon(adminView).setActionHandler(action -> {
            L2UI.getInstance().getRegistrar().getStaticL2UserInterface(new TicketListInterface(module)).join(player);
        }));
    }
}

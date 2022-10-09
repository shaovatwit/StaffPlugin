package com.ventedmc.staff.tickets.sql;

import com.ventedmc.staff.StaffPlugin;
import com.ventedmc.staff.manager.database.DatabaseManager;
import com.ventedmc.staff.tickets.TicketModule;
import com.ventedmc.staff.tickets.interfaces.DataSource;
import com.ventedmc.staff.tickets.interfaces.Response;
import com.ventedmc.staff.tickets.interfaces.Ticket;
import com.ventedmc.staff.tickets.models.TicketModel;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class TicketDataSource implements DataSource {
    private TicketModule ticketModule;
    private DatabaseManager databaseManager;
    private ResponseDataSource responseDataSource;

    public TicketDataSource(TicketModule ticketModule, DatabaseManager databaseManager, ResponseDataSource responseDataSource) {
        this.ticketModule = ticketModule;
        this.databaseManager = databaseManager;
        this.responseDataSource = responseDataSource;
    }

    public List<Ticket> getAllTickets() {
        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TICKETS)) {
            return getTickets(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Ticket> getPlayerTickets(Player player) {
        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PLAYER_TICKETS)) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            return getTickets(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Nullable
    public Ticket getTicketFromID(long ticketID) {
        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TICKET_FROM_ID)) {
            preparedStatement.setLong(1, ticketID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) return null;
                Ticket ticket = new TicketModel(resultSet.getLong("ticket_id"), UUID.fromString(resultSet.getString("ticket_creator")), resultSet.getString("subject"));
                for (Response response : responseDataSource.getResponses(ticket)) {
                    ticket.addResponse(response);
                }
                return ticket;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Ticket> getTickets(PreparedStatement preparedStatement) {
        List<Ticket> tickets = new ArrayList<>();
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Ticket ticket = new TicketModel(resultSet.getLong("ticket_id"), UUID.fromString(resultSet.getString("ticket_creator")), resultSet.getString("subject"));
                for (Response response : responseDataSource.getResponses(ticket)) {
                    ticket.addResponse(response);
                }
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public void insertTicket(Ticket ticket) {
        long ticketID = 0;
        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TICKET, Statement.RETURN_GENERATED_KEYS)) { // Return the TICKET ID SQL Generates
            preparedStatement.setString(1, ticket.getTicketCreator().toString());
            preparedStatement.setString(2, ticket.getSubject());
            preparedStatement.executeUpdate();
            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                while (resultSet.next()) {
                    ticketID = resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ticket.setTicketID(ticketID);
        ticketModule.getRestHandler().notifyTicketCreation(ticket); // Send the notification to the discord.
    }

    @Override
    public void createDefaultTables() {
        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropTables() {

    }

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ticket_data (ticket_id bigint(20) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY, ticket_creator VARCHAR(64) NOT NULL, subject TEXT NOT NULL);";

    private static final String SELECT_ALL_TICKETS = "SELECT * FROM ticket_data;";
    private static final String SELECT_TICKET_FROM_ID = "SELECT * FROM ticket_data WHERE ticket_id=?;";
    private static final String SELECT_PLAYER_TICKETS = "SELECT * FROM ticket_data WHERE ticket_creator=?;";
    private static final String INSERT_TICKET = "INSERT INTO ticket_data (ticket_creator, subject) VALUES (?,?);";
}

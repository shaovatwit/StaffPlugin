package com.ventedmc.staff.tickets.sql;

import com.ventedmc.staff.StaffPlugin;
import com.ventedmc.staff.manager.database.DatabaseManager;
import com.ventedmc.staff.tickets.TicketModule;
import com.ventedmc.staff.tickets.interfaces.DataSource;
import com.ventedmc.staff.tickets.interfaces.Response;
import com.ventedmc.staff.tickets.interfaces.Ticket;
import com.ventedmc.staff.tickets.models.ResponseModel;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class ResponseDataSource implements DataSource {
    private TicketModule ticketModule;
    private DatabaseManager databaseManager;

    public ResponseDataSource(TicketModule ticketModule, DatabaseManager databaseManager) {
        this.ticketModule = ticketModule;
        this.databaseManager = databaseManager;
    }

    public List<Response> getResponses(Ticket ticket) {
        List<Response> responses = new ArrayList<>();
        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_RESP0NSE)) {
            preparedStatement.setLong(1, ticket.getTicketID());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Response response = new ResponseModel(resultSet.getLong("response_id"), resultSet.getLong("ticket_id"),
                            resultSet.getString("responder"), resultSet.getString("response"));
                    responses.add(response);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responses;
    }

    public void insertResponse(Ticket ticket, Response response) {
        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_RESPONSE)) {
            preparedStatement.setLong(1, ticket.getTicketID());
            preparedStatement.setString(2, response.getResponder());
            preparedStatement.setString(3, response.getResponse());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ticketModule.getRestHandler().notifyTicketResponse(ticket, response); // Send the notification to the discord.

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

    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS response_data (response_id bigint(20) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY, ticket_id bigint(20) unsigned NOT NULL, responder VARCHAR(64) NOT NULL, response TEXT NOT NULL, FOREIGN KEY (ticket_id) REFERENCES ticket_data (ticket_id));";
    private static final String SELECT_RESP0NSE = "SELECT * FROM response_data WHERE ticket_id=?;";
    private static final String INSERT_RESPONSE = "INSERT INTO response_data (ticket_id, responder, response) VALUES (?,?,?);";
}

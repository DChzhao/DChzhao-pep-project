// data access object for Message
package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Message createMessage(Message message) {

        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, message.getPosted_by());
            stmt.setString(2, message.getMessage_text());
            stmt.setLong(3, message.getTime_posted_epoch());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                message.setMessage_id(rs.getInt(1));
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Message> getAllMessages() {

        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message";

        try (Connection conn = ConnectionUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public Message getMessageById(int messageId) {

        String sql = "SELECT * FROM message WHERE message_id = ?";

        try (Connection conn = ConnectionUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, messageId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Message deleteMessage(int messageId) {

        String selectSql = "SELECT * FROM message WHERE message_id = ?";
        String deleteSql = "DELETE FROM message WHERE message_id = ?";

        try (Connection conn = ConnectionUtil.getConnection();
                PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            selectStmt.setInt(1, messageId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) { // Found the message
                Message messageToDelete = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));

                // Now delete the message
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                    deleteStmt.setInt(1, messageId);
                    deleteStmt.executeUpdate();
                }

                // Return the deleted message
                return messageToDelete;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Message was NOT found
        return null;
    }

    public boolean updateMessage(int messageId, String newMessageText) {

        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";

        try (Connection conn = ConnectionUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newMessageText);
            stmt.setInt(2, messageId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Message> getMessagesByUser(int userId) {

        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?";

        try (Connection conn = ConnectionUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }
}
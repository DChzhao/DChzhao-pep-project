package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {

    // Instance of MessageDAO
    private MessageDAO messageDAO = new MessageDAO();

    public Message createMessage(Message message) {

        // Check the message requirements
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() ||
                message.getMessage_text().length() > 255 || message.getPosted_by() <= 0) {

            // validation fails
            return null;
        }

        // If validation passes create and save the message
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    public boolean updateMessage(int messageId, String newMessageText) {

        // Check the message requirements
        if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {

            // fail
            return false;
        }

        // Success
        return messageDAO.updateMessage(messageId, newMessageText);
    }

    public List<Message> getMessagesByUser(int userId) {
        return messageDAO.getMessagesByUser(userId);
    }
}
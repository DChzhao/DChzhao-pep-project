package Controller;

import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SocialMediaController {
    private MessageService messageService = new MessageService();
    private ObjectMapper mapper = new ObjectMapper();
    private AccountService accountService = new AccountService();

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Register a new account
        app.post("/register", ctx -> {
            Account account = ctx.bodyAsClass(Account.class);

            Account createdAccount = accountService.registerUser(account.getUsername(), account.getPassword());
            if (createdAccount != null) {
                ctx.json(createdAccount);
            } else {
                // Registration failed (invalid input or username taken)
                ctx.status(400);
            }
        });

        // Login an existing user
        app.post("/login", ctx -> {
            Account account = ctx.bodyAsClass(Account.class);

            Account loggedInAccount = accountService.loginUser(account.getUsername(), account.getPassword());
            if (loggedInAccount != null) {
                ctx.json(loggedInAccount);
            } else {
                // Unauthorized (invalid credentials)
                ctx.status(401);
            }
        });

        // Create a new message
        app.post("/messages", ctx -> {
            Message message = mapper.readValue(ctx.body(), Message.class);
            Message createdMessage = messageService.createMessage(message);
            if (createdMessage != null) {
                ctx.json(createdMessage);
            } else {
                ctx.status(400);
            }
        });

        // Get All MEssages
        app.get("/messages", ctx -> ctx.json(messageService.getAllMessages()));

        // Get Message by message ID
        app.get("/messages/{message_id}", ctx -> {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            if (message != null) {
                ctx.json(message);
            } else {
                ctx.status(200);
            }
        });

        // Get Message by account ID
        app.get("/accounts/{account_id}/messages", ctx -> {
            int accountId = Integer.parseInt(ctx.pathParam("account_id"));
            ctx.json(messageService.getMessagesByUser(accountId));
        });

        // Delete Message 
        app.delete("/messages/{message_id}", ctx -> {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));

            // Retrieve the message before deletion
            Message deletedMessage = messageService.getMessageById(messageId); 

            if (deletedMessage != null) {
                // Perform the deletion
                messageService.deleteMessage(messageId); 
                // Return the deleted message
                ctx.json(deletedMessage); 
            } else {
                 // Message was not found, return 200 with empty body
                ctx.status(200);
            }
        });

        // Update an existing message
        app.patch("/messages/{message_id}", ctx -> {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message updatedMessage = mapper.readValue(ctx.body(), Message.class);
            boolean updated = messageService.updateMessage(messageId, updatedMessage.getMessage_text());

            if (updated) {
                ctx.json(messageService.getMessageById(messageId));
            } else {
                ctx.status(400);
            }
        });

        return app;
    }
}
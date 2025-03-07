package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {

    private AccountDAO accountDAO = new AccountDAO();

    public Account registerUser(String username, String password) {

        // Check if username is not empty and password is at least 4 characters
        if (username == null || username.trim().isEmpty() || password.length() < 4) {
            // Registration fails
            return null;
        }

        // Check if the username already exist
        if (accountDAO.getAccountByUsername(username) != null) {
            // Duplicate
            return null;
        }

        // Create a new account
        Account newAccount = new Account(0, username, password);
        return accountDAO.createAccount(newAccount);
    }

    public Account loginUser(String username, String password) {

        Account existingAccount = accountDAO.getAccountByUsername(username);

        // Check if the account exists and the password matches
        if (existingAccount != null && existingAccount.getPassword().equals(password)) {
            return existingAccount;
        }

        // Login fails
        return null;
    }
}

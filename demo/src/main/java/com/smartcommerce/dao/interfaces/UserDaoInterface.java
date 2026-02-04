package com.smartcommerce.dao.interfaces;

import com.model.User;
import java.util.List;

public interface UserDaoInterface {

    /**
     * Adds a new user to the database
     * @param user User object to be added
     * @return true if user was successfully added, false otherwise
     */
    boolean addUser(User user);

    /**
     * Retrieves all users from the database
     * @return List of all users ordered by user_id
     */
    List<User> getAllUsers();

    /**
     * Retrieves a user by their ID
     * @param id User ID to search for
     * @return User object if found, null otherwise
     */
    User getUserById(int id);

    /**
     * Retrieves a user by their email address
     * @param email Email address to search for
     * @return User object if found, null otherwise
     */
    User getUserByEmail(String email);

    /**
     * Updates an existing user's information
     * @param user User object with updated information
     * @return true if user was successfully updated, false otherwise
     */
    boolean updateUser(User user);

    /**
     * Deletes a user from the database
     * @param id User ID to delete
     * @return true if user was successfully deleted, false otherwise
     */
    boolean deleteUser(int id);
}

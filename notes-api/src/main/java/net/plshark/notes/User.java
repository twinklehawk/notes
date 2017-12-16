package net.plshark.notes;

import java.util.Objects;
import java.util.OptionalLong;

/**
 * Data for a user
 */
public class User {

    private OptionalLong id = OptionalLong.empty();
    private String username;
    private String password;

    /**
     * Create a new instance
     */
    public User() {

    }

    /**
     * Create a new instance
     * @param username the username
     * @param password the password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Create a new instance
     * @param id the user ID
     * @param username the username
     * @param password the password
     */
    public User(long id, String username, String password) {
        this.id = OptionalLong.of(id);
        this.username = username;
        this.password = password;
    }

    /**
     * @return the ID, not set if the user has not been saved yet
     */
    public OptionalLong getId() {
        return id;
    }

    public void setId(long id) {
        this.id = OptionalLong.of(id);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = Objects.requireNonNull(username, "username cannot be null");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Objects.requireNonNull(password, "password cannot be null");
    }
}

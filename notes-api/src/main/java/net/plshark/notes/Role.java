package net.plshark.notes;

import java.util.OptionalLong;

/**
 * Data for a role
 */
public class Role {

    private OptionalLong id;
    private String name;

    /**
     * Create a new instance
     */
    public Role() {

    }

    /**
     * Create a new instance
     * @param id the role ID
     * @param name the name
     */
    public Role(long id, String name) {
        this.id = OptionalLong.of(id);
        this.name = name;
    }

    /**
     * @return the ID, can be empty if not saved yet
     */
    public OptionalLong getId() {
        return id;
    }

    public void setId(long id) {
        this.id = OptionalLong.of(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

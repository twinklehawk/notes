package net.plshark.notes;

import java.util.Objects;
import java.util.OptionalLong;

/**
 * Data for a role
 */
public class Role {

    private OptionalLong id;
    private String name;

    /**
     * Create a new instance
     * @param name the role name
     */
    public Role(String name) {
        this(OptionalLong.empty(), name);
    }

    /**
     * Create a new instance
     * @param id the role ID
     * @param name the role name
     */
    public Role(long id, String name) {
        this(OptionalLong.of(id), name);
    }

    private Role(OptionalLong id, String name) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name, "name cannot be null");
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
        this.name = Objects.requireNonNull(name, "name cannot be null");
    }
}

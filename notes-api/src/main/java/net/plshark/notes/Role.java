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

    /**
     * @param id the role ID
     */
    public void setId(long id) {
        this.id = OptionalLong.of(id);
    }

    /**
     * @return the role name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the role name
     */
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
    }

    @Override
    public String toString() {
        return "Role [id=" + id + ", name=" + name + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Role other = (Role) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}

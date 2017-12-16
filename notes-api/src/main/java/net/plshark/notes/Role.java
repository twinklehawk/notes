package net.plshark.notes;

/**
 * Data for a role
 */
public class Role {

    private long id;
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
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

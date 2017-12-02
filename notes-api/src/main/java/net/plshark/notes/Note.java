package net.plshark.notes;

import java.util.OptionalLong;

/**
 * Data for a note
 */
public class Note {

    private OptionalLong id = OptionalLong.empty();
    private long ownerId;
    private long correlationId;
    private String title;
    private String content;

    /**
     * @return the ID, can be unset if the note has not been saved yet
     */
    public OptionalLong getId() {
        return id;
    }

    /**
     * @param id the ID
     */
    public void setId(long id) {
        this.id = OptionalLong.of(id);
    }

    /**
     * @return the ID of the user that owns this note
     */
    public long getOwnerId() {
        return ownerId;
    }

    /**
     * @param ownerId the ID of the user that owns this note
     */
    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * @return an ID for use by external clients
     */
    public long getCorrelationId() {
        return correlationId;
    }

    /**
     * @param correlationId an ID for use by external clients
     */
    public void setCorrelationId(long correlationId) {
        this.correlationId = correlationId;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }
}

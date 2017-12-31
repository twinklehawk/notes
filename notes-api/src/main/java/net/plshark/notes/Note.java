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
     * Create a new instance
     */
    public Note() {

    }

    /**
     * Create a new instance
     * @param ownerId the ID of the user that owns this note
     * @param correlationId the external ID
     * @param title the title
     * @param content the content
     */
    public Note(long ownerId, long correlationId, String title, String content) {
        this.ownerId = ownerId;
        this.correlationId = correlationId;
        this.title = title;
        this.content = content;
    }

    /**
     * Create a new instance
     * @param id the ID
     * @param ownerId the ID of the user that owns this note
     * @param correlationId the external ID
     * @param title the title
     * @param content the content
     */
    public Note(long id, long ownerId, long correlationId, String title, String content) {
        this(ownerId, correlationId, title, content);
        this.id = OptionalLong.of(id);
    }

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

    @Override
    public String toString() {
        return "Note [id=" + id + ", ownerId=" + ownerId + ", correlationId=" + correlationId + ", title=" + title
                + ", content=" + content + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + (int) (correlationId ^ (correlationId >>> 32));
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + (int) (ownerId ^ (ownerId >>> 32));
        result = prime * result + ((title == null) ? 0 : title.hashCode());
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
        Note other = (Note) obj;
        if (content == null) {
            if (other.content != null)
                return false;
        } else if (!content.equals(other.content))
            return false;
        if (correlationId != other.correlationId)
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (ownerId != other.ownerId)
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }
}

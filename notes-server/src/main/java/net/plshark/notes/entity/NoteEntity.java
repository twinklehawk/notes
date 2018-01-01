package net.plshark.notes.entity;

import java.util.Objects;
import java.util.OptionalLong;

/**
 * Entity class for holding additional data about a note
 */
public class NoteEntity {

    private Long id;
    private long ownerId;
    private long correlationId;
    private String title;
    private String content;

    /**
     * Create a new instance with an empty ID and a correlation ID of 0
     * @param ownerId the owner ID
     * @param title the title
     * @param content the note content
     */
    public NoteEntity(long ownerId, String title, String content) {
        this(OptionalLong.empty(), ownerId, 0, title, content);
    }

    /**
     * Create a new instance
     * @param id the ID
     * @param ownerId the owner ID
     * @param correlationId the correlation ID
     * @param title the title
     * @param content the note content
     */
    public NoteEntity(long id, long ownerId, long correlationId, String title, String content) {
        this(OptionalLong.of(id), ownerId, correlationId, title, content);
    }

    /**
     * Create a new instance
     * @param id the ID, can be empty if the note has not been saved yet
     * @param ownerId the owner ID
     * @param correlationId the correlation ID
     * @param title the title
     * @param content the note content
     */
    public NoteEntity(OptionalLong id, long ownerId, long correlationId, String title, String content) {
        id.ifPresent(l -> this.id = l);
        this.ownerId = ownerId;
        this.correlationId = correlationId;
        this.title = Objects.requireNonNull(title, "title cannot be null");
        this.content = Objects.requireNonNull(content, "content cannot be null");
    }

    /**
     * @return the ID, can be empty if the note has not been saved
     */
    public OptionalLong getId() {
        return id != null ? OptionalLong.of(id) : OptionalLong.empty();
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public long getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(long correlationId) {
        this.correlationId = correlationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Objects.requireNonNull(title, "title cannot be null");
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = Objects.requireNonNull(content, "content cannot be null");
    }

    @Override
    public String toString() {
        return "NoteEntity [id=" + id + ", ownerId=" + ownerId + ", correlationId=" + correlationId + ", title=" + title
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
        NoteEntity other = (NoteEntity) obj;
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

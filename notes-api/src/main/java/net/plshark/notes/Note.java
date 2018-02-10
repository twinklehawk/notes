package net.plshark.notes;

import java.util.Objects;

import com.google.common.base.Optional;

/**
 * Data for a note
 */
public class Note {

    private Long id;
    private long correlationId;
    private String title;
    private String content;

    /**
     * Create a new instance with a correlation ID of 0
     * @param title the title
     * @param content the content
     */
    public Note(String title, String content) {
        this(Optional.<Long>absent(), 0, title, content);
    }

    /**
     * Create a new instance
     * @param id the ID
     * @param correlationId the external ID
     * @param title the title
     * @param content the content
     */
    public Note(long id, long correlationId, String title, String content) {
        this(Optional.of(id), correlationId, title, content);
    }

    /**
     * Create a new instance
     * @param id the ID
     * @param correlationId the external ID
     * @param title the title
     * @param content the content
     */
    private Note(Optional<Long> id, long correlationId, String title, String content) {
        if (id.isPresent())
            this.id = id.get();
        this.correlationId = correlationId;
        this.title = Objects.requireNonNull(title, "title cannot be null");
        this.content = Objects.requireNonNull(content, "content cannot be null");
    }

    /**
     * @return the ID, can be unset if the note has not been saved yet
     */
    public Optional<Long> getId() {
        return Optional.fromNullable(id);
    }

    /**
     * @return an ID for use by external clients
     */
    public long getCorrelationId() {
        return correlationId;
    }

    /**
     * Set the correlation ID. If not set, the default is 0
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
        this.title = Objects.requireNonNull(title, "title cannot be null");
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
        this.content = Objects.requireNonNull(content, "content cannot be null");
    }

    @Override
    public String toString() {
        return "Note [id=" + id + ", correlationId=" + correlationId + ", title=" + title + ", content=" + content
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + (int) (correlationId ^ (correlationId >>> 32));
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }
}

package net.plshark.notes;

import java.util.Objects;

/**
 * Data for a note
 */
public class Note {

    private final Long id;
    private long correlationId;
    private String title;
    private String content;

    /**
     * Create a new instance with a correlation ID of 0
     * @param title the title
     * @param content the content
     */
    public Note(String title, String content) {
        this(null, 0, title, content);
    }

    /**
     * Create a new instance
     * @param id the ID, can be null
     * @param correlationId the external ID
     * @param title the title
     * @param content the content
     */
    public Note(Long id, long correlationId, String title, String content) {
        this.id = id;
        this.correlationId = correlationId;
        this.title = Objects.requireNonNull(title, "title cannot be null");
        this.content = Objects.requireNonNull(content, "content cannot be null");
    }

    /**
     * @return the ID, can be null if the note has not been saved yet
     */
    public Long getId() {
        return id;
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
        return "Note{" +
                "id=" + id +
                ", correlationId=" + correlationId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return correlationId == note.correlationId &&
                Objects.equals(id, note.id) &&
                Objects.equals(title, note.title) &&
                Objects.equals(content, note.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, correlationId, title, content);
    }
}

package net.plshark.notes;

import java.util.Objects;

/**
 * Holds a user's permissions for a note
 */
public class UserNotePermission {

    private final String username;
    private final long noteId;
    private boolean readable;
    private boolean writable;
    private boolean owner;

    /**
     * Create a new instance with no permissions
     * @param username the username
     * @param noteId the note ID
     */
    public UserNotePermission(String username, long noteId) {
        this(username, noteId, false, false, false);
    }

    /**
     * Create a new instance that does not own the note
     * @param username the username
     * @param noteId the note ID
     * @param readable if the user can read the note
     * @param writable if the user can modify the note
     */
    public UserNotePermission(String username, long noteId, boolean readable, boolean writable) {
        this(username, noteId, readable, writable, false);
    }

    /**
     * Create a new instance
     * @param username the username
     * @param noteId the note ID
     * @param readable if the user can read the note
     * @param writable if the user can modify the note
     * @param owner if the user owns the note
     */
    public UserNotePermission(String username, long noteId, boolean readable, boolean writable, boolean owner) {
        this.username = username;
        this.noteId = noteId;
        this.readable = readable;
        this.writable = writable;
        this.owner = owner;
    }

    /**
     * @return the username of the user this permission applies to
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the note ID for the note this permission applies to
     */
    public long getNoteId() {
        return noteId;
    }

    /**
     * @return if the user can read the note
     */
    public boolean isReadable() {
        return readable;
    }

    /**
     * @param readable if the user can read the note
     */
    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    /**
     * @return if the user can modify the note
     */
    public boolean isWritable() {
        return writable;
    }

    /**
     * @param writable if the user can modify the note
     */
    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    /**
     * @return if the user is the owner of the note
     */
    public boolean isOwner() {
        return owner;
    }

    /**
     * @param owner if the user is the owner of the note
     */
    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "UserNotePermission{" +
                "username='" + username + '\'' +
                ", noteId=" + noteId +
                ", readable=" + readable +
                ", writable=" + writable +
                ", owner=" + owner +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserNotePermission that = (UserNotePermission) o;
        return noteId == that.noteId &&
                readable == that.readable &&
                writable == that.writable &&
                owner == that.owner &&
                Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, noteId, readable, writable, owner);
    }
}

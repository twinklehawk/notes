package net.plshark.notes;

/**
 * Holds a user's permissions for a note
 */
public class UserNotePermission {

    private final long userId;
    private final long noteId;
    private boolean readable;
    private boolean writable;

    /**
     * Create a new instance with no permissions
     * @param userId the user ID
     * @param noteId the note ID
     */
    public UserNotePermission(long userId, long noteId) {
        this(userId, noteId, false, false);
    }

    /**
     * Create a new instance
     * @param userId the user ID
     * @param noteId the note ID
     * @param readable if the user can read the note
     * @param writable if the user can modify the note
     */
    public UserNotePermission(long userId, long noteId, boolean readable, boolean writable) {
        this.userId = userId;
        this.noteId = noteId;
        this.readable = readable;
        this.writable = writable;
    }

    /**
     * @return the user ID for the user having this permission
     */
    public long getUserId() {
        return userId;
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

    @Override
    public String toString() {
        return "UserNotePermission [userId=" + userId + ", noteId=" + noteId + ", readable=" + readable + ", writable="
                + writable + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (noteId ^ (noteId >>> 32));
        result = prime * result + (readable ? 1231 : 1237);
        result = prime * result + (int) (userId ^ (userId >>> 32));
        result = prime * result + (writable ? 1231 : 1237);
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
        UserNotePermission other = (UserNotePermission) obj;
        if (noteId != other.noteId)
            return false;
        if (readable != other.readable)
            return false;
        if (userId != other.userId)
            return false;
        if (writable != other.writable)
            return false;
        return true;
    }
}

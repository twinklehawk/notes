package net.plshark.notes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a user's permissions for a note
 */
public class NotePermission {

    private final boolean readable;
    private final boolean writable;

    private NotePermission(boolean readable, boolean writable) {
        this.readable = readable;
        this.writable = writable;
    }

    /**
     * Create an instance
     * @param readable if the note is readable
     * @param writable if the note is writable
     * @return the NotePermission instance
     */
    @JsonCreator
    public static NotePermission create(@JsonProperty("readable") boolean readable,
            @JsonProperty("writable") boolean writable) {
        return new NotePermission(readable, writable);
    }

    /**
     * @return if the note is readable
     */
    public boolean isReadable() {
        return readable;
    }

    /**
     * @return if the note is writable
     */
    public boolean isWritable() {
        return writable;
    }

    @Override
    public String toString() {
        return "NotePermission [readable=" + readable + ", writable=" + writable + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (readable ? 1231 : 1237);
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
        NotePermission other = (NotePermission) obj;
        if (readable != other.readable)
            return false;
        if (writable != other.writable)
            return false;
        return true;
    }
}

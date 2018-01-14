package net.plshark.notes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

/**
 * Represents a user's permissions for a note
 */
@AutoValue
public abstract class NotePermission {

    /**
     * Create an instance
     * @param readable if the note is readable
     * @param writable if the note is writable
     * @return the NotePermission instance
     */
    @JsonCreator
    public static NotePermission create(@JsonProperty("readable") boolean readable,
            @JsonProperty("writable") boolean writable) {
        return new AutoValue_NotePermission(readable, writable);
    }

    /**
     * @return if the note is readable
     */
    public abstract boolean isReadable();

    /**
     * @return if the note is writable
     */
    public abstract boolean isWritable();
}

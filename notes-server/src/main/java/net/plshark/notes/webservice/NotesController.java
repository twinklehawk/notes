package net.plshark.notes.webservice;

import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.plshark.notes.BadRequestException;
import net.plshark.notes.Note;
import net.plshark.notes.NotesService;
import net.plshark.notes.ObjectNotFoundException;

/**
 * Controller to provide web service methods for notes
 */
@RestController
@RequestMapping("notes")
public class NotesController {

    private final NotesService notesService;

    /**
     * Create a new instance
     * @param notesService the service to use for notes
     */
    public NotesController(NotesService notesService) {
        this.notesService = Objects.requireNonNull(notesService, "notesService cannot be null");
    }

    /**
     * Get a note by ID
     * @param id the note ID
     * @return the matching note
     * @throws ObjectNotFoundException if the note was not found
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Note get(@PathVariable("id") long id) throws ObjectNotFoundException {
        return notesService.get(id);
    }

    /**
     * Insert a new note
     * @param note the note to insert
     * @return the inserted note
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Note insert(@RequestBody Note note) {
        return notesService.save(note);
    }

    /**
     * Update a note
     * @param id the ID of the note to update
     * @param note the note fields to update. ID is optional, but if present must
     *            match {@code id}
     * @return the updated note
     * @throws BadRequestException if the note ID is present and does not match
     *             {@code id}
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Note update(@PathVariable("id") long id, @RequestBody Note note) throws BadRequestException {
        if (!note.getId().isPresent())
            note.setId(id);
        else if (note.getId().getAsLong() != id)
            throw new BadRequestException("Note ID must match ID in path");

        return notesService.save(note);
    }

    /**
     * Delete a note by ID
     * @param id the note ID
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("id") long id) {
        notesService.delete(id);
    }
}

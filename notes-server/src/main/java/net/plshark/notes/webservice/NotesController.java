package net.plshark.notes.webservice;

import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.plshark.notes.BadRequestException;
import net.plshark.notes.Note;
import net.plshark.notes.ObjectNotFoundException;
import net.plshark.notes.service.NotesService;
import net.plshark.notes.service.UserAuthenticationService;

/**
 * Controller to provide web service methods for notes
 */
@RestController
@RequestMapping("/notes")
public class NotesController {

    private final NotesService notesService;
    private final UserAuthenticationService userAuthService;

    /**
     * Create a new instance
     * @param notesService the service to use for notes
     * @param userAuthService the service to use to retrieve authenticated user information
     */
    public NotesController(NotesService notesService, UserAuthenticationService userAuthService) {
        this.notesService = Objects.requireNonNull(notesService, "notesService cannot be null");
        this.userAuthService = Objects.requireNonNull(userAuthService, "userAuthService cannot be null");
    }

    /**
     * Get a note by ID
     * @param id the note ID
     * @param auth the currently authenticated user
     * @return the matching note
     * @throws ObjectNotFoundException if the note was not found
     */
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Note get(@PathVariable("id") long id, Authentication auth) throws ObjectNotFoundException {
        return notesService.getForUser(id, userAuthService.getUserIdForAuthentication(auth))
                .orElseThrow(() -> new ObjectNotFoundException("No note found for id " + id));
    }

    /**
     * Insert a new note
     * @param note the note to insert
     * @param auth the currently authenticated user
     * @return the inserted note
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Note insert(@RequestBody Note note, Authentication auth) {
        return notesService.save(note, userAuthService.getUserIdForAuthentication(auth));
    }

    /**
     * Update a note
     * @param id the ID of the note to update
     * @param note the note fields to update. ID is optional, but if present must
     *            match {@code id}
     * @param auth the currently authenticated user
     * @return the updated note
     * @throws BadRequestException if the note ID is present and does not match
     *             {@code id}
     */
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Note update(@PathVariable("id") long id, @RequestBody Note note, Authentication auth)
            throws BadRequestException {
        if (!note.getId().isPresent())
            note.setId(id);
        else if (note.getId().getAsLong() != id)
            throw new BadRequestException("Note ID must match ID in path");

        return notesService.save(note, userAuthService.getUserIdForAuthentication(auth));
    }

    /**
     * Delete a note by ID
     * @param id the note ID
     */
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") long id) {
        notesService.delete(id);
    }
}

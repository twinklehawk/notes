package net.plshark.notes.webservice;

import java.util.Objects;
import net.plshark.BadRequestException;
import net.plshark.ObjectNotFoundException;
import net.plshark.notes.Note;
import net.plshark.notes.service.NotesService;
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
import reactor.core.publisher.Mono;

/**
 * Controller to provide web service methods for notes
 */
@RestController
@RequestMapping("/notes")
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
     * @param auth the currently authenticated user
     * @return the matching note or ObjectNotFoundException if the note was not found
     */
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Note> get(@PathVariable("id") long id, Authentication auth) {
        return notesService.getForUser(id, auth.getName())
            .switchIfEmpty(Mono.error(new ObjectNotFoundException("No note found for id " + id)));
    }

    /**
     * Insert a new note or update an existing note
     * @param note the note to insert or update if the ID is set
     * @param auth the currently authenticated user
     * @return the inserted or updated note or ObjectNotFoundException if the note has an ID but cannot
     *         be found
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Note> insert(@RequestBody Note note, Authentication auth) {
        return notesService.save(note, auth.getName());
    }

    /**
     * Update a note
     * @param id the ID of the note to update
     * @param note the note fields to update. ID is optional, but if present must match {@code id}
     * @param auth the currently authenticated user
     * @return the updated note, or BadRequestException if the note ID is present and does not match
     *         {@code id}, or ObjectNotFoundException if the note ID is present but the note is not
     *         found
     */
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Note> update(@PathVariable("id") long id, @RequestBody Note note, Authentication auth) {
        Note n = note.getId().isPresent() ? note : new Note(id, note.getCorrelationId(), note.getTitle(), note.getContent());
        if (n.getId().get() != id)
            return Mono.error(new BadRequestException("Note ID must match ID in path"));
        else
            return notesService.save(n, auth.getName());
    }

    /**
     * Delete a note by ID
     * @param id the note ID
     * @param auth the currently authenticated user
     * @return an empty result or ObjectNotFoundException if the note is not found
     */
    @DeleteMapping(path = "/{id}")
    public Mono<Void> delete(@PathVariable("id") long id, Authentication auth) {
        return notesService.deleteForUser(id, auth.getName());
    }
}

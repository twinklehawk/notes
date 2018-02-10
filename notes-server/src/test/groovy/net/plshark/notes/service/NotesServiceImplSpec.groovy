package net.plshark.notes.service

import net.plshark.ObjectNotFoundException
import net.plshark.notes.Note
import net.plshark.notes.repo.NotesRepository
import spock.lang.Specification

class NotesServiceImplSpec extends Specification {

    NotesRepository notesRepo = Mock()
    UserNotePermissionsService permissionService = Mock()
    NotesServiceImpl service = new NotesServiceImpl(notesRepo, permissionService)

    def "constructor does not accept null"() {
        when:
        new NotesServiceImpl(null, permissionService)

        then:
        thrown(NullPointerException)

        when:
        new NotesServiceImpl(notesRepo, null)

        then:
        thrown(NullPointerException)
    }

    def "notes with no ID should be inserted and owner set to current user"() {
        notesRepo.insert(_) >> new Note(1, 3, "", "")

        when:
        Note note = service.save(new Note("", ""), 2)

        then:
        note == new Note(1, 3, "", "")
        1 * permissionService.grantOwnerPermissions(1, 2)
    }

    def "notes with ID should be updated"() {
        permissionService.userHasWritePermission(4, 1) >> true
        notesRepo.update(new Note(4, 2, "", "")) >> new Note(4, 2, "", "")

        when:
        Note note = service.save(new Note(4, 2, "", ""), 1)

        then:
        note == new Note(4, 2, "", "")
    }

    def "updating a note without write permission should throw ObjectNotFoundException"() {
        permissionService.userHasWritePermission(4, 1) >> false

        when:
        service.save(new Note(4, 2, "", ""), 1)

        then:
        thrown(ObjectNotFoundException)
    }

    def "retrieving a note by ID for a user should pass the IDs through"() {
        permissionService.userHasReadPermission(100, 200) >> true
        notesRepo.get(100) >> Optional.of(new Note(100, 3, "title", "content"))

        when:
        Optional<Note> note = service.getForUser(100, 200)

        then:
        note.get() == new Note(100, 3, "title", "content")
    }

    def "attempting to retrieve a note that does not exist should return an empty optional"() {
        permissionService.userHasReadPermission(100, 200) >> true
        notesRepo.get(100) >> { Optional.empty() }

        when:
        Optional<Note> note = service.getForUser(100, 200)

        then:
        !note.isPresent()
    }

    def "attempting to retrieve a note when the user does not have read permision should return an empty optional"() {
        permissionService.userHasReadPermission(100, 200) >> false

        when:
        Optional<Note> note = service.getForUser(100, 200)

        then:
        !note.isPresent()
    }

    def "deleting a note should pass the ID through"() {
        permissionService.userIsOwner(100, 200) >> true

        when:
        service.deleteForUser(100, 200)

        then:
        1 * notesRepo.delete(100)
        1 * permissionService.deletePermissionsForNote(100)
    }

    def "attempting to delete a note not owned by the user should throw an ObjectNotFoundException"() {
        permissionService.userIsOwner(100, 200) >> false

        when:
        service.deleteForUser(100, 200)

        then:
        thrown(ObjectNotFoundException)
    }
}

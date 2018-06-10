package net.plshark.notes.service

import net.plshark.ObjectNotFoundException
import net.plshark.notes.Note
import net.plshark.notes.repo.NotesRepository
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.test.publisher.PublisherProbe
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
        PublisherProbe probe = PublisherProbe.empty()
        permissionService.grantOwnerPermissions(1, 2) >> probe.mono()
        notesRepo.insert(_) >> Mono.just(new Note(1, 3, "", ""))

        expect:
        StepVerifier.create(service.save(new Note("", ""), 2))
            .expectNext(new Note(1, 3, "", ""))
            .verifyComplete()
        probe.assertWasSubscribed()
        probe.assertWasRequested()
        probe.assertWasNotCancelled()
    }

    def "notes with ID should be updated"() {
        permissionService.userHasWritePermission(4, 1) >> Mono.just(true)
        notesRepo.update(new Note(4, 2, "", "")) >> Mono.just(new Note(4, 2, "", ""))

        expect:
        StepVerifier.create(service.save(new Note(4, 2, "", ""), 1))
            .expectNext(new Note(4, 2, "", ""))
            .verifyComplete()
    }

    def "updating a note without write permission should throw ObjectNotFoundException"() {
        permissionService.userHasWritePermission(4, 1) >> Mono.just(false)

        expect:
        StepVerifier.create(service.save(new Note(4, 2, "", ""), 1))
            .verifyError(ObjectNotFoundException.class)
    }

    def "retrieving a note by ID for a user should pass the IDs through"() {
        permissionService.userHasReadPermission(100, 200) >> Mono.just(true)
        notesRepo.get(100) >> Mono.just(new Note(100, 3, "title", "content"))

        expect:
        StepVerifier.create(service.getForUser(100, 200))
            .expectNext(new Note(100, 3, "title", "content"))
            .verifyComplete()
    }

    def "attempting to retrieve a note that does not exist should return an empty Mono"() {
        permissionService.userHasReadPermission(100, 200) >> Mono.just(true)
        notesRepo.get(100) >> Mono.empty()

        expect:
        StepVerifier.create(service.getForUser(100, 200))
            .verifyComplete()
    }

    def "attempting to retrieve a note when the user does not have read permision should return an empty Mono"() {
        permissionService.userHasReadPermission(100, 200) >> Mono.just(false)

        expect:
        StepVerifier.create(service.getForUser(100, 200))
            .verifyComplete()
    }

    def "deleting a note should pass the ID through"() {
        permissionService.userIsOwner(100, 200) >> Mono.just(true)
        PublisherProbe deleteNoteProbe = PublisherProbe.empty()
        notesRepo.delete(100) >> deleteNoteProbe.mono()
        PublisherProbe deletePermsProbe = PublisherProbe.empty()
        permissionService.deletePermissionsForNote(100) >> deletePermsProbe.mono()

        expect:
        StepVerifier.create(service.deleteForUser(100, 200))
            .verifyComplete()
        deleteNoteProbe.assertWasSubscribed()
        deleteNoteProbe.assertWasRequested()
        deleteNoteProbe.assertWasNotCancelled()
        deletePermsProbe.assertWasSubscribed()
        deletePermsProbe.assertWasRequested()
        deletePermsProbe.assertWasNotCancelled()
    }

    def "attempting to delete a note not owned by the user should throw an ObjectNotFoundException"() {
        permissionService.userIsOwner(100, 200) >> Mono.just(false)

        expect:
        StepVerifier.create(service.deleteForUser(100, 200))
            .verifyError(ObjectNotFoundException.class)
    }
}

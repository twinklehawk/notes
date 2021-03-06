package net.plshark.notes.service

import net.plshark.ObjectNotFoundException
import net.plshark.notes.NotePermission
import net.plshark.notes.UserNotePermission
import net.plshark.notes.repo.UserNotePermissionsRepository
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.test.publisher.PublisherProbe
import spock.lang.Specification

class UserNotePermissionsServiceImplSpec extends Specification {

    UserNotePermissionsRepository permissionRepo = Mock()
    UserNotePermissionsServiceImpl service = new UserNotePermissionsServiceImpl(permissionRepo)

    def "null arguments are not accepted in the constructor"() {
        when:
        new UserNotePermissionsServiceImpl(null)

        then:
        thrown(NullPointerException)
    }

    def "a user has read permission when isReadable is true"() {
        permissionRepo.getByUserAndNote('user', 20) >> Mono.just(new UserNotePermission('user', 20, true, false))

        expect:
        StepVerifier.create(service.userHasReadPermission(20, 'user'))
            .expectNext(true)
            .verifyComplete()
    }

    def "a user does not have read permission when isReadable is false"() {
        permissionRepo.getByUserAndNote('user', 20) >> Mono.just(new UserNotePermission('user', 20, false, false))

        expect:
        StepVerifier.create(service.userHasReadPermission(20, 'user'))
            .expectNext(false)
            .verifyComplete()
    }

    def "a user does not have read permission when there is no permission row"() {
        permissionRepo.getByUserAndNote('user', 20) >> Mono.empty()

        expect:
        StepVerifier.create(service.userHasReadPermission(20, 'user'))
            .expectNext(false)
            .verifyComplete()
    }

    def "a user has write permission when isWritable is true"() {
        permissionRepo.getByUserAndNote('user', 20) >> Mono.just(new UserNotePermission('user', 20, true, true))

        expect:
        StepVerifier.create(service.userHasWritePermission(20, 'user'))
            .expectNext(true)
            .verifyComplete()
    }

    def "a user does not have write permission when isWritable is false"() {
        permissionRepo.getByUserAndNote('user', 20) >> Mono.just(new UserNotePermission('user', 20, true, false))

        expect:
        StepVerifier.create(service.userHasWritePermission(20, 'user'))
            .expectNext(false)
            .verifyComplete()
    }

    def "a user does not have write permission when there is no permission row"() {
        permissionRepo.getByUserAndNote('user', 20) >> Mono.empty()

        expect:
        StepVerifier.create(service.userHasWritePermission(20, 'user'))
            .expectNext(false)
            .verifyComplete()
    }

    def "a user is the owner when isOwner is true"() {
        permissionRepo.getByUserAndNote('user', 20) >> Mono.just(new UserNotePermission('user', 20, true, true, true))

        expect:
        StepVerifier.create(service.userIsOwner(20, 'user'))
            .expectNext(true)
            .verifyComplete()
    }

    def "a user is not the owner when isOwner is false"() {
        permissionRepo.getByUserAndNote('user', 20) >> Mono.just(new UserNotePermission('user', 20, true, true, false))

        expect:
        StepVerifier.create(service.userIsOwner(20, 'user'))
            .expectNext(false)
            .verifyComplete()
    }

    def "a user is not the owner when there is no permission row"() {
        permissionRepo.getByUserAndNote('user', 20) >> Mono.empty()

        expect:
        StepVerifier.create(service.userIsOwner(20, 'user'))
            .expectNext(false)
            .verifyComplete()
    }

    def "granting a user owner privileges grants readable, writable, and owner"() {
        PublisherProbe probe = PublisherProbe.empty()
        permissionRepo.insert({ UserNotePermission p -> p.noteId == 20 && p.username == 'user' && p.readable && p.writable && p.owner }) >> probe.mono()

        expect:
        StepVerifier.create(service.grantOwnerPermissions(20, 'user'))
            .verifyComplete()
        probe.assertWasSubscribed()
        probe.assertWasRequested()
        probe.assertWasNotCancelled()
    }

    def "setting permissions when no permissions exist creates a new row"() {
        permissionRepo.getByUserAndNote('user', 20) >> Mono.just(new UserNotePermission('user', 20, true, true, true))
        permissionRepo.getByUserAndNote('user2', 20) >> Mono.empty()
        PublisherProbe probe = PublisherProbe.of(Mono.just(new UserNotePermission('user2', 20, true, true, false)))
        permissionRepo.insert(new UserNotePermission('user2', 20, true, true, false)) >> probe.mono()

        expect:
        StepVerifier.create(service.setPermissionForUser(20, 'user2', 'user', NotePermission.create(true, true)))
            .verifyComplete()
        probe.assertWasSubscribed()
        probe.assertWasRequested()
        probe.assertWasNotCancelled()
    }

    def "setting permissions when permissions exist updates the existing row"() {
        permissionRepo.getByUserAndNote('user', 20) >> Mono.just(new UserNotePermission('user', 20, true, true, true))
        permissionRepo.getByUserAndNote('user2', 20) >> Mono.just(new UserNotePermission('user2', 20, false, false, false))
        PublisherProbe probe = PublisherProbe.of(Mono.just(new UserNotePermission('user2', 20, true, true, false)))
        permissionRepo.update(new UserNotePermission('user2', 20, true, true, false)) >> probe.mono()

        expect:
        StepVerifier.create(service.setPermissionForUser(20, 'user2', 'user', NotePermission.create(true, true)))
            .verifyComplete()
        probe.assertWasSubscribed()
        probe.assertWasRequested()
        probe.assertWasNotCancelled()
    }

    def "attempting to set permissions when not the note owner throws an ObjectNotFoundException"() {
        permissionRepo.getByUserAndNote('user', 20) >> Mono.just(new UserNotePermission('user', 20, true, true, false))

        expect:
        StepVerifier.create( service.setPermissionForUser(20, 'user2', 'user', NotePermission.create(true, true)))
            .verifyError(ObjectNotFoundException.class)
    }

    def "removing permissions from a user deletes the permissions row"() {
        permissionRepo.getByUserAndNote('user', 20) >> Mono.just(new UserNotePermission('user', 20, true, true, true))
        PublisherProbe probe = PublisherProbe.empty()
        permissionRepo.deleteByUserAndNote('user2', 20) >> probe.mono()

        expect:
        StepVerifier.create(service.removePermissionForUser(20, 'user2', 'user'))
            .verifyComplete()
        probe.assertWasSubscribed()
        probe.assertWasRequested()
        probe.assertWasNotCancelled()
    }

    def "attempting to remove permissions when not the owner throws an ObjectNotFoundException"() {
        permissionRepo.getByUserAndNote('user', 20) >> Mono.just(new UserNotePermission('user', 20, true, true, false))
        permissionRepo.deleteByUserAndNote(_, _) >> { throw new RuntimeException() }

        expect:
        StepVerifier.create(service.removePermissionForUser(20, 'user2', 'user'))
            .verifyError(ObjectNotFoundException.class)
    }
}

package net.plshark.notes.service

import net.plshark.notes.UserNotePermission
import net.plshark.notes.repo.UserNotePermissionsRepository
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
        permissionRepo.getByUserAndNote(10, 20) >> Optional.of(new UserNotePermission(10, 20, true, false))

        expect:
        service.userHasReadPermission(20, 10) == true
    }

    def "a user does not have read permission when isReadable is false"() {
        permissionRepo.getByUserAndNote(10, 20) >> Optional.of(new UserNotePermission(10, 20, false, false))

        expect:
        service.userHasReadPermission(20, 10) == false
    }

    def "a user does not have read permission when there is no permission row"() {
        permissionRepo.getByUserAndNote(10, 20) >> Optional.empty()

        expect:
        service.userHasReadPermission(20, 10) == false
    }

    def "a user has write permission when isWritable is true"() {
        permissionRepo.getByUserAndNote(10, 20) >> Optional.of(new UserNotePermission(10, 20, true, true))

        expect:
        service.userHasWritePermission(20, 10) == true
    }

    def "a user does not have write permission when isWritable is false"() {
        permissionRepo.getByUserAndNote(10, 20) >> Optional.of(new UserNotePermission(10, 20, true, false))

        expect:
        service.userHasWritePermission(20, 10) == false
    }

    def "a user does not have write permission when there is no permission row"() {
        permissionRepo.getByUserAndNote(10, 20) >> Optional.empty()

        expect:
        service.userHasWritePermission(20, 10) == false
    }


    def "a user is the owner when isOwner is true"() {
        permissionRepo.getByUserAndNote(10, 20) >> Optional.of(new UserNotePermission(10, 20, true, true, true))

        expect:
        service.userIsOwner(20, 10) == true
    }

    def "a user is not the owner when isOwner is false"() {
        permissionRepo.getByUserAndNote(10, 20) >> Optional.of(new UserNotePermission(10, 20, true, true, false))

        expect:
        service.userIsOwner(20, 10) == false
    }

    def "a user is not the owner when there is no permission row"() {
        permissionRepo.getByUserAndNote(10, 20) >> Optional.empty()

        expect:
        service.userIsOwner(20, 10) == false
    }

    def "granting a user owner privileges grants readable, writable, and owner"() {
        when:
        service.grantOwnerPermissions(20, 10)

        then:
        1 * permissionRepo.insert({ UserNotePermission p -> p.noteId == 20 && p.userId == 10 && p.readable && p.writable && p.owner })
    }
}

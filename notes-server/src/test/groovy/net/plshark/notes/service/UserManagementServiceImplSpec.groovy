package net.plshark.notes.service

import org.springframework.security.crypto.password.PasswordEncoder

import net.plshark.notes.repo.RoleRepository
import net.plshark.notes.repo.UserRepository
import spock.lang.Specification

class UserManagementServiceImplSpec extends Specification {

    UserRepository userRepo = Mock()
    RoleRepository roleRepo = Mock()
    PasswordEncoder encoder = Mock()
    UserManagementServiceImpl service = new UserManagementServiceImpl(userRepo, roleRepo, encoder)

    def "cannot save user with ID set"() {

    }

    def "cannot insert user with null username or password"() {

    }

    def "new users have password encoded"() {

    }

    def "cannot save role with ID set"() {

    }


}

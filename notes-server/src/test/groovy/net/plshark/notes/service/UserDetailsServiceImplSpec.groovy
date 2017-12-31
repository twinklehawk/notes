package net.plshark.notes.service

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

import net.plshark.notes.Role
import net.plshark.notes.User
import net.plshark.notes.repo.UsersRepository
import net.plshark.notes.repo.UserRolesRepository
import spock.lang.Specification

class UserDetailsServiceImplSpec extends Specification {

    UsersRepository usersRepo = Mock()
    UserRolesRepository userRolesRepo = Mock()

    def "constructor does not accept null args"() {
        when:
        new UserDetailsServiceImpl(null, userRolesRepo)

        then:
        thrown(NullPointerException)

        when:
        new UserDetailsServiceImpl(usersRepo, null)

        then:
        thrown(NullPointerException)
    }

    def "a user and its roles are mapped to the correct UserDetails"() {
        usersRepo.getForUsername("user") >> new User(25, "user", "pass")
        userRolesRepo.getRolesForUser(25) >> Arrays.asList(new Role(3, "normal-user"), new Role(5, "admin"))
        UserDetailsServiceImpl service = new UserDetailsServiceImpl(usersRepo, userRolesRepo)

        when:
        UserDetails details = service.loadUserByUsername("user")

        then:
        details.username == "user"
        details.password == "pass"
        details.authorities.size() == 2
        details.authorities.contains(new SimpleGrantedAuthority("ROLE_normal-user"))
        details.authorities.contains(new SimpleGrantedAuthority("ROLE_admin"))
    }

    def "UsernameNotFoundException thrown when no user is found for username"() {
        usersRepo.getForUsername("user") >> { throw new EmptyResultDataAccessException(1) }
        UserDetailsServiceImpl service = new UserDetailsServiceImpl(usersRepo, userRolesRepo)

        when:
        service.loadUserByUsername("user")

        then:
        thrown(UsernameNotFoundException)
    }

    def "empty roles returns no granted authorities"() {
        usersRepo.getForUsername("user") >> new User(25, "user", "pass")
        userRolesRepo.getRolesForUser(25) >> Collections.emptyList()
        UserDetailsServiceImpl service = new UserDetailsServiceImpl(usersRepo, userRolesRepo)

        when:
        UserDetails details = service.loadUserByUsername("user")

        then:
        details.authorities.size() == 0
    }
}

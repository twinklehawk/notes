package net.plshark.notes.service

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

import net.plshark.notes.Role
import net.plshark.notes.User
import net.plshark.notes.repo.UserRepository
import spock.lang.Specification

class UserDetailsServiceImplSpec extends Specification {

    def "constructor does not accept null args"() {
        when:
        new UserDetailsServiceImpl(null)

        then:
        thrown(NullPointerException)
    }

    def "a user and its roles are mapped to the correct UserDetails"() {
        UserRepository repo = Mock()
        repo.getForUsername("user") >> new User(25, "user", "pass")
        repo.getRolesForUser(25) >> Arrays.asList(new Role(3, "normal-user"), new Role(5, "admin"))
        UserDetailsServiceImpl service = new UserDetailsServiceImpl(repo)

        when:
        UserDetails details = service.loadUserByUsername("user")

        then:
        details.username == "user"
        details.password == "pass"
        details.authorities.size() == 2
        details.authorities.contains(new SimpleGrantedAuthority("normal-user"))
        details.authorities.contains(new SimpleGrantedAuthority("admin"))
    }

    def "UsernameNotFoundException thrown when no user is found for username"() {
        UserRepository repo = Mock()
        repo.getForUsername("user") >> { throw new EmptyResultDataAccessException(1) }
        UserDetailsServiceImpl service = new UserDetailsServiceImpl(repo)

        when:
        service.loadUserByUsername("user")

        then:
        thrown(UsernameNotFoundException)
    }

    def "empty roles returns no granted authorities"() {
        UserRepository repo = Mock()
        repo.getForUsername("user") >> new User(25, "user", "pass")
        repo.getRolesForUser(25) >> Collections.emptyList()
        UserDetailsServiceImpl service = new UserDetailsServiceImpl(repo)

        when:
        UserDetails details = service.loadUserByUsername("user")

        then:
        details.authorities.size() == 0
    }
}

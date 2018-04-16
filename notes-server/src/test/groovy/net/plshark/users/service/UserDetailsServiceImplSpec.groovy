package net.plshark.users.service

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

import net.plshark.users.Role
import net.plshark.users.User
import net.plshark.users.repo.UserRolesRepository
import net.plshark.users.repo.UsersRepository
import net.plshark.users.service.UserDetailsServiceImpl
import net.plshark.users.service.UserDetailsServiceImpl.UserInfo
import spock.lang.Specification

class UserDetailsServiceImplSpec extends Specification {

    UsersRepository usersRepo = Mock()
    UserRolesRepository userRolesRepo = Mock()
    UserDetailsServiceImpl service = new UserDetailsServiceImpl(usersRepo, userRolesRepo)

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
        usersRepo.getForUsername("user") >> Optional.of(new User(25, "user", "pass"))
        userRolesRepo.getRolesForUser(25) >> Arrays.asList(new Role(3, "normal-user"), new Role(5, "admin"))

        when:
        UserDetails details = service.loadUserByUsername("user")

        then:
        details.username == "user"
        details.password == "pass"
        details.authorities.size() == 2
        details.authorities.contains(new SimpleGrantedAuthority("ROLE_normal-user"))
        details.authorities.contains(new SimpleGrantedAuthority("ROLE_admin"))
        details instanceof UserInfo
        ((UserInfo) details).userId == 25
    }

    def "UsernameNotFoundException thrown when no user is found for username"() {
        usersRepo.getForUsername("user") >> Optional.empty()

        when:
        UserDetails user = service.loadUserByUsername("user")

        then:
        thrown(UsernameNotFoundException)
    }

    def "empty roles returns no granted authorities"() {
        usersRepo.getForUsername("user") >> Optional.of(new User(25, "user", "pass"))
        userRolesRepo.getRolesForUser(25) >> Collections.emptyList()

        when:
        UserDetails details = service.loadUserByUsername("user")

        then:
        details.authorities.size() == 0
    }

    def "user ID returned from authentication when authentication is instance of UserInfo"() {
        usersRepo.getForUsername("user") >> Optional.of(new User(25, "user", "pass"))
        userRolesRepo.getRolesForUser(25) >> Arrays.asList(new Role(3, "normal-user"), new Role(5, "admin"))
        UserDetails details = service.loadUserByUsername("user")
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(details,
            details.getPassword(), details.getAuthorities())

        when:
        long userId = service.getUserIdForAuthentication(token)

        then:
        userId == 25
    }

    def "user ID is looked up when using authentication from external source"() {
        usersRepo.getForUsername("user") >> Optional.of(new User(25, "user", "pass"))
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("user", "pass",
            Collections.emptyList())

        when:
        long userId = service.getUserIdForAuthentication(token)

        then:
        userId == 25
    }
}

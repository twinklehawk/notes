package net.plshark.notes.webservice

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

import javax.inject.Inject

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import com.fasterxml.jackson.databind.ObjectMapper

import net.plshark.auth.throttle.impl.LoginAttemptServiceImpl
import net.plshark.notes.Note
import net.plshark.notes.Role
import net.plshark.notes.User
import net.plshark.notes.service.UserManagementService
import spock.lang.Specification

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@WebAppConfiguration
class WebSecurityConfigITSpec extends Specification {

    @Inject
    WebApplicationContext context
    @Inject
    UserManagementService userMgmt
    @Inject
    ObjectMapper mapper
    MockMvc mvc
    User notesUser

    def setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build()

        Role userRole = userMgmt.getRoleByName("notes-user")
        Role adminRole = userMgmt.getRoleByName("notes-admin")

        notesUser = userMgmt.saveUser(new User("test-user", "pass"))
        userMgmt.grantRoleToUser(notesUser, userRole)
    }

    def cleanup() {
        if (notesUser != null)
            userMgmt.deleteUser(notesUser.id.asLong)
    }

    def "csrf tokens are required on post requests"() {
        expect:
        mvc.perform(post("/notes")).andExpect(status().isForbidden())
        mvc.perform(post("/notes").with(csrf().useInvalidToken())).andExpect(status().isForbidden())
        mvc.perform(
            post("/notes")
                .content(mapper.writeValueAsString(new Note(1, 0, "title", "content")))
                .with(csrf()))
        .andExpect(status().isUnauthorized())
    }

    def "http basic authentication is enabled"() {
        expect:
        mvc.perform(get("/notes/1").with(csrf())).andExpect(status().isUnauthorized())
        mvc.perform(get("/notes/1").with(csrf()).with(httpBasic("bad-user", "bad-pass")))
            .andExpect(status().isUnauthorized())
        mvc.perform(get("/notes/1").with(csrf()).with(httpBasic("test-user", "bad-pass")))
            .andExpect(status().isUnauthorized())
        mvc.perform(get("/notes/1").with(csrf()).with(httpBasic("test-user", "pass")))
            .andExpect(status().isNotFound())
    }

    def "too many failed login attempts are blocked"() {
        RequestPostProcessor remoteAddr = new RequestPostProcessor() {
            MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setRemoteAddr("192.168.1.2")
                return request
            }
        }

        expect:
        // because the throttling filter is before the basic auth, the throttling filter does not start blocking requests until maxAttempts + 2
        for (int i = 0; i < LoginAttemptServiceImpl.DEFAULT_MAX_ATTEMPTS + 1; ++i)
            mvc.perform(get("/notes/1").with(remoteAddr).with(csrf()).with(httpBasic("lockout-user", "bad-pass")))
                .andExpect(status().isUnauthorized())
        mvc.perform(get("/notes/1").with(remoteAddr).with(csrf()).with(httpBasic("lockout-user", "bad-pass")))
            .andExpect(status().isTooManyRequests())
    }

    def "normal methods require notes-user role"() {
        expect:
        mvc.perform(get("/notes/1").with(csrf()).with(user("test-user").password("pass").roles("notes-admin")))
            .andExpect(status().isForbidden())
        mvc.perform(get("/notes/1").with(csrf()).with(user("test-user").password("pass").roles("notes-user")))
            .andExpect(status().isNotFound())
    }

    def "admin methods require notes-admin role"() {
        expect:
        mvc.perform(delete("/users/100").with(csrf()).with(user("test-user").password("pass").roles("notes-user")))
            .andExpect(status().isForbidden())
        mvc.perform(delete("/users/100").with(csrf()).with(user("test-user").password("pass").roles("notes-admin")))
            .andExpect(status().isOk())

        mvc.perform(delete("/roles/100").with(csrf()).with(user("test-user").password("pass").roles("notes-user")))
            .andExpect(status().isForbidden())
        mvc.perform(delete("/roles/100").with(csrf()).with(user("test-user").password("pass").roles("notes-admin")))
            .andExpect(status().isOk())
    }
}

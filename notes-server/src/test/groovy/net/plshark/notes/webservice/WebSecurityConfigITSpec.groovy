package net.plshark.notes.webservice

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

import javax.inject.Inject

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import com.fasterxml.jackson.databind.ObjectMapper

import net.plshark.notes.Note
import spock.lang.Specification

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@WebAppConfiguration
class WebSecurityConfigITSpec extends Specification {

    @Inject
    WebApplicationContext context
    @Inject
    ObjectMapper mapper
    MockMvc mvc

    def setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build()
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
        mvc.perform(get("/notes/1").with(csrf()).with(httpBasic("bad-user", "bad-pass"))).andExpect(status().isUnauthorized())
        mvc.perform(get("/notes/1").with(csrf()).with(httpBasic("admin", "bad-pass"))).andExpect(status().isUnauthorized())
        mvc.perform(get("/notes/1").with(csrf()).with(httpBasic("admin", "password"))).andExpect(status().isNotFound())
    }
}

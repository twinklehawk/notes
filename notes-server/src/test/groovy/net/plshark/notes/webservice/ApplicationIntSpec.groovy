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
class ApplicationIntSpec extends Specification {

    @Inject
    Application application

    def "context can be built"() {
        expect:
        application != null
    }
}

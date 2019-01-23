package net.plshark.notes.webservice

import javax.inject.Inject
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(classes = [ Application.class, TestConfig.class],
        properties = [
                'spring.flyway.placeholders.schema=notes',
                'spring.flyway.placeholders.username=testuser',
                'spring.flyway.placeholders.password=pass',
                'spring.flyway.schemas=notes' ])
class ApplicationIntSpec extends Specification {

    @Inject
    Application application

    def "context can be built"() {
        expect:
        application != null
    }
}

package pl.dk.cdocs.compare

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import pl.dk.cdocs.CaslDocComparatorApp
import spock.lang.Specification
import spock.lang.Subject

import static java.util.UUID.fromString

@ContextConfiguration(classes = [CaslDocComparatorApp])
@ActiveProfiles("test")
@WebAppConfiguration
@SpringBootTest
class CmrsComparatorSpec extends Specification {

    @Subject
    @Autowired
    CmrsComparator cmrsComparator

    def "should compare cmrs"() {
        when:
            cmrsComparator.compare(fromString('ca276db0-2c07-11e3-8a73-0026b9414f30'))

        then:
            noExceptionThrown()
    }
}

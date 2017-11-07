package pl.dk.cdocs.compare

import com.mongodb.client.FindIterable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import pl.dk.cdocs.CaslDocComparatorApp
import pl.dk.cdocs.repo.ContentRecords
import spock.lang.Specification
import spock.lang.Subject

import javax.swing.text.Document

import static java.util.UUID.fromString
import static pl.dk.cdocs.model.ContentTypes.PERSON_CONTEXT

@ContextConfiguration(classes = [CaslDocComparatorApp])
@ActiveProfiles("test")
@WebAppConfiguration
@SpringBootTest
class DocumentsComparatorSpec extends Specification {

    @Autowired
    ContentRecords contentRecords

    @Subject
    @Autowired
    DocumentsComparator documentsComparator

    def "should compare documents"() {
          given:
              FindIterable<Document> documents = contentRecords.findByLinksAndContentType(fromString('d9c01b92-dcdc-11e3-9765-0026b9414f30'), PERSON_CONTEXT)

          when:
              documentsComparator.compare(documents)

          then:
              noExceptionThrown()
      }

}

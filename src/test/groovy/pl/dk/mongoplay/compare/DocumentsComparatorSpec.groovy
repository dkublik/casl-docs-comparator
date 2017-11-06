package pl.dk.mongoplay.compare

import com.mongodb.client.FindIterable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import pl.dk.mongoplay.MongoPlayApp
import pl.dk.mongoplay.repo.ContentRecords
import spock.lang.Specification
import spock.lang.Subject

import javax.swing.text.Document

@ContextConfiguration(classes = [MongoPlayApp])
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
              FindIterable<Document> documents = contentRecords.findByLinks(UUID.fromString('39ca49c4-5fd2-11e2-8963-0026b9414f30'))

          when:
              documentsComparator.compare(documents)

          then:
              noExceptionThrown()
      }

}

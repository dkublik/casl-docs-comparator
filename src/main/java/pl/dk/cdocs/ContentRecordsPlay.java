package pl.dk.cdocs;

import com.mongodb.client.MongoCursor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.bson.Document;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.dk.cdocs.repo.ContentRecords;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.UUID;

@Service
@Profile("!test")
class ContentRecordsPlay {

    private final ContentRecords contentRecords;

    ContentRecordsPlay(ContentRecords contentRecords) {
        this.contentRecords = contentRecords;
    }

    @PostConstruct
    void postConstruct() throws Exception {
        Reader artistsReader =
                new BufferedReader(new InputStreamReader(
                        this.getClass().getResourceAsStream("/" + "selection_of_artist_mtv_de.csv")));
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withDelimiter(';').parse(artistsReader);
        for (CSVRecord record : records) {
            UUID personId = UUID.fromString(record.get(0));
            UUID personContextId = UUID.fromString(record.get(2));

            MongoCursor<Document> documents = contentRecords.findByLinks(personId).iterator();

            System.out.println("-- " + personId + ": " + size(documents) + ", cmrs: " + size( contentRecords.findCmrs(personContextId).iterator()));
        }

    }

    private int size(MongoCursor<Document> documents) {
        int size = 0;
        while (documents.hasNext()) {
            size ++;
            documents.next();
        }
        return size;
    }
}

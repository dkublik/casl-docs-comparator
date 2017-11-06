package pl.dk.mongoplay.repo;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

@Service
public class ContentRecords {


    @Value("${spring.data.mongodb.database}")
    @Getter
    private String databaseName;

    private MongoDatabase mongoDatabase;

    private String contentRecordsCollectionName = "mtvi_content_record";

    private final MongoClient mongoClient;

    ContentRecords(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @PostConstruct
    void getMongoDatabase() {
        mongoDatabase = mongoClient.getDatabase(databaseName);
    }

    @PreDestroy
    void destroy() {
        mongoClient.close();
    }

    private MongoCollection<Document> contentRecords() {
        return mongoDatabase.getCollection(contentRecordsCollectionName);
    }


    public Document findContent(UUID id) {
        return contentRecords().find(eq("_id", id))
                .first();
    }

    public FindIterable<Document> findByLinks(UUID id) {
        return contentRecords().find(eq("_links", id));
    }

    public FindIterable<Document> findCmrs(UUID id) {
        return contentRecords().find(eq("MasterLink", id));
    }


}

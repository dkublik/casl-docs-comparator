package pl.dk.mongoplay.compare;

import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static pl.dk.mongoplay.util.AnsiColors.ANSI_CYAN;
import static pl.dk.mongoplay.util.AnsiColors.ANSI_GREEN;
import static pl.dk.mongoplay.util.AnsiColors.ANSI_PURPLE;
import static pl.dk.mongoplay.util.AnsiColors.ANSI_RESET;

@Service
class DocumentsComparator {

    void compare(Iterable<Document> documentsIterable) {
        List<Document> documents = new ArrayList<>();
        documentsIterable.iterator().forEachRemaining(documents::add);

        System.out.println();
        System.out.println(ANSI_PURPLE + "---------------- number of docs: " + documents.size() + ANSI_RESET);
        System.out.println();

        Set<String> allProperties = collectAllProperties(documents);

        allProperties.stream()
                .sorted()
                .forEach(property -> {
                    if (propertyEverywhereEqual(property, documents)) {
                        System.out.println(ANSI_GREEN + "-------- " + property + ": everywhere equal" + ANSI_RESET);
                    }
                    else {
                        System.out.println(ANSI_GREEN + "-------- " + property + ANSI_RESET);
                        for (Document document: documents) {
                            Object value = document.get(property);
                            System.out.println(ANSI_CYAN + "  -- " + document.get("_id") + ANSI_RESET);
                            System.out.println(value);
                            System.out.println();
                        }
                    }
                });
        System.out.println();
    }

    private boolean propertyEverywhereEqual(String property, List<Document> documents) {
        Object firstDocumentPropertyValue = documents.get(0).get(property);
        for (Document document: documents) {
            Object documentPropertyValue = document.get(property);
            if (!Objects.equals(firstDocumentPropertyValue, documentPropertyValue)) {
                return false;
            }
        }
        return true;
    }

    private Set<String> collectAllProperties(List<Document> documents) {
        return documents.stream()
                .flatMap(document -> document.keySet().stream())
                .filter(PropertiesToSkip::shouldNotSkip)
                .collect(toSet());
    }

}

package pl.dk.cdocs.compare;

import org.bson.Document;
import org.springframework.stereotype.Service;
import pl.dk.cdocs.repo.ContentRecords;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;
import static pl.dk.cdocs.util.AnsiColors.ANSI_CYAN;
import static pl.dk.cdocs.util.AnsiColors.ANSI_GREEN;
import static pl.dk.cdocs.util.AnsiColors.ANSI_PURPLE;
import static pl.dk.cdocs.util.AnsiColors.ANSI_RESET;

@Service
class CmrsComparator {

    private final ContentRecords contentRecords;

    CmrsComparator(ContentRecords contentRecords) {
        this.contentRecords = contentRecords;
    }

    void compare(UUID masterId) {
        Document master = contentRecords.findContent(masterId);
        List<Document> cmrs = new ArrayList<>();
        contentRecords.findCmrs(masterId).iterator().forEachRemaining(cmrs::add);
        List<Document> allDocuments = new ArrayList<>(cmrs);
        allDocuments.add(0, master);

        System.out.println();
        System.out.println(ANSI_PURPLE + "---------------- number of docs: " + allDocuments.size() + ANSI_RESET);
        System.out.println();

        Set<String> allProperties = collectAllProperties(allDocuments);

        allProperties.stream()
                .sorted()
                .forEach(property -> {
                    if (propertyEverywhereEqual(property, master, cmrs)) {
                        System.out.println(ANSI_GREEN + "-------- " + property + ": everywhere equal" + ANSI_RESET);
                    }
                    else {
                        System.out.println(ANSI_GREEN + "-------- " + property + ANSI_RESET);
                        for (Document document: allDocuments) {
                            Object value = document.get(property);
                            System.out.println(documentHeader(document, master));
                            System.out.println(value);
                            System.out.println();
                        }
                    }
                });
        System.out.println();
    }

    private String documentHeader(Document document, Document master) {
        String documentId = document.get("_id").toString();
        if (document.get("_id").equals(master.get("_id"))) {
            documentId += " (*) ";
        }
        return ANSI_CYAN + "  -- " + documentId + ANSI_RESET;
    }

    private boolean propertyEverywhereEqual(String property, Document master, List<Document> cmrs) {
        Object masterPropertyValue = master.get(property);
        for (Document cmr: cmrs) {
            Object cmrPropertyValue = cmr.get(property);
            if (cmrPropertyValue != null  && !cmrPropertyValue.equals(masterPropertyValue)) {
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

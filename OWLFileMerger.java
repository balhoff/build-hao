import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

public class OWLFileMerger {

    /**
     * @param args first: main file, second: file to merge in, third: new file
     * @throws OWLOntologyStorageException 
     * @throws OWLOntologyCreationException 
     */
    public static void main(String[] args) throws OWLOntologyStorageException, OWLOntologyCreationException {
        final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        manager.setSilentMissingImportsHandling(true);
        final OWLOntology mainOntology = manager.loadOntologyFromOntologyDocument(new File(args[0]));
        manager.loadOntologyFromOntologyDocument(new File(args[1]));
        final OWLOntologyManager mergeManager = OWLManager.createOWLOntologyManager();
        final OWLOntologyMerger merger = new OWLOntologyMerger(manager);
        final OWLOntology mergedOntology = merger.createMergedOntology(mergeManager, mainOntology.getOntologyID().getOntologyIRI());
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String date = dateFormat.format(new Date());
        final OWLDataFactory dataFactory = mergeManager.getOWLDataFactory();
        final OWLAnnotationProperty versionIRI = dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.OWL_VERSION_IRI.getIRI());
        final OWLAnnotationAssertionAxiom versionIRIAxiom = dataFactory.getOWLAnnotationAssertionAxiom(versionIRI, mergedOntology.getOntologyID().getOntologyIRI(), IRI.create(String.format("http://purl.obolibrary.org/obo/hao/%s/hao.owl", date)));
        mergeManager.addAxiom(mergedOntology, versionIRIAxiom);
        final OWLAnnotationProperty versionInfo = dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.OWL_VERSION_INFO.getIRI());
        final OWLAnnotationAssertionAxiom versionInfoAxiom = dataFactory.getOWLAnnotationAssertionAxiom(versionInfo, mergedOntology.getOntologyID().getOntologyIRI(), dataFactory.getOWLLiteral(date));
        mergeManager.addAxiom(mergedOntology, versionInfoAxiom);
        mergeManager.saveOntology(mergedOntology, new RDFXMLOntologyFormat(), IRI.create(new File(args[2])));
    }

}

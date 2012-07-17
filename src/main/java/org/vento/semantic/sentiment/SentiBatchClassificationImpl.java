package org.vento.semantic.sentiment;

import gate.*;
import gate.corpora.DocumentImpl;
import gate.util.ExtensionFileFilter;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;
import org.vento.gate.GateBatchClassification;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Martin
 * Date: 06/04/2012
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */
public class SentiBatchClassificationImpl implements GateBatchClassification{

    private CorpusController application;
    private Corpus documentCorpus;
    //SerialDataStore persistentDS;
    private File gateHome;
    private File gateConfigFile;
    private String corpusName;

    /* constructor not required
    public SentiBatchClassificationImpl(File gateHome, File projectConfigFile, String dataStoreDir, String corpusName) throws IOException, GateException {
        init(gateHome, projectConfigFile, dataStoreDir, corpusName);
    }
    */

    private void init() throws GateException, IOException {

        Gate.setGateHome(gateHome);

        // initialise GATE - this must be done before calling any GATE APIs
        Gate.init();

        /* persistent corpus not required in this class

        //  create&open a new Serial Data Store
        //  pass the datastore class and path as parameteres
        persistentDS = (SerialDataStore)Factory.createDataStore("gate.persist.SerialDataStore", dataStoreDir);
        persistentDS.open();

        Corpus corpus = Factory.newCorpus(corpusName);

        // save corpus in datastore
        //    SecurityInfo is ignored for SerialDataStore - just pass null
        //    a new persisent corpus is returned
        persistentCorpus = (Corpus) persistentDS.adopt(corpus, null);
        persistentDS.sync(persistentCorpus);

        */

        documentCorpus = Factory.newCorpus(corpusName);

        // load the saved application
        application = (CorpusController) PersistenceManager.loadObjectFromFile(gateConfigFile);
        application.setCorpus(documentCorpus);
    }

    private void addAllToCorpus(URL directory, String extension) throws IOException, GateException {
        //assuming UTF-8 encoding and recursive iteration
        documentCorpus.populate(directory, new ExtensionFileFilter("XML files", extension), "UTF-8", true);
    }

    private void addToCorpus(File file, String encoding, String mimeType) throws MalformedURLException, GateException {

        String docName = file.getName() + "_" + Gate.genSym();
        FeatureMap params = Factory.newFeatureMap();

        params.put(Document.DOCUMENT_URL_PARAMETER_NAME, file.toURI().toURL());

        if(encoding != null)
            params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, encoding);

        if(mimeType != null)
            params.put(Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, mimeType);

        Document doc = (Document)Factory.createResource(DocumentImpl.class
                .getName(), params, null, docName);

        documentCorpus.add(doc);
        if(documentCorpus.getLRPersistenceId() != null) {
            // persistent corpus -> unload the document
            documentCorpus.unloadDocument(doc);
            Factory.deleteResource(doc);
        }

    }

    public double simpleClassify(File file, String encoding, String mimeType) throws IOException, GateException {

        double classificationScore = 0.0;

        if (!Gate.isInitialised()) init();
        documentCorpus.clear();
        addToCorpus(file,encoding,mimeType);
        application.execute();

        Document classifiedDoc = documentCorpus.iterator().next();

        Iterator<Annotation> classificationScoreStr = classifiedDoc.getAnnotations("Output").get("Review").iterator();

        if (classificationScoreStr.hasNext()){

            classificationScore = Float.parseFloat((String)classificationScoreStr.next().getFeatures().get("score"));
        }

        return classificationScore;
    }

    //possible to implement but requires XML parser to add the <score> tag, to discuss
   /*
    public File classify(File file, String encoding, String mimeType) throws IOException, GateException {

        if (!Gate.isInitialised()) init();
        documentCorpus.clear();
        addToCorpus(file,encoding,mimeType);
        application.execute();

        Document classifiedDoc = documentCorpus.iterator().next();

        Iterator<Annotation> classificationScoreStr = classifiedDoc.getAnnotations("Output").get("Review").iterator();

        if (classificationScoreStr.hasNext()){

            root=xmlParser(file);
            score = new XMLElement()
            score.setText((String)classificationScoreStr.next().getFeatures().get("score"));
            root.addElement(score);
            fileWithScore << root
        }

        return fileWithScore;
    }
    */

    public File getGateHome() {
        return gateHome;
    }

    public void setGateHome(File gateHome) {
        this.gateHome = gateHome;
    }

    public File getGateConfigFile() {
        return gateConfigFile;
    }

    public void setGateConfigFile(File gateConfigFile) {
        this.gateConfigFile = gateConfigFile;
    }

    public String getCorpusName() {
        return corpusName;
    }

    public void setCorpusName(String corpusName) {
        this.corpusName = corpusName;
    }
}

package org.vento.sentiment.gate;

import gate.*;
import gate.corpora.DocumentImpl;
import gate.creole.ExecutionException;
import gate.persist.SerialDataStore;
import gate.util.ExtensionFileFilter;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;
import org.apache.commons.io.FileUtils;
import org.vento.sentiment.gate.GateBatchProcessing;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Martin
 * Date: 06/04/2012
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */
public class SentiBatchProcessingImpl implements GateBatchProcessing {

    CorpusController application;
    Corpus persistentCorpus;
    SerialDataStore persistentDS;

    public SentiBatchProcessingImpl(File gateHome, File projectConfigFile, String dataStoreDir, String corpusName) throws IOException, GateException, URISyntaxException {
        init(gateHome, projectConfigFile, dataStoreDir, corpusName);
    }


    public void init(File gateHome, File gateConfigFile, String dataStoreDir, String corpusName) throws GateException, IOException, URISyntaxException {

        if (!Gate.isInitialised()) {

            Gate.setGateHome(gateHome);

            // initialise GATE - this must be done before calling any GATE APIs
            Gate.init();

        }

        if (dataStoreDir != null && (new File(new URI(dataStoreDir))).listFiles().length > 0) {
            FileUtils.cleanDirectory(new File(new URI(dataStoreDir)));
        }
        //  create&open a new Serial Data Store
        //  pass the datastore class and path as parameteres
        persistentDS = (SerialDataStore) Factory.createDataStore("gate.persist.SerialDataStore", dataStoreDir);
        persistentDS.open();

        Corpus corpus = Factory.newCorpus(corpusName);

        // save corpus in datastore
        //    SecurityInfo is ignored for SerialDataStore - just pass null
        //    a new persisent corpus is returned
        persistentCorpus = (Corpus) persistentDS.adopt(corpus, null);
        persistentDS.sync(persistentCorpus);

        // load the saved application
        application = (CorpusController) PersistenceManager.loadObjectFromFile(gateConfigFile);
        application.setCorpus(persistentCorpus);
    }

    public void addAllToCorpus(URL directory, String extension) throws IOException, GateException, URISyntaxException {
        //assuming UTF-8 encoding and recursive iteration
        persistentCorpus.populate(directory, new ExtensionFileFilter("XML files", extension), "UTF-8", true);
    //    FileUtils.cleanDirectory(new File(directory.toURI()));
    }

    public void addToCorpus(File file, String encoding, String mimeType) throws MalformedURLException, GateException {

        String docName = file.getName() + "_" + Gate.genSym();
        FeatureMap params = Factory.newFeatureMap();

        params.put(Document.DOCUMENT_URL_PARAMETER_NAME, file.toURI().toURL());

        if (encoding != null)
            params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, encoding);

        if (mimeType != null)
            params.put(Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, mimeType);

        Document doc = (Document) Factory.createResource(DocumentImpl.class
                .getName(), params, null, docName);

        persistentCorpus.add(doc);
        if (persistentCorpus.getLRPersistenceId() != null) {
            // persistent corpus -> unload the document
            persistentCorpus.unloadDocument(doc);
            Factory.deleteResource(doc);
        }
    }

    public Corpus getCorpus() {
        return persistentCorpus;
    }

    public void perform() throws ExecutionException, IOException, GateException {
        application.execute();
        persistentDS.close();
        FileUtils.cleanDirectory(persistentDS.getStorageDir());
    }
}

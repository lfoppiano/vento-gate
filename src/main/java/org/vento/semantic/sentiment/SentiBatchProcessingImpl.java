package org.vento.semantic.sentiment;

import gate.*;
import gate.Corpus;
import gate.CorpusController;
import gate.Factory;
import gate.Gate;
import gate.corpora.DocumentImpl;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.event.StatusListener;
import gate.persist.SerialDataStore;
import gate.util.Err;
import gate.util.ExtensionFileFilter;
import gate.util.GateException;
import gate.util.Strings;
import gate.util.persistence.PersistenceManager;
import org.vento.gate.GateBatchProcessing;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Martin
 * Date: 06/04/2012
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */
public class SentiBatchProcessingImpl implements GateBatchProcessing{

    CorpusController application;
    Corpus persistentCorpus;
    SerialDataStore persistentDS;

    public SentiBatchProcessingImpl(File gateHome, File projectConfigFile, String dataStoreDir, String corpusName) throws IOException, GateException {
        init(gateHome, projectConfigFile, dataStoreDir, corpusName);
    }


    @Override
    public void init(File gateHome, File gateConfigFile, String dataStoreDir, String corpusName) throws GateException, IOException {

        Gate.setGateHome(gateHome);

        // initialise GATE - this must be done before calling any GATE APIs
        Gate.init();

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

        // load the saved application
        application = (CorpusController) PersistenceManager.loadObjectFromFile(gateConfigFile);
        application.setCorpus(persistentCorpus);
    }

    public void addAllToCorpus(URL directory, String extension) throws IOException, GateException {
        //assuming UTF-8 encoding and recursive iteration
        persistentCorpus.populate(directory, new ExtensionFileFilter("XML files", extension), "UTF-8", true);
        persistentDS.sync(persistentCorpus);
    }

    public void addToCorpus(File file, String encoding, String mimeType) throws MalformedURLException, GateException {

        StatusListener sListener = (StatusListener)Gate.getListeners().get(
                "gate.event.StatusListener");
        if(sListener != null)
            sListener.statusChanged("Reading: " + file.getName());
        String docName = file.getName() + "_" + Gate.genSym();
        FeatureMap params = Factory.newFeatureMap();
        params.put(Document.DOCUMENT_URL_PARAMETER_NAME, file.toURI().toURL());
        if(encoding != null)
            params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, encoding);
        if(mimeType != null)
            params.put(Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, mimeType);

        //try {
            Document doc = (Document)Factory.createResource(DocumentImpl.class
                    .getName(), params, null, docName);
            persistentCorpus.add(doc);
            if(persistentCorpus.getLRPersistenceId() != null) {
                // persistent corpus -> unload the document
                persistentCorpus.unloadDocument(doc);
                Factory.deleteResource(doc);
            }
        //}
        /*catch(Throwable t) {
            String nl = Strings.getNl();
            Err.prln("WARNING: Corpus.populate could not instantiate document" + nl
                    + "  Document name was: " + docName + nl + "  Exception was: "
                    + t + nl + nl);
            t.printStackTrace();
        }*/
        if(sListener != null) sListener.statusChanged(file.getName() + " read");

       /*
       persistentCorpus.add(Factory.newDocument(file.toURI().toURL()));
       persistentDS.sync(persistentCorpus);
       */

    }

    public void perform() throws ExecutionException, GateException {
        application.execute();
        persistentDS.close();

    }
}

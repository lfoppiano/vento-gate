package org.vento.semantic.sentiment;

import gate.*;
import gate.Corpus;
import gate.CorpusController;
import gate.Factory;
import gate.Gate;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.persist.SerialDataStore;
import gate.util.ExtensionFileFilter;
import gate.util.GateException;
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
public class SentiBatchLearning implements GateBatchProcessing{

    CorpusController application;
    Corpus persistentCorpus;
    SerialDataStore persistentDS;

    public SentiBatchLearning(File gateConfigFile) throws IOException, GateException {
        init(gateConfigFile);
    }

    public SentiBatchLearning(File gateHome, File gateConfigFile, String dataStoreDir, String corpusName) throws IOException, GateException {
        init(gateHome, gateConfigFile, dataStoreDir, corpusName);
    }

    public void init(File gateConfigFile) throws GateException, IOException {

        // initialise GATE - this must be done before calling any GATE APIs
        Gate.init();

        // load the saved application
        application = (CorpusController) PersistenceManager.loadObjectFromFile(gateConfigFile);

    }

    public void init(File gateHome, File gateConfigFile, String dataStoreDir, String corpusName) throws GateException, IOException {

        Gate.setGateHome(gateHome);

        // initialise GATE - this must be done before calling any GATE APIs
        Gate.init();

        /// /create&open a new Serial Data Store
        //    pass the datastore class and path as parameteres
        persistentDS  = (SerialDataStore)Factory.createDataStore("gate.persist.SerialDataStore",dataStoreDir);
        persistentDS.open();

        Corpus learningCorpus = Factory.newCorpus(corpusName);
        persistentCorpus = null;

        //save corpus in datastore
        //    SecurityInfo is ingored for SerialDataStore - just pass null
        //    a new persisent corpus is returned
        persistentCorpus = (Corpus)persistentDS.adopt(learningCorpus,null);
        persistentDS.sync(persistentCorpus);

        // load the saved application
        application = (CorpusController) PersistenceManager.loadObjectFromFile(gateConfigFile);

    }

    public void loadExample(String corpusName, File example) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void populateCorpus(URL directory, String extension, String encoding, Boolean recurseDirs) throws IOException, GateException {

        persistentCorpus.populate(directory, new ExtensionFileFilter("XML files",extension),encoding,recurseDirs);
        persistentDS.sync(persistentCorpus);
    }

    public void addToCorpus(File file) throws MalformedURLException, GateException {

       persistentCorpus.add(Factory.newDocument(file.toURI().toURL()));

       //Document persistentDoc = persistentDS.adopt(Factory.newDocument(file.toURI().toURL()), null);
       //persistentDS.sync(persistentDoc);

       persistentDS.sync(persistentCorpus);

    }

    public void performTraining() throws ExecutionException, GateException {
        application.setCorpus(persistentCorpus);
        persistentDS.sync(persistentCorpus);
        application.execute();

    }
}

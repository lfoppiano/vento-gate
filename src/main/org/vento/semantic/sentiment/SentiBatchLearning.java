package org.vento.semantic.sentiment;

import gate.*;
import gate.Corpus;
import gate.CorpusController;
import gate.Factory;
import gate.Gate;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;
import org.vento.gate.GateBatchProcessing;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by IntelliJ IDEA.
 * User: Martin
 * Date: 06/04/2012
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */
public class SentiBatchLearning implements GateBatchProcessing{

    CorpusController application;

    public SentiBatchLearning(File gateConfigFile) throws IOException, GateException {
        init(gateConfigFile);
    }

    public void init(File gateConfigFile) throws GateException, IOException {
        // initialise GATE - this must be done before calling any GATE APIs
        Gate.init();

        // load the saved application
        application = (CorpusController) PersistenceManager.loadObjectFromFile(gateConfigFile);

    }

    public void loadExample(String corpusName, File example) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void loadExamples(String corpusName, File[] examples) throws ResourceInstantiationException, MalformedURLException {
        Corpus corpus = Factory.newCorpus(corpusName);
        for (File file:examples) {
            corpus.add(Factory.newDocument(file.toURI().toURL()));
        }
        application.setCorpus(corpus);

    }

    public void perform() throws ExecutionException {
        application.execute();

    }
}

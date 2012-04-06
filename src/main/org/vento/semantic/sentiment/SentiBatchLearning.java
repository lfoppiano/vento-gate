package org.vento.semantic.sentiment;

import gate.*;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Martin
 * Date: 06/04/2012
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */
public class SentiBatchLearning {

  public void conductLearning(File[] learningBatch, File gateConfigFile) throws GateException,IOException{

    // initialise GATE - this must be done before calling any GATE APIs
    Gate.init();

    // load the saved application
    CorpusController application = (CorpusController) PersistenceManager.loadObjectFromFile(gateConfigFile);

    Corpus corpus = Factory.newCorpus("BatchLearning Corpus");
    for (File file:learningBatch) {

        corpus.add(Factory.newDocument(file.toURI().toURL()));

    }

    application.setCorpus(corpus);
    application.execute();

  }
}

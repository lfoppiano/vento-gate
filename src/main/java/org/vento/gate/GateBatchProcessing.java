package org.vento.gate;

import gate.Corpus;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: lfoppiano
 * Date: 4/6/12
 * Time: 11:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GateBatchProcessing {
    
    public void init(File gateHome, File gateConfigFile, String dataStoreDir, String corpusName) throws GateException, IOException;

    public void addAllToCorpus(URL directory, String extension) throws IOException, GateException;

    public void addToCorpus(File file, String encoding, String mimeType) throws MalformedURLException, GateException;

    public Corpus getCorpus();

    public void perform() throws ExecutionException, IOException, GateException;

}

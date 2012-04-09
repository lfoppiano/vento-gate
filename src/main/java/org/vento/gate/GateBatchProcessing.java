package org.vento.gate;

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
    
    public void init(File gateConfigFile) throws GateException, IOException;
    
    public void loadExample(String corpusName, File example);

    public void populateCorpus(URL directory, String extension, String encoding, Boolean recurseDirs) throws IOException, GateException;

    public void addToCorpus(File file) throws MalformedURLException, GateException;

    public void performTraining() throws ExecutionException, GateException;

}

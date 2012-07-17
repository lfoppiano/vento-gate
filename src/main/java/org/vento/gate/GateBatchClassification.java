package org.vento.gate;

import gate.Corpus;
import gate.creole.ExecutionException;
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
public interface GateBatchClassification {

    //public void init() throws GateException, IOException;

    //public void addAllToCorpus(URL directory, String extension) throws IOException, GateException;

    //public void addToCorpus(File file, String encoding, String mimeType) throws MalformedURLException, GateException;

    //public File classify(File file, String encoding, String mimeType) throws IOException, GateException;

    public double simpleClassify(File file, String encoding, String mimeType) throws IOException, GateException;

}

package org.vento.semantic.sentiment;

import gate.util.GateException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: Martin
 * Date: 06/04/2012
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public class SentiBatchLearningTest {
    
    SentiBatchLearning batchLearning; 
    
    @Before
    public void setUp() throws URISyntaxException, IOException, GateException {
        

        File gateConfigFile = new File(this.getClass().getResource("/batch-learning.training.configuration.xml").toURI());
        //Fix this path
        File gateHome = new File("/Applications/GATE_Developer_7.0/");
        String dataStore = this.getClass().getResource("/").toURI().toString()+"/temp_learning_datastore/";

        batchLearning = new SentiBatchLearning(gateHome, gateConfigFile, dataStore,"learningCorpus");
    }

    @Test
    public void testEnd2EndLearning() throws Exception {
        //Fix this path
        File corpusDirectory = new File("/Users/Martin/Desktop/learningTest/realLearningInput/");

        println "start loading"
        int good = 0
        int bad = 0
        int counter = 0

        batchLearning.populateCorpus(corpusDirectory.toURI().toURL(), "xml", "UTF-8", false)
        /*
        corpusDirectory.eachFile { file ->

            //adding the corpus
            counter++
            try{
                batchLearning.addToCorpus(file) //loadCorpus("cristoCheCorpus", corpusDirectory.listFiles());
                good++
           }
            catch(e){
                bad++
                println e.getStackTrace()
            }
        }
        
        println "finished loading, total= ${counter}, good = ${good}, bad = ${bad}"
        */
        println "learning session started..."

        def before = System.currentTimeMillis()
        batchLearning.performTraining();
        def after = System.currentTimeMillis()

        println "learning session finished, took ${after-before} ms"

    }
    

}

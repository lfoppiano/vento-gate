package org.vento.semantic.sentiment;

import gate.util.GateException;
import org.junit.Before;
import org.junit.Test
import org.vento.gate.GateBatchProcessing;

/**
 * Created by IntelliJ IDEA.
 * User: Martin
 * Date: 06/04/2012
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public class SentiBatchProcessingImplTest {
    
    GateBatchProcessing batchLearning;
    
    @Before
    public void setUp() throws URISyntaxException, IOException, GateException {

        File gateConfigFile = new File(this.getClass().getResource("/gate-project-learning-test/batch-learning.training.configuration.xml").toURI());
        File gateHome = new File("/Applications/GATE_Developer_7.0");

        String dataStore = this.getClass().getResource("/").toURI().toString()+"/temp_learning_datastore/";
        
        batchLearning = new SentiBatchProcessingImpl(gateHome, gateConfigFile, dataStore, "learningCorpus");
    }

    @Test
    public void testEnd2EndLearning() throws Exception {
        //TODO: Fix this path
        //File corpusDirectory = new File("/Users/Martin/Desktop/learningTest/realLearningInput/");
        File corpusDirectory = new File("/Users/Martin/Desktop/learningTest/realLearningInput");

        println "start loading"

        batchLearning.addAllToCorpus(corpusDirectory.toURI().toURL(), "xml")

        println "learning session started..."

        def before = System.currentTimeMillis()
        batchLearning.perform();
        def after = System.currentTimeMillis()

        println "learning session finished, took ${after-before} ms"



    }

    @Test
    public void testEnd2EndLearningSingular() throws Exception {
        //TODO: Fix this path
        //File corpusDirectory = new File("/Users/Martin/Desktop/learningTest/realLearningInput/");
        File corpusDirectory = new File("/Users/Martin/Desktop/learningTest/realLearningInput");

        println "start loading"

        corpusDirectory.eachFile{file->

          batchLearning.addToCorpus(file,"UTF-8",null)

        }

        //batchLearning.addAllToCorpus(corpusDirectory.toURI().toURL(), "xml")

        println "learning session started..."

        def before = System.currentTimeMillis()
        batchLearning.perform();
        def after = System.currentTimeMillis()

        println "learning session finished, took ${after-before} ms"



    }
    

}

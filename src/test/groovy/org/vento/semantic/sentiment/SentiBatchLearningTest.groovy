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

        File gateConfigFile = new File(this.getClass().getResource("/gate-project-learning-test/batch-learning.training.configuration.xml").toURI());
        File gateHome = new File("/opt/GATE_7.0");

        batchLearning = new SentiBatchLearning(gateHome, gateConfigFile);
    }

    @Test
    public void testEnd2EndLearning() throws Exception {

        File corpusDirectory = new File("/home/lfoppiano/develop/bi/batch_learning_GATE_resources/realLearningInput");

        println "start loading"
        int good = 0
        int bad = 0
        int counter = 0

        corpusDirectory.eachFile { file ->

            //adding the corpus
            counter++
            try{
                batchLearning.loadCorpus("cristoCheCorpus", file);
                good++
            }
            catch(e){
                bad++
                println e.getStackTrace()
            }
        }
        
        println "finish loading, total= ${counter}, good = ${good}, bad = ${bad}"

        // starting learning
        batchLearning.performTraining();

    }
    

}

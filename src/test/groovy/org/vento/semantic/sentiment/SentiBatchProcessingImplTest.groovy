package org.vento.semantic.sentiment;

import gate.util.GateException
import org.junit.Before
import org.junit.Test
import org.vento.gate.GateBatchProcessing
import gate.Document
import gate.Annotation
import gate.Factory

/**
 * Created by IntelliJ IDEA.
 * User: Martin
 * Date: 06/04/2012
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public class SentiBatchProcessingImplTest {
    
    GateBatchProcessing batchLearning;
    GateBatchProcessing batchClassification;
    //File gateHome = new File("/home/mpolojko/GATE_Developer_7.0");
    File gateHome = new File("/Applications/GATE_Developer_7.0");
    //File gateHome = new File("/opt/GATE_7.0");


    public void setUpLearning() throws URISyntaxException, IOException, GateException {

        File gateConfigFile = new File(this.getClass().getResource("/gate-project-training/batch-learning.training.configuration.xml").toURI());

        String dataStore = this.getClass().getResource("/").toURI().toString()+"/temp_learning_datastore/";

        batchLearning = new SentiBatchProcessingImpl(gateHome, gateConfigFile, dataStore, "learningCorpus");
    }

    public void setUpClassification() throws URISyntaxException, IOException, GateException {

        File gateConfigFile = new File(this.getClass().getResource("/gate-project-classification/batch-learning.classification.configuration.xml").toURI());

        String dataStore = this.getClass().getResource("/").toURI().toString()+"/temp_classification_datastore/";

        batchClassification = new SentiBatchProcessingImpl(gateHome, gateConfigFile, dataStore, "classificationCorpus");
    }

    @Test
    public void testEnd2EndLearning() throws Exception {
        setUpLearning();

        //File corpusDirectory = new File("/home/mpolojko/Desktop/realLearningInput/");
        File corpusDirectory = new File("/Users/Martin/Desktop/realLearningInput/");
        //File corpusDirectory = new File("/home/lfoppiano/develop/bi/batch_learning_GATE_resources/realLearningInput");

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
        setUpLearning();

        File corpusDirectory = new File("/Users/Martin/Desktop/realLearningInput/");
        //File corpusDirectory = new File("/home/lfoppiano/develop/bi/batch_learning_GATE_resources/realLearningInput");

        println "start loading"

        corpusDirectory.eachFile{file->

          batchLearning.addToCorpus(file,"UTF-8",null)

        }

        println "learning session started..."

        def before = System.currentTimeMillis()
        batchLearning.perform();
        def after = System.currentTimeMillis()

        println "learning session finished, took ${after-before} ms"

    }

    @Test
    public void testEnd2EndClassification() throws Exception {
        setUpClassification()

        //File corpusDirectory = new File(this.getClass().getResource("/gate-project-classification/classification-input/").toURI());
        File corpusDirectory = new File("/Users/Martin/Desktop/filteredClassificationInput/5.0/");

        def stats = ['correct':0,'positiveMiss':0,'negativeMiss':0, 'notClassified':0, 'oneOff':0]


        println "start loading"

        batchClassification.addAllToCorpus(corpusDirectory.toURI().toURL(), "xml")
        //corpusDirectory.eachFile{file->

        //    batchClassification.addToCorpus(file,"UTF-8",null)

        //}

        println "classification session started..."

        def before = System.currentTimeMillis()
        batchClassification.perform();
        def after = System.currentTimeMillis()

        println "classification session finished, took ${after-before} ms"

        Iterator<Document> corpusIterator = batchClassification.getCorpus().iterator();
        while (corpusIterator.hasNext()){
            Document tempDoc = corpusIterator.next();
            Annotation tempAnnotation = tempDoc.getAnnotations("Original markups").get("score").iterator().next()

            String originalScoreStr = tempDoc.getContent().getContent(tempAnnotation.startNode.getOffset(),tempAnnotation.endNode.getOffset()).toString()

            Iterator classificationScoreStr = tempDoc.getAnnotations("Output").get("Review").iterator()

            if (classificationScoreStr.hasNext()){

                float originalScore = originalScoreStr.toFloat()

                float classificationScore = classificationScoreStr.next().getFeatures().get("score").toFloat()

                if (originalScore==classificationScore+1 ||  originalScore==classificationScore-1)
                    stats['oneOff']++
                else
                    if (originalScore>classificationScore) stats['negativeMiss']++
                        else if (originalScore<classificationScore) stats['positiveMiss']++
                             else
                                stats['correct']++
            }
            else
                stats['notClassified']++

            Factory.deleteResource(tempDoc)
        }

        int docsTotal = batchClassification.getCorpus().size()

        stats.each {label,number->
            println "${label}: ${(number/docsTotal)*100}"
        }
    }

}

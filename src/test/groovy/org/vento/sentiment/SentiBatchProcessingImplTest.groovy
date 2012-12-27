package org.vento.sentiment;


import gate.Annotation
import gate.Document
import gate.Factory
import gate.util.GateException
import groovy.xml.MarkupBuilder
import org.vento.sentiment.gate.GateBatchProcessing
import org.junit.Test
import org.junit.Ignore
import org.vento.sentiment.gate.SentiBatchProcessingImpl

/**
 * Created by IntelliJ IDEA.
 * User: Martin
 * Date: 06/04/2012
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public class SentiBatchProcessingImplTest {

    File gateHome
    GateBatchProcessing batchLearning
    //GateBatchProcessing batchClassification;
    //File gateHome = new File("/home/mpolojko/GATE_Developer_7.0");
    //File gateHome = new File("/opt/GATE_7.0");

    public void setUpLearning() throws URISyntaxException, IOException, GateException {

        gateHome = new File("/gateWorkspace/GATE_Developer_7.1");

        File gateConfigFile = new File(this.getClass().getResource("/gate-project-training/batch-learning.training.configuration.xml").toURI());

        String dataStore = this.getClass().getResource("/gate-project-training/temp-learning-datastore").toURI().toString();

        batchLearning = new SentiBatchProcessingImpl(gateHome, gateConfigFile, dataStore, "learningCorpus");
    }
/*
    public void setUpClassification() throws URISyntaxException, IOException, GateException {

        File gateConfigFile = new File(this.getClass().getResource("/gate-project-classification/batch-learning.classification.configuration.xml").toURI());

        String dataStore = this.getClass().getResource("/").toURI().toString() + "/temp_classification_datastore/";

        batchClassification = new SentiBatchProcessingImpl(gateHome, gateConfigFile, dataStore, "classificationCorpus");
    }
  */

    @Test
    public void testEnd2EndLearning() throws Exception {
        setUpLearning();

        //File corpusDirectory = new File("/home/mpolojko/Desktop/realLearningInput/");
        File corpusDirectory = new File(this.getClass().getResource("/gate-project-training/training-input/").toURI());
        //File corpusDirectory = new File("/home/lfoppiano/develop/bi/batch_learning_GATE_resources/realLearningInput");

        println "start loading"

        batchLearning.addAllToCorpus(corpusDirectory.toURI().toURL(), "xml")

        println "learning session started..."

        def before = System.currentTimeMillis()
        batchLearning.perform();
        def after = System.currentTimeMillis()

        println "learning session finished, took ${after - before} ms"
    }

    @Ignore("Not working this way of doing things... ahahahah")
    @Test
    public void testEnd2EndLearningSingular() throws Exception {
        setUpLearning();

        File corpusDirectory = new File("/Users/Martin/Desktop/realLearningInput/");
        //File corpusDirectory = new File("/home/lfoppiano/develop/bi/batch_learning_GATE_resources/realLearningInput");

        println "start loading"

        corpusDirectory.eachFile { file ->

            batchLearning.addToCorpus(file, "UTF-8", null)

        }

        println "learning session started..."

        def before = System.currentTimeMillis()
        batchLearning.perform();
        def after = System.currentTimeMillis()

        println "learning session finished, took ${after - before} ms"

    }
/*
    public void testClassificationOutput() throws Exception {
        setUpClassification()

        File corpusDirectory = new File("/Users/Martin/Desktop/twitter/")
        def classifiedEntriesOut = "/Users/Martin/Desktop/twitter/out/"

        println "start loading"

        batchClassification.addAllToCorpus(corpusDirectory.toURI().toURL(), "xml")

        println "classification session started..."

        def before = System.currentTimeMillis()
        batchClassification.perform();
        def after = System.currentTimeMillis()

        println "classification session finished, took ${after - before} ms"

        Iterator<Document> corpusIterator = batchClassification.getCorpus().iterator()

        while (corpusIterator.hasNext()) {

            Document tempDoc = corpusIterator.next()

            Iterator classificationScoreStr = tempDoc.getAnnotations("Output").get("Review").iterator()
            Annotation tempAnnotation = tempDoc.getAnnotations("Original markups").get("text").iterator().next()

            if (classificationScoreStr.hasNext()) {

                def classificationScore = classificationScoreStr.next().getFeatures().get("score")

                String content = tempDoc.getContent().getContent(tempAnnotation.getStartNode().getOffset(),
                        tempAnnotation.getEndNode().getOffset()).toString();

                def classifiedFile = new FileWriter(classifiedEntriesOut + "${classificationScore}/" + tempDoc.getName()[0..-7])
                def xml = new MarkupBuilder(classifiedFile)

                xml.'twit' {
                    text(content)
                    score(classificationScore)
                }
            }
        }
    }



    public void testEnd2EndClassification() throws Exception {
        setUpClassification()

        //['1.0','2.0','3.0'].each{

        //File corpusDirectory = new File(this.getClass().getResource("/gate-project-classification/classification-input/").toURI());
        File corpusDirectory = new File("/Users/Martin/Desktop/amazonReviewsSorted/quickTest/2.0/")

        def stats = ['correct': 0, 'positiveMiss': 0, 'negativeMiss': 0, 'notClassified': 0, 'oneOff': 0]


        println "start loading"

        batchClassification.addAllToCorpus(corpusDirectory.toURI().toURL(), "xml")
        //corpusDirectory.eachFile{file->

        //    batchClassification.addToCorpus(file,"UTF-8",null)

        //}

        println "classification session started..."

        def before = System.currentTimeMillis()
        batchClassification.perform();
        def after = System.currentTimeMillis()

        println "classification session finished, took ${after - before} ms"

        Iterator<Document> corpusIterator = batchClassification.getCorpus().iterator()
        while (corpusIterator.hasNext()) {
            Document tempDoc = corpusIterator.next()
            Annotation tempAnnotation = tempDoc.getAnnotations("Original markups").get("score").iterator().next()

            String originalScoreStr = tempDoc.getContent().getContent(tempAnnotation.startNode.getOffset(), tempAnnotation.endNode.getOffset()).toString()

            Iterator classificationScoreStr = tempDoc.getAnnotations("Output").get("Review").iterator()

            if (classificationScoreStr.hasNext()) {

                float originalScore = originalScoreStr.toFloat()

                float classificationScore = classificationScoreStr.next().getFeatures().get("score").toFloat()

                if (originalScore == classificationScore + 1 || originalScore == classificationScore - 1)
                    stats['oneOff']++
                else
                if (originalScore > classificationScore) stats['negativeMiss']++
                else if (originalScore < classificationScore) stats['positiveMiss']++
                else
                    stats['correct']++
            }
            else
                stats['notClassified']++

            Factory.deleteResource(tempDoc)
        }

        int docsTotal = batchClassification.getCorpus().size()

        stats.each {label, number ->
            println "${label}: ${(number / docsTotal) * 100}"
        }
        //}
    }
*/
}

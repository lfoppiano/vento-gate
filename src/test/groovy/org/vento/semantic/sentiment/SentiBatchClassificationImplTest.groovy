package org.vento.semantic.sentiment

import org.junit.Test
import org.vento.gate.GateBatchClassification

/**
 * Created with IntelliJ IDEA.
 * User: Martin
 * Date: 17/07/2012
 * Time: 23:17
 * To change this template use File | Settings | File Templates.
 */
class SentiBatchClassificationImplTest {

    GateBatchClassification batchClassification;

    public void initialize() {

        batchClassification = new SentiBatchClassificationImpl()
        batchClassification.setGateHome(new File("/Applications/GATE_Developer_7.0"))
        batchClassification.setGateConfigFile(new File(this.getClass().getResource("/gate-project-classification/batch-learning.classification.configuration.xml").toURI()))
        batchClassification.setCorpusName("classificationTestCorpus")
    }



    public void testSimpleClassify() {
          initialize()

          double score1 = batchClassification.simpleClassify(new File(this.getClass().getResource("/gate-project-classification/classification-input/output-1203292259570224.xml").toURI()),"UTF-8","text/xml")
          double score2 = batchClassification.simpleClassify(new File(this.getClass().getResource("/gate-project-classification/classification-input/output-1203292259570256.xml").toURI()),"UTF-8","text/xml")
          double score3 = batchClassification.simpleClassify(new File(this.getClass().getResource("/gate-project-classification/classification-input/output-1203292259570320.xml").toURI()),"UTF-8","text/xml")
          double score4 = batchClassification.simpleClassify(new File(this.getClass().getResource("/gate-project-classification/classification-input/output-1203292300280458.xml").toURI()),"UTF-8","text/xml")
          double score5 = batchClassification.simpleClassify(new File(this.getClass().getResource("/gate-project-classification/classification-input/output-1203292300310147.xml").toURI()),"UTF-8","text/xml")

          //double realScore = batchClassification.simpleClassify(new File("/Users/Martin/Desktop/twitter/out/1.0/twitter-output-1206300007110064.xml"),"UTF-8","text/xml")

          println score1
          println score2
          println score3
          println score4
          println score5
          //println realScore
    }

}

package org.vento.sentiment.statistical

import org.vento.sentiment.SimpleBatchClassification

/**
 * Created by IntelliJ IDEA.
 * User: Martin
 * Date: 06/04/2012
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */
public class SentiBatchClassificationSimpleImpl implements SimpleBatchClassification{

    private File positiveWordFile
    private File negativeWordFile
    private boolean initialized = false
    def positiveWordList = []
    def negativeWordList = []

    /* constructor not required
    public SentiBatchClassificationImpl(File gateHome, File projectConfigFile, String dataStoreDir, String corpusName) throws IOException, GateException {
        init(gateHome, projectConfigFile, dataStoreDir, corpusName);
    }
    */

    private void init() throws IOException {

        positiveWordFile?.eachLine{line->
            positiveWordList << line?.toLowerCase()//.split(" ")[1]?.split("\\.")[0]
        }
        negativeWordFile?.eachLine{line->
            negativeWordList << line?.toLowerCase()//.split(" ")[1]?.split("\\.")[0]
        }

        initialized = true
    }

    public double simpleClassify(String messageToClassify) throws IOException {

        def totalWordsCount = 0
        def negativeWordsCount = 0
        def positiveWordsCount = 0

        def classificationScore = 0.0

        if (!initialized) this.init()

        messageToClassify.split(' ').each{word->

            if (negativeWordList.contains(word)) negativeWordsCount++
            if (positiveWordList.contains(word)) positiveWordsCount++

            totalWordsCount++ //not used for now, might be interesting to compare percentages
        }

        if (negativeWordsCount > 0 && positiveWordsCount == 0) classificationScore = 1.0
        else
            if (negativeWordsCount == 0 && positiveWordsCount > 0) classificationScore = 3.0
           else
                classificationScore = 2.0

        return classificationScore;
    }

    public void setNegativeWordFile(String negativeWordFilePath) {
        this.negativeWordFile = new File(negativeWordFilePath);
    }

    public void setPositiveWordFile(String positiveWordFilePath) {
        this.positiveWordFile = new File(positiveWordFilePath);
    }
}

package org.vento.semantic.sentiment

import org.junit.Test
import org.vento.gate.SimpleBatchClassification
import org.vento.samantic.sentiment.SentiBatchClassificationSimpleImpl

/**
 * Created with IntelliJ IDEA.
 * User: Martin
 * Date: 17/07/2012
 * Time: 23:17
 * To change this template use File | Settings | File Templates.
 */
class SentiBatchClassificationSimpleImplTest {

    SimpleBatchClassification simpleBatchClassification

    public void initialize() {

        simpleBatchClassification = new SentiBatchClassificationSimpleImpl()
        simpleBatchClassification.setNegativeWordFile(this.getClass().getResource('negative_adj_list.txt').getPath())
        simpleBatchClassification.setPositiveWordFile(this.getClass().getResource('positive_adj_list.txt').getPath())
    }
    @Test
    public void testSimpleClassify() {
          initialize()
          assert simpleBatchClassification.simpleClassify("I saw a horrible movie today") == 1.0
          assert simpleBatchClassification.simpleClassify("I am very happy right now") == 3.0
          assert simpleBatchClassification.simpleClassify("RT : On this day in 1987, Saddam's regime attacked the Kurdish city of Serdet (Sardasht) with chemical weapons - 1000s parished") == 1.0
          assert simpleBatchClassification.simpleClassify("fridge magnet satu! RT : My euro trip is almost done. Just arrived at London from Amsterdam, so much fun") == 3.0
          assert simpleBatchClassification.simpleClassify("I went for a walk around the park") == 2.0
    }

}

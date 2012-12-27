package org.vento.sentiment;

import gate.util.GateException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: lfoppiano
 * Date: 4/6/12
 * Time: 11:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SimpleBatchClassification {

    public double simpleClassify(String toCheck) throws Exception;

}

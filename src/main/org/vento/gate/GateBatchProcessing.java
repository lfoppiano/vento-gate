package org.vento.gate;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: lfoppiano
 * Date: 4/6/12
 * Time: 11:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GateBatchProcessing {
    
    public void init(File gateConfigFile);
    
    public void loadExample(File example);

    public void loadExamples(File[] examples);

    public void perform();

}

package org.vento.semantic.sentiment;

import gate.*;
import gate.corpora.DocumentImpl;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.vento.gate.SimpleBatchClassification;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Martin
 * Date: 06/04/2012
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */
public class SentiBatchClassificationImpl implements SimpleBatchClassification{

    private CorpusController application;
    private Corpus documentCorpus;
    private File gateHome;
    private File gateConfigFile;
    private String corpusName;
    private boolean applicationInit = false;

    private void gateInit() throws GateException {

        Gate.setGateHome(gateHome);

        // initialise GATE - this must be done before calling any GATE APIs
        Gate.init();
    }

    private void appInit() throws GateException, IOException {

        //initialise application and new document corpus for this object
        documentCorpus = Factory.newCorpus(corpusName);

        // load the saved application
        application = (CorpusController) PersistenceManager.loadObjectFromFile(gateConfigFile);
        application.setCorpus(documentCorpus);

        this.applicationInit = true;
    }

    private void addToCorpus(File file, String encoding, String mimeType) throws MalformedURLException, GateException {

        String docName = file.getName() + "_" + Gate.genSym();
        FeatureMap params = Factory.newFeatureMap();

        params.put(Document.DOCUMENT_URL_PARAMETER_NAME, file.toURI().toURL());

        if(encoding != null)
            params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, encoding);

        if(mimeType != null)
            params.put(Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, mimeType);

        Document doc = (Document)Factory.createResource(DocumentImpl.class
                .getName(), params, null, docName);

        documentCorpus.add(doc);
        if(documentCorpus.getLRPersistenceId() != null) {
            // persistent corpus -> unload the document
            documentCorpus.unloadDocument(doc);
            Factory.deleteResource(doc);
        }

    }

    public double simpleClassify(String messageToClassify) throws IOException, GateException {

        double classificationScore = 0.0;

        if (!Gate.isInitialised()) gateInit();
        if (!this.applicationInit) appInit();

        File tempFile = new File("tempClassificationImp" + "_" + Gate.genSym() + ".xml");
        FileWriterWithEncoding tempWriter = new FileWriterWithEncoding(tempFile,"UTF-8");
        tempWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<twit>\n<text>" + messageToClassify + "</text>\n</twit>");
        tempWriter.flush();
        tempWriter.close();

        documentCorpus.clear();
        addToCorpus(tempFile,"UTF-8","text/xml");
        application.execute();

        Document classifiedDoc = documentCorpus.iterator().next();

        Iterator<Annotation> classificationScoreStr = classifiedDoc.getAnnotations("Output").get("Review").iterator();

        if (classificationScoreStr.hasNext()){

            classificationScore = Float.parseFloat((String)classificationScoreStr.next().getFeatures().get("score"));
        }

        tempFile.delete();
        return classificationScore;
    }

    //possible to implement but requires XML parser to add the <score> tag, to discuss
   /*
    public File classify(File file, String encoding, String mimeType) throws IOException, GateException {

        if (!Gate.isInitialised()) init();
        documentCorpus.clear();
        addToCorpus(file,encoding,mimeType);
        application.execute();

        Document classifiedDoc = documentCorpus.iterator().next();

        Iterator<Annotation> classificationScoreStr = classifiedDoc.getAnnotations("Output").get("Review").iterator();

        if (classificationScoreStr.hasNext()){

            root=xmlParser(file);
            score = new XMLElement()
            score.setText((String)classificationScoreStr.next().getFeatures().get("score"));
            root.addElement(score);
            fileWithScore << root
        }

        return fileWithScore;
    }
    */

    public File getGateHome() {
        return gateHome;
    }

    public void setGateHome(File gateHome) {
        this.gateHome = gateHome;
    }

    public File getGateConfigFile() {
        return gateConfigFile;
    }

    public void setGateConfigFile(File gateConfigFile) {
        this.gateConfigFile = gateConfigFile;
    }

    public String getCorpusName() {
        return corpusName;
    }

    public void setCorpusName(String corpusName) {
        this.corpusName = corpusName;
    }
}

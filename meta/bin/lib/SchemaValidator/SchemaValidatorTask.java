import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.EntityResolver;

import org.xml.sax.helpers.XMLReaderFactory;    

import org.apache.tools.ant.types.XMLCatalog;
import org.apache.tools.ant.types.DTDLocation;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;


/**
 * Checks XML files are valid. Validation occurs against XML shema or DTD.
 * The task uses SAX 2 parser implementation.
 *
 * @author Vladimir Kotov
 * @version 1.0
 */
public class SchemaValidatorTask extends Task {
    //  analyser
    protected static String vendorParserClass = "org.apache.xerces.parsers.SAXParser";

    private static String MSG_INIT_FAILED = "Could not start xml validation: ";
    // file to check
    protected String xmlURI = null;
    /**
     * Specify how parser errors are to be handled. 
     */
    protected boolean failOnError = true;

    protected XMLCatalog xmlCatalog = new XMLCatalog();

    protected String xsdSchema;

    public void setSchema(String schema) {
        xsdSchema = schema;
    }

    /**
     * Specify how parser errors are to be handled. 
     * If set to <code>true</code> (default), 
     * throw a <code>BuildException</code> if the parser yields an error.
     */
    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    /**
     * Specifies file to be checked.
     */
    public void setFile(String file) {
        xmlURI = file;
    }

    /**
     * Invokes when <code>xmlcatalog</code> inner tag is found.
     */
    public void addConfiguredXMLCatalog(XMLCatalog xmlCatalog) {
        this.xmlCatalog.addConfiguredXMLCatalog(xmlCatalog);
    }

    /**
     * Invoked when <code>dtd</code> inner tag is found.
     */
    public DTDLocation createDTD() {
        DTDLocation dtdLocation = new DTDLocation();
        xmlCatalog.addDTD(dtdLocation);
        return dtdLocation;
    }

    /**
     * Called by the project to let the task initialize properly.
     */
    public void init() throws BuildException {
        super.init();
        xmlCatalog.setProject(getProject());
    }

    /**
     * Returns <code>EntityResolver</code> which 
     * resolves <code>&lt;dtd&gt;</code> and <code>&lt;xmlcatalog&gt;</code> entities.
     */
    protected EntityResolver getEntityResolver() {
        return xmlCatalog;
    }

    /**
     * ValidatorErrorHandler roles are:
     * <ul>
     *    <li>Log SAX parse exceptions.</li>
     *    <li>Remember if an error occured.</li>
     * </ul>
     */
    class ValidatorErrorHandler implements ErrorHandler {
        private boolean failed = false;

        /**
         * @return <code>true</code> if an error happened during last parsing, 
         * <code>false</code> otherwise
         */
        public boolean isFailed() {
            return failed;
        }

        public void error(SAXParseException e) throws BuildException {
            failed = true;
            log(getInfo(e), Project.MSG_ERR);
        }

        public void fatalError(SAXParseException e) throws BuildException {
            failed = true;
            log(getInfo(e), Project.MSG_ERR);
        }

        public void warning(SAXParseException e) throws BuildException {
            log(getInfo(e), Project.MSG_WARN);
        }

        protected String getInfo(SAXParseException e) {
            String sysID = e.getSystemId();
            if (sysID != null) {
                try {
                    int line = e.getLineNumber();
                    int col = e.getColumnNumber();
                    return new URL(sysID).getFile() +
                        (line == -1 ? "" : (":" + line +
                                            (col == -1 ? "" : (":" + col)))) +
                        ": " + e.getMessage();
                } catch (MalformedURLException mfue) {}
            }
            return e.getMessage();
        }
    }

    /**
     * Executes this task. Called by the project to let the task do its work.
     */
    public void execute() throws BuildException {
        XMLReader reader;
        try {
            reader = XMLReaderFactory.createXMLReader(vendorParserClass);
        } catch (SAXException e) {
            throw new BuildException(MSG_INIT_FAILED + xmlURI, e);
        }
        ValidatorErrorHandler errorHandler = new ValidatorErrorHandler();       
        reader.setEntityResolver(getEntityResolver());
        reader.setErrorHandler(errorHandler);
        try {
            File f = new File(xsdSchema);
            final URL url;
            try {
                url = f.toURL();
            } catch (MalformedURLException e) {
                throw new BuildException("Could not costruct URL for schema location: " + e.getMessage());
            }

            reader.setFeature("http://xml.org/sax/features/namespaces", true);
            reader.setFeature("http://xml.org/sax/features/validation", true);
            reader.setFeature("http://apache.org/xml/features/validation/schema", true);
            reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
            reader.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", url.toString());

            //reader.setFeature("http://apache.org/xml/features/validation/dynamic", true);
            //reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
            // reader.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", xsdSchema);
        } catch (SAXNotRecognizedException e) {
            throw new BuildException(MSG_INIT_FAILED + xmlURI, e);
        } catch (SAXNotSupportedException e) {
            throw new BuildException(MSG_INIT_FAILED + xmlURI, e);
        }
        InputSource inputSource = new InputSource(xmlURI);
        try {
            reader.parse(inputSource);
        } catch (SAXException e) {
            throw new BuildException("Could not validate document: " + xmlURI + ": " + e.getMessage());
        } catch (IOException e) {
            throw new BuildException("Could not validate document: " + xmlURI + ": " + e.getMessage());
        }
        if (errorHandler.isFailed()) {
            if (failOnError) {
                throw new BuildException(xmlURI + " is not a valid XML document.");
            } else {
                log(xmlURI + " is not a valid XML document", Project.MSG_ERR);
            }
        }
        log("File have been successfully validated.");
    }
}

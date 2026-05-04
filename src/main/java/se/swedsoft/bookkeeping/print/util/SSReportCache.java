package se.swedsoft.bookkeeping.print.util;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlTemplateLoader;
import org.fribok.bookkeeping.app.Path;

import java.io.*;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @version $Id: SSReportCache.java 243 2025-06-26 13:14:31Z ellefj $
 *
 */
public class SSReportCache {
    private static final File REPORT_DIR = new File(Path.get(Path.APP_DATA), "report");
    private static final File COMPILED_DIR = new File(REPORT_DIR, "compiled");
    private static final String REPORT_RESOURCE = "/reports/report/";
    private static final String DATE_TIME_FORMAT_NO_MS = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    // The report cache with compiled report definitions.
    private Map<String, AbstractMap.SimpleEntry<Date,JasperDesign>> iReportCache;

    // our instance
    private static SSReportCache cInstance;

    /**
     * Get the instance of this class
     * @return The instance
     */
    public static SSReportCache getInstance() {
        if (cInstance == null) {
            cInstance = new SSReportCache();
        }
        return cInstance;
    }

    /**
     *
     */
    private SSReportCache() {
        iReportCache = new HashMap<String, AbstractMap.SimpleEntry<Date,JasperDesign>>();
    }

    /**
     * This function will load a report, either from the runtime cache or
     * from the report source.
     *
     * @param pReportName The name of the report to load, ie vatcontrol.jrxml.
     *
     * @return The JasperDesign object
     */
    public JasperDesign getReport(String pReportName) {
        AbstractMap.SimpleEntry<Date,JasperDesign> KVP = iReportCache.get(pReportName);
        String iReportResourceName = REPORT_RESOURCE + pReportName;
        URL url;
        Date iReportDate = new Date();
        try {
        	url = getClass().getResource(iReportResourceName);
        	iReportDate = new Date(url.openConnection().getLastModified());
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        
        if (KVP == null) {
            try {
                System.out.printf("Using report resource %s from archive ...\n", iReportResourceName);
                KVP = new AbstractMap.SimpleEntry<>(iReportDate, 
                JRXmlLoader.load(getClass().getResourceAsStream(iReportResourceName)));
                iReportCache.put(pReportName, KVP);
                return KVP.getValue();
            } catch (JRException ex) {
                ex.printStackTrace();
            }
        }
        Date iInternalDate = KVP.getKey();
        if (iInternalDate.compareTo(iReportDate) >= 0) {
            System.out.printf("Using cached report %s...\n", pReportName);
            return KVP.getValue();
        }

        try {
            System.out.printf("Using report resource %s from archive ...\n", iReportResourceName);
            KVP = new AbstractMap.SimpleEntry<>(iReportDate, 
            JRXmlLoader.load(getClass().getResourceAsStream(iReportResourceName)));
            iReportCache.put(pReportName, KVP);
            return KVP.getValue();
        } catch (JRException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public JRTemplate getStyleTemplate() {
        return JRXmlTemplateLoader.load(getClass().getResourceAsStream(REPORT_RESOURCE + "styles.jrtx"));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.util.SSReportCache");
        sb.append("{iReportCache=").append(iReportCache);
        sb.append('}');
        return sb.toString();
    }
}

package com.arg.swu.docx;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.docx4j.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arg.swu.commons.ApiConstant;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author niwatroongroj
 * 
 */
public class DocxGenerator implements ApiConstant {
    protected static Logger LOG = LoggerFactory.getLogger(DocxGenerator.class);

    protected HashMap<String, String> params = new HashMap<>();
    protected String fileTemplate;
    
    private XWPFDocument doc;

    public DocxGenerator(DocxGeneratorBuilder builder){
        // this.params = builder.params;
        this.fileTemplate = builder.fileTemplate;
    }

    public DocxGenerator exec() throws InvalidFormatException, IOException{
        LOG.info("Resources loading....{}", this.fileTemplate);
        InputStream raw = ResourceUtils.getResource(this.fileTemplate);
        this.doc = new XWPFDocument(OPCPackage.open(raw));

        for (XWPFParagraph p : doc.getParagraphs()) {
            replace2(p, this.params);
        }
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        replace2(p, this.params);
                    }
                }
            }
        }
        
        return this;
    }

    private void replace2(XWPFParagraph p, Map<String, String> data) {
	    String pText = p.getText(); // complete paragraph as string
	    if (pText.contains("${")) { // if paragraph does not include our pattern, ignore
	//        TreeMap<Integer, XWPFRun> p;
	        TreeMap<Integer, XWPFRun> posRuns = getPosToRuns(p);
	        Pattern pat = Pattern.compile("\\$\\{(.+?)\\}");
	        Matcher m = pat.matcher(pText);
	        while (m.find()) { // for all patterns in the paragraph
	            String g = m.group(1);  // extract key start and end pos
	            int s = m.start(1);
	            int e = m.end(1);
	            String key = g;
	            String x = data.get(key);
	            if (x == null)
	                x = "";
	            SortedMap<Integer, XWPFRun> range = posRuns.subMap(s - 2, true, e + 1, true); // get runs which contain the pattern
	            boolean found1 = false; // found $
	            boolean found2 = false; // found {
	            boolean found3 = false; // found }
	            XWPFRun prevRun = null; // previous run handled in the loop
	            XWPFRun found2Run = null; // run in which { was found
	            int found2Pos = -1; // pos of { within above run
	            for (XWPFRun r : range.values())
	            {
	                if (r == prevRun)
	                    continue; // this run has already been handled
	                if (found3)
	                    break; // done working on current key pattern
	                prevRun = r;
	                for (int k = 0;; k++) { // iterate over texts of run r
	                    if (found3)
	                        break;
	                    String txt = null;
	                    try {
	                        txt = r.getText(k); // note: should return null, but throws exception if the text does not exist
	                    } catch (Exception ex) {
	
	                    }
	                    if (txt == null)
	                        break; // no more texts in the run, exit loop
	                    if (txt.contains("$") && !found1) {  // found $, replace it with value from data map
	                        txt = txt.replaceFirst("\\$", x);
	                        found1 = true;
	                    }
	                    if (txt.contains("{") && !found2 && found1) {
	                        found2Run = r; // found { replace it with empty string and remember location
	                        found2Pos = txt.indexOf('{');
	                        txt = txt.replaceFirst("\\{", "");
	                        found2 = true;
	                    }
	                    if (found1 && found2 && !found3) { // find } and set all chars between { and } to blank
	                        if (txt.contains("}"))
	                        {
	                            if (r == found2Run)
	                            { // complete pattern was within a single run
	                                txt = txt.substring(0, found2Pos)+txt.substring(txt.indexOf('}'));
	                            }
	                            else // pattern spread across multiple runs
	                                txt = txt.substring(txt.indexOf('}'));
	                        }
	                        else if (r == found2Run) // same run as { but no }, remove all text starting at {
	                            txt = txt.substring(0,  found2Pos);
	                        else
	                            txt = ""; // run between { and }, set text to blank
	                    }
	                    if (txt.contains("}") && !found3) {
	                        txt = txt.replaceFirst("\\}", "");
	                        found3 = true;
	                    }
	                    r.setText(txt, k);
	                }
	            }
	        }
	    }
	
	}

	private TreeMap<Integer, XWPFRun> getPosToRuns(XWPFParagraph paragraph) {
	    int pos = 0;
	    TreeMap<Integer, XWPFRun> map = new TreeMap<Integer, XWPFRun>();
	    for (XWPFRun run : paragraph.getRuns()) {
	        String runText = run.text();
	        if (runText != null && runText.length() > 0) {
	            for (int i = 0; i < runText.length(); i++) {
	                map.put(pos + i, run);
	            }
	            pos += runText.length();
	        }
	
	    }
	    return map;
	}

    public void writeToFile(String fileName) throws FileNotFoundException, IOException{
        doc.write(new FileOutputStream(fileName));
    }
    
    public byte[] exportByteArray() throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.doc.write(out);
        out.close();
        this.doc.close();
        return out.toByteArray();
    }
    
    private StringBuilder replace(String wmlTemplateString, StringBuilder strB,
	                                     java.util.Map<String, ?> mappings) {
                                            if(StringUtils.isBlank(wmlTemplateString)) return null;
		int offset = 0;
		while (true) {

			int startKey = wmlTemplateString.indexOf("${", offset);
			if (startKey == -1)
				return strB.append(wmlTemplateString.substring(offset));
			else {
				strB.append(wmlTemplateString.substring(offset, startKey));
				int keyEnd = wmlTemplateString.indexOf('}', startKey);
                LOG.info("key...{} ---> {} , {}",wmlTemplateString, startKey, keyEnd);
				String key = wmlTemplateString.substring(startKey + 2, keyEnd);
				Object val = mappings.get(key);
				if (val == null) {
					LOG.warn("Invalid key '" + key + "' or key not mapped to a value");
					strB.append(key);
				} else {
					strB.append(val.toString());
				}
				offset = keyEnd + 1;
			}
		}
	}

    @Getter
    @Setter
    public static class DocxGeneratorBuilder {
        // private HashMap<String,String> params;
        private String fileTemplate;
        public DocxGeneratorBuilder(String fileTemplate) {
            // this.params = params;
            this.fileTemplate = fileTemplate;
        }

        public DocxGenerator build(){
            return new DocxGenerator(this);
        }
    }

     
}
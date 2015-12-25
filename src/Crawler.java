import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.fa.PersianAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.regex.Pattern;


/**
 * Created by dell on 2015-12-12.
 */
public class Crawler extends WebCrawler {
    private IndexWriter writer;
    public int counter ;
    public int sholudVisitCounter;

    public Crawler() throws IOException {
        this.counter = 0;
        this.sholudVisitCounter = 0 ;
        Directory indexDir = FSDirectory.open(Paths.get("D:\\Indexed_files"));
        Analyzer analyzer = new PersianAnalyzer();
        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        cfg.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        writer = new IndexWriter(indexDir, cfg);
    }

    public boolean shouldVisit( WebURL url) {    //What pages should visit in a single page? All?? absolutely nots
        String href = url.getURL();

        try {
            boolean should = false;
            String real_href = java.net.URLDecoder.decode(href, "UTF-8");

            if( href.startsWith("http://www.e-estekhdam.com/") ){
                System.out.println("in estekh");
                Pattern filter = Pattern.compile("^http://www.e-estekhdam.com/استخدام.*");
                if(filter.matcher(real_href).matches() && !real_href.contains("comment") && !real_href.contains("replytocom")) {
                    sholudVisitCounter++;
                    System.out.println("matchess");
                    System.out.println("shouldVisitCounterForEes: " + sholudVisitCounter);
                    System.out.println("realhref:" + real_href);
                    should = true;
                }
            }
            //System.out.println("real: " + real_href);
            else if( href.startsWith("http://dehvand.ir") &&  real_href.length() > 24 && real_href.length() < 27 ){
                if(real_href.substring(18, 24).matches("-?\\d+(\\.\\d+)?")) {
                    sholudVisitCounter++;
                    System.out.println("shouldVisitCounter: "+ sholudVisitCounter);
                    should = true;
                }
            }
            return should;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }

    }
    public void visit(Page page) {   //Indexing what it sees on the page as title contetn , etc
        String real_href = null;
        try {
            real_href = java.net.URLDecoder.decode(page.getWebURL().toString(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HtmlParser parser = new HtmlParser();
        Document doc = null;
        try {
            doc = Jsoup.connect(page.getWebURL().toString()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert real_href != null;
        if( real_href.startsWith("http://www.e-estekhdam.com/") ) {
            System.out.println("hrefees:" + real_href );
            String title = parser.get_title(doc, "e-estekhdam");
            String body = parser.get_content(doc,"e-estekhdam");
            String date = parser.get_date(doc, "e-estekhdam");
            System.out.println("titleees: " + title);
            if (!title.isEmpty() && !body.isEmpty() && !date.isEmpty()) {
                counter++;
//                System.out.println("counter is: " + counter);
                System.out.println("title ees: " + title);
                org.apache.lucene.document.Document l_doc = get_ldocument(title, body, date);
                try {
                    if(sholudVisitCounter >= counter + 150) {
                        writer.addDocument(l_doc);
                        System.out.println("doc added!");
                        System.out.println("eesCounter: " + counter);
                        System.out.println("eesShouldCounter: " + sholudVisitCounter);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
                sholudVisitCounter--;
            if(sholudVisitCounter == counter+150){
                try {
                    System.out.println("writer closed at shouldCounter: " + sholudVisitCounter + " and Counter:" + counter);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(real_href.startsWith("http://dehvand.ir")){
            counter++;
            String title = parser.get_title(doc, "dehvand");
            String body = parser.get_content(doc,"dehvand");
            String date = parser.get_date(doc, "dehvand");
           // System.out.println("dehvand title: " + title);
            //System.out.println("sholudVisitCounterDHVN: " + sholudVisitCounter);
            if( !title.isEmpty() || !body.isEmpty() || !date.isEmpty() ){
                org.apache.lucene.document.Document l_doc = get_ldocument(title, body, date);
                try {
                    writer.addDocument(l_doc);
                    System.out.println("doc added in dehvand!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                sholudVisitCounter--;
            }

            // System.out.println("dehvand body: (out) " + body);
            // System.out.println("dehvand date: (out) " + date);
            System.out.println("counter: " + counter);
        }

    }

    public org.apache.lucene.document.Document get_ldocument(String title, String body, String date) {
        org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new TextField("body", body, Field.Store.YES));
        doc.add(new TextField("date", date, Field.Store.YES));
        return doc;
    }
}
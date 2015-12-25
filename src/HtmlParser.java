import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.nio.channels.CancelledKeyException;

/**
 * Created by dell on 2015-12-20.
 */
public class HtmlParser {

    public String get_title(Document doc, String site){
        Element title = null;
        switch (site){
            case "e-estekhdam":
                Elements elems = doc.getElementsByClass("entry-title");
                if(elems.size() < 1){
                    return null;
                }
                for ( Element elem : elems ){
                    if( elem.getElementsByTag("a") != null){
                        title =  elem.getElementsByTag("a").get(0);
                    }
                }
            break;
            case "dehvand":
               // Element dehvand_title = doc.select("div#wrapper  div#main  div.post single-post  div.posts  h1  a").first();
                //System.out.println(dehvand_title.text());
                title = doc.getElementsByTag("h1").first();
                //System.out.println("devand title: "+ dehvand_title.text());
                break;
            case "mazand":
                Element mazand_elems = doc.getElementsByClass("detail-view").first();
                title = mazand_elems.select("tr").first().select("td").first();
               // title = doc.
        }
        return title != null ? title.text() : null;
    }


    public String get_content(Document doc, String site){
        String content = "";
        switch (site) {
            case "e-estekhdam":
                Elements result_ees = doc.getElementsByClass("entry-content");
                if(result_ees.size()<1){
                    content=null;
                }else{
                    content = result_ees.text();
                }
                //System.out.println("Entry Content: " + content);
                break;
            case "dehvand":
                Elements result_deh = doc.select("div#wrapper > div#main > div.post.single-post > div.posts > div.excerpt.single-excerpt > p");
                if(result_deh.size()<1){
                    content = null;
                }else{
                    content = result_deh.text();
                }
                //System.out.println("dehvand cnt: " + result_deh.text());
                break;
            case "mazand":
                content = doc.getElementsByClass("detail-view").first().text();
                break;
        }
        return content;
    }

    public String get_date(Document doc, String site){
        String date="";
        switch (site){
            case "e-estekhdam":
                Element elem_ees = doc.getElementsByTag("time").get(0);
                date = elem_ees.attr("datetime").substring(0,10);
                //System.out.println("Dateis: " + date);
                break;
            case "dehvand":
                Elements res_deh = doc.select("span[itemprop=datePosted]");
                if(res_deh.size() < 1){
                    date = null;
                }else {
                    date = res_deh.attr("content").substring(0, 10);
                }
                //System.out.println("deh date: " + date);
                break;
            case "mazand":
                Element mazand_elems = doc.getElementsByClass("detail-view").first();
                date = mazand_elems.select("tr").get(14).select("td").first().text();
        }
        return date;
    }

}

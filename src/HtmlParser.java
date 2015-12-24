import com.ghasemkiani.util.icu.PersianCalendar;
import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.nio.channels.CancelledKeyException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dell on 2015-12-20.
 */
public class HtmlParser {

    public String get_title(Document doc, String site){
        Element title = null;
        switch (site){
            case "irkaryabi":
                Element jobTitle_irkaryabi = doc.getElementsByClass("topic").get(0).getElementsByTag("a").get(0);
                if(jobTitle_irkaryabi != null) {
                    title = jobTitle_irkaryabi;
                }
                else
                    title = null;
                break;
            case "karbank":
                Element jobTitle_karbank = doc.getElementById("jobTitle");
                if(jobTitle_karbank != null){
                    title = jobTitle_karbank;
                }
                else
                    title = null;
                break;
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
            case "irkaryabi":
                Element jobContent_karyabi = doc.select("div.summary").get(0);
                if (jobContent_karyabi != null ){
                    content = jobContent_karyabi.text();
                }
                else
                    content = null;
                break;
            case "karbank":
                Elements jobContent_karbank = doc.select("div.ls15");
                if(jobContent_karbank != null){
                    content = jobContent_karbank.text();
                }else
                content = null;
                break;
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
            case "irkaryabi":
                PersianCalendar persianCalendar = new PersianCalendar(new Date());
                String y = String.valueOf(persianCalendar.get(Calendar.YEAR));
                String m = String.valueOf(persianCalendar.get(Calendar.MONTH) + 1);
                String d = String.valueOf(persianCalendar.get(Calendar.DAY_OF_MONTH));
                date = y + "-" + m + "-" + d;
                break;
            case "karbank":
                Elements dateJob = doc.select("div#jobDetail > div");
                for (Element elem_k : dateJob){
                    if(elem_k.text().equals("تاريخ انتشار آگهی")){
                        date = elem_k.nextElementSibling().text();
                    }
                }
                break;
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
    public String get_numeric_date(String date){
        Pattern filter = Pattern.compile("^(\\d+) (.*) (\\d+)");
        String date1 = "16 آذر 1394";
        Matcher m = filter.matcher(date1);
        String real_time = null;
        System.out.println("datee in f: " + date1);
        if(m.find()){
//            System.out.println("first: " +m.group(0));
//            System.out.println("firstt: " +m.group(1));
//            System.out.println("secondd: " +m.group(2));
//            System.out.println("thirdd: " +m.group(3));
            String day = m.group(1);
            String year = m.group(3);
            String month = m.group(2);
            switch (month){
                case "فروردین":
                    month = "1";
                    break;
                case "اردیبهشت":
                    month = "2";
                    break;
                case "خرداد":
                    month = "3";
                    break;
                case "تیر":
                    month = "4";
                    break;
                case "مرداد":
                    month = "5";
                    break;
                case "شهریور":
                    month = "6";
                    break;
                case "مهر":
                    month = "7";
                    break;
                case "آبان":
                    month = "8";
                    break;
                case "آذر":
                    month = "9";
                    break;
                case "دی":
                    month = "10";
                    break;
                case "بهمن":
                    month = "11";
                    break;
                case "اسفند":
                    month = "12";
                    break;
            }
            real_time = year + "-" + month + "-" + day;
        }
        return real_time;
    }
}

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
        String title = null;
        switch (site){
            case "gilantabligh":
                Element jobTitle_gilan = doc.getElementsByClass("entry-title").get(0);
                if(jobTitle_gilan != null){
                    title = jobTitle_gilan.text();
                }
                break;
            case "mikhkub":
                doc.getElementsByClass("h3_job_title").get(0).getElementsByClass("job_title_desc").remove();
                Element jobTitle_mikhkub1 = doc.getElementsByClass("h3_job_title").get(0);
                if(jobTitle_mikhkub1 != null){
                    title = jobTitle_mikhkub1.text();
                }
                else
                    title = null;
                break;
            case "irkaryabi":
                Element jobTitle_irkaryabi = doc.getElementsByClass("topic").get(0).getElementsByTag("a").get(0);
                if(jobTitle_irkaryabi != null) {
                    title = jobTitle_irkaryabi.text();
                }
                else
                    title = null;
                break;
            case "karbank":
                Element jobTitle_karbank = doc.getElementById("jobTitle");
                if(jobTitle_karbank != null){
                    title = jobTitle_karbank.text();
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
                        title =  elem.getElementsByTag("a").get(0).text();
                    }
                }
            break;
            case "dehvand":
               // Element dehvand_title = doc.select("div#wrapper  div#main  div.post single-post  div.posts  h1  a").first();
                //System.out.println(dehvand_title.text());
                title = doc.getElementsByTag("h1").first().text();
                //System.out.println("devand title: "+ dehvand_title.text());
                break;
            case "mazand":
                Element mazand_elems = doc.getElementsByClass("detail-view").first();
                title = mazand_elems.select("tr").first().select("td").first().text();
               // title = doc.
                break;
            case "niazerooz":
                Element jobTitle_niazerooz = doc.getElementsByClass("ads-title").get(0).getElementsByTag("h1").get(0);
                if (jobTitle_niazerooz != null){
                    title = jobTitle_niazerooz.text();
                }
                break;
            case "harjaee":
                Element jobTitle_harjaee = doc.select("div#container > div#content > h1").get(0);
                if (jobTitle_harjaee != null){
                    title = jobTitle_harjaee.text();
                }
                break;
            case "ahwniaz":
                Element jobTitle_ahwniaz = doc.select("div.ad-body > h1").get(0);
                if (jobTitle_ahwniaz != null){
                    title = jobTitle_ahwniaz.text();
                }
                break;
        }
        return title;
    }


    public String get_content(Document doc, String site){
        String content = null;
        switch (site) {
            case "gilantabligh":
                Element jobContent_gilan = doc.getElementsByClass("entry-content").get(0);
                if(jobContent_gilan != null){
                    content = jobContent_gilan.text();
                }
                break;
            case "mikhkub":
                Element jobContent_mikhkub =  doc.select("div.matnjob").first();
                if(jobContent_mikhkub != null){
                    content = jobContent_mikhkub.text();
                }
                else
                    content = null;
                break;
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
            case "niazerooz":
                Element jobContent_niazerooz = doc.getElementsByClass("discription").get(0);
                if (jobContent_niazerooz != null){
                    content = jobContent_niazerooz.text();
                }
                break;
            case "harjaee":
                Element jobContent_harjaee = doc.select("div.center > div").get(2);
                if(jobContent_harjaee != null){
                    content = jobContent_harjaee.text();
                }
                break;
            case "ahwniaz":
                Element jobContent_ahwniaz = doc.getElementsByClass("matn").get(1);
                if (jobContent_ahwniaz != null){
                    content = jobContent_ahwniaz.text();
                }
                break;
        }
        return content;
    }

    public String get_date(Document doc, String site){
        String date = null;
        switch (site){
            case "gilantabligh":
                PersianCalendar persianCalendar_gilan = new PersianCalendar(new Date());
                String y_gilan = String.valueOf(persianCalendar_gilan.get(Calendar.YEAR));
                String m_gilan = String.valueOf(persianCalendar_gilan.get(Calendar.MONTH) + 1);
                String d_gilan = String.valueOf(persianCalendar_gilan.get(Calendar.DAY_OF_MONTH));
                date = y_gilan + "-" + m_gilan + "-" + d_gilan;
                break;
            case "mikhkub":
                PersianCalendar persianCalendar1 = new PersianCalendar(new Date());
                String y1 = String.valueOf(persianCalendar1.get(Calendar.YEAR));
                String m1 = String.valueOf(persianCalendar1.get(Calendar.MONTH) + 1);
                String d1 = String.valueOf(persianCalendar1.get(Calendar.DAY_OF_MONTH));
                date = y1 + "-" + m1 + "-" + d1;
                break;
            case "irkaryabi":
                PersianCalendar persianCalendar2 = new PersianCalendar(new Date());
                String y2 = String.valueOf(persianCalendar2.get(Calendar.YEAR));
                String m2 = String.valueOf(persianCalendar2.get(Calendar.MONTH) + 1);
                String d2 = String.valueOf(persianCalendar2.get(Calendar.DAY_OF_MONTH));
                date = y2 + "-" + m2 + "-" + d2;
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
                break;
            case "niazerooz":
                PersianCalendar persianCalendar_niazerooz = new PersianCalendar(new Date());
                String y_niazerooz = String.valueOf(persianCalendar_niazerooz.get(Calendar.YEAR));
                String m_niazerooz = String.valueOf(persianCalendar_niazerooz.get(Calendar.MONTH) + 1);
                String d_niazerooz = String.valueOf(persianCalendar_niazerooz.get(Calendar.DAY_OF_MONTH));
                date = y_niazerooz + "-" + m_niazerooz + "-" + d_niazerooz;
                break;
            case "harjaee":
                PersianCalendar persianCalendar_harjaee = new PersianCalendar(new Date());
                String y_harjaee = String.valueOf(persianCalendar_harjaee.get(Calendar.YEAR));
                String m_harjaee = String.valueOf(persianCalendar_harjaee.get(Calendar.MONTH) + 1);
                String d_harjaee = String.valueOf(persianCalendar_harjaee.get(Calendar.DAY_OF_MONTH));
                date = y_harjaee + "-" + m_harjaee + "-" + d_harjaee;
                break;
            case "ahwniaz":
                PersianCalendar persianCalendar_ahwniaz = new PersianCalendar(new Date());
                String y_ahwniaz = String.valueOf(persianCalendar_ahwniaz.get(Calendar.YEAR));
                String m_ahwniaz = String.valueOf(persianCalendar_ahwniaz.get(Calendar.MONTH) + 1);
                String d_ahwniaz = String.valueOf(persianCalendar_ahwniaz.get(Calendar.DAY_OF_MONTH));
                date = y_ahwniaz + "-" + m_ahwniaz + "-" + d_ahwniaz;
                break;
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

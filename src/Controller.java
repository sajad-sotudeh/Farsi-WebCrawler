import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.log4j.BasicConfigurator;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        String crawlStorageFolder = "D:\\crawler4j";
        int numberOfCrawlers = 1;
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);

    /*
     * Instantiate the controller for this crawl.
     */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        config.setMaxDepthOfCrawling(5);
        config.setMaxPagesToFetch(1000);

    /*
     * For each crawl, you need to add some seed urls. These are the first
     * URLs that are fetched and then the crawler starts following links
     * which are found in these pages
     */

       // controller.addSeed("http://www.e-estekhdam.com");

        List<String> url_list = new ArrayList<String>();
        for (int i=1; i<25; i++) {
            String mem = "http://mazandkar.ir/index.php/site/index?Addjob_page=" + i + "&language=fa";
            url_list.add(mem);
        }

                //,"http://dehvand.ir/","http://www.e-estekhdam.com/"};
        for(String url : url_list) {
            System.out.println(url + " START");
            controller.addSeed(url);
            System.out.println(url + " END");
        }
//        controller.addSeed("http://dehvand.ir/");
//        System.out.println("CRAWLER for estekhdam START");
//        controller.addSeed("http://www.e-estekhdam.com");
//        System.out.println("CRAWLER for estekhdam FINISH");


    /*
     * Start the crawl. This is a blocking operation, meaning that your code
     * will reach the line after this only when crawling is finished.
     */
        controller.start(Crawler.class, numberOfCrawlers);
    }
}
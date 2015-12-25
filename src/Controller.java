import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.log4j.BasicConfigurator;

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
        config.setMaxDepthOfCrawling(2);
        config.setMaxPagesToFetch(1000);

    /*
     * For each crawl, you need to add some seed urls. These are the first
     * URLs that are fetched and then the crawler starts following links
     * which are found in these pages
     */

       // controller.addSeed("http://www.e-estekhdam.com");

        String urls[] = {"http://dehvand.ir/","http://www.e-estekhdam.com/"};
        for(String url : urls) {
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
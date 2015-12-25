import org.apache.lucene.analysis.fa.PersianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by dell on 2015-12-23.
 */
public class SearchUI {
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("SAlam");
        Scanner in = new Scanner(System.in);
        String title = in.nextLine();
        IndexReader rdr =  DirectoryReader.open(FSDirectory.open(Paths.get("D:\\Indexed_files")));
        IndexSearcher is = new IndexSearcher(rdr);
        QueryParser parser_body =  new QueryParser("title", new PersianAnalyzer());
        Query q = parser_body.parse(title);
        TopDocs hits = is.search(q,200);
        for (ScoreDoc scoreDoc: hits.scoreDocs){
            Document doc = is.doc(scoreDoc.doc);
            System.out.println("++++++++++++++++++++++: ");
            System.out.println("Title: "+ doc.get("title") );
            System.out.println("Body: "+ doc.get("body") );
            System.out.println("Date: "+ doc.get("date") );
            System.out.println("++++++++++++++++++++++: ");
        }
    }
}
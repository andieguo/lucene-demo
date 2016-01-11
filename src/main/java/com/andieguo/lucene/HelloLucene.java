package com.andieguo.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;
/**
 * 参考：http://www.lucenetutorial.com/lucene-in-5-minutes.html
 * @author andieguo
 *
 */
public class HelloLucene {
  public static void main(String[] args) throws IOException, ParseException {
	  
	//0 实例化一个StandardAnalyzer，用于建立索引和查询
    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_42);
    
    // 1、创建index，为字符串创建内存索引
    Directory index = new RAMDirectory();
    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_42, analyzer);
    IndexWriter w = new IndexWriter(index, config);
    addDoc(w, "Lucene in Action", "193398817");
    addDoc(w, "Lucene for Dummies", "55320055Z");
    addDoc(w, "Managing Gigabytes", "55063554A");
    addDoc(w, "The Art of Computer Science", "9900333X");
    addDoc(w, "Lucene in Action", "193398817");
    addDoc(w, "Lucene for Dummies", "55320055Z");
    addDoc(w, "Managing Gigabytes", "55063554A");
    addDoc(w, "The Art of Computer Science", "9900333X");
    addDoc(w, "Lucene in Action", "193398817");
    addDoc(w, "Lucene for Dummies", "55320055Z");
    addDoc(w, "Managing Gigabytes", "55063554A");
    addDoc(w, "The Art of Computer Science", "9900333X");
    w.close();

    // 2. 创建query对象
    String querystr = args.length > 0 ? args[0] : "Art";
    Query q = new QueryParser(Version.LUCENE_42, "title", analyzer).parse(querystr);

    // 3. 查询。我们创建一个Searcher对象并且使用上面创建的Query对象来进行搜索，匹配到的前10个结果封装在TopScoreDocCollector对象里返回。
    int hitsPerPage = 10;
    IndexReader reader = DirectoryReader.open(index);
    IndexSearcher searcher = new IndexSearcher(reader);
    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
    searcher.search(q, collector);
    ScoreDoc[] hits = collector.topDocs().scoreDocs;
    
    // 4. 显示查询结构
    System.out.println("Found " + hits.length + " hits.");
    for(int i=0;i<hits.length;++i) {
      int docId = hits[i].doc;
      Document d = searcher.doc(docId);
      System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
    }

    // 5、关闭资源
    reader.close();
  }

  /**
   * addDoc()方法把Document类的实例添加到索引中
   * 注意:对于需要分词的内容我们使用TextField，对于像id这样不需要分词的内容我们使用StringField。
   * @param w
   * @param title
   * @param isbn
   * @throws IOException
   */
  private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
    Document doc = new Document();
    doc.add(new TextField("title", title, Field.Store.YES));
    doc.add(new StringField("isbn", isbn, Field.Store.YES));
    w.addDocument(doc);
  }

}

package com.holly.analyzer.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
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
import org.junit.Test;


public class TestApI {
	private static Directory dic = null;
	static{
		try {
			dic = FSDirectory.open(Paths.get(new URI("D:/lucene/index02")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 创建索引 (步骤)
	 * 1 创建directory
	 * 2创建indexWriter
	 * 3 创建Document
	 * 4 添加Field
	 * 5向indexWriter中添加document
	 */
	@Test
	public void index(){
		//Directory directory = new RAMDirectory();//创建内存目录
		IndexWriter indexWriter=null;
		try {
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
			indexWriter = new IndexWriter(dic, indexWriterConfig);
			Document doc = null;
			File file = new File("C:/Users/Ye/Documents/temp");
			File[] files = file.listFiles();
			for(File f :files){
				doc = new Document();
				doc.add(new TextField("file",new FileReader(f)));
				doc.add(new StringField("fileName",f.getName(),Store.YES));
				doc.add(new StringField("filepath",f.getAbsolutePath(),Store.YES));
				indexWriter.addDocument(doc);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(indexWriter!=null){
				try {
					indexWriter.close();
				} catch (CorruptIndexException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	/**
	 * 搜索(步骤)
	 * 1 创建directory
	 * 2 创建indexReader
	 * 3 通过indexReaderr创建indexSearcher
	 * 4 创建query
	 * 5 通过query获取topDocs
	 */
	@Test
	public void search(){
		DirectoryReader indexReader =null;
		try {
			indexReader = DirectoryReader.open(dic);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			QueryParser queryParser = new QueryParser( "file",new StandardAnalyzer());
			Query query = queryParser.parse("工单");
			TopDocs topDocs =  indexSearcher.search(query, 10);
			for(ScoreDoc scoreDoc:topDocs.scoreDocs){
				Document doc = indexSearcher.doc(scoreDoc.doc);
				System.out.println("score="+scoreDoc.score+" "+doc.get("fileName")+" " + doc.get("filepath"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(indexReader!=null){
				try {
					indexReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	
}

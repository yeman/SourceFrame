package com.holly.analyzer.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryTermScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.junit.Test;

import lombok.extern.log4j.Log4j;

@Log4j
public class TestHighlighterApI {
	
	@Test
	public void testHightLighter01(){
		TopDocs topDocs = null;
		StandardAnalyzer standardAnalyzer =null; 
		try {
			standardAnalyzer = new StandardAnalyzer();
			//1  创建 Highlighter 通过构造器 Formatter 和  Scorer 
			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter();
			Query query = new QueryParser("content",standardAnalyzer).parse("like");

			IndexUtil indexUtil = new IndexUtil();
			topDocs = indexUtil.queryScoreDocByPage(1,10,query);
			if(topDocs==null){
				log.warn("testHightLighter01 method topDocs should not null");
				return;
			}
			ScoreDoc[] hits = topDocs.scoreDocs;//命中文档数
			Scorer scorer = new QueryTermScorer(query);
			Highlighter highlighter = new Highlighter(simpleHTMLFormatter,scorer);
			highlighter.setTextFragmenter(new SimpleFragmenter(10));///设置每次返回的字符数
			IndexSearcher indexSearcher =  indexUtil.getIndexSearch();
			for(int i=0;i<hits.length;i++){
				Document doc = indexSearcher.doc(hits[i].doc);
				String txt = highlighter.getBestFragment(standardAnalyzer, "content", doc.get("id"));
				System.out.println(txt);
			}
		} catch (IOException | InvalidTokenOffsetsException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

package com.holly.analyzer.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.BytesRef;
import org.junit.Test;


public class TestIndexUtil {

	@Test
	public void index() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.index();
		indexUtil.search();
	}
	
	@Test
	public void search() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.search();
	}
	
	@Test
	public void delete() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.delete();
		indexUtil.search();
	}
	
	@Test
	public void undelete() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.undelete();
		indexUtil.search();
	}
	@Test
	public void deleteAll() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.deleteAll();
		indexUtil.search();
	}
	
	@Test
	public void forcedelete() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.foreDelete();
		indexUtil.search();
	}
	
	@Test
	public void foreMerge() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.foreMerge();
		indexUtil.search();
	}
	
	@Test
	public void update() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.update();
		indexUtil.search();
	}
	
	@Test
	public void boost() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		Map<String,List<String>> map = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		list.add("aa@gmail.com");
		list.add("aa@sina.com");
		map.put("email", list);
		//StringField不支持boost加权
		indexUtil.boost(map);
	
		
	}
	
	@Test
	public void termQuery() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		Term term = new Term("email","aa@gmail.com");
		Query query = new TermQuery(term);
		indexUtil.query(query,10);
	}
	
	//相似度查询
	@Test
	public void fuzzyQuery() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		Term term = new Term("email","aa@gmail.com");
		Query query = new FuzzyQuery(term);
		indexUtil.query(query,10);
	}
	//booleanQuery 查询
	@Test
	public void booleanQuery() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		TermQuery termq1 = new TermQuery(new Term("id", "5"));
		TermQuery termq2 = new TermQuery(new Term("from", "qson"));
		Builder builder = new BooleanQuery.Builder();
		
		//且
		//query.add(termq1, BooleanClause.Occur.MUST);
		//query.add(termq2, BooleanClause.Occur.MUST);
		
		//包含前者不包含后者
		//TermQuery termq3 = new TermQuery(new Term("email", "aa@qq.com"));
		//TermQuery termq4 = new TermQuery(new Term("id", "6"));
		//query.add(termq3, BooleanClause.Occur.MUST);
		//query.add(termq4, BooleanClause.Occur.MUST_NOT);
		
		// should 和 must 表示must
		//TermQuery termq5 = new TermQuery(new Term("email", "aa@qq.com"));
		//TermQuery termq6 = new TermQuery(new Term("id", "6"));
		//query.add(termq5, BooleanClause.Occur.SHOULD);
		//query.add(termq6, BooleanClause.Occur.MUST);
		
		//should 和 mustnot 相当于must 和 mustnot
		//TermQuery termq7 = new TermQuery(new Term("email", "aa@qq.com"));
		//TermQuery termq8 = new TermQuery(new Term("id", "6"));
		//query.add(termq7, BooleanClause.Occur.SHOULD);
		//query.add(termq8, BooleanClause.Occur.MUST_NOT);
		
		//或
		TermQuery termq9 = new TermQuery(new Term("email", "aa@qq.com"));
		TermQuery termq10 = new TermQuery(new Term("from", "jackyin"));
		builder.add(termq9, BooleanClause.Occur.SHOULD);
		builder.add(termq10, BooleanClause.Occur.SHOULD);
		BooleanQuery query = builder.build();
		
		indexUtil.query(query,10);
	}
	
	//PhraseQuery 短语查询
	@Test
	public void phraseQuery() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		PhraseQuery phraseQuery = new PhraseQuery(10,"content","like","here");
		/*phraseQuery.setSlop(10);//设置最大间距距离
		phraseQuery.add(new Term("content", "like"));
		phraseQuery.add(new Term("content", "here"));*/
		indexUtil.query(phraseQuery,10);
		
	}
	
	//prefixQuery 查询
	@Test
	public void prefixQuery() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		PrefixQuery prefixQuery = new PrefixQuery(new Term("email","aa"));
		indexUtil.query(prefixQuery, 10);
	}
	
	//WildcardQuery 查询
	@Test
	public void wildcardQuery() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		WildcardQuery wildcardQuery = new WildcardQuery(new Term("content","*on*"));
		indexUtil.query(wildcardQuery, 10);
	}
	
	//RangeQuery 查询
	@Test
	public void rangeQuery() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		//TermRangeQuery rangeQuery = new TermRangeQuery("id","3","6",true,true);
		TermRangeQuery rangeQuery = new TermRangeQuery("id",new BytesRef("3".getBytes()),  new BytesRef("6".getBytes()), true, true);
;		indexUtil.query(rangeQuery, 10);
	}
	@Test
	public void compareTo(){
		// jackyin  fksion  faker
		String term = "fksion";
		int lower = term.compareTo("a");
		int upper = term.compareTo("k");
		System.out.println("lower "+ lower + " upper "+upper);
		
		
	}
	@Test
	public void rangeQuery2() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		TermRangeQuery rangeQuery = new TermRangeQuery("from",new BytesRef("a".getBytes()),
				new BytesRef("k".getBytes()),true,true);
		indexUtil.query(rangeQuery, 10);
	}
	
	//
	@Test
	public void numrialQuery() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		//Query  query = NumericRangeQuery.newLongRange("attachments", 1L, 4L, true, true);
		Query  query = LongPoint.newRangeQuery("attachments", 1L, 4L);
		indexUtil.query(query, 10);
	}
	
	//filterQuery
	@Test
	public void filterQuery() throws IOException{
		IndexUtil indexUtil = new IndexUtil();
		WildcardQuery q = new WildcardQuery(new Term("content", "*on*"));
		TermRangeQuery teamQuery = new TermRangeQuery("id", new BytesRef("1".getBytes()),  new BytesRef("5".getBytes()), true, true);
		// Use {@link TermRangeQuery} and {@link BooleanClause.Occur#FILTER} clauses instead.
		//TermRangeFilter filter = new TermRangeFilter("id", "1", "5", true, true);
		//FilteredQuery filteredQuery = new FilteredQuery(q, filter);
		Builder builder = new BooleanQuery.Builder();
		builder.add(q, BooleanClause.Occur.SHOULD).add(teamQuery, BooleanClause.Occur.FILTER);
		BooleanQuery booleanQuery = builder.build();
		indexUtil.query(booleanQuery, 10);
		
	}
	@Test
	public void testStandardAnalyzer() throws IOException {
		Analyzer analyzer = new StandardAnalyzer();
		try {
			StringReader sr = new StringReader("aabc@qq.com");
			TokenStream tk = analyzer.tokenStream("", sr);
			tk.reset();
			System.out.println("analyzer:" + analyzer.getClass().getName());
			CharTermAttribute charTerm = tk.getAttribute(CharTermAttribute.class);
			while (tk.incrementToken()) {
				System.out.print(charTerm.toString() + "|");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (analyzer != null)
				analyzer.close();
		}
	}
	@Test
	public void queryParser() throws IOException, ParseException{
		String field = "content";
		QueryParser qp = new QueryParser(field, new StandardAnalyzer());
		Query query = qp.parse("welcome");
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.query(query, 10);
	}
	//id:改变搜索域选项
	@Test
	public void queryParser2() throws IOException, ParseException{
		String field = "content";
		QueryParser qp = new QueryParser(field, new StandardAnalyzer());
		Query query = qp.parse("id:2");
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.query(query, 10);
	}
	//闭区间查询
	@Test
	public void queryParser3() throws IOException, ParseException{
		String field = "content";
		QueryParser qp = new QueryParser(field, new StandardAnalyzer());
		Query query = qp.parse("id:[2 TO 4]");
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.query(query, 10);
	}
	//开区间查询
	@Test
	public void queryParser4() throws IOException, ParseException{
		String field = "content";
		QueryParser qp = new QueryParser(field, new StandardAnalyzer());
		Query query = qp.parse("id:{2 TO 4}");
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.query(query, 10);
	}
	//模糊查询setAllowLeadingWildcard开启为true 避免 ParseException not allowed as first character in WildcardQuery
	@Test
	public void queryParser5() throws IOException, ParseException{
		String field = "content";
		QueryParser qp = new QueryParser(field, new StandardAnalyzer());
		qp.setAllowLeadingWildcard(true);
		Query query = qp.parse("email:*@qq.com");
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.query(query, 10);
	}
	@Test
	public void queryParser6() throws IOException, ParseException{
		String field = "content";
		QueryParser qp = new QueryParser(field, new StandardAnalyzer());
		qp.setAllowLeadingWildcard(true);
		qp.setFuzzyMinSim(9f);
		Query query = qp.parse("email:aa*@qq.com");
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.query(query, 10);
	}
	//正则表达式查询
	@Test
	public void queryParser7() throws IOException, ParseException{
		String field = "email";
		String regexp = "aa[a-f]{2,}@qq.com";
		Query query = new RegexpQuery(new Term(field,regexp));
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.query(query, 10);
	}
	@Test
	public void queryParser54() throws IOException, ParseException{
		String field = "email";
		QueryParser qp = new QueryParser(field, new KeywordAnalyzer());
		Query query = qp.parse("(email:aabc@qq.com AND id:1) OR from:jackyin");
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.query(query, 10);
	}
	
	@Test
	public void testSort() throws IOException, ParseException{
		IndexUtil indexUtil = new IndexUtil();
		Query query = new TermQuery(new Term("content", "like"));
		indexUtil.querySortBySore(query,10);
		System.out.println("----------------------------------------------------------");
		indexUtil.querySortById(query,10);
		System.out.println("----------------------------------------------------------");
		indexUtil.sortByMutiField(query,10);
	}
	
	
}

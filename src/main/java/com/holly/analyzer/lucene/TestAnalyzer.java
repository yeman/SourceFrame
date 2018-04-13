package com.holly.analyzer.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class TestAnalyzer {

	private String news = "中国青年网北京1月9日电（记者　杨月　实习记者　曹若鸿）1月5日，新进中央委员会的委员、候补委员和省部级主要领导干部学习贯彻习近平新时代中国特色社会主义思想和党的十九大精神研讨班在中央党校开班。中共中央总书记、国家主席、中央军委主席习近平在开班式上发表重要讲话。讲话中，习近平提出很多精彩的重要论述，并提出了“6351453”这一组决胜新时代的密码";
	

	@Test
	public void testCJKAnalyzer() throws IOException {
		CharArraySet set = new CharArraySet(new ArrayList(),true);
		set.add("中国");
		set.add("北京");
		set.add("青年");
		set.add("中共中央");
		set.add("中国特色社会主义思想");
		Analyzer analyzer = new CJKAnalyzer(set);
		try {
			StringReader sr = new StringReader(news);
			TokenStream tk = analyzer.tokenStream("", sr);
			 tk.reset();
			CharTermAttribute charTerm = tk.getAttribute(CharTermAttribute.class);
			while (tk.incrementToken()) {
				System.out.print(charTerm.toString() + "|");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (analyzer != null) {
				analyzer.close();
			}
		}
	}
	
	@Test
	public void testMyCJKAnalyzer() throws IOException {
		CharArraySet set = new CharArraySet(new ArrayList(),true);
		set.add("中国");
		set.add("北京");
		set.add("青年");
		set.add("中共中央");
		set.add("中国特色社会主义思想");
		Analyzer analyzer = new MyCJKAnalyzer(set);
		try {
			StringReader sr = new StringReader(news);
			TokenStream tk = analyzer.tokenStream("", sr);
			 tk.reset();
			CharTermAttribute charTerm = tk.getAttribute(CharTermAttribute.class);
			while (tk.incrementToken()) {
				System.out.print(charTerm.toString() + "|");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (analyzer != null) {
				analyzer.close();
			}
		}
	}

	@Test
	public void testStandardAnalyzer() throws IOException {
		Analyzer analyzer = new StandardAnalyzer();
		try {
			StringReader sr = new StringReader(news);
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
	public void testKeywordAnalyzer() {
		KeywordAnalyzer analyzer = null;
		try {
			analyzer = new KeywordAnalyzer();
			StringReader stringReader = new StringReader(news);
			TokenStream tokenStream = analyzer.tokenStream("", stringReader);
			tokenStream.reset();
			CharTermAttribute charTerm = tokenStream.getAttribute(CharTermAttribute.class);
			while(tokenStream.incrementToken()){
				System.out.print(charTerm.toString() + "|");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(analyzer!=null){
				analyzer.close();
			}
		}
	}
	
	//中文分词器
	@Test
	public void testIKAnalyze() {
		Analyzer analyzer = null;
		boolean useSmart = true;
		try {
			analyzer = new IKAnalyzer(useSmart);
			StringReader stringReader = new StringReader(news);
			TokenStream tokenStream = analyzer.tokenStream("myfield", stringReader);
			tokenStream.reset();
			// 获取词元文本属性
			CharTermAttribute charTerm = tokenStream.getAttribute(CharTermAttribute.class);
			//获取词元位置属性
			OffsetAttribute offSetTerm = tokenStream.getAttribute(OffsetAttribute.class);
			//
			TypeAttribute typeTerm = tokenStream.getAttribute(TypeAttribute.class);
			while(tokenStream.incrementToken()){
				System.out.println(offSetTerm.startOffset()+"-"+offSetTerm.endOffset() +":" + charTerm.toString() + "|"+typeTerm.type());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(analyzer!=null){
				analyzer.close();
			}
		}
	}

}

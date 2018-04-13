package com.holly.analyzer.lucene;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.cjk.CJKBigramFilter;
import org.apache.lucene.analysis.cjk.CJKWidthFilter;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class MyCJKAnalyzer extends StopwordAnalyzerBase {
	public static final CharArraySet STOP_WORDS_SET = StopAnalyzer.ENGLISH_STOP_WORDS_SET;

	protected MyCJKAnalyzer(CharArraySet charArraySet) {
		super(charArraySet);
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {

		// 创建标准分词器
		// 创建CJKWidthFilter
		// 创建LowerCaseFilter
		// 创建CJKBigramFilter
		// 创建 TokenStreamComponents
		final Tokenizer tokenizer = new StandardTokenizer();
		TokenStream result = new CJKWidthFilter(tokenizer);
		result = new LowerCaseFilter(result);
		result = new CJKBigramFilter(result);
		// return new TokenStreamComponents(tokenizer, result);
		return new TokenStreamComponents(tokenizer, new StopFilter(result, stopwords)) {

		};

	}

}

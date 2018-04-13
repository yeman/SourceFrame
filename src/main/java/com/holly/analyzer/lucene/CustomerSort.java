package com.holly.analyzer.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.BinaryDocValuesField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.Test;

import junit.framework.TestCase;

public class CustomerSort extends TestCase {
	private static RAMDirectory directory;
	private IndexSearcher indexSearcher;
	private DirectoryReader reader;
	private IndexWriter writer;

	public Directory getDiretory() {
		synchronized (CustomerSort.class) {
			if (directory == null) {
				directory = new RAMDirectory();
			}
		}
		return directory;
	}

	public DirectoryReader getReader() throws IOException {
		synchronized (CustomerSort.class) {
			if (reader == null) {
				reader = DirectoryReader.open(getDiretory());
			}
		}
		return reader;
	}

	public IndexWriter getWriter() throws IOException {
		synchronized (CustomerSort.class) {
			if(writer ==null){
				writer = new IndexWriter(getDiretory(), new IndexWriterConfig(new StandardAnalyzer()));
			}
		}
		return writer;
	}

	@Override
	protected void setUp() throws Exception {
		addPoint(getWriter(), "ezhou", "home", 1, 2);
		addPoint(getWriter(), "gs", "home", 6, 8);
		addPoint(getWriter(), "wuhan", "home", 5, 8);
		addPoint(getWriter(), "huangshi", "home", 3, 6);
		super.setUp();
	}

	@Test
	public void testCustomerSort() throws IOException {
		indexSearcher = new IndexSearcher(getReader());
		Sort sort = new Sort(new SortField("location", new DistanceComparatorSource(0, 0)));
		TopDocs tps = indexSearcher.search(new TermQuery(new Term("nodeType", "home")), 4, sort);
		ScoreDoc[] sds = tps.scoreDocs;
		for (ScoreDoc sd : sds) {
			int docId = sd.doc;
			Document doc = getReader().document(docId);
			System.out.println(" nodeName " + doc.get("nodeName") + " nodeName " + doc.get("nodeType") + " location "
					+ doc.get("location"));

		}
	}

	private void addPoint(IndexWriter writer, String nodeName, String nodeType, int x, int y) throws IOException {
		Document doc = new Document();
		String xy = x + "," + y;
		doc.add(new StringField("nodeName", nodeName, Store.YES));
		doc.add(new StringField("nodeType", nodeType, Store.YES));
		doc.add(new StringField("location", xy, Store.YES));
        doc.add(new BinaryDocValuesField("location", new BytesRef(xy.getBytes())));    
		writer.addDocument(doc);
		writer.commit();

	}

	@Override
	protected void tearDown() throws Exception {
		if (writer != null) {
			writer.close();
		}
		if (reader != null) {
			reader.close();
		}
		super.tearDown();
	}

}

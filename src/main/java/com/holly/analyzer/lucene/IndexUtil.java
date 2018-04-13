package com.holly.analyzer.lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortedNumericSortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

import lombok.extern.log4j.Log4j;

@Log4j
public class IndexUtil {
	private String[] ids = {"1","2","3","4","5","6"};
	private String[] email = {"aabc@qq.com","aabc@qq.com","aaereredfg@qq.com",
			"aabcgmail@.com","aa@gmail.com","aabc@qq.com"};
	private String[] mailContent = {"welcome to  visited the space,I like book","I like  tony"," I like join",
			"I like jackson","I like  some content here"," I like team holly"};
	private int[] attachments = {2,1,0,4,5,7};
	private String[] from = {"zhangsan","jackyin","mike","fksion","qson","faker"};
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static Date[] dates= new Date[6];
	
	IndexUtil() throws IOException{
		try {
			
			dates[0] =  sdf.parse("2017-11-12 01:45");
			dates[1] =  sdf.parse("2017-12-12 21:00");
			dates[2] =  sdf.parse("2017-09-12 09:09");
			dates[3] =  sdf.parse("2016-12-12 12:06");
			dates[4] =  sdf.parse("2017-11-11 00:45");
			dates[5] =  sdf.parse("2017-10-09 21:25");
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	private static Directory getDirectory() throws IOException{
		return FSDirectory.open(Paths.get("D:/lucene/index02"));
	}
	public IndexSearcher getIndexSearch(){
		IndexSearcher indexSearcher = null;
		try {
			indexSearcher = new IndexSearcher(DirectoryReader.open(getDirectory()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return indexSearcher;
	}
	
	@SuppressWarnings("deprecation")
	public void index(){
		IndexWriter iw = null;
		
		try {
			iw =  new IndexWriter(getDirectory(),new IndexWriterConfig(new StandardAnalyzer()));
			Document doc = null;
			
			for(int i =0;i<ids.length;i++){
				doc = new Document();
				doc.add(new StringField("id", ids[i],Field.Store.YES));
				doc.add(new StringField("email", email[i],Field.Store.YES));
				doc.add(new TextField("content", mailContent[i],Field.Store.NO));
				doc.add(new StringField("from", from[i],Field.Store.YES));
				doc.add(new NumericDocValuesField("attachments",(long)attachments[i]));
				doc.add(new LongPoint("date",dates[i].getTime()));
				iw.addDocument(doc);
			}
			
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(iw!=null){
				try {
					iw.close();
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
	
	public void deleteAll(){
		IndexWriter writer = null;
		try {
			writer =  new IndexWriter(getDirectory(), new IndexWriterConfig(new StandardAnalyzer()));
			writer.deleteAll();
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer!=null){
				try {
					writer.close();
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
	
	public void search(){
		DirectoryReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
			int maxDoc =reader.maxDoc();
			int numDocs = reader.numDocs();
			int numberDeleteDocs = reader.numDeletedDocs();
			System.out.println("numDocs:"+numDocs);
			System.out.println("maxDoc:"+maxDoc);
			System.out.println("numberDeleteDocs:"+numberDeleteDocs);
			for(int i=0;i<maxDoc;i++){
				Document doc = reader.document(i);
				System.out.println(doc.toString()+ "boost:"+doc);
			}
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
	//
	public void query(Query query,int rows){
		DirectoryReader reader = null;
		IndexSearcher indexSearcher = null; 
		try {
			reader = DirectoryReader.open(getDirectory());
			indexSearcher = new IndexSearcher(reader);
			TopDocs  topDocs = indexSearcher.search(query, rows);
			System.out.println("一共查询 "+ topDocs.totalHits);
			for(ScoreDoc sd : topDocs.scoreDocs){
				Document doc = indexSearcher.doc(sd.doc);
				System.out.println(" [email]="+ doc.get("email")+" [id]="+doc.get("id")+
						" [content]="+doc.get("content") +" [from]="+doc.get("from"));
			}
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	
	public void delete(){
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(getDirectory(), new IndexWriterConfig(new StandardAnalyzer()));
			writer.deleteDocuments(new Term("id","1"));
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer!=null){
				 try {
					 writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	public void undelete(){
		IndexWriter writer = null;
		try {
			 writer = new IndexWriter(getDirectory(), new IndexWriterConfig(new StandardAnalyzer()));
			writer.rollback();
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	
	}
	public void foreDelete(){
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(getDirectory(), new IndexWriterConfig(new StandardAnalyzer()));
			writer.deleteDocuments(new Term("id","1"));
			writer.forceMergeDeletes();
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer!=null) {
				try {
					writer.close();
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
	
	public void foreMerge(){
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(getDirectory(), new IndexWriterConfig(new StandardAnalyzer()));
			writer.forceMerge(2);
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer!=null) {
				try {
					writer.close();
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
	
	public void update(){

		IndexWriter writer = null;
		try {
			writer = new IndexWriter(getDirectory(), new IndexWriterConfig(new StandardAnalyzer()));
			Document doc = new Document();
			doc.add(new StringField("id", ids[0],Field.Store.YES));
			doc.add(new StringField("email", email[0],Field.Store.YES));
			doc.add(new TextField("content", mailContent[0],Field.Store.NO));
			doc.add(new StringField("from", from[0],Field.Store.YES));
			doc.add(new LongPoint("attachments", attachments[0]));
			doc.add(new LongPoint("date", dates[0].getTime()));
			
			writer.updateDocument(new Term("5", "222"), doc);
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer!=null) {
				try {
					writer.close();
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
	//索引时候加权
	public void boost(Map<String,List<String>> boostpair){
		IndexWriter iw = null;
		try {
			iw = new IndexWriter(getDirectory(),new IndexWriterConfig(new StandardAnalyzer()));
			Document doc = null;
			for(int i=0;i<ids.length;i++){
				doc = new Document();
				doc.add(new StringField("id", ids[i],Field.Store.YES));
				doc.add(new TextField("content", mailContent[i],Field.Store.NO));
				doc.add(new StringField("from", from[i],Field.Store.YES));
				doc.add(new LongPoint("attachments", attachments[i]));
				doc.add(new LongPoint("date",dates[i].getTime()));
				
				Field field = new StringField("email", email[i],Field.Store.YES);
				List<String> list = boostpair.get("email");
				if(list.contains(email[i])){
					field.setBoost(3.0f); //加权操作 默认1.0
				}else{
					
				}
				doc.add(field);
				
				iw.addDocument(doc);
			}
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(iw!=null)
				try {
					iw.close();
				} catch (CorruptIndexException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	//分页查询
	public TopDocs queryScoreDocByPage(int beginPage,int pageSize,Query query) {
		TopDocs result = null;  
		if(query==null){
			log.warn("queryScoreDocByPage() param query is null ");
			return null;
		}
		DirectoryReader reader = null;
		IndexSearcher indexSearcher = null; 
		try {
			reader = DirectoryReader.open(getDirectory());
			indexSearcher = new IndexSearcher(reader);
			ScoreDoc after = null;
			if(beginPage!=1){
				 TopDocs docsBefore = indexSearcher.search(query, (beginPage-1)*pageSize);
				 ScoreDoc[] scoreDocs = docsBefore.scoreDocs;
				 if(scoreDocs!=null && scoreDocs.length>0){
					 after = scoreDocs[scoreDocs.length-1];
				 }
				 
			}
			result = indexSearcher.searchAfter(after, query,pageSize);
			for(ScoreDoc sd:result.scoreDocs){
				Document doc = indexSearcher.doc(sd.doc);
				System.out.println(sd.doc + ":"+doc.get("id")+"-->"+doc.get("email"));
			}
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	
		
	}
	
	public void querySortBySore(Query query, int count) {
		IndexSearcher indexSearcher = null;
		IndexReader indexReader = null;
		try {
			indexReader = DirectoryReader.open(getDirectory());
			indexSearcher = new IndexSearcher(indexReader);
			//默认情况下  luence默认排序是不会进行评分的
			
			//Sort 构造器默认根据评分排序
			TopDocs docs = indexSearcher.search(query, count,Sort.RELEVANCE,true,true);
			ScoreDoc[] sds = docs.scoreDocs;
			for(ScoreDoc sd : sds){
				int docId = sd.doc;
				float score = sd.score;
				Document doc = indexSearcher.doc(docId);
				Explanation explanation = indexSearcher.explain(query, docId);
				//System.out.println("Explanation -->"+explanation);
				 System.out.println(" docId = " + docId + "\t" + " score "+ score 
    		  			 + " [field id] "+doc.get("id") 
                    	 + " [field email] "+doc.get("email")
                    	 + " [field content] "+doc.get("content")
                         + " [field attachments] "+doc.get("attachments")
      					 + " [field from] "+doc.get("from"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(indexReader!=null){
				try {
					indexReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	//根据索引号排序
	public void querySortById(Query query, int count) {
		IndexSearcher indexSearcher = null;
		IndexReader indexReader = null;
		try {
			indexReader = DirectoryReader.open(getDirectory());
			indexSearcher = new IndexSearcher(indexReader);
			//默认情况下  luence默认排序是不会进行评分的
			
			//Sort 构造器默认根据评分排序
			TopDocs docs = indexSearcher.search(query, count,Sort.INDEXORDER,true,true);
			ScoreDoc[] sds = docs.scoreDocs;
			for(ScoreDoc sd : sds){
				int docId = sd.doc;
				float score = sd.score;
				Document doc = indexSearcher.doc(docId);
				Explanation explanation = indexSearcher.explain(query, docId);
				//System.out.println("Explanation -->"+explanation);
				 System.out.println(" docId = " + docId + "\t" + " score "+ score 
    		  			 + " [field id] "+doc.get("id") 
                    	 + " [field email] "+doc.get("email")
                    	 + " [field content] "+doc.get("content")
                         + " [field attachments] "+doc.get("attachments")
      					 + " [field from] "+doc.get("from"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(indexReader!=null){
				try {
					indexReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	//根据不通的域进行排序
	public void sortByMutiField(Query query, int count){
		 IndexSearcher indexSearcher = null;
		 DirectoryReader directoryReader = null;
		 try {
			directoryReader =  DirectoryReader.open(FSDirectory.open(Paths.get("D:/lucene/index02")));
			 indexSearcher = new IndexSearcher(directoryReader);
			 SortField[] sortFields = new SortField[]{
					 						new SortField("id", SortField.Type.DOC,true),
					 						new SortField("from", SortField.Type.SCORE,true)
					 						};
			 TopDocs tds = indexSearcher.search(query, count, new Sort(sortFields),true,true);
			 ScoreDoc[] sds = tds.scoreDocs;
			 for(ScoreDoc sd : sds){
                  int docId =  sd.doc;
                 float score = sd.score;
                  Document doc = indexSearcher.doc(docId);
                  System.out.println(" docId = " + docId + "\t" + " score "+ score 
                		  			 + " [field id] "+doc.get("id") 
                                	 + " [field email] "+doc.get("email")
                                	 + " [field content] "+doc.get("content")
                                     + " [field attachments] "+doc.get("attachments")
                  					 + " [field from] "+doc.get("from"));
                  
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
	}
			
}

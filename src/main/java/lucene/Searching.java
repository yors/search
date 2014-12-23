package lucene;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;


public class Searching {
	/*
	 * permet de lire l'index comme IndexWriter permet d'écrire dans l'index
	 */
	private IndexReader index;
	
	/*
	 * requete construite a partir de QueryBuild
	 */
	private String requete;
	
	/*
	 * l'ensemble des documents lucene correspondants au motif de la recherche	
	 */
	
	/*
	 * association de mots clé et de ressources.Un mot clé est associé à 0 ou plusieurs ressources
	 */
	private HashMap<String,Set<Document> > keywordResources;
	
	/*
	 * @param indexDirectory est le repertoire de l'index crée pendant la phase d'indexation
	 * @param setOfKeywords est un ensemble de mots clés à rechercher dans l'index
	 */
	public Searching(Directory indexDirectory,String requete)throws Exception{
		keywordResources = new HashMap<String,Set<Document> >();//on instancie 
		Set<Document> documents ;//ensemble de documents associés à un mot-clé
		
		IndexReader reader = DirectoryReader.open(indexDirectory);//on ouvre le repertoire de l'index pour la lecture
	

			
			documents = new HashSet<Document>();
			//QueryParser parser = new QueryParser("description", new StandardAnalyzer());//la requete porte sur les champs propriete et litteral
		    //    String requete  = QueryBuilder(setOfKeywords);// retourne la requête
		      String word = requete;
		
		    QueryParser parser = new QueryParser("description", new StandardAnalyzer());//la requete porte sur la description de la ressource
			Query mot = parser.parse(requete);
			
		
			
			IndexSearcher searcher = new IndexSearcher(reader);						

			int hitsPerPage = 10;	
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			searcher.search(mot, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			for(int i= 0 ; i < hits.length ; ++i) {
			    int docId = hits[i].doc;
			    Document doc = searcher.doc(docId);
			    documents.add(doc);
			   	
		   }
			 keywordResources.put(word,documents);
			
		
		    reader.close();		    
		
	}


	public void setIndex(IndexReader index) {
		this.index = index;
	}


	public IndexReader getIndex() {
		return index;
	}

	/*
	 * @param motif est le motif saisi pour la recherche
	 * @return retourne un ensemble de mots clé à partir de la saisi
	 */
	
	


	public HashMap<String, Set<Document>> getKeywordResource() {
		return keywordResources;
	}


	public void setKeywordResource(HashMap<String, Set<Document>> keywordResource) {
		this.keywordResources = keywordResource;
	}


	public String getRequete() {
		return requete;
	}


	public void setRequete(String requete) {
		this.requete = requete;
	}	
	

}

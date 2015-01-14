package lucene;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;

/**
 * Class responsible of searching withing the Lucene index
 * 
 */
public class Searching {
	
	//permet de lire l'index comme IndexWriter permet d'écrire dans l'index	
	private IndexReader index;	
	
	//requete construite a partir de QueryBuild	 
	private String requete;	
	
	private int nbrMaxKeywords;
	
	//l'ensemble des documents lucene correspondants au motif de la recherche	
	// association de mots clé et de ressources.Un mot clé est associé à 0 ou plusieurs ressources
	 
	private HashMap<String,Set<Document> > keywordResources;	
	
	/**
	 * 
	 * @param indexDirectory directory containing the Lucene index (created during indexation)
	 * @param motif keywords entered by user for searching
	 * @throws Exception 
	 */
	public Searching(Directory indexDirectory,String motif)throws Exception{
		
		nbrMaxKeywords =3;
		keywordResources = new HashMap<String,Set<Document> >();    //on  instancie 
		Set<Document> documents ;	//ensemble de documents associés à un mot-clé
		
		//on ouvre le repertoire de l'index pour la lecture
		IndexReader reader = DirectoryReader.open(indexDirectory);
			
		documents = new HashSet<Document>();
		
		QueryBuild queryBuild = new QueryBuild(nbrMaxKeywords,motif);
		IndexSearcher searcher = new IndexSearcher(reader);						

		int hitsPerPage = 10;	
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		searcher.search(queryBuild.getRequete(), collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		
		for(int i= 0 ; i < hits.length ; ++i) {
		    int docId = hits[i].doc;
		    Document doc = searcher.doc(docId);
		    documents.add(doc);			   	
		}
		
		keywordResources.put(motif,documents);		
		reader.close();		
	}


	public void setIndex(IndexReader index) {
		this.index = index;
	}

	public IndexReader getIndex() {
		return index;
	}

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
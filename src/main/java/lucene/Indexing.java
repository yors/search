package lucene;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.io.File;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * This class creates an index on the RDF/Jena's model
 * 
 * @author Daniel
 *
 */
public class Indexing {	
	
	//indexe les documents rdf.	 
	private IndexWriter index;
	
	//repertoire contenant l'index	 
	private FSDirectory indexDirectory;
	
	// sert à la configuration de l'index et prends en paramètre l'analyseur et 
	// la version de la version de librairie lucene	 
	private IndexWriterConfig indexConfig;
	
	//analyseur de l'index	
	private Analyzer indexAnalyser;
	
	//model du graphe jena	 
	private Model model;
	
	private  File indexFile;	
	
	/**
	 * 
	 * @param model the model to indexed
	 * @throws Exception excpetion throws when the index directory doesn't exist
	 */
	public Indexing(Model model) throws Exception{
		
		 //on instance tous les objets nécessaires à la création de l'index puis on instancie l'index.	
		 this.model = model;
		 indexAnalyser = new StandardAnalyzer();
		 indexFile = new File("src\\main\\resources\\luceneIndexes");
		 deleteDirectory();
		
		 indexDirectory = FSDirectory.open(indexFile);
		 indexConfig = new IndexWriterConfig(Version.LATEST, indexAnalyser);
		 index = new IndexWriter(indexDirectory, indexConfig);		
	
		//itérator pour parcourir les statements, obtenir la prochaine déclaration
		
		  StmtIterator iter = model.listStatements();//on l'ensemble des statements du model à l'aide d'un iterator
		  //l'objectif est décrire une ressource entiere ,c'est à dire par toutes les propriètes et valeurs qui la définissent (en profondeur)
		  //ressDesc;
		  //pour le premier statement qui n'a pas de précedent 
		  if(!iter.hasNext())
			  throw new Exception("aucun statement trouvé");
		  
		  Statement st = iter.next();
          String lastSubject = st.getSubject().toString();
          String ressDesc = this.descripRessource(st,"");
          Document doc ;
    
	     while(iter.hasNext()){
	    	    st = iter.next();
	    	    if(st.getSubject().toString()!=lastSubject){// ajouter la ressource a l'index
	    	    	doc = new Document();
	  				doc.add(new StringField("ressource",lastSubject,Field.Store.YES));
	  				doc.add(new TextField("description",ressDesc,Field.Store.YES));
	  				index.addDocument(doc);
	  				ressDesc = "";// apres avoir décrit entièrement une ressource, on remet la variable à une chaine vide.
	    	    }
	    	    
	    	    //descrire la ressource
	    	    ressDesc = ressDesc + " " + this.descripRessource(st,"");
	    	    
	    	    //recuperer le dernier sujet décrit
			    lastSubject =  st.getSubject().toString();			    
	      }
	     
	     //pour traiter le dernier statement qui n'a pas de suivant
	     doc = new Document();
		 doc.add(new StringField("ressource",lastSubject,Field.Store.YES));
		 doc.add(new TextField("description",ressDesc,Field.Store.YES));
		 index.addDocument(doc);
		 index.close();		
	}	
	
	public File getIndexFile() {
		return indexFile;
	}

	public void setIndexFile(File indexFile) {
		this.indexFile = indexFile;
	}

	public void setIndex(IndexWriter index) {
		this.index = index;
	}

	public IndexWriter getIndex() {
		return index;
	}

	public void setIndexDirectory(FSDirectory indexDirectory) {
		this.indexDirectory = indexDirectory;
	}

	public Directory getIndexDirectory() {
		return indexDirectory;
	}

	public void setIndexConfig(IndexWriterConfig indexConfig) {
		this.indexConfig = indexConfig;
	}

	public IndexWriterConfig getIndexConfig() {
		return indexConfig;
	}

	public void setIndexAnalyser(Analyzer indexAnalyser) {
		this.indexAnalyser = indexAnalyser;
	}

	public Analyzer getIndexAnalyser() {
		return indexAnalyser;
	}
	
	public Model getModel() {
			return model;
	}

	public void setModel(Model model) {
			this.model = model;
	}	
	
	/**
	 * 
	 * @param stat node which properties and literals we want to know (directly or indirectly)
	 * @param litteral It is the concatenation of all literals with blank space as separator
	 * @return The whole description of the node (all literals and properties)
	 */
	private String descripRessource(Statement stat,String litteral){
		//cette fonction permet de décrire entierement une ressource en l'associant
		//à toutes les propriètes et littéraux qui la décrivent directement et indirectement.
		//String description;// c'est la description de la ressource
		
		RDFNode node = stat.getObject();// on lit l'objet du statement
		RDFNode prt = stat.getPredicate();// on lit la propriete		
	
		if(node.isLiteral())//si le noeud est un litteral
		{		
			//return litteral+" "+node.toString();
			return prt.toString()+" "+litteral+" "+node.toString();//on prend la valeur du noeud
		}
		
		else{//ou bien si le noeud est une ressource 
			
			Resource res = (Resource) node;//continuer à l'explorer
			
			StmtIterator iter = model.listStatements(new SimpleSelector(res,null,null,null));//chercher tous les statement
		                                                    //qui ont res pour ressource
			while(iter.hasNext())
				
		     litteral =  prt.toString()+" "+descripRessource(iter.nextStatement(),litteral);
			//litteral =  descripRessource(iter.nextStatement(),litteral);	
			return litteral;
		}		
	}	
	
	/**
	 * delete the directory containing indexes
	 */
	private void deleteDirectory(){
	  if(indexFile.exists()){
	    File[] files =  indexFile.listFiles();
	    for(int i = 0 ; i < files.length ; i++){
	      files[ i ].delete();
	    }
	  }
	}	
}
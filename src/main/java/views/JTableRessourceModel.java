package views;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import org.apache.lucene.document.Document;


public class JTableRessourceModel extends  AbstractTableModel{

	
	private static final long serialVersionUID = 1L;
	
	/**
	 * contient l'association mot clé et set de documents trouvés
	 */
	private HashMap<String,Set<Document>> keysDocs;
	/**
	 * element position permet de reperer la position dans le table 
	 * d'une paire mot-clé
	 */

	private ArrayList<String> keysRows; // l'ensemble des lignes mots clés;
	private ArrayList<Document> docsRows ;//l'ensemble des lignes documents
	
	//constructeur
	public JTableRessourceModel(HashMap<String,Set<Document>> keyDocs){	  
	  this.keysDocs = keyDocs;
	  hashMaptoArray(keyDocs);
	}

	public final HashMap<String, Set<Document>> getKeyDocs() {
		return keysDocs;
	}

	public final void setKeyDocs(HashMap<String, Set<Document>> keyDocs) {
		this.keysDocs = keyDocs;
		keysRows = new ArrayList<String>();
		docsRows = new ArrayList<Document>();
		hashMaptoArray(keyDocs);
		fireTableDataChanged();
	}

	public int getColumnCount() {		
		return 2;
	}

	public int getRowCount() {		
		int nDocuments=0;
		
		for(Entry<String, Set<Document>> entry : keysDocs.entrySet()) {
		   // String key = entry.getKey();
		    //System.out.println("nombre de key" +key);
		    Set<Document> value = entry.getValue();
		    //System.out.println("nombre de Docs" +value.size());
		    // comptabilisons le nombre de documents 
		    nDocuments += value.size();		    
		}
		System.out.println("nombre de docs: "+nDocuments);
		return nDocuments;
	}

	public Object getValueAt(int row, int col) {
		
		if(col == 0){
			return keysRows.get(row);
		}
		else{
			return docsRows.get(row).get("ressource");
		}

	}
	
	/* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int col) {
        switch (col) {
        case 0:            
            return "Keyword";
        case 1:
            return "Document Path";        
        }
        return null;
    }

	/*
	 * permet de convertir l'ensemble hashmap en tableau pour l'affichage
	 */
    
    private void hashMaptoArray(HashMap<String,Set<Document>> keyDocs){
    	 keysRows =  new ArrayList<String>(); // l'ensemble des lignes mots clés
    	 docsRows = new ArrayList<Document>(); //
    	for(Entry<String, Set<Document>> entry : keysDocs.entrySet()){//parcourir les ensembles du HashMap
    		String key = entry.getKey();
    		Set<Document> value = entry.getValue(); ;//un mot-clé associé a un ensemble de docs
    		Iterator<Document>  iterkeydocs = value.iterator();
    		while(iterkeydocs.hasNext()){ // parcourir l'ensemble de ressources associé à key
    			keysRows.add(key);//pour mulitplier la clé dans l'array afin d'avoir autan de clés que de ressources
    			iterkeydocs.next();
    		}
    		docsRows.addAll(value);//remplir le tableau de docs avec les ensembles du HashMap
    		}
    
    } 
   
  
    
}

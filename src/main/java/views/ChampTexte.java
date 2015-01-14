package views;

import java.util.HashMap;
import java.util.Set;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import lucene.Searching;
import org.apache.lucene.document.Document;

/**********************************************************************************/
/*****       CLASSE QUI CONSTRUIT NOTRE ZONE DE RECHERCHE   ***********************/
/**********************************************************************************/

/**
 * 
 * @author Daniel
 *
 */
public class ChampTexte extends JTextField implements DocumentListener {

	
	private static final long serialVersionUID = 1L;	
	
	/**
	 * constructor with the reference to frame table model
	 * @param tableModel
	 */
	public ChampTexte(){
		super();		
		this.getDocument().addDocumentListener(this);
	}
	
	/**
	 * constructor with the given field's width
	 * @param i the field's width
	 * @param tableModel the reference to the the frame table's model
	 */
	public ChampTexte(int i) {
		super(i);		
		this.getDocument().addDocumentListener(this);
	}

	public void changedUpdate(DocumentEvent arg0) {
		//rien a faire dans notre cas
	}

	public void insertUpdate(DocumentEvent arg0) {
		refreshSearch();
	}

	public void removeUpdate(DocumentEvent arg0) {
		refreshSearch();
	}
	
	/**
	 * effectue la recherche dans l'index à partir de la chaine actuelle entrée dans le champs de recheerche
	 */
	void refreshSearch(){
				
		//recupération du texte-> mots clé à chercher
		String motif = this.getText();
		if(motif.isEmpty())
		{}
		else
		{
			HashMap<String,Set<Document>> resultats= new HashMap<String,Set<Document>>();
		
			//lancement de la recherche			
			try {

				//previous version
				
				//File indexFile = new File("lucene");
				//if(indexFile.listFiles().length == 0) throw new Exception("l'index est vide. Veuillez en créer un!");
				//FSDirectory indexDirectory = FSDirectory.open(indexFile);		
				//Searching search= new Searching(indexDirectory,motif);

				Searching search= new Searching(fr.uvsq.project.rdfSearch.App.index.getIndexDirectory(),motif);
				//System.out.print(search.getKeywordResource().get(new String("l2")).iterator().next().get("ressource"));
				
				//recuperation des eventuels documents trouvés
				resultats= search.getKeywordResource();	
				
				//System.out.println(motif+": result ds champ texte "+resultats);
				
			} 
	    	catch (Exception e1) {			
				e1.printStackTrace();
			}
			
			//traitement et affichage du resultat sur le tableau -- recharger l'ancien model de la table par le nouveau 			
			
			//on recharge le model			 
			ProjectFrame.tableModel.setKeyDocs(resultats);			
			
			//on informe la table que les données du modèle ont changé			 
			ProjectFrame.tableModel.fireTableDataChanged();		
		}
	}
}

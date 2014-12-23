package lucene;

import java.util.ArrayList;
/*
 * @author Alassane Ndiaye
 * @version 1
 */
import java.util.StringTokenizer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

public class QueryBuild {//cette classe permet de construire une requete à partir de la chaine saisie
	/*
	 * permet de preciser le nombre de mots clé gérés à traiter
	 * permet de parametre la requete
	 */
	private	int nbreMot;
	/*motif de la recherche
	 * 
	 * */
	private String motSaisie;
	
	/*
	 * requete construite à partir des mots clé saisis
	 */
	private Query requete;
	/*
	 * liste des mots clés constituant la requête
	 */
	
	private QueryParser parser;
	
	public QueryBuild(int nbreMot,String motif) throws Exception{
		if(motif.isEmpty())//si aucun mot n'est entré 
			throw new Exception("motif vide");
		QueryParser parser = new QueryParser("description", new StandardAnalyzer());//la requete porte sur la description de la ressource
		ArrayList<String> listMot =  keywords(motif);
		
		//si le premier mot du motif est un film
		if(listMot.size()==3){
			if(listMot.get(0).equals("film"))
				requete = parser.parse("(film AND "+listMot.get(1)+" AND "+listMot.get(2)+")^5"//requete sur 3 mots liés par l'opérateur AND
					+ " (((film AND "+listMot.get(1)+") OR (film AND "+listMot.get(2)+"))^5 "//2 requetes liées par l'opérateur OR
							+ "(film OR "+listMot.get(1)+" OR "+listMot.get(2)+"))");	//3 requetes.chaque requet constitué d'un seul 
		}
		
		if(listMot.size() == 2){
			if(listMot.get(0).equals("film"))
				requete = parser.parse("(film AND "+listMot.get(1)+")^5 "//2 requete OR.pour chaque requete 2 mots avec AND
					+ "(film OR "+listMot.get(1)+" OR "+listMot.get(1)+")");	//3 requetes.chaque requet constitué d'un seul 
		}
		
		
		if(listMot.size() == 1){
				requete = parser.parse(listMot.get(0)); 
		}
		
		
		
		
		
		
	}

	
public  ArrayList<String> keywords(String motif){
		
		motif.toLowerCase();//mettre toute la chaine en minuscule
		
		ArrayList<String> setOfKeywords = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(motif," ");//pour extraire les mots clés du motifs
		while(tokenizer.hasMoreTokens())
			setOfKeywords.add(tokenizer.nextToken());//on ajoute chaque token à l'ensemble des mots-clé 
					
		return setOfKeywords;
			
	}


public int getNbreMot() {
	return nbreMot;
}


public void setNbreMot(int nbreMot) {
	this.nbreMot = nbreMot;
}


public String getMotSaisie() {
	return motSaisie;
}


public void setMotSaisie(String motSaisie) {
	this.motSaisie = motSaisie;
}


public Query getRequete() {
	return requete;
}


public void setRequete(Query requete) {
	this.requete = requete;
}


public QueryParser getParser() {
	return parser;
}


public void setParser(QueryParser parser) {
	this.parser = parser;
}
	
	
	
	
}

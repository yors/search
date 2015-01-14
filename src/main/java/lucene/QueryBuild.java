package lucene;

import java.util.ArrayList;
import java.util.StringTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

/**
 * Class that builds Lucene queries from user keywords
 * 
 */
public class QueryBuild {	
	
	//number of keywords 
	private	int nbreMot;
	
	//keywords entered
	private String motSaisie;
	
	public static ArrayList<String> listMot = new ArrayList<String>();
	//resulted query from keywords entered
	private Query requete;	
	
	private QueryParser parser;
	
	public QueryBuild(int nbreMot,String motif){
		
		//la requete porte sur le champ description de la ressource
		QueryParser parser = new QueryParser("description", new StandardAnalyzer());
		listMot =  keywords(motif);
		String requeteString = "";
		
		//elements du motif different de film, actor maker
		String litteral1 ="" ;
		String litteral2="";
		
		try {			
			 //si le nombre mots constituant le motif entré par l'utilisateur est egal a 3
			
	       	   if(listMot.size()==3){
	       	        //film realisé par un realisateur ou film joué par un acteur
	       		   
	       		   if(listMot.contains("Film") && (listMot.contains("FilmMaker") || listMot.contains("Actor"))){
	       			    //requete boostée de 5
	       			    requeteString = "("+listMot.get(0)+" AND "+listMot.get(1)+" AND "+listMot.get(2)+")^5";
		
				    	 //si le motif contient film et actor
				    	 if(!listMot.contains("Actor")){
				            for(int i = 0;i<3;i++)
				            	//trouver le dernier mot du motif different de film et maker
				    	        if(!listMot.get(i).equals("Film")  && !listMot.get(i).equals("FilmMaker"))
				    	            litteral1 = listMot.get(i);  
				            //construire une requete de mots puis une d'un mot
					        requeteString = requeteString+" OR ((Film AND "+litteral1+") OR (FilmMaker AND "+litteral1+"))^2 OR "
									+ "("+listMot.get(0)+" OR "+listMot.get(1)+" OR "+listMot.get(2)+")";
				    	 }			    	 
				    	 
				    	//si le motif contient film and filmMaker
				    	 if(!listMot.contains("FilmMaker")){ 
					            for(int i = 0;i<3;i++)
					            	//trouver le dernier mot du motif different de film et maker
					    	        if(!listMot.get(i).equals("Film") && !listMot.get(i).equals("Actor"))
					    	            litteral1 = listMot.get(i);  
					            //construire une requete de mots puis une d'un mot
						        requeteString = requeteString+" OR ((Film AND "+litteral1+") OR (Actor AND "+litteral1+"))^2 OR "
										+ "("+listMot.get(1)+" OR "+listMot.get(1)+" OR "+listMot.get(2)+")";
					      }
			    	}
	       		   
				    else if(listMot.contains("Film")){
				    	int i= 0;
				    	boolean trouve =false;
				    	
				    	//trouver le mot différent de film en partant de la gauche
				    	while(trouve==false || i==3){
				    		 if(!listMot.get(i).equals("Film")){
				    			 litteral1 = listMot.get(i);
				    			 trouve = true;
				    		 }
				    		 i++;
				    	}
				    	i= 2; 
			    	
				    	//trouver le mot différent de film en partant de la droite
				    	while(trouve==true || i==0){
				    		 if(!listMot.get(i).equals("Film")){
				    			 litteral2 = listMot.get(i);
				    			 trouve = false;
				    		 }
				    		 i--;
				    	}
           			    requeteString = "(Film AND FilmMaker AND "+litteral1+") OR "+"(Film AND Actor AND "+litteral2+")"
           			 		+ " OR (Film AND Actor AND "+litteral1+") OR "+"(Film AND FilmMaker AND "+litteral2+")";
				    }//end else	       		   
	       		   
	       		 
	       		 //film d'un producteur .Par exemple Film producteur Donald
	       		if(listMot.contains("Film") && listMot.contains("Producer")){
       			    //requete boostée de 5
       			    requeteString = "("+listMot.get(0)+" AND "+listMot.get(1)+" AND "+listMot.get(2)+")^5";
       			    
       			    for(int i = 0;i<3;i++)
		            	//trouver le dernier mot du motif different de film et producer
		    	        if(!listMot.get(i).equals("Film")  && !listMot.get(i).equals("Producer"))
		    	            litteral1 = listMot.get(i);  
		            //construire une requete de mots puis une d'un mot
			        requeteString = requeteString+" OR ((Film AND "+litteral1+") OR (Producer AND "+litteral1+"))^2 OR "
							+ "("+listMot.get(0)+" OR "+listMot.get(1)+" OR "+listMot.get(2)+")";
       			    
       			    
	       		}	       		
	       		
	       		//film joué par les acteurs actor1 et actor 2
	       		
	       		if(listMot.contains("Actor") && !listMot.contains("Film") && !listMot.contains("Producer") && !listMot.contains("FilmMaker"))
	       	          
	       		 requeteString = "(Film AND "+listMot.get(0)+" AND "+listMot.get(1)+" AND "+listMot.get(2)+")^5"
	       		 		+ " OR (Film AND ("+listMot.get(0)+" AND "+listMot.get(1)+"))^2 "+" OR (Film AND ("+listMot.get(0)+" AND "+listMot.get(2)+"))^2 "
	       		 		+ " OR (Film AND ("+listMot.get(1)+" AND "+listMot.get(2)+"))^2 OR "+listMot.get(0)+" OR "+listMot.get(1)
	       		 		+" OR "+listMot.get(2);
	       			
	    
				}//end size=3
	       	   
	       	   
	       	   	//une requete sur un mot-clé de 2 élèments
	          	if(listMot.size() == 2){
			               if(listMot.contains("Film"))
				requete = parser.parse("("+listMot.get(0)+" AND "+listMot.get(1)+")^5 "
					+ "(Film OR "+listMot.get(0)+" OR "+listMot.get(1)+")");	
			               
			               else   
			            	   if(listMot.contains("Actor"))
			   				requete = parser.parse("("+listMot.get(0)+ " AND "+listMot.get(1)+")^5 "
			   					+ "(Film OR "+listMot.get(0)+" OR "+listMot.get(1)+")");			               
			              
	          	}       
	          	
	          	
	          	//une requete sur un mot clé d'un seul élément
	      	    if(listMot.size() == 1){
	      		   requete = parser.parse(listMot.get(0)); 
                }	
			}
		    catch (ParseException e){					
				e.printStackTrace();
		    }	//3 requetes.chaque requet constitué d'un seul 
	}
	

	/**
	 * function that retrieve split keywords from keywords entered
	 * @param motif keywords entered
	 * @return return a set of keywords
	 */
	public  ArrayList<String> keywords(String motif){
			
		motif.toLowerCase();									  //mettre toute la chaine en minuscule
			
		ArrayList<String> setOfKeywords = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(motif," ");//pour extraire les mots clés du motifs
		while(tokenizer.hasMoreTokens())
			setOfKeywords.add(tokenizer.nextToken());              //on ajoute chaque token à l'ensemble des mots-clé 
						
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

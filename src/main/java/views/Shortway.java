package views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
//import org.graphstream.graph.implementations.SingleGraph;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;

import edu.uci.ics.jung.graph.Graph;

public class Shortway{
	
	  private final List<RDFNode> nodes;
	  private final List<Statement> edges;
	  private Set<RDFNode> settledNodes;
	  private Set<RDFNode> unSettledNodes;
	  private Map<RDFNode, RDFNode> predecessors;
	  private Map<RDFNode, Integer> distance;
	  Graph<RDFNode, Statement> mongraph;
	  private Map<String,RDFNode> node;	 
	  
	  public Shortway(Graph<RDFNode, Statement> graph){
		   this. nodes = new ArrayList<RDFNode>();
		    this.edges = new ArrayList<Statement>(graph.getEdges());
		    this.node=new HashMap<String,RDFNode>();
		    this.mongraph=graph;
		    Collection<RDFNode> nod=graph.getVertices();
		    Iterator<RDFNode> ite=nod.iterator();
		    while(ite.hasNext())
            {
		    	RDFNode a=ite.next();
                nodes.add(a);
               // System.out.println("NODE:"+a.toString()+"------>"+a);               
            }
		    Collection<Statement> h=graph.getEdges();
	        Iterator<Statement> i=h.iterator();

             while(i.hasNext())
             {
           	  Statement s=i.next();
                 edges.add(s);
                 node.put(graph.getSource(s).toString(),graph.getSource(s));
                 node.put(graph.getDest(s).toString(),graph.getDest(s));
              }		   
	  }//end constructeur
	  
	  public Map<String,RDFNode> mynode(){
		  return node;
	  }
	  
	  public List<RDFNode>Node(){
		  return nodes;
	  }
	  
	  public List<Statement>Edge(){
		  return edges;
	  }
	  
	  public RDFNode getNode(String a){		         
		  return node.get(a) ;
	  }	  
	  
	  public void execute(RDFNode source){
		    settledNodes = new HashSet<RDFNode>();
		    unSettledNodes = new HashSet<RDFNode>();
		    distance = new HashMap<RDFNode, Integer>();
		    predecessors = new  HashMap<RDFNode, RDFNode>();
		    distance.put(source, 0);
		    unSettledNodes.add(source);
		    while (unSettledNodes.size() > 0){
		    	  RDFNode node = getMinimum(unSettledNodes);
			      settledNodes.add(node);
			      unSettledNodes.remove(node);
			      findMinimalDistances(node);
		    }
	  }
	  
	  private void findMinimalDistances(RDFNode node){
		   
		    Collection<RDFNode> adjacentNodes=mongraph.getNeighbors(node);
		    Iterator<RDFNode> i=adjacentNodes.iterator();
		    List<RDFNode> voisin =new  ArrayList<RDFNode>();
		    while(i.hasNext())
		    voisin.add(i.next());
                     
		    for (RDFNode target : voisin){
			      if (getShortestDistance(target) > getShortestDistance(node)+ 1){
			        distance.put(target, getShortestDistance(node)
			            + 1);
			        predecessors.put(target, node);
			        unSettledNodes.add(target);
			      }
		    }
	  }
	
	  private RDFNode getMinimum(Set<RDFNode>  vertexes){
			  RDFNode minimum = null;
		    for (RDFNode vertex : vertexes) {
		      if (minimum == null) {
		        minimum = vertex;
		      } else {
		        if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
		          minimum = vertex;
		        }
		      }
		    }
		    return minimum;
		  }
		  private int getShortestDistance(RDFNode destination) {
		    Integer d = distance.get(destination);
		    if (d == null) {
		      return Integer.MAX_VALUE;
		    } else {
		      return d;
		    }
		  }
		  
		//Retourne l'ensemble des noeud qui constitue le chemin
		public LinkedList<RDFNode> getPath(RDFNode target) {
			    LinkedList<RDFNode> path = new LinkedList<RDFNode>();
			    RDFNode step = target;
			    // check if a path exists
			    if (predecessors.get(step) == null) {
			      return null;
			    }
			    path.add(step);
			    while (predecessors.get(step) != null) {
			      step = predecessors.get(step);
			      
			      path.add(step);
			    }
			    // Put it into the correct order
			    Collections.reverse(path);
			    return path;
		}			

}

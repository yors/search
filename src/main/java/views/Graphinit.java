/*package views;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.FileManager;

import edu.uci.ics.jung.graph.Graph;

public class Graphinit {
	 private  org.graphstream.graph.Graph graph;
	 
	   List<String>res;
	   Graph<RDFNode, Statement> g;
	   JFrame fenetre;
	   String lien;
	   List<String> Ressource;
	   Shortway dijkstra;
	   public Graphinit(String mot,org.graphstream.graph.Graph graphfinal, JFrame fenetre, List<String> Ress)
	   {
	      this.lien=mot;
	     
	      this.graph=graphfinal;
	      this.fenetre=fenetre;
	      this.Ressource=Ress;
	   }
	public void execute()
	   {
		
		
		
		
		 Model model = FileManager.get().loadModel(lien);
	      Graph<RDFNode, Statement> g = new JenaJungGraph(model);
	      init(g);
	      List<RDFNode>Resource=new ArrayList<RDFNode>();
	      for(String vf: Ressource)
	      {
	         Resource.add(dijkstra.getNode(vf));
	      }
		 Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, "result", "length");
		 dijkstra.init(graph);
		 dijkstra.setSource(graph.getNode(Ressource.get(0)));
		 dijkstra.compute();
		 
		        
		 // Print the lengths of all the shortest paths
		 for (Node node : graph)
		     System.out.printf("%s->%s:%6.2f%n", dijkstra.getSource(), node, dijkstra.getPathLength(node));
		     int i=0;   
		for(i=0;i<Ressource.size();i++)
		{
			 if(i!=0)
			 {
		 for (Node node : dijkstra.getPathNodes(graph.getNode(Ressource.get(i))))
		     node.addAttribute("ui.style", "fill-color: blue;");
		}
		}*/
		        
		 // Color in red all the edges in the shortest path tree
		// for (Edge edge : dijkstra.getTreeEdges())
		    // edge.addAttribute("ui.style", "fill-color: red;");
		 
		 // Print the shortest path from A to B
		// System.out.println(dijkstra.getPath(graph.getNode("B"));
		 
		 // Build a list containing the nodes in the shortest path from A to B
		 // Note that nodes are added at the beginning of the list
		 // because the iterator traverses them in reverse order, from B to A
		 /*List <Node> list1 = new ArrayList<Node>();
		 for (Node node : dijkstra.getPathNodes(graph.getNode("B")))
		     list1.add(0, node);*/
		 
		 // A shorter but less efficient way to do the same thing
		// List<Node> list2 = dijkstra.getPath(graph.getNode("B")).getNodePath();
	     //
	      //Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
	     // viewer.enableAutoLayout();
	     // View view = viewer.addDefaultView(false);
	      
	     // fenetre.add((Component) view);
	     // fenetre.setVisible(true);
	      
	 //  }
	/*public void CreateNoeud(String c,org.graphstream.graph.Graph f)
	   {
	      try
	      {
	         Node e=f.getNode(c);
	         String s=e.toString();
	         
	      }
	      catch(Exception e){
	         
	         Node a=f.addNode(c);
	         a.setAttribute("ui.label",c);
	         
	      }
	   }
	public void CreateEdge(String nom,String source,String Dest,String name)
	   {
	      
	      Edge ab=graph.addEdge(nom,source,Dest);
	      ab.setAttribute("ui.label",name,true);
	   }
	   public void init(Graph<RDFNode, Statement> g)
	   {
	      Collection<Statement> h=g.getEdges();
	      Iterator<Statement> i=h.iterator();
	      int j=0;
	      graph= new SingleGraph("Le plus court chemin");
	      while(i.hasNext())
	      {
	         Statement s=i.next();
            
	         RDFNode source= g.getSource(s);
	         RDFNode destination= g.getDest(s);
	         CreateNoeud(source.toString(),graph);
	         CreateNoeud(destination.toString(),graph);
	        // System.out.println(source.toString());
	       //  System.out.println(destination.toString());
	        CreateEdge(j+"",source.toString(),destination.toString(),s.getPredicate().toString());
	         j++;
	         
	      }
	     
	      graph.addAttribute("ui.quality");
	      graph.addAttribute("ui.antialias");
	      graph.addAttribute("ui.stylesheet",
	      
	      " graph {fill-color: #D8BFD8;}"+
	      "edge{shape: L-square-line; fill-color:white ;arrow-size: 5px, 10px;size:2px;}"+
	      "node {text-color:yellow;shadow-mode:plain;size: 20px, 21px; shape: box;fill-color:red;stroke-mode: plain;stroke-color: yellow;}"
	      
	      );
	   }

}*/

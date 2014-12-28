package views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.VCARD;

import edu.uci.ics.jung.graph.Graph;

public class SousGraph {
	private  org.graphstream.graph.Graph graph;
	private  org.graphstream.graph.Graph graphinit;
	private  org.graphstream.graph.Graph graphfinal;
	private Graph<RDFNode, Statement> g;
	private List<Collection<Statement>> Statements;
	private Set<List<String>> chemin;
	private  Map<RDFNode, Integer> busyNode;
	private  Model model;
	private JTabbedPane myonglet;
	private  Model newmodel;
	private List<Node> mode;
	List<RDFNode>Resource;
	List<String>res;
	JPanel fenetre;
	JFrame fenetre1;
	List<RDFNode> Ressource;
	String lien;
	Shortway dijkstra;
	boolean f;

	public SousGraph(String mot,JPanel fenetre)
	{
		this.lien=mot;
		this.fenetre=fenetre;
	}
	
	public SousGraph(String mot,List<String>Resource,org.graphstream.graph.Graph graphfinal,JFrame fe, JTabbedPane onglet,boolean h)
	{
		this.lien=mot;
		f=h;
		fenetre1=fe;
		this.res=Resource;
		this.graphfinal=graphfinal;
		this.myonglet=onglet;
		model = FileManager.get().loadModel(lien);
	}

	public SousGraph(org.graphstream.graph.Graph graphfinal,JTabbedPane onglet,Model model,boolean affiche)
	{
		this.graphfinal=graphfinal;
		this.myonglet=onglet;
		f=affiche;
		System.out.println(" sparql version ERICCC: "+model.size());
		this.model= model;
		GRAPHINIT();
	}

	public void exec()
	{
		execute(model);
	}

	public void execute(Model model)
	{
		g = new JenaJungGraph(model);
		chemin=new HashSet<List<String>>();
		// System.out.println("NOMBRE DE NOEUD :"+g.getEdgeCount());
		graph = new SingleGraph("Le plus court chemin");
		newmodel = ModelFactory.createDefaultModel();
		busyNode=new HashMap<RDFNode,Integer>();
		mode=new ArrayList<Node>();

		init(g);

		dijkstra = new Shortway(g);
		// System.out.println("NODE:"+dijkstra.mynode());
		Resource=new ArrayList<RDFNode>();
		if(f)
		{
			for(String vf: res)
			{
				//System.out.println(vf+" "+"--YORICK---->"+dijkstra.getNode(vf));
				Resource.add(dijkstra.getNode(vf));
			}

			int i,j;

			for(i=0;i<Resource.size();i++)
			{
				for(j=0;j<Resource.size();j++)
				{
					chemin(Resource.get(i),Resource.get(j),graph);
				}
			}
		}
		ajoutarreteInstance();
		// parcours();
		//System.out.println("NOMBRE DE STATEMENT:"+ newmodel.size());

		//LIGNE ERIC
		if(!f)
		{
			Viewer viewer = new Viewer(graphfinal, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
			viewer.enableAutoLayout();
			View view = viewer.addDefaultView(false);
			JPanel panel=new JPanel();
			panel.setLayout(new BorderLayout());
			panel.add(view,BorderLayout.CENTER);
			myonglet.addTab("Resultat de La recherche", panel);

		}
	}

	//Renvoie le grpah initial

	public  void  ajoutarreteInstance()
	{
		int i;
		Statements= new ArrayList<Collection<Statement>>();
		for(i=0;i<Resource.size();i++)
		{
			Collection<Statement> a=g.getOutEdges(Resource.get(i));
			Iterator<Statement> d=a.iterator();
			while(d.hasNext())
			{
				Statement f=d.next();

				try
				{
					String v=f.getObject().toString();
					String  t=transform(v,"http");
					if(t.isEmpty()){t=transform(v,"#");}
					Node e=graphfinal.addNode(v);
					e.setAttribute("ui.label",t);
					Edge ab1=graphfinal.addEdge(f.getSubject().toString()+v,f.getSubject().toString(),v,true);
					String h=f.getPredicate().toString();
					String[] pass=h.split("#");
					String t2=pass[1]; 
					ab1.setAttribute("ui.label",t2);
				}
				catch(Exception e){
					System.out.println("Noeud:"+i+" existant déjà");
				} 
			}
		}

		//System.out.println("STATEMENTTTTTTTTTTTTTTTTT:"+Statements);
	}

	public  void GRAPHINIT()
	{
		// model = FileManager.get().loadModel(lien);
		g = new JenaJungGraph(model);
		graphinit = new SingleGraph("Graph rdf");
		initGRPAH(g);

		Viewer viewer = new Viewer(graphinit, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		View view = viewer.addDefaultView(false);
		JPanel panel=new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(view,BorderLayout.CENTER);


		myonglet.addTab("Resultat de La recherche", panel);
	}

	//Cree un fichier RDF	
	public void ecrireFileRDF(String fichier){

		try (OutputStream out = new BufferedOutputStream(new FileOutputStream(fichier),1024)) {
			newmodel.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initGRPAH(Graph<RDFNode, Statement> g)
	{
		Collection<Statement> h=g.getEdges();
		Iterator<Statement> i=h.iterator();
		int j=0;

		while(i.hasNext())
		{
			Statement s=i.next();
			RDFNode source= g.getSource(s);
			RDFNode destination= g.getDest(s);
			String dest=destination.toString().replaceAll("[^\\w]","");
			String sourc=source.toString().replaceAll("[^\\w]","");
			CreateNoeud(sourc,graphinit);
			CreateNoeud(dest,graphinit);
			CreateEdge(j+"",sourc,dest,s.getPredicate().toString(),graphinit);
			j++;
		}
		graphinit.addAttribute("ui.quality");
		graphinit.addAttribute("ui.antialias");
		graphinit.addAttribute("ui.stylesheet",

				" graph {fill-color: #D8BFD8;}"+
						"edge{shape: L-square-line; fill-color:white ;arrow-size: 5px, 10px;size:2px;}"+
						"node {text-color:black;shadow-mode:plain;size: 20px, 21px; shape: box;fill-color:red;stroke-mode: plain;stroke-color: black;}"

				);
	}

	public void chemin(RDFNode a,RDFNode b,org.graphstream.graph.Graph graph1)
	{
		List<String> chem=new ArrayList<String>();
		try
		{
			dijkstra = new Shortway(g);
			dijkstra.execute(a);
			LinkedList<RDFNode> path = dijkstra.getPath(b);
			//System.out.println(path.size());

			int i,teste;

			if(path.isEmpty())
			{}
			else
			{
				for(i=0;i<path.size();i++)
				{
					teste=0;
					if(i!=path.size()-1 && i+1<path.size())
					{
						RDFNode z=dijkstra.getNode(path.get(i).toString());
						RDFNode z1=dijkstra.getNode(path.get(i+1).toString());
						Statement e1=g.findEdge(z, z1);

						if(e1.isReified()){}
						else
						{
							//System.out.println("taille:"+path.size());
							for(List<String> st : chemin)
							{
								int c=st.size();
								if( st.contains(a.toString()) && st.contains(b.toString()))
								{

									if(st.get(0).equals(b.toString() ) && st.get(c-1).equals(a.toString() ))
										// System.out.println("L'AJOUT DE "+" "+a.toString()+" "+"CREE UN CYCLE"+"avec"+" "+b.toString());
										teste=1;
								}
							}

							if(teste==0)
							{
								if(!chem.contains(a))
									chem.add(a.toString());
								if(!chem.contains(b))

									chem.add(b.toString());
								CreateNoeud(path.get(i).toString(),graphfinal);
								CreateNoeud(path.get(i+1).toString(),graphfinal);
								Edge ab=graph1.addEdge(path.get(i).toString()+path.get(i+1).toString(), path.get(i).toString(),path.get(i+1).toString());

								//Construction 
								Edge ab1=graphfinal.addEdge(path.get(i).toString()+path.get(i+1).toString(), path.get(i).toString(),path.get(i+1).toString(),true);

								String  t=transform(e1.getPredicate().toString(),"http"),t1="";
								String h="";

								h=e1.getPredicate().toString();
								String[] pass=h.split("#");
								String t2=pass[1]; 
								ab.setAttribute("ui.label",t2);
								ab1.setAttribute("ui.label",t2);

								//Construction du NOUVEAU MODEL
								String resource    =path.get(i).toString().replaceAll("[^\\w]","");
								String predicat    =e1.getPredicate().toString();
								String litteral   = path.get(i+1).toString();
								String litn;
								litteral=litteral.replaceAll("[^\\w]","");
								//litn=transform( litn,"http");
								Resource johnSmith = model.createResource(resource);
								newmodel.add(model.getResource(resource),model.getProperty(predicat),litteral);
							}
							else
								System.out.println("AJOUT imposible"); 						

						}
					}

				}
			}
		}
		catch(Exception e){}

		chemin.add(chem);
	}

	public Model getNewmodel()
	{
		return newmodel;
	}

	public void parcours(){
		StmtIterator iter = newmodel.listStatements();

		// affiche l'objet, le prédicat et le sujet de chaque déclaration
		while (iter.hasNext()) {

			Statement stmt      = iter.nextStatement();  // obtenir la prochaine déclaration
			Resource  subject   = stmt.getSubject();     // obtenir le sujet
			Property  predicate = stmt.getPredicate();   // obtenir le prédicat
			RDFNode   object    = stmt.getObject();      // obtenir l'objet
			// System.out.print(subject.toString());
			System.out.print(" " + " ");
			if (object instanceof Resource) {
				System.out.print("");
			} else {
				// l'objet est un littéral
				System.out.print(" \"" + "\"");
			}
			System.out.println(" ");
		}
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
			// CreateEdge(j+"",source.toString(),destination.toString(),s.getPredicate().toString());
			j++;

		}
		graphfinal.addAttribute("ui.quality");
		graphfinal.addAttribute("ui.antialias");
		graphfinal.addAttribute("ui.stylesheet",

				" graph {fill-color: #D8BFD8;}"+
						"edge{shape: L-square-line; fill-color:white ;arrow-size: 5px, 10px;size:2px;}"+
						"node {text-color:black;shadow-mode:plain;size: 20px, 21px; shape: box;fill-color:red;stroke-mode: plain;stroke-color: black;}"

				);
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
		graph.addAttribute("ui.stylesheet",

				" graph {fill-color: #D8BFD8;}"+
						"edge{shape: L-square-line; fill-color:white ;arrow-size: 5px, 10px;size:2px;}"+
						"node {text-color:black;shadow-mode:plain;size: 20px, 21px; shape: box;fill-color:red;stroke-mode: plain;stroke-color: black;}"

				);
	}
	public void CreateEdge(String nom,String source,String Dest,String name,org.graphstream.graph.Graph graph)
	{
		String  t=transform(name,"#");
		// if(t==null){t=transform(name,"#");}
		Edge ab=graph.addEdge(nom,source,Dest,true);
		ab.setAttribute("ui.label",t);
	}

	public String transform(String c,String delimit)
	{
		if(delimit.equals("http"))
		{
			String[] pass=c.split(delimit);
			String t =""; 
			try
			{
				t =pass[0];
				t=t.replaceAll("[^\\w]","");
				return t;
			}
			catch(Exception e){
				return c;
			}
		}
		else
		{
			String[] pass=c.split(delimit);
			String t="";
			try
			{
				t=pass[1];
				return t;
			}
			catch(Exception e){
				return c;
			}
		}
	}

	public void CreateNoeud(String c,org.graphstream.graph.Graph f)
	{
		String t;
		t=c.replaceAll("[^\\w]","");
		String[] pass1=t.split("#");
		try
		{
			Node e=f.getNode(c);
			String s=e.toString();
		}
		catch(Exception e){

			t=transform( c,"http");
			if(t.isEmpty()){t=transform( c,"#");}

			Node a=f.addNode(c);
			a.setAttribute("ui.label",t);
		}
	}
}

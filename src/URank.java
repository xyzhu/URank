import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;



import edu.uci.ics.jung.algorithms.scoring.BarycenterScorer;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.algorithms.scoring.DegreeScorer;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.algorithms.scoring.HITS;
import edu.uci.ics.jung.algorithms.scoring.KStepMarkov;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedSparseGraph;


public class URank {

	/**
	 * @param args
	 */
	private class myEdge{
		int number;
		int weight;
		myEdge(int number,int weight)
		{
			this.number=number;
			this.weight=weight;
		}
	}
	 DirectedSparseGraph<String,myEdge> graph=new DirectedSparseGraph<String,myEdge>();
	
	 
	 int edge_no=0;
	 int graph_size=0;
	 
	 public HashMap<String,Double> calculateHubAuth(String alg)
		{
			HashMap<String,Double> result=new HashMap<String,Double>();
			HITS<String,myEdge> hits=new HITS<String,myEdge>(graph);
			hits.evaluate();						
				for(Iterator i=graph.getVertices().iterator();i.hasNext();)
				{
					String verticeName=(String)i.next();									
					Double value;
					if(alg.equals("HITS-authorities")){value=(Double)hits.getVertexScore(verticeName).authority;}
					else{value=(Double)hits.getVertexScore(verticeName).hub;}
					result.put(verticeName,value);
				}													
				return result;					
			
		} 
	 
	 private HashMap<String,Double > calculateClosenessCentrality() {			
			HashMap<String,Double> result=new HashMap<String,Double>();
			ClosenessCentrality<String,myEdge> cc=new ClosenessCentrality<String,myEdge>(graph);			
			for(Iterator i=graph.getVertices().iterator();i.hasNext();)
			{
				String verticeName=(String)i.next();
				Double value;
				value=cc.getVertexScore(verticeName);
				result.put(verticeName,value);
				
				
			}
			return result;
	 }
	 
	 private HashMap<String, Double> calculateBarycenterScorer() {
			// TODO Auto-generated method stub
			HashMap<String,Double> result=new HashMap<String,Double>();
			BarycenterScorer<String,myEdge> bs=new BarycenterScorer<String,myEdge>(graph);
			for(Iterator i=graph.getVertices().iterator();i.hasNext();)
			{
				String verticeName=(String)i.next();
				Double value;
				value=-1.0;
				value=bs.getVertexScore(verticeName);
				result.put(verticeName,value);
				
				
			}
			return result;
		}
	 
	 private HashMap<String, Double> calculateBetweennessCentrality() {
			// TODO Auto-generated method stub
			HashMap<String,Double> result=new HashMap<String,Double>();
			BetweennessCentrality<String,myEdge> bc=new BetweennessCentrality<String,myEdge>(graph);
			for(Iterator i=graph.getVertices().iterator();i.hasNext();)
			{
				String verticeName=(String)i.next();
				Double value;
				value=-1.0;
				value=bc.getVertexScore(verticeName);
				result.put(verticeName,value);
				
				
			}
			return result;
		}
	 
	 private HashMap<String, Double> calculateEigenvectorCentrality() {
			// TODO Auto-generated method stub
			HashMap<String,Double> result=new HashMap<String,Double>();
			EigenvectorCentrality ec=new EigenvectorCentrality(graph);
			ec.evaluate();			
			for(Iterator i=graph.getVertices().iterator();i.hasNext();)
			{
				String verticeName=(String)i.next();
				Double value;
				value=-1.0;
				value=(Double) ec.getVertexScore(verticeName);
				result.put(verticeName,value);	
			}	
			return result;
		}
	 
	 private HashMap<String, Double> calculatePageRank() {
			// TODO Auto-generated method stub
			HashMap<String,Double> result=new HashMap<String,Double>();
			PageRank<String,myEdge> pr=new PageRank<String,myEdge>(graph,0.15);
			pr.evaluate();
			for(Iterator i=graph.getVertices().iterator();i.hasNext();)
			{
				
				String verticeName=(String)i.next();
				Double value;
				value=-1.0;
				value=pr.getVertexScore(verticeName);
				result.put(verticeName,value);			
			}
			return result;
			
		}
	 
	 private HashMap<String, Double> calculateKStepMarkov() {
			// TODO Auto-generated method stub
			HashMap<String,Double> result=new HashMap<String,Double>();
			KStepMarkov<String,myEdge> km=new KStepMarkov<String,myEdge>(graph,5);
			km.evaluate();
			for(Iterator i=graph.getVertices().iterator();i.hasNext();)
			{
				
				String verticeName=(String)i.next();
				Double value;
				value=-1.0;
				value=km.getVertexScore(verticeName);
				result.put(verticeName,value);			
			}
			return result;
		}
	 
	 private HashMap<String, Double> calculateDegreeScorer(String degreeType) {
			// TODO Auto-generated method stub
			HashMap<String,Double> result=new HashMap<String,Double>();
			DegreeScorer<String> ds=new DegreeScorer<String>(graph);
			for(Iterator i=graph.getVertices().iterator();i.hasNext();)
			{
				
				String verticeName=(String)i.next();
				Double value;
				value=-1.0;
				if(degreeType.equals("Degree")){value=(double)ds.getVertexScore(verticeName);}
				else if(degreeType.equals("inDegree")){value=(double)graph.inDegree(verticeName);}
				else if(degreeType.equals("outDegree")){value=(double)graph.outDegree(verticeName);}
				result.put(verticeName,value);			
			}
			return result;		
		}
		
	 private HashMap<String, Double> calculateVoltageScorer() {
			// TODO Auto-generated method stub
			HashMap<String,Double> result=new HashMap<String,Double>();
			//VoltageScorer<AbstractMetricSource,String> km=new VoltageScorer<AbstractMetricSource,String>(graph);
		    return null;
		}
	private HashMap<String, Double> calculateReferenceTimes(String type)
	{
		HashMap<String,Double> result=new HashMap<String,Double>();
		HashSet<myEdge> InEdge=new HashSet<myEdge>();
		HashSet<myEdge> OutEdge=new HashSet<myEdge>();
		for(Iterator i=graph.getVertices().iterator();i.hasNext();)
		{
			String vertexName=(String)i.next();
			Double times=0.0;			
			InEdge.addAll(graph.getInEdges(vertexName));
			OutEdge.addAll(graph.getOutEdges(vertexName));
			if(type.equals("In")||type.equals("Total")){
			for(Iterator j=InEdge.iterator();j.hasNext();)
			{
				myEdge edge=(myEdge)j.next();
				times+=edge.weight;
			}
			}
			if(type.equals("Out")||type.equals("Total")){
			for(Iterator k=OutEdge.iterator();k.hasNext();)
			{
				myEdge edge=(myEdge)k.next();
				times+=edge.weight;
			}
			}
			InEdge.clear();
			OutEdge.clear();
			result.put(vertexName, times);
		}
			
		return result;
	}
    private  void processLine(String line,String filter)
    {
    	String source,destination,tempstr;
    	int weight=0;
    	int i,j,k;
    	int l=0,m=0;
    	boolean source_flag=false;
    	boolean destination_flag=false;
    	i=line.indexOf(',');
    	j=line.indexOf(',', i+1);
    	k=line.indexOf(',', j+1);
    	source=line.substring(0, i);
    	destination=line.substring(i+1, j);    	
    	tempstr=line.substring(j+1, k);
    	weight=Integer.parseInt(tempstr);
 //   	System.out.println(source+"         "+destination);
    	if(source.contains(".java"))
    	{
    		
      	     if(!graph.containsVertex(source)){graph.addVertex(source);}
      	     source_flag=true;
      	     
    	}
    	
    	
    	if(destination.contains(".java"))
    	{
    		
     	    if(!graph.containsVertex(destination)){graph.addVertex(destination);}
     	    destination_flag=true;
    	}
    	
    	
    	if(source_flag&&destination_flag)
    	{
    	   //System.out.println(source+"       "+destination+"   "+weight);
   	       if(graph.findEdge(source, destination)==null){
   	    	   edge_no++;
   	    	   myEdge tempEdge=new myEdge(edge_no, weight);
   	    	   graph.addEdge(tempEdge, source, destination);}
    	}
    	
    	
    }
    
    
    private  void outputRank(String projectname)
    {
    	/*Output all rank values of the software*/
		HashMap<String,Double>[] ScoreArray;		
		ScoreArray=new HashMap[8];				
		ScoreArray[0]=calculateHubAuth("HITS-hubs");
		ScoreArray[1]=calculateHubAuth("HITS-authorities");	
//		ScoreArray[2]=calculateBetweennessCentrality();
//		ScoreArray[3]=calculatePageRank();
		ScoreArray[2]=calculateDegreeScorer("Degree");
		ScoreArray[3]=calculateDegreeScorer("inDegree");
		ScoreArray[4]=calculateDegreeScorer("outDegree");
		ScoreArray[5]=calculateReferenceTimes("Total");
		ScoreArray[6]=calculateReferenceTimes("In");
		ScoreArray[7]=calculateReferenceTimes("Out");
	
		try{
			OutputStream f1=new FileOutputStream(projectname+".csv",false);			
			String str="file_name,HITS_hubs,HITS_authorities,DegreeScorer,InDegree,OutDegree,ReferenceTimes,InReference,OutReference\n";
			f1.write(str.getBytes());
			str="";			
			int m=0;
			int num_sig = ScoreArray.length;
			for(Iterator i=ScoreArray[0].keySet().iterator();i.hasNext();)
			{
				String class_name=(String)i.next();				
				str=class_name;
				if(class_name.contains(".java")){
				for(int j=0;j<num_sig;j++)
				{
					Double t=ScoreArray[j].get(class_name);					
					str=str+","+t.toString();
					m++;
				}										
				   str=str+"\n";
				   f1.write(str.getBytes());}
		    }
			f1.close();
			System.out.println("m is:"+m);
		}
		catch(Exception e)
		{
			System.out.println("error1");
		}
		/*end output*/
    }
	private  void readFileandCreateGraph(String filename,String filter)
    {
    	File file=new File(filename);
		BufferedReader reader = null;
		try {
			 reader = new BufferedReader(new FileReader(file));
			   String tempString = null;
			   int line = 1;
			   while ((tempString = reader.readLine()) != null){
			//   System.out.println("line " + line + ": " + tempString);
		       	if(line>1){processLine(tempString,filter);} 
			    line++;		       	 
		  	}
			   reader.close();
			} 
		   catch (IOException e) {
			e.printStackTrace();
			} finally {
			    if (reader != null){
			    						try {reader.close();} catch (IOException e1) {}
			    					}
						}
			}
	
	
	public static void main(String[] args) {
		String file_path=args[0];
		String file_name=args[1];
		String out_path=args[2];
		//String pathFilter="/home/xyzhu/change-prediction/repos/short/ant_short/";
		String pathFilter="";
		URank rank=new URank();
		String full_file_name = file_path+file_name+"_FileDependencies.csv";
        rank.readFileandCreateGraph(full_file_name,pathFilter);
        rank.outputRank(out_path+file_name);
        
		System.out.println("calculation finished!");

	}
}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Queue;
import java.util.Stack;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;


public class LongestString {
	String [] titles;
	ArrayList<Integer> roots;
	//ArrayList<String> indexedTitles = new ArrayList<String>();
	Hashtable<String,ArrayList<String>> indexedBeginnings = new Hashtable<String,ArrayList<String>>();
	Hashtable<String,ArrayList<String>> indexedEnds = new Hashtable<String,ArrayList<String>>();
	Integer [][] adjacencyMatrix; 
	String longestString;
	public LongestString (String path){
		openAndReadFile(path);
		adjacencyMatrix= new Integer[titles.length][titles.length];
		indexTitles();
		//System.out.println("Beginnings: "+indexedBeginnings);
		//System.out.println("Ends: "+indexedEnds);
		createMatrix();
		//System.out.println(toString(adjacencyMatrix,"src\\matrixtitles.txt"));
		findRoots();
		longestString=longestString(roots);
		System.out.println(longestString);
	}
	public void openAndReadFile(String path){
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			//this.titles = br.readLine().toLowerCase().split("¥([^¥]+)*¥+");
			this.titles = br.readLine().toLowerCase().split(", ");
			//System.out.println(toString(titles));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static <E> String toString(E[] array){
		String str=new String();
		for (int i=0;i<array.length;i++){
			str+=array[i]+"\n";
		}
		return str;
	}
	public String toString(Integer[][] array, String path){
		String str=new String();
		try {
			BufferedWriter pw=new BufferedWriter(new FileWriter(path));

			
			for(int i=0;i<titles.length;i++){
				str+="\t"+titles[i]+" "+i;
			}
			str+="\n";

			for (int i=0;i<array.length;i++){
				for(int j=0;j<array[0].length;j++){
					if(array[i][j]!=null){
						str+=array[i][j]+"";
					}
					else {
						str+="-";
					}
					str+="\t";
				}
				str+="\n";
			}
			pw.write(str);
			pw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return str;
	}
	public void indexTitles(){
		String [] titleWords;
		String end, beginning;

		for (int i=0;i<titles.length;i++){
			titleWords = titles[i].split(" ");
			if(titleWords.length>1){
				int currentIndexEnd = titles[i].indexOf(" ");
				int currentIndexBeginning = titles[i].lastIndexOf(" ");
				end = titles[i].substring(currentIndexEnd+1, titles[i].length());
				beginning = titles[i].substring(0, currentIndexBeginning);
				for (int j = 1; j<titleWords.length;j++){
					if(indexedEnds.get(titles[i])==null){
						indexedEnds.put(titles[i], new ArrayList<String>());

					}
					if(indexedBeginnings.get(titles[i])==null){
						indexedBeginnings.put(titles[i], new ArrayList<String>());
					}
					indexedEnds.get(titles[i]).add(end);
					indexedBeginnings.get(titles[i]).add(beginning);
					if(j+1<titleWords.length){
						end = end.substring(end.indexOf(" ")+1, end.length());
						beginning = beginning.substring(0, beginning.lastIndexOf(" "));
					}

				}
			}
		}

	}
	public void createMatrix(){
		ArrayList<String> contained;
		for (int i=0;i<titles.length;i++){
			if(indexedEnds.get(titles[i])!=null){
				for(int j=0;j<titles.length;j++){
					if(indexedBeginnings.get(titles[j])!=null){
						contained = new ArrayList<String>(indexedEnds.get(titles[i]));
						//System.out.println(contained);
						//System.out.println(indexedBeginnings.get(titles[j]));
						contained.retainAll(indexedBeginnings.get(titles[j]));
						//System.out.print(contained);
						if(contained.size()>0){
							adjacencyMatrix[i][j] = contained.get(0).length();
						}
					}
				}
			}
			//System.out.println();
		}
	}
	public String longestString(ArrayList<Integer> roots){

		ArrayList<String> strings = new ArrayList<String>();
		Integer distance=0; 

		for(int i=0; i<roots.size();i++){
			String result = new String();
			longestString(roots.get(i), strings, result,0);

		}
		int maxSize=0;
		String longestString = new String();
		for(String s:strings){
			if(s.length()>maxSize){
				maxSize=s.length();
				longestString=s;
			}
		}
		return longestString;
	}
	public String longestString(int v, ArrayList<String> strings, String currentString, int weight){

		currentString=currentString.substring(0, currentString.length()-weight)+titles[v];
		if(!vertexHasEdges(v)){
			strings.add(currentString);
			return currentString;
		}
		else {
			for(int i=0;i<adjacencyMatrix[v].length;i++){
				if(adjacencyMatrix[v][i]!=null){
					longestString(i,strings,currentString,adjacencyMatrix[v][i]);
				}
			}			
		}
		return null;
	}
	public void findRoots() {
		int i=0;
		while(!vertexHasEdges(i)){
			i++;
		}
		ArrayList<Integer> initialRoots=DFS(i);
		ArrayList<Integer> tmp;
		ArrayList<Integer> roots = initialRoots;
		//ArrayList<Integer> roots=DFS(18);
		int minRoots=initialRoots.size();
		for(int j=0;j<initialRoots.size();j++){
			tmp=DFS(initialRoots.get(j));
			if(minRoots>tmp.size()){
				roots = tmp;
				minRoots=roots.size();
			}
		}
		this.roots=roots;
		System.out.println(roots);
	}
	public ArrayList<Integer> DFS(int vertex){
		int v = vertex;
		Stack<Integer> stack = new Stack<Integer>();
		Boolean [] discovered = new Boolean[adjacencyMatrix.length];
		ArrayList<Integer> dfs = new ArrayList<Integer>();
		ArrayList<Integer> possibleRoots = new ArrayList<Integer>();
		Arrays.fill(discovered, false);
		stack.push(vertex);
		int counter=0;
		while(!stack.isEmpty()){
			v=stack.pop();
			if(!discovered[v]){
				dfs.add(v);
				discovered[v]=true;
				for(int i=0;i<adjacencyMatrix.length;i++){
					if(adjacencyMatrix[v][i]!=null){
						stack.push(i);
					}
				}
			}
			counter++;
		}
		Integer i=0;
		for(Boolean b: discovered){
			if(!b && vertexHasEdges(i)){
				possibleRoots.add(i);
				//System.out.print("Vertex "+titles[i]+": "+b+" ");
			}
			i++;
		}

		if(!possibleRoots.contains(vertex)){
			possibleRoots.add(vertex);	
		}

		//System.out.println(dfs);
		return possibleRoots;

	}
	public boolean vertexHasEdges(int vertex){
		for(int i=0;i<titles.length;i++){
			if(adjacencyMatrix[vertex][i]!=null){
				return true;
			}
		}
		return false;
	}

	public static void main (String [] args){
		LongestString ls = new LongestString("src\\test.txt");
	}
}



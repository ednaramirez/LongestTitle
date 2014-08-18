import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;


public class LongestString {
	String [] titles;
	//ArrayList<String> indexedTitles = new ArrayList<String>();
	Hashtable<String,ArrayList<String>> indexedBeginnings = new Hashtable<String,ArrayList<String>>();
	Hashtable<String,ArrayList<String>> indexedEnds = new Hashtable<String,ArrayList<String>>();
	Integer [][] adjacencyMatrix; 
	public LongestString (String path){
		openAndReadFile(path);
		adjacencyMatrix= new Integer[titles.length][titles.length];
		indexTitles();
		//System.out.println("Beginnings: "+indexedBeginnings);
		//System.out.println("Ends: "+indexedEnds);
		createMatrix();
		System.out.println(toString(adjacencyMatrix));

	}
	public void openAndReadFile(String path){
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			this.titles = br.readLine().split(", ");
			//System.out.println(toString(titles));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String toString(String[] array){
		String str=new String();
		for (int i=0;i<array.length;i++){
			str+=array[i]+"\n";
		}
		return str;
	}
	public String toString(Integer[][] array){
		String str=new String();
		for(int i=0;i<titles.length;i++){
			str+="\t"+titles[i];
		}
		str+="\n";
		for (int i=0;i<array.length;i++){
			str+=titles[i]+"\t";
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
	public static void main (String [] args){
		LongestString ls = new LongestString("src\\test.txt");
	}
}

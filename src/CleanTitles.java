import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class CleanTitles {
	public static void toString(String[] str){
		for(String s: str){
			System.out.println(s);
		}
	}
	public static void main (String [] args){
		try {
			BufferedReader br = new BufferedReader(new FileReader("src\\titles.txt"));
			String strTitles=br.readLine();
			String [] titles = strTitles.split("¥([^¥]+)*¥+");
					
			toString(titles);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

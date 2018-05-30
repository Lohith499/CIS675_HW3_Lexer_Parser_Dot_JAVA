import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LexAndParser {
	private static BufferedReader br;
	public static void main(String[] args) throws IOException {
		lex l1 = new lex();
		// Fetching input file
		File file = new File("C:\\Users\\lohit\\Desktop\\input.txt");
		br = new BufferedReader(new FileReader(file));
		String st;
		System.out.println("Input");
		while ((st = br.readLine()) != null) {
			System.out.println(st);
		}
		int count=1;
		// Initiating List variable to capture all the tokens from lexer
		Map<Integer,String> Tokens = new HashMap<Integer, String>();
		// Printing output content
		System.out.println("Output of lexer(tokens)");
		br = new BufferedReader(new FileReader(file));
		while ((st = br.readLine()) != null) {
			st = st.replaceAll("<.*>", " ID "); // Replacing all the text in between ".." as ID
			st = st.replaceAll("\".*\"", " ID "); // Replacing all the text in between < > as ID
			st = st.replaceAll("/\\*.*\\*/", " "); // Replacing all the text in between /* */ as blank since those are
			st = st.replaceAll("\t", "");	
			// comment
			List<String> kk = l1.lex(st);
			for (String s : kk) {
				System.out.printf(count+"."+s+" ");
				Tokens.put(count,s); // adding all the tokens received in the Tokens list variable
				count += 1;
			}
			System.out.println();
		}	
		System.out.println();
		System.out.println();
		Parser p1 = new Parser(Tokens);
		String status = p1.v_parser();
		System.out.println("Status of Parser execution = "+status);
	}
}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class lex {
	
	static String tok = "";
	static int state = 0;
	public static List<String> lex(String input) {
			tok = "";
			state = 0;
		 List<String> result = new ArrayList<String>();		
		 for(int i = 0; i < input.length(); ) {
			// System.out.println(input.charAt(i));
			switch(input.charAt(i)) {
			    case '{':
			    	if( state == 1) result.add(checkID(tok));
			        setzero();
			        i++;
			        result.add("LBRACE");
			        
			        break;
			    case '}':
			    	if( state ==1) result.add(checkID(tok));
			        setzero();
			        i++;
			        result.add("RBRACE");
			        
			        break;		
			    case ']':
			    	if( state ==1) result.add(checkID(tok));
			        setzero();
			        i++;
			        result.add("RBRACKET");
			        
			        break;
			    case '[':
			    	 if( state ==1) result.add(checkID(tok));
				        setzero();
				        i++;
			        result.add("LBRACKET");
			       
			        break;
			    case '=':
			    	if( state ==1) result.add(checkID(tok));
			        setzero();
			        i++;
			        result.add("EQUAL");
			        break;
			    case ':':
			    	if( state ==1) result.add(checkID(tok));
			        setzero();
			        i++;
			        result.add("COLON");    
			        
			        break;
			    case ';':
			    	if( state ==1) result.add(checkID(tok));
			        setzero();
			        i++;
			        result.add("SEMICOLON");
			        
			        break;
			    case ',':
			    	if( state ==1) result.add(checkID(tok));
			        setzero();
			        i++;
			        result.add("COMMA");
			        break;
			    /*case '_':
			    	if( state ==1) result.add(checkID(tok));
			        setzero();
			        i++;
			        result.add("UNDERSCORE");
			        break;*/
			    case ' ': 
			    	if( state ==1) result.add(checkID(tok));
			    	setzero();
			    	i++;
			    	break;
			    default :  	state = 1;
		        			tok = tok + Character.toString(input.charAt(i));
		        			i++;
				}
			
			} 
		if( state ==1) result.add(checkID(tok));
		return result;
	}
		
		public static void setzero() {
			tok = "";
			state = 0;
		}
		
		
		public static String checkID(String tt) {
			//System.out.println("Checking-->"+tt);
			String[] predefined = {"node", "edge", "graph", "digraph", "subgraph", "strict","id","->","n","ne","e","se","s","sw","w","nw","c" };
			for(String s : predefined) {
				if (s.toUpperCase().compareTo(tt.toUpperCase())==0) {
					if ((s.toUpperCase().compareTo("->"))==0) {
						return "EDGEOP";
					}
					return s.toUpperCase();	
				}			
			}
			//Checking for ID pattern 
			//Any string of alphabetic ([a-zA-Z\200-\377]) characters, underscores ('_') or digits ([0-9]), not beginning with a digit;
			if(Pattern.matches("[^0-9]+[a-zA-Z_0-9\200-\377]?", tt)) {
				return "ID";
			}
			//a numeral [-]?(.[0-9]+ | [0-9]+(.[0-9]*)? );
			else if (Pattern.matches("[-]?(.[0-9]+|[0-9]+(.[0-9]*)?)",tt)) {
				return "ID";
			}
			//if none of those matched considering it as error			
			return "ERROR-->"+tt;
		}
		
		
		
		

}

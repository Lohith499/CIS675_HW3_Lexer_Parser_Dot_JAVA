import java.util.HashMap;
import java.util.Map;

public class Parser {
	
	Map<Integer,String> pTokens = new HashMap<Integer, String>();
	int focus = 1;
	String Error = "";
	String token = "";
	
	Parser(Map<Integer, String> Tokens){
		this.pTokens = Tokens;
	}
	public String v_parser() {
		
		v_graph();
		if(Error.compareTo("")==0) {
			return "Program has no Syntax Errors";
		}
		return Error;
	}
	
	public void v_graph() {
		//graph : [ strict ] (graph | digraph) [ ID ] '{' stmt_list '}'
		v_Terminal("STRICT", "Optional");
		if(v_Terminal("GRAPH") || v_Terminal("DIGRAPH")) {
			v_Terminal("ID");
			 if (v_Terminal("LBRACE" )) {
				 
				 if (v_stmt_list()) {
					 if (v_Terminal("RBRACE")) {
						 Error ="";
					 }else {
						 Error = "Right Brace is missing at token "+ focus;
					 }
					 
				 } else {
				 }
				 
			 } else {
				 Error = "Left Brace is missing at token "+ focus;
			 }
			
			
		}
	}
	//To verify the terminals
	public Boolean v_Terminal(String terminal, String op) {
		token = pTokens.get(focus);
		//System.out.println("*"+ token+"***"+terminal+"*");
		if (token.compareTo(terminal)==0) {
			focus = focus+1;
			return true; }
		else if (op.compareTo("Optional")==0) {
			return true;
		} else {
			Error = terminal + " is missing at token ="+focus;
			return false;
		}
	}
	
	public Boolean v_Terminal(String terminal) {
		token = pTokens.get(focus);
		//System.out.println("*"+ token+"***"+terminal+"*");
			if (token.compareTo(terminal)==0) {
				focus = focus+1;
				return true;
			}
			Error = terminal + " is missing at token ="+focus;
		return false;
	}


	
	public Boolean v_stmt_list() {
		int cfocus = focus;
		token = pTokens.get(focus);
		//stmt_list : [ stmt [ ';' ] stmt_list ]
		 if (v_stmt()) {
			 focus = focus-1;
			 if (v_Terminal("SEMICOLON")) {
				 cfocus = focus;
				 if (!(v_Terminal("RBRACE"))) {
					 Error="";
					 focus = cfocus;
					 v_stmt_list();
						
					} else {
						
						focus = cfocus;
						return true;
					}
			 } else {
				 if ((v_Terminal("RBRACE"))) {
					 focus=focus-1;
					 return true;
				 } else {
					 Error = "Semi colon is missing at token "+focus;
					 return false;
				 }
			 }
		 }
		return false;
	}
	
	public Boolean v_stmt() {
		
		token = pTokens.get(focus);
		int cfocus = focus;
		int start = focus;
		if (v_assignment() ) {
			
			if(v_Terminal("SEMICOLON")) {
				//System.out.println("Succesful statement"+start);
				return true;
			}
			else {
				Error = "Semi Colon is missing at "+focus;
				return false;
			}
		}
		else {
			//System.out.println("Unsuccesful statement");
			//System.out.println("Entered Node Verification statement");
			focus = cfocus;
			if (v_node_stmt() ) {
				if (v_Terminal("SEMICOLON")) { 
					//System.out.println("Succesful v_node_stmt"+start);
					Error="";
					return true;
				} else {
					Error = "Semi Colon is missing at "+focus;
					return false;
				}
				
			} else {
				focus = cfocus;
				//System.out.println("Entered attr_stmt Verification statement");
				if(v_attr_stmt()) {
					//System.out.println("Succesful v_attr_stmt"+focus);
					if (v_Terminal("SEMICOLON")) { 
					//	System.out.println("Succesful v_attr_stmt()"+start);
						Error="";
						return true;
					} else {
						Error = "Semi Colon is missing at "+focus;
						return false;
					}
				}
				else {
					focus = cfocus;
					//System.out.println("Entered Edge_stmt Verification statement");
					if(v_edge_stmt()) {
						//System.out.println("Succesful v_edge_stmt"+focus);
						if (v_Terminal("SEMICOLON")) { 
						//	System.out.println("Succesful Edge statement "+start);
							Error="";
							return true;
						} else {
							Error = "Semi Colon is missing at "+focus;
							return false;
						}
					} 
					else {
						focus = cfocus;
						System.out.println("Entered Subgraph Verification statement");
						if(v_subgraph()) {
							//System.out.println("Succesful v_subgraph"+focus);
							
							if (v_Terminal("SEMICOLON")) { 
							//	System.out.println("Succesful Subgraph statement "+start);
								Error="";
								return true;
							} else {
								Error = "Semi Colon is missing at "+focus;
								return false;
							}
							
						} else {
							return false;
						}
					}
				}
					
			}
		}
	}
	
	
	// ID = ID
	public Boolean v_assignment() {
		
		if (v_Terminal("ID") ) {
			 if (v_Terminal("EQUAL")) {
				 if (v_Terminal("ID")) {
					 //System.out.println("Succesful assignment");
					 return true;
				 } else {
					 
					 Error="Value is not assigned at token "+focus;
					 System.out.println(Error);
					 System.exit(0);
					 return false;
				 }
			 } else {
				 return false;
			 }
		} else {
			return false;
		}
	}
	
	public Boolean v_subgraph() {
		int cfocus = focus;
		Boolean prev =false;
		if (v_Terminal("SUBGRAPH")) {
			cfocus = focus;
			prev = true;
			if (v_Terminal("ID")) {
				cfocus =focus;
			} 
		} 
		Error="";
		//if (v_Terminal("LBRACE" ) && v_stmt_list() && v_Terminal("RBRACE") ) {
		
		if (v_Terminal("LBRACE" )) {
			cfocus=focus;
			
			if (v_stmt_list()) {
				
				if (v_Terminal("RBRACE")){
					Error="";
					return true;
					
				} else {
					System.out.println("RBRACE is missing at token="+focus);
					System.exit(0);
					return false;
				}
			
			} else {
				return false;
			}
		} else {
			if (prev)  {
				System.out.println("LBRACE is missing at token="+focus);
				System.exit(0);
				return false;
			} else
			{	
				return false;
			}
		}
		
	}
	
	
	public Boolean v_node_stmt () {
		int cnfocus = focus;
		if ( v_node_id()) {
			cnfocus = focus;
			if (v_Terminal("LBRACKET")) {
				cnfocus=focus;
				focus=focus-1;
				if (v_attr_list()) {
					return true;
				} else {
					focus =cnfocus;
					System.out.println(Error+focus);
					System.exit(0);
					return true;
				}
			}
		}
		return false;
	}
	
	public Boolean v_node_id() {
		int cfocus =focus;
		if (v_Terminal("ID")) {
			cfocus = focus;
			if (v_Terminal("COLON")) {
				focus=focus-1;
				return v_port();
			} else {
				return true;
			}
 		}
		return false;
	}
	
	public Boolean v_port( ) {
		int cfocus = focus;
		Boolean prev = false;
		if(v_Terminal("COLON")) {
			cfocus=focus;
			
			if (v_Terminal("ID")) {
				prev = true;
				if(v_Terminal("COLON")) {
					if( v_compass_pt()) {
						return true;
					}
					else {
						System.out.println("Commpass_pt is missing at "+focus);
						System.exit(0);
						return false;
					}
				}
			} 
			if(v_compass_pt()) {
				return true;
			} 
			else {
				System.out.println("ID or Commpass_pt is missing at "+focus);
				System.exit(0);
				return false;
			}
		}
		else {
			return false;
		}
	}
		

	
	public Boolean v_compass_pt() {
		
		if (v_Terminal("N") || v_Terminal("NE") || v_Terminal("E") || v_Terminal("SE") || v_Terminal("S") || v_Terminal("SW") || v_Terminal("W") || v_Terminal("NW") || v_Terminal("C") || v_Terminal("UNDERSCORE") ) {
			Error = "";
			return true;
		} 
		return false ;
	}
	
	
	public Boolean v_attr_stmt () {
		int cfocus= focus;
		if (v_Terminal("GRAPH") || v_Terminal("NODE") || v_Terminal("EDGE"))  {
			Error = "";
			cfocus= focus;
			if (v_attr_list()) {
				return true;
			} else {
				focus = cfocus;
				return false;
			}
		}
		return false;
	}
	
	public Boolean v_attr_list() {
		int cfocus= focus;
		Boolean prev = false;
		//System.out.println("Entered att list  "+cfocus);
		if (v_Terminal("LBRACKET")) {
			cfocus = focus;
			 if (v_a_list()) {
				 cfocus =focus;
				 if (v_Terminal("RBRACKET")) {
					 cfocus=focus;
					 if (v_Terminal("LBRACKET")) {
						 focus= cfocus;
						 v_attr_list();
						 return true;
					 } else {
						 return true;
					 }
					 
				 } else {
					 Error = "Right Bracket is missing";
					 System.out.println("Missing Right Brace at token"+focus);
					 System.exit(0);					 
					 return false;
					
				 }
			 } else {
				 Error = "Invalid alist";
				 focus = cfocus;
				 return false ;
			 }
		} 
		else {
			Error = "LBRACKET is missing at token "+focus;
			return false;
		}
		
	}
	
	
	public Boolean v_a_list() {
		int cfocus = focus;
		boolean prev = false;
		//ID '=' ID [ (';' | ',') ] [ a_list ]
		 if (v_assignment())
		 {
			 cfocus=focus;
			// if (v_Terminal("SEMICOLON") || v_Terminal("COMMA")){
			if (v_Terminal("COMMA")){
				 prev= true;
				 cfocus=focus;
				 //System.out.println("COMMA");
				 if (v_Terminal("ID")) {
					 focus =focus-1;
					 cfocus=focus;
					  if (v_a_list()) {
						  return true;
					  } else {
						  focus=cfocus;
						  return false;
					  }
				 }
				 else {
					 if (v_Terminal("RBRACKET")) {
						focus = focus-1;
						cfocus=focus;
						 prev=false;
						// System.out.println("recevied RBRACKET in a list"+focus);
						 return true;	
					 } else {
						 Error = "Right Bracket is missing";
						 System.out.println("Missing Right Brace at token"+focus);
						 System.exit(0);
						 return false;
					 }
				 }
				 
			 }	else {
				 if (v_Terminal("RBRACKET")) {
						focus = focus-1;
						cfocus=focus;
						 prev=false;
						// System.out.println("recevied RBRACKET in a list"+focus);
						 return true;	
					 } else {
						 Error = "Right Bracket is missing";
						 System.out.println("Missing Right Brace at token"+focus);
						 System.exit(0);
						 return false;
					 }
			 }
		 }		 
		 return false;
	}
	
	
	
	
	
	public Boolean v_edge_stmt() {
		int cfocus=focus;
		int start = focus;
		Boolean prev = false;
		if (v_node_id()) {
			prev =true;
			//System.out.println("Edge Node pass");
		} 
		if (!prev) {
			focus=cfocus;
			if(v_subgraph()) {
				prev =true;
				//System.out.println("Edge Subgraph pass");
			}
		}
		if (prev) {
			//System.out.println("Edge Node or Subgraphpass");
			if (v_edgeRHS()) {
				prev = true;
				cfocus=focus;
				if (v_Terminal("LBRACKET")) {
					focus=focus-1;
					return v_attr_list();
				} else {
					
					return true;
				}
				
			} else {
				focus=start;
				return false;
			}
		} 
		focus=start;
		return false;
	}
	
	
	public Boolean v_edgeRHS() {
		int start=focus;
		int cfocus=focus;
		Boolean prev= false;
		if (v_Terminal("EDGEOP")) {
			cfocus=focus;
			if(v_node_id()) {
				prev=true;
				cfocus=focus;
			}
			if (!prev) {
				focus=cfocus;
				if(v_subgraph()) {
					prev=true;
					cfocus=focus;
				} 
				else {
					System.out.println("TARGET NODE ID is missing after EDGEOP at "+cfocus);
					System.exit(0);
					return false;
				}
			}
			if(prev) {
				if(v_Terminal("EDGEOP")) {
					focus=focus-1;
					return v_edgeRHS();
				} 
				else {
					return true;
				}
			}
			else {
				return false;
			}
		} 
		else {
			return false;
		}
	}
	
}

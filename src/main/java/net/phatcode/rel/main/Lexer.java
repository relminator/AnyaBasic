package net.phatcode.rel.main; /********************************************************************
 *  net.phatcode.rel.main.Lexer.java
 *  A state machine tokenizer
 * 
 *  Richard Eric Lope BSN RN
 *  http://rel.phatcode.net
 *  Started: March 06, 2016
 *  Ended: Ongoing
 *  
 * License MIT: 
 * Copyright (c) 2016 Richard Eric Lope 

 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software. (As clarification, there is no
 * requirement that the copyright notice and permission be included in binary
 * distributions of the Software.)

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *
 *******************************************************************/

import java.util.ArrayList;
import java.util.List;


class Lexer
{
	private enum State
	{
		DEFAULT,
		KEYWORD,
		OPERATOR,
		NUMBER,
		STRING,
        COMMENT,
	}

	Lexer()
	{
		
	}
	
	List<Token> tokenize( String source )
	{
		source += " ";
		List<Token> tokens = new ArrayList<>();
		State state;
		String token = "";
		char firstOperator = 0;
		Token tok = null;
		
		state = State.DEFAULT;
		
		for( int i = 0; i < source.length(); i++ )
		{
			char c = source.charAt(i);
			switch( state )
			{
			case DEFAULT:
				if( isOperator(c) )
				{
					firstOperator = c;
                    Token.Type type = findOperatorType(c, (char) 0);
                    tok = new Token( type,Character.toString(c) );
					state = State.OPERATOR;
				
				}
				else if( Character.isDigit(c))
				{
					token += c;
					state = State.NUMBER;
				}
				else if(isParenthesis(c))
                {
                    Token.Type parenType = findParenthesisType(c);
                    tokens.add(new Token(parenType,Character.toString(c)));
                }
				else if(isPunctuation(c))
				{
					Token.Type puncType = findPunctuationType(c);
					tokens.add(new Token(puncType,Character.toString(c)));
				}
				else if(c == '"')
                {
                    state = State.STRING;
                }
                else if(c == '#')
                {
                    state = State.COMMENT;
                }
				else if(c == '.')
				{
                    tok = new Token( Token.Type.DOT,Character.toString(c) );
                    tokens.add(tok);
                }
				else if(Character.isLetter(c))
				{
				    token += c;
                    state = State.KEYWORD;
				}
				break;
			case OPERATOR:
				if (isOperator(c))
                {                            
                    Token.Type opType = findOperatorType(firstOperator, c);
                    tok = new Token(opType, Character.toString(firstOperator)
                                      + Character.toString(c));
                }
                else
                {                                                 
                    tokens.add(tok);
                    state = State.DEFAULT;                            
                    i--;
                } 
				break;
				
			case NUMBER:
				if( Character.isDigit(c) || (c == '.'))
				{
					token += c;
				}
				else
				{
					tokens.add( new Token(Token.Type.NUMBER, token ) );
					token = "";
					state = State.DEFAULT;
					i--;
				}
				break;
			case STRING:
                if( c == '"' )
                {
                    tokens.add( new Token( Token.Type.STRING, token ));
                    token = "";
                    state = State.DEFAULT;
                }
                else
                {
                    token += c;
                }
                break;
            case COMMENT:
                if(c == '#')
                    state = State.DEFAULT;
                break;
            case KEYWORD:
                if(Character.isLetterOrDigit(c) ||
						c == '_' || c == '!')
                {
                    token += c;
                }
                else
                {
                    Token.Type type = findStatementType(token);
                    tokens.add(new Token(type, token));
                    token = "";
                    state = State.DEFAULT;
                    i--;
                }
                break;                
    
			default:
				break;
			}
		}
		
		
		return tokens;
	}
	
	
	private Token.Type findStatementType(String s)
    {
        Token.Type type;
        s = s.toLowerCase();
        switch(s)
        {
        	case "print":
        	case "phr!nt":
        	case "ilimbag":	
            		type = Token.Type.PRINT;
        		break;
        	case "write":
        	case "wr!t3":
        	case "isulat":
                	type = Token.Type.WRITE;
        		break;
        	case "delay":
        	case "d3l4ey":
        	case "bagalan":
            		type = Token.Type.DELAY;
        		break;
			case "input":
            case "inp0wtz":
            case "itakda":
                type = Token.Type.INPUT;
				break;
			case "start":
        	case "szt4rt":
        	case "umpisa":
                	type = Token.Type.START_BLOCK;
        		break;
        	case "end":
        	case "ehnd":
        	case "wakas":
                	type = Token.Type.END_BLOCK;
        		break;
        	case "while":
        	case "wh!ll3":
        	case "habang":
                	type = Token.Type.WHILE;
        		break;
        	case "if":
        	case "e!f":
        	case "kung":
                	type = Token.Type.IF;
        		break;
        	case "then":
        	case "th3n":
        	case "dapat":
                	type = Token.Type.THEN;
        		break;
        	case "elseif":
            case "el53eyp":
            case "kungganito":
            		type = Token.Type.ELSEIF;
        		break;
        	case "else":
        	case "ellsz3":
        	case "pagiba":
                	type = Token.Type.ELSE;
        		break;
			case "for":
            case "fh0wr":
            case "kada":
                type = Token.Type.FOR;
				break;
			case "to":
            case "t0h":
            case "hanggang":
                type = Token.Type.TO;
				break;
			case "step":
            case "szt3hp":
            case "hakbang":
                type = Token.Type.STEP;
				break;
			case "repeat":
            case "r3p34th":
            case "ulitin":
                type = Token.Type.REPEAT;
				break;
			case "free":
            case "fw33y":
            case "palayain":
				type = Token.Type.FREE;
				break;
			case "function":
            case "fuwnct!own":
            case "gawain":
                type = Token.Type.FUNCTION;
				break;
			case "type":
            case "th4yph":
            case "uri":
				type = Token.Type.TYPE;
				break;
			case "screen":
				type = Token.Type.SCREEN;
				break;
			case "screenflip":
				type = Token.Type.SCREEN_FLIP;
				break;
			case "cls":
				type = Token.Type.SCREEN_CLEAR;
				break;
			case "drawline":
				type = Token.Type.DRAW_LINE;
				break;
			case "drawellipse":
				type = Token.Type.DRAW_ELLIPSE;
				break;
			case "drawstring":
				type = Token.Type.DRAW_STRING;
				break;
			case "setcolor":
				type = Token.Type.SET_COLOR;
				break;
			case "setblendmode":
				type = Token.Type.SET_BLENDMODE;
				break;
			case "loadimage":
				type = Token.Type.LOAD_IMAGE;
				break;
			case "drawimage":
				type = Token.Type.DRAW_IMAGE;
				break;
			case "drawtransformedimage":
				type = Token.Type.DRAW_TRANSFORMED_IMAGE;
				break;
			case "drawscrolledimage":
				type = Token.Type.DRAW_SCROLLED_IMAGE;
				break;
			case "drawfancyline":
				type = Token.Type.DRAW_FANCY_LINE;
				break;
			case "getsubimage":
				type = Token.Type.GET_SUB_IMAGE;
				break;
			case "soundinit":
				type = Token.Type.SOUND_INIT;
				break;
			case "soundload":
				type = Token.Type.SOUND_LOAD;
				break;
			case "soundplay":
				type = Token.Type.SOUND_PLAY;
				break;
			case "soundpause":
				type = Token.Type.SOUND_PAUSE;
				break;
			case "soundstop":
				type = Token.Type.SOUND_STOP;
				break;
			case "return":
            case "r3ht0hrn":
            case "ibalik":
				type = Token.Type.RETURN;
				break;
			case "exit":
            case "paalamna":
            case "bY3ph0exz":
                type = Token.Type.EXIT;
				break;
			case "swap":
            case "zw4hp":
            case "ipalit":
				type = Token.Type.SWAP;
				break;
			case "break":
            case "bhr34k":
			case "walangforever":
				type = Token.Type.BREAK;
				break;
			case "var":
            case "v4hr":
            case "baryabol":
                type = Token.Type.VAR;
				break;
			default:
            	type = Token.Type.KEYWORD;
        }
        return type;
    }
	

	private boolean isParenthesis( char c )
	{
		boolean prntOp = c == '(' || c == ')';
		boolean braceOp = c == '{' || c == '}';
		boolean brakOp = c == '[' || c == ']';
		
		return prntOp || braceOp || brakOp;
	}
	
	private boolean isOperator( char c )
	{
		boolean addOp = c == '+' || c == '-';
        boolean mulOp = c == '*' || c == '/' || c == '^' || c == '%';
        
        boolean compOp = c == '<' || c == '>' || c == '=';
        boolean lgicOp = c == '!' || c == '|' || c == '&';
        
        return addOp || mulOp || compOp || lgicOp;
	}

	private boolean isPunctuation(char c)
	{
		return c == ',' || c == ':';
	}

	private Token.Type findOperatorType( char c, char nextChar )
	{
		Token.Type type = Token.Type.UNKNOWN;
		switch(c)
		{
			case '+':
				type = Token.Type.PLUS;
				break;
			case '-':
				type = Token.Type.MINUS;
				break;
			case '*':
				type = Token.Type.ASTERISK;
				break;
			case '/':
				type = Token.Type.SLASH;
				break;
			case '%':
				type = Token.Type.PERCENT;
				break;
			case '^':
				type = Token.Type.CARET;
				break;
			case '|':
				type = Token.Type.OR;
                if (nextChar == '|') type = Token.Type.OR;
                break;
			case '&':
				type = Token.Type.AND;
				if (nextChar == '&') type = Token.Type.AND;
				break;
			case '=':
				type = Token.Type.ASSIGNMENT;
				if (nextChar == '=') type = Token.Type.EQUAL;
				break;
			case '!':
				type = Token.Type.NOT;
				if (nextChar == '=') type = Token.Type.NOTEQUAL;
				break;
			case '<':
				type = Token.Type.LESS;
				if (nextChar == '=') type = Token.Type.LESSEQUAL;
				break;
			case '>':
				type = Token.Type.GREATER;
				if (nextChar == '=') type = Token.Type.GREATEREQUAL;
				break;
		}
		return type;
	}
	
	private Token.Type findParenthesisType( char c )
	{
		Token.Type type = Token.Type.UNKNOWN;
		switch( c )
		{
			case '(':
				type = Token.Type.LEFT_PAREN;
				break;
			case ')':
				type = Token.Type.RIGHT_PAREN;
				break;
			case '{':
				type = Token.Type.LEFT_BRACE;
				break;
			case '}':
				type = Token.Type.RIGHT_BRACE;
				break;
			case '[':
				type = Token.Type.LEFT_BRACKET;
				break;
			case ']':
				type = Token.Type.RIGHT_BRACKET;
				break;
		}
		
		return type;

	}

	private Token.Type findPunctuationType( char c )
	{
		Token.Type type = Token.Type.UNKNOWN;
		switch( c )
		{
			case ',':
				type = Token.Type.COMMA;
				break;
			case ':':
				type = Token.Type.COLON;
				break;

		}

		return type;

	}



}

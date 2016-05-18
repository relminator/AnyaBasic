package net.phatcode.rel.main;

/*********************************************************************************
 *  net.phatcode.rel.main.Token.java
 *  
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
 *******************************************************************************/

public class Token
{

	public enum Type
	{
		PLUS,
		MINUS,
		ASTERISK,
		SLASH,
		PERCENT,
		CARET,
		LEFT_PAREN,
		RIGHT_PAREN,
        LEFT_BRACE,
        RIGHT_BRACE,
        LEFT_BRACKET,
        RIGHT_BRACKET,
        NUMBER,
		STRING,
		KEYWORD,
		OR,
		AND,
		EQUAL,
		NOTEQUAL,
		LESS,
		GREATER,
		LESSEQUAL,
		GREATEREQUAL,
		ASSIGNMENT,
		NOT,
		PRINT,
		WRITE,
		DELAY,
		INPUT,
		START_BLOCK,
		END_BLOCK,
		WHILE,
		IF,
		THEN,
		ELSEIF,
		ELSE,
		FOR,
		TO,
		STEP,
		REPEAT,
		FREE,
		COMMA,
		COLON,
		COMMENT,
		FUNCTION,
		TYPE,
        DOT,
		SCREEN,
        SCREEN_FLIP,
        SCREEN_CLEAR,
        DRAW_LINE,
		DRAW_ELLIPSE,
		DRAW_STRING,
		SET_COLOR,
		SET_BLENDMODE,
		LOAD_IMAGE,
		DRAW_IMAGE,
		DRAW_TRANSFORMED_IMAGE,
        DRAW_SCROLLED_IMAGE,
        DRAW_FANCY_LINE,
		GET_SUB_IMAGE,
		SOUND_INIT,
		SOUND_LOAD,
		SOUND_PLAY,
		SOUND_PAUSE,
		SOUND_STOP,
		RETURN,
        SWAP,
		BREAK,
		EXIT,
		VAR,
		UNKNOWN
	}

	private Type type;
	private String text;
	private String textOriginal;

	Token( Type type, String text )
	{
		this.type = type;
		this.text = text.toLowerCase();
		this.textOriginal = text;
	}


	public String toString()
	{
		return "Type: " + type + "--- Text: " + textOriginal;
	}

	public Type getType()
	{
		return type;
	}

	public String getText()
	{
		return text;
	}
	String getTextOriginal()
	{
		return textOriginal;
	}

}

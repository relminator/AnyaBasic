package net.phatcode.rel.main;

import net.phatcode.rel.expressions.*;
import net.phatcode.rel.multimedia.Graphics;
import net.phatcode.rel.multimedia.Sonics;
import net.phatcode.rel.parselets.Parselets;
import net.phatcode.rel.parselets.Statement;
import net.phatcode.rel.values.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/********************************************************************
 *  net.phatcode.rel.main.Parser.java
 *  A handcrafted top down recursive descent parser
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

public class Parser {

    private List<Statement> statements = new ArrayList<>();  //AST

    private Map<String, Value> symbolTable =
            new HashMap<>();  // global variables

    private Map<String, Statement> userDefinedStructs =
            new HashMap<>();  // global user defined structs


    private Parselets parselets = new Parselets();

    private Graphics graphics;
    private Sonics sonics;

    private List<Token> tokens;               // tokens
    private int currentTokenIndex = 0;
    private int currentStatementIndex = 0;  //error reporting
    private int errorTokenIndex = 0;        //error reporting
    private int currentCodeLine = 0;        //error reporting

    public Parser() {
        // Fetch this for our return statement
        symbolTable.put( "AnyaBASICfunctionReturn123456",
                new ValueNumber( 0 ) );
        // Fetched for break statement
        symbolTable.put( "AnyaBASIConditonalBreak123456",
                new ValueInteger( 0 ) );

    }

    public Parser( List<Token> tokens ) {
        super();
        this.tokens = tokens;
    }

    private Token getToken( int offset ) {
        if ( ( currentTokenIndex + offset ) >= tokens.size() ) {
            return new Token( Token.Type.UNKNOWN, "NO_TOKEN" );
        }
        return tokens.get( currentTokenIndex + offset );
    }

    // lookahead token
    public Token nextToken() {
        return getToken( 1 );
    }

    public Token currentToken() {
        return getToken( 0 );
    }

    private void eatToken( int offset ) {
        currentTokenIndex += offset;
    }

    public Token match( Token.Type type ) {

        Token token = currentToken();

        if ( token.getType() == type ) {
            eatToken( 1 );
        } else {
            System.out.println( "Syntax ERROR: " +
                    "Got '" + token.getText() + "'" +
                    ". Expected: " + type +
                    "\n@line: " + ( currentCodeLine + 1 ) );

            errorTokenIndex = currentTokenIndex;
            printErrorLine();
            System.exit( 1 );
        }

        return token;

    }


    private Expression atom() {

        Expression result;
        if ( currentToken().getType() == Token.Type.LEFT_PAREN ) {
            // handle parentheses
            match( Token.Type.LEFT_PAREN );
            result = expression();
            match( Token.Type.RIGHT_PAREN );
        } else if ( currentToken().getType() == Token.Type.NUMBER )     // is the expression a number?
        {
            result = new ValueNumber( Double.parseDouble( currentToken().getText() ) );
            match( Token.Type.NUMBER );
        } else if ( currentToken().getType() == Token.Type.STRING )     // is it a string?
        {
            result = new ValueString( currentToken().getTextOriginal() );
            match( Token.Type.STRING );
        } else //variables and functions
        {
            // if not a left paren (
            if ( nextToken().getType() != Token.Type.LEFT_PAREN ) {
                // not a left bracket [ either so it's a named variable or a struct
                if ( nextToken().getType() != Token.Type.LEFT_BRACKET ) {
                    if ( nextToken().getType() != Token.Type.DOT ) {
                        if ( nextToken().getType() != Token.Type.LEFT_BRACE ) {
                            result = new ExpressionVariable( currentToken().getText(), this );
                            match( Token.Type.KEYWORD );
                        } else     // x = map{key}
                        {
                            String mapName = currentToken().getText();
                            match( Token.Type.KEYWORD );
                            match( Token.Type.LEFT_BRACE );
                            Expression key = arithmeticExpression();
                            match( Token.Type.RIGHT_BRACE );
                            result = new ExpressionMapVariable( mapName, key, this );
                        }
                    } else //struct field expression
                    {
                        String varname = currentToken().getText();
                        match( Token.Type.KEYWORD );
                        match( Token.Type.DOT );
                        String fieldname = currentToken().getText();
                        result = new ExpressionStructFieldVariable( varname,
                                fieldname,
                                this );
                        match( Token.Type.KEYWORD );
                    }

                } else  //it's a bracket so it's an array access. ie. a = arrayKho[bhe]
                {
                    String name = currentToken().getText();
                    match( Token.Type.KEYWORD );
                    match( Token.Type.LEFT_BRACKET );
                    Expression index = expression();
                    match( Token.Type.RIGHT_BRACKET );
                    // check for dot
                    // if dot then it's an struct array acccess
                    // print(a[0].z)
                    if ( currentToken().getType() == Token.Type.DOT ) {
                        match( Token.Type.DOT );
                        result = new ExpressionArrayStructFieldVariable( name,
                                index, currentToken().getText(), this );
                        match( Token.Type.KEYWORD );
                    } else if ( currentToken().getType() == Token.Type.LEFT_BRACKET ) {   // 2D array
                        match( Token.Type.LEFT_BRACKET );
                        Expression column = expression();
                        match( Token.Type.RIGHT_BRACKET );
                        if ( currentToken().getType() != Token.Type.DOT ) {
                            result = new ExpressionArray2dVariable( name,
                                    index, column, this );
                        } else {
                            match( Token.Type.DOT );
                            String fieldname = currentToken().getText();
                            result = new ExpressionArray2dStructFieldVariable( name,
                                    index, column, fieldname, this );
                            match( Token.Type.KEYWORD );
                        }
                    } else  // normal array access.  print(a[0])
                    {
                        result = new ExpressionArrayVariable( name,
                                index,
                                this );
                    }
                }
            } else   // Function name is undefined at call
            {
                result = parselets.expressionFunction( this );
            }

        }

        return result;  // get value

    }

    private Expression signedAtom() {
        if ( currentToken().getType() == Token.Type.MINUS ) {
            match( Token.Type.MINUS );
            return new ExpressionSignedAtom( atom() );
        }
        if ( currentToken().getType() == Token.Type.PLUS ) {
            match( Token.Type.PLUS );
        }
        return atom();
    }

    // Base ^ exponent
    private Expression factor() {
        Expression result = signedAtom();
        while ( currentToken().getType() == Token.Type.CARET ) {
            switch ( currentToken().getType() ) {
                case CARET:
                    match( Token.Type.CARET );
                    result = new ExpressionOperator( result, Token.Type.CARET, atom() );
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    private Expression term() {
        Expression result = factor();
        while ( isMulOp( currentToken().getType() ) ) {
            switch ( currentToken().getType() ) {
                case ASTERISK:
                    match( Token.Type.ASTERISK );
                    result = new ExpressionOperator( result, Token.Type.ASTERISK, factor() );
                    break;
                case SLASH:
                    match( Token.Type.SLASH );
                    result = new ExpressionOperator( result, Token.Type.SLASH, factor() );
                    break;
                case PERCENT:
                    match( Token.Type.PERCENT );
                    result = new ExpressionOperator( result, Token.Type.PERCENT, factor() );
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    public Expression arithmeticExpression() {
        Expression result = term();
        while ( isAddOp( currentToken().getType() ) ) {
            switch ( currentToken().getType() ) {
                case PLUS:
                    match( Token.Type.PLUS );
                    result = new ExpressionOperator( result, Token.Type.PLUS, term() );
                    break;
                case MINUS:
                    match( Token.Type.MINUS );
                    result = new ExpressionOperator( result, Token.Type.MINUS, term() );
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    // Used for conditions in while loops, if then else constructs, etc.
    private Expression relationalExpression() {

        Expression result = arithmeticExpression();

        if ( isRelationalOp( currentToken().getType() ) ) {
            switch ( currentToken().getType() ) {
                case EQUAL:
                    match( Token.Type.EQUAL );
                    result = new ExpressionOperator( result, Token.Type.EQUAL, arithmeticExpression() );
                    break;
                case NOTEQUAL:
                    match( Token.Type.NOTEQUAL );
                    result = new ExpressionOperator( result, Token.Type.NOTEQUAL, arithmeticExpression() );
                    break;
                case LESS:
                    match( Token.Type.LESS );
                    result = new ExpressionOperator( result, Token.Type.LESS, arithmeticExpression() );
                    break;
                case GREATER:
                    match( Token.Type.GREATER );
                    result = new ExpressionOperator( result, Token.Type.GREATER, arithmeticExpression() );
                    break;
                case LESSEQUAL:
                    match( Token.Type.LESSEQUAL );
                    result = new ExpressionOperator( result, Token.Type.LESSEQUAL, arithmeticExpression() );
                    break;
                case GREATEREQUAL:
                    match( Token.Type.GREATEREQUAL );
                    result = new ExpressionOperator( result, Token.Type.GREATEREQUAL, arithmeticExpression() );
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    private Expression booleanFactor() {
        return relationalExpression();
    }

    private Expression booleanNotFactor() {
        if ( currentToken().getType() == Token.Type.NOT ) {
            match( Token.Type.NOT );
            return new ExpressionNotOperator( booleanFactor() );
        }
        return booleanFactor();
    }

    private Expression booleanTerm() {
        Expression result = booleanNotFactor();
        while ( currentToken().getType() == Token.Type.AND ) {
            match( Token.Type.AND );
            result = new ExpressionOperator( result, Token.Type.AND, booleanNotFactor() );
        }
        return result;
    }

    private Expression booleanExpression() {
        Expression result = booleanTerm();
        while ( currentToken().getType() == Token.Type.OR ) {
            match( Token.Type.OR );
            result = new ExpressionOperator( result, Token.Type.OR, booleanTerm() );
        }
        return result;
    }

    public Expression expression() {
        return booleanExpression();
    }


    public void setGraphicsMode( List<Expression> params ) {
        if ( params.size() < 1 ) {
            graphics = new Graphics();
        } else if ( params.size() == 2 ) {
            graphics = new Graphics( params.get( 0 ).evaluate().toInteger(),
                    params.get( 1 ).evaluate().toInteger(),
                    0 );

        } else {
            graphics = new Graphics( params.get( 0 ).evaluate().toInteger(),
                    params.get( 1 ).evaluate().toInteger(),
                    params.get( 2 ).evaluate().toInteger() );

        }

    }

    public void screenClear( List<Expression> params ) {
        if ( params.size() < 1 ) {
            graphics.cls();
        } else {
            graphics.cls();
            graphics.drawBoxFilled( 0, 0,
                    graphics.getScreenWidth(), graphics.getScreenHeight(),
                    ( float ) params.get( 0 ).evaluate().toNumber(),
                    ( float ) params.get( 1 ).evaluate().toNumber(),
                    ( float ) params.get( 2 ).evaluate().toNumber(),
                    ( float ) params.get( 3 ).evaluate().toNumber()
            );
        }
    }

    public void setColor( List<Expression> params ) {
        float r = ( float ) params.get( 0 ).evaluate().toNumber();
        float g = ( float ) params.get( 1 ).evaluate().toNumber();
        float b = ( float ) params.get( 2 ).evaluate().toNumber();
        float a = ( float ) params.get( 3 ).evaluate().toNumber();
        graphics.setColor( r, g, b, a );
    }

    public void setBlendMode( Expression mode ) {
        graphics.setBlendMode( mode.evaluate().toInteger() );
    }

    public void drawLine( List<Expression> params ) {
        int x1 = ( int ) params.get( 0 ).evaluate().toNumber();
        int y1 = ( int ) params.get( 1 ).evaluate().toNumber();
        int x2 = ( int ) params.get( 2 ).evaluate().toNumber();
        int y2 = ( int ) params.get( 3 ).evaluate().toNumber();
        if ( params.size() > 4 ) {
            float r = ( float ) params.get( 4 ).evaluate().toNumber();
            float g = ( float ) params.get( 5 ).evaluate().toNumber();
            float b = ( float ) params.get( 6 ).evaluate().toNumber();
            float a = ( float ) params.get( 7 ).evaluate().toNumber();
            graphics.drawLine( x1, y1, x2, y2, r, g, b, a );
        } else {
            graphics.drawLine( x1, y1, x2, y2 );
        }
    }

    public void drawRect( List<Expression> params ) {
        int x1 = ( int ) params.get( 0 ).evaluate().toNumber();
        int y1 = ( int ) params.get( 1 ).evaluate().toNumber();
        int x2 = ( int ) params.get( 2 ).evaluate().toNumber();
        int y2 = ( int ) params.get( 3 ).evaluate().toNumber();
        if ( params.size() > 4 ) {
            float r = ( float ) params.get( 4 ).evaluate().toNumber();
            float g = ( float ) params.get( 5 ).evaluate().toNumber();
            float b = ( float ) params.get( 6 ).evaluate().toNumber();
            float a = ( float ) params.get( 7 ).evaluate().toNumber();
            graphics.drawBox( x1, y1, x2, y2, r, g, b, a );
        } else {
            graphics.drawBox( x1, y1, x2, y2 );
        }
    }

    public void drawRectFilled( List<Expression> params ) {
        int x1 = ( int ) params.get( 0 ).evaluate().toNumber();
        int y1 = ( int ) params.get( 1 ).evaluate().toNumber();
        int x2 = ( int ) params.get( 2 ).evaluate().toNumber();
        int y2 = ( int ) params.get( 3 ).evaluate().toNumber();
        if ( params.size() > 4 ) {
            float r = ( float ) params.get( 4 ).evaluate().toNumber();
            float g = ( float ) params.get( 5 ).evaluate().toNumber();
            float b = ( float ) params.get( 6 ).evaluate().toNumber();
            float a = ( float ) params.get( 7 ).evaluate().toNumber();
            graphics.drawBoxFilled( x1, y1, x2, y2, r, g, b, a );
        } else {
            graphics.drawBoxFilled( x1, y1, x2, y2 );
        }
    }

    //public void drawEllipse( int x, int y, int a, int b, int degrees,
    //                         float red, float green, float blue, float alpha )

    public void drawEllipse( List<Expression> params ) {
        int x = ( int ) params.get( 0 ).evaluate().toNumber();
        int y = ( int ) params.get( 1 ).evaluate().toNumber();
        int a = ( int ) params.get( 2 ).evaluate().toNumber();
        int b = ( int ) params.get( 3 ).evaluate().toNumber();
        int degrees = ( int ) params.get( 4 ).evaluate().toNumber();
        if ( params.size() > 5 ) {
            float red = ( float ) params.get( 5 ).evaluate().toNumber();
            float green = ( float ) params.get( 6 ).evaluate().toNumber();
            float blue = ( float ) params.get( 7 ).evaluate().toNumber();
            float alpha = ( float ) params.get( 8 ).evaluate().toNumber();
            graphics.drawEllipse( x, y, a, b, degrees,
                    red, green, blue, alpha );
        } else {
            graphics.drawEllipse( x, y, a, b, degrees );
        }
    }

    public void drawEllipseFilled( List<Expression> params ) {
        int x = ( int ) params.get( 0 ).evaluate().toNumber();
        int y = ( int ) params.get( 1 ).evaluate().toNumber();
        int a = ( int ) params.get( 2 ).evaluate().toNumber();
        int b = ( int ) params.get( 3 ).evaluate().toNumber();
        int degrees = ( int ) params.get( 4 ).evaluate().toNumber();
        if ( params.size() > 5 ) {
            float red = ( float ) params.get( 5 ).evaluate().toNumber();
            float green = ( float ) params.get( 6 ).evaluate().toNumber();
            float blue = ( float ) params.get( 7 ).evaluate().toNumber();
            float alpha = ( float ) params.get( 8 ).evaluate().toNumber();
            graphics.drawEllipseFilled( x, y, a, b, degrees,
                    red, green, blue, alpha );
        } else {
            graphics.drawEllipseFilled( x, y, a, b, degrees );
        }
    }

    public void drawString( List<Expression> params ) {
        int x = ( int ) params.get( 0 ).evaluate().toNumber();
        int y = ( int ) params.get( 1 ).evaluate().toNumber();

        if ( params.size() > 3 ) {
            float red = ( float ) params.get( 2 ).evaluate().toNumber();
            float green = ( float ) params.get( 3 ).evaluate().toNumber();
            float blue = ( float ) params.get( 4 ).evaluate().toNumber();
            float alpha = ( float ) params.get( 5 ).evaluate().toNumber();
            String text = params.get( 6 ).evaluate().toString();
            graphics.drawString( x, y, red, green, blue, alpha, text );
        } else {
            String text = params.get( 2 ).evaluate().toString();
            graphics.drawString( x, y, text );
        }

    }

    //public void drawSprite( float x, float y,
    //                        int flipmode, SpriteGL sprite )
    //public void drawSprite( float x, float y,
    //                        float r, float g, float b,
    //                        int flipmode, SpriteGL sprite )
    // spriteGL is an index fetched from multimedia
    public void drawImage( List<Expression> params ) {
        int x = ( int ) params.get( 0 ).evaluate().toNumber();
        int y = ( int ) params.get( 1 ).evaluate().toNumber();
        if ( params.size() == 4 ) {
            int flip = ( int ) params.get( 2 ).evaluate().toNumber();
            int index = ( int ) params.get( 3 ).evaluate().toNumber();
            graphics.drawSprite( x, y, flip, index );
        } else {
            float red = ( float ) params.get( 2 ).evaluate().toNumber();
            float green = ( float ) params.get( 3 ).evaluate().toNumber();
            float blue = ( float ) params.get( 4 ).evaluate().toNumber();
            float alpha = ( float ) params.get( 5 ).evaluate().toNumber();
            int flip = ( int ) params.get( 6 ).evaluate().toNumber();
            int index = ( int ) params.get( 7 ).evaluate().toNumber();
            graphics.drawSprite( x, y, red, green, blue, alpha, flip, index );
        }
    }

    //public void drawTransformedSprite( float x, float y,
    //                        float rotation,
    //                        float scaleX, float scaleY,
    //                        int flipmode, SpriteGL sprite )
    //public void drawTransformedSprite( float x, float y,
    //                        float rotation,
    //                        float scaleX, float scaleY,
    //                        float r, float g, float b, float a,
    //                        int flipmode, SpriteGL sprite )
    // spriteGL is an index fetched from multimedia
    public void drawTransformedImage( List<Expression> params ) {
        int x = ( int ) params.get( 0 ).evaluate().toNumber();
        int y = ( int ) params.get( 1 ).evaluate().toNumber();
        float angle = ( int ) params.get( 2 ).evaluate().toNumber();
        float scaleX = ( float ) params.get( 3 ).evaluate().toNumber();
        float scaleY = ( float ) params.get( 4 ).evaluate().toNumber();
        if ( params.size() == 7 ) {
            int flip = ( int ) params.get( 5 ).evaluate().toNumber();
            int index = ( int ) params.get( 6 ).evaluate().toNumber();
            graphics.drawTransformedSprite( x, y, angle, scaleX, scaleY, flip, index );
        } else {
            float red = ( float ) params.get( 5 ).evaluate().toNumber();
            float green = ( float ) params.get( 6 ).evaluate().toNumber();
            float blue = ( float ) params.get( 7 ).evaluate().toNumber();
            float alpha = ( float ) params.get( 8 ).evaluate().toNumber();
            int flip = ( int ) params.get( 9 ).evaluate().toNumber();
            int index = ( int ) params.get( 10 ).evaluate().toNumber();
            graphics.drawTransformedSprite( x, y,
                    angle, scaleX, scaleY,
                    red, green, blue, alpha,
                    flip, index );
        }
    }

    // DrawScrolledImage(x,y,scalex,scaley,u,v[,r,g,b,a]),flipmode,arrayIndex
    public void drawScrolledImage( List<Expression> params ) {
        float x = ( float ) params.get( 0 ).evaluate().toNumber();
        float y = ( float ) params.get( 1 ).evaluate().toNumber();
        float sx = ( float ) params.get( 2 ).evaluate().toNumber();
        float sy = ( float ) params.get( 3 ).evaluate().toNumber();
        float u = ( float ) params.get( 4 ).evaluate().toNumber();
        float v = ( float ) params.get( 5 ).evaluate().toNumber();


        if ( params.size() == 8 ) {
            int flipmode = params.get( 6 ).evaluate().toInteger();
            int index = params.get( 7 ).evaluate().toInteger();
            graphics.drawScrolledSprite( x, y, sx, sy, u, v, flipmode, index );
        } else {
            float red = ( float ) params.get( 6 ).evaluate().toNumber();
            float green = ( float ) params.get( 7 ).evaluate().toNumber();
            float blue = ( float ) params.get( 8 ).evaluate().toNumber();
            float alpha = ( float ) params.get( 9 ).evaluate().toNumber();
            int flipmode = params.get( 10 ).evaluate().toInteger();
            int index = params.get( 11 ).evaluate().toInteger();
            graphics.drawScrolledSprite( x, y, sx, sy, u, v,
                    red, green, blue, alpha, flipmode, index );
        }
    }

    // DrawFancyLine(x1, y1, x2, y2, width, type )
    // DrawFancyLine(x1, y1, x2, y2, width, type, r, g, b, a )
    public void drawFancyLine( List<Expression> params ) {
        int x1 = params.get( 0 ).evaluate().toInteger();
        int y1 = params.get( 1 ).evaluate().toInteger();
        int x2 = params.get( 2 ).evaluate().toInteger();
        int y2 = params.get( 3 ).evaluate().toInteger();
        int width = params.get( 4 ).evaluate().toInteger();
        int type = params.get( 5 ).evaluate().toInteger();

        if ( params.size() == 6 ) {
            graphics.drawSpriteOnLine( x1, y1, x2, y2, width, type );
        } else {
            float red = ( float ) params.get( 6 ).evaluate().toNumber();
            float green = ( float ) params.get( 7 ).evaluate().toNumber();
            float blue = ( float ) params.get( 8 ).evaluate().toNumber();
            float alpha = ( float ) params.get( 9 ).evaluate().toNumber();
            graphics.drawSpriteOnLine( x1, y1, x2, y2, width, type,
                    red, green, blue, alpha );
        }
    }

    // loadimage(String a, String array, index )
    // a = filename
    // array = name of array
    // index = index on the array where to load the image
    public void loadImage( List<Expression> params ) {
        String fileName = params.get( 0 ).evaluate().toString();
        String arrayName = params.get( 1 ).evaluate().toString();
        int imageIndex = graphics.loadImage( fileName );
        assignArrayVariable( arrayName,
                params.get( 2 ).evaluate(),
                new ValueNumber( imageIndex ) );
    }

    //public void getSubImage( float x1, float y1, float x2, float y2,
    //                         int source, dest )
    // spriteGL is an index fetched from multimedia
    //"getsubimage(0,0, 200, 200,imageArray[0]),imageArray,2",
    public void getSubImage( List<Expression> params ) {
        int x1 = ( int ) params.get( 0 ).evaluate().toNumber();
        int y1 = ( int ) params.get( 1 ).evaluate().toNumber();
        int x2 = ( int ) params.get( 2 ).evaluate().toNumber();
        int y2 = ( int ) params.get( 3 ).evaluate().toNumber();
        int indexSource = ( int ) params.get( 4 ).evaluate().toNumber();
        String arrayName = params.get( 5 ).evaluate().toString();
        int imageIndex = graphics.getSprites().size();
        assignArrayVariable( arrayName,
                params.get( 6 ).evaluate(),
                new ValueNumber( imageIndex ) );

        graphics.getSprite( x1, y1, x2, y2, indexSource, imageIndex );

    }

    public void executeSoundInit() {
        sonics = new Sonics();
    }

    public void executeSoundLoad( List<Expression> params ) {
        String name = params.get( 0 ).evaluate().toString();
        boolean isLooping = params.get( 1 ).evaluate().toBoolean();
        sonics.addSample( name, isLooping );
    }

    public void executeSoundPlay( String name ) {
        sonics.play( name );
    }

    public void executeSoundPause( String name ) {
        sonics.pause( name );
    }

    public void executeSoundStop( String name ) {
        sonics.stop( name );
    }

    public void exit( int value ) {
        if ( ( graphics == null ) && ( sonics == null ) ) {
            System.exit( value );
        } else {
            if ( graphics != null ) graphics.destroy();
            if ( sonics != null ) sonics.shutDown();
            System.exit( value );
        }
    }

    private Expression keyDown( int param ) {
        int res = 0;
        if ( Keyboard.isKeyDown( param ) ) {
            res = 1;
        }
        return new ValueNumber( res );

    }

    private Expression boxIntersects( List<Expression> args ) {
        float ax1 = ( float ) args.get( 0 ).evaluate().toNumber();
        float ay1 = ( float ) args.get( 1 ).evaluate().toNumber();
        float ax2 = ( float ) args.get( 2 ).evaluate().toNumber();
        float ay2 = ( float ) args.get( 3 ).evaluate().toNumber();
        float bx1 = ( float ) args.get( 4 ).evaluate().toNumber();
        float by1 = ( float ) args.get( 5 ).evaluate().toNumber();
        float bx2 = ( float ) args.get( 6 ).evaluate().toNumber();
        float by2 = ( float ) args.get( 7 ).evaluate().toNumber();
        int res = Utils.boxIntersects( ax1, ay1, ax2, ay2,
                bx1, by1, bx2, by2 );

        return new ValueInteger( res );

    }

    private Expression circleIntersects( List<Expression> args ) {
        float ax = ( float ) args.get( 0 ).evaluate().toNumber();
        float ay = ( float ) args.get( 1 ).evaluate().toNumber();
        float ar = ( float ) args.get( 2 ).evaluate().toNumber();
        float bx = ( float ) args.get( 3 ).evaluate().toNumber();
        float by = ( float ) args.get( 4 ).evaluate().toNumber();
        float br = ( float ) args.get( 5 ).evaluate().toNumber();
        int res = Utils.circleIntersects( ax, ay, ar,
                bx, by, br );

        return new ValueInteger( res );

    }

    private Expression subString( List<Expression> args ) {
        String name = args.get( 0 ).evaluate().toString();
        int begin = args.get( 1 ).evaluate().toInteger();
        if ( args.size() == 2 ) {
            return new ValueString( name.substring( begin ) );
        } else {
            int end = args.get( 2 ).evaluate().toInteger();
            return new ValueString( name.substring( begin, end ) );
        }

    }

    private Expression splitString( List<Expression> args ) {
        String name = args.get( 0 ).evaluate().toString();
        String regex = args.get( 1 ).evaluate().toString();

        String[] array = name.split( regex );
        List<Expression> elements = new ArrayList<>();

        for ( int i = 0; i < array.length; i++ ) {
            elements.add( new ValueString( array[i] ) );
        }
        return new ValueArray( name, elements );
    }

    private Expression windowClosed() {
        int res = 0;
        if ( Display.isCloseRequested() ) {
            res = 1;
        }
        return new ValueNumber( res );

    }

    //
    // identify keyword
    public Statement identifyKeyword( Token.Type type, List<Statement> statementAST ) {
        Statement result = null;
        switch ( type ) {
            case PRINT:
                result = parselets.statementPrint( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case WRITE:
                result = parselets.statementWrite( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case DELAY:
                result = parselets.statementDelay( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case INPUT:
                result = parselets.statementInput( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case WHILE:
                result = parselets.statementWhile( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case IF:
                result = parselets.statementIf( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case FOR:
                result = parselets.statementFor( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case REPEAT:
                result = parselets.statementRepeat( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case FREE:
                result = parselets.statementFree( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case FUNCTION:    // function definition
                result = parselets.statementFunctionDefinition( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case TYPE:    // function definition
                result = parselets.statementStruct( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case RETURN:    // function definition
                result = parselets.statementReturn( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case SCREEN:    // function definition
                result = parselets.statementScreen( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case SCREEN_CLEAR:    // function definition
                result = parselets.statementScreenClear( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case SCREEN_FLIP:    // function definition
                result = parselets.statementScreenFlip( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case DRAW_LINE:    // function definition
                result = parselets.statementDrawLine( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case DRAW_ELLIPSE:    // function definition
                result = parselets.statementDrawEllipse( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case DRAW_STRING:    // function definition
                result = parselets.statementDrawString( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case SET_COLOR:    // function definition
                result = parselets.statementSetColor( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case SET_BLENDMODE:    // function definition
                result = parselets.statementSetBlendMode( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case LOAD_IMAGE:    // function definition
                result = parselets.statementLoadImage( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case DRAW_IMAGE:    // function definition
                result = parselets.statementDrawImage( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case DRAW_FANCY_LINE:    // function definition
                result = parselets.statementDrawFancyLine( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case DRAW_TRANSFORMED_IMAGE:    // function definition
                result = parselets.statementDrawTransformedImage( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case DRAW_SCROLLED_IMAGE:    // function definition
                result = parselets.statementDrawScrolledImage( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case GET_SUB_IMAGE:    // function definition
                result = parselets.statementGetSubImage( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case SOUND_INIT:    // function definition
                result = parselets.statementSoundInit( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case SOUND_LOAD:    // function definition
                result = parselets.statementSoundLoad( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case SOUND_PLAY:
                result = parselets.statementSoundPlay( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case SOUND_PAUSE:
                result = parselets.statementSoundPause( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case SOUND_STOP:
                result = parselets.statementSoundStop( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case SWAP:    // swap
                result = parselets.statementSwap( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case BREAK:    // swap
                result = parselets.statementBreak( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case EXIT:    // function definition
                result = parselets.statementExit( statementAST, this );
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case VAR:    // var definition
                if ( getToken( 2 ).getType() == Token.Type.LEFT_BRACKET ) {
                    // var vector2d[] = {...}
                    result = parselets.statementArrayDefinition( statementAST, this );
                } else if ( getToken( 2 ).getType() == Token.Type.LEFT_BRACE ) {
                    // var map{:}
                    result = parselets.statementMapDefinition( statementAST, this );
                } else {
                    result = parselets.statementVarDefinition( statementAST, this );
                }
                currentCodeLine++;
                currentStatementIndex = currentTokenIndex;
                break;
            case KEYWORD:
                if ( nextToken().getType() == Token.Type.ASSIGNMENT ) {
                    result = parselets.statementAssign( statementAST, this );
                    currentCodeLine++;
                    currentStatementIndex = currentTokenIndex;
                } else if ( nextToken().getType() == Token.Type.LEFT_BRACKET ) {
                    result = parselets.statementArrayAssignment( statementAST, this );
                    currentCodeLine++;
                    currentStatementIndex = currentTokenIndex;
                } else if ( nextToken().getType() == Token.Type.DOT ) // Struct
                {
                    result = parselets.statementStructFieldAssign( statementAST, this );
                    currentCodeLine++;
                    currentStatementIndex = currentTokenIndex;
                } else if ( nextToken().getType() == Token.Type.LEFT_BRACE ) {
                    result = parselets.statementMapAssignment( statementAST, this );
                    currentCodeLine++;
                    currentStatementIndex = currentTokenIndex;
                } else if ( userDefinedStructs.containsKey( currentToken().getText() ) ) {  // user defined struct
                    // Check two tokens forward if it's "["
                    if ( getToken( 2 ).getType() != Token.Type.LEFT_BRACKET ) {
                        result = parselets.statementStructInstantiation( statementAST, this );
                    } else     // Array of Structs.  Vector2D a[100]
                    {
                        result = parselets.statementArrayStructInitialization( statementAST, this );
                    }
                    currentCodeLine++;
                    currentStatementIndex = currentTokenIndex;
                } else  //function call as a statement
                {
                    result = parselets.statementFunctionCall( statementAST, this );
                    currentCodeLine++;
                    currentStatementIndex = currentTokenIndex;
                }
                break;
            default:
                System.out.println( "ERROR! Undefined Keyword!: " + type.toString() );
                exit( 1 );
                break;
        }

        return result;
    }


    // TODO: 4/7/2016
    // make Arg a List so that we can support multiple parameters
    public Expression executeFunction( String funky, List<Expression> args ) {
        Expression res = null;
        switch ( funky ) {
            case "sin":
                res = new ValueNumber( Math.sin( args.get( 0 ).evaluate().toNumber() ) );
                break;
            case "cos":
                res = new ValueNumber( Math.cos( args.get( 0 ).evaluate().toNumber() ) );
                break;
            case "tan":
                res = new ValueNumber( Math.tan( args.get( 0 ).evaluate().toNumber() ) );
                break;
            case "asin":
                res = new ValueNumber( Math.asin( args.get( 0 ).evaluate().toNumber() ) );
                break;
            case "acos":
                res = new ValueNumber( Math.acos( args.get( 0 ).evaluate().toNumber() ) );
                break;
            case "atan":
                res = new ValueNumber( Math.atan( args.get( 0 ).evaluate().toNumber() ) );
                break;
            case "atan2":
                res = new ValueNumber( Math.atan2( args.get( 0 ).evaluate().toNumber(),
                        args.get( 1 ).evaluate().toNumber() ) );
                break;
            case "ceil":
                res = new ValueNumber( Math.ceil( args.get( 0 ).evaluate().toNumber() ) );
                break;
            case "floor":
                res = new ValueNumber( Math.floor( args.get( 0 ).evaluate().toNumber() ) );
                break;
            case "round":
                res = new ValueNumber( Math.round( args.get( 0 ).evaluate().toNumber() ) );
                break;
            case "exp":
                res = new ValueNumber( Math.exp( args.get( 0 ).evaluate().toNumber() ) );
                break;
            case "log":
                res = new ValueNumber( Math.log( args.get( 0 ).evaluate().toNumber() ) );
                break;
            case "random":
                res = new ValueNumber( Math.random() * args.get( 0 ).evaluate().toNumber() );
                break;
            case "abs":
                res = new ValueNumber( Math.abs( args.get( 0 ).evaluate().toNumber() ) );
                break;
            case "sqrt":
                res = new ValueNumber( Math.sqrt( args.get( 0 ).evaluate().toNumber() ) );
                break;
            case "keydown":
                res = keyDown( ( int ) args.get( 0 ).evaluate().toNumber() );
                break;
            case "timer":
                res = new ValueNumber( System.nanoTime() / 1000000000.0 );
                break;
            case "format":

                res = new ValueString( String.format( args.get( 0 ).evaluate().toString(),
                        args.get( 1 ).evaluate().toNumber() ) );
                break;
            case "int":
                res = new ValueInteger( args.get( 0 ).evaluate().toInteger() );
                break;
            case "boxintersects":
                res = boxIntersects( args );
                break;
            case "circleintersects":
                res = circleIntersects( args );
                break;
            case "sizeof":
                if ( args.size() == 1 ) {
                    if ( args.get( 0 ).evaluate() instanceof ValueArray )
                        res = new ValueInteger( args.get( 0 ).evaluate().toArray().size() );
                    else
                        res = new ValueInteger( ( ( ValueMap ) args.get( 0 ).evaluate() ).toMap().size() );
                } else {
                    if ( args.get( 1 ).evaluate().toInteger() == 0 )
                        res = new ValueInteger( ( ( ValueArray ) args.get( 0 ).evaluate() ).getRows() );
                    else
                        res = new ValueInteger( ( ( ValueArray ) args.get( 0 ).evaluate() ).getColumns() );
                }
                break;
            case "containskey":   // containsKye(mapName, keyName)
                boolean val = ( ( ValueMap ) args.get( 0 ).evaluate() ).toMap().containsKey(
                        args.get( 1 ).evaluate().toString() );
                res = new ValueInteger( val ? 1 : 0 );
                break;
            case "substring":
                res = subString( args );
                break;
            case "stringlength":
                res = new ValueInteger( args.get( 0 ).evaluate().toString().length() );
                break;
            case "valascii":
                res = new ValueInteger( args.get( 0 ).evaluate().toString().charAt(
                        args.get( 1 ).evaluate().toInteger() ) );
                break;
            case "val":
                res = new ValueNumber( Double.parseDouble( args.get( 0 ).evaluate().toString() ) );
                break;
            case "isdigit":
                res = new ValueInteger( Character.isDigit( args.get(
                        0 ).evaluate().toString().charAt( 0 ) ) ? 1 : 0 );
                break;
            case "isletter":
                res = new ValueInteger( Character.isLetter( args.get(
                        0 ).evaluate().toString().charAt( 0 ) ) ? 1 : 0 );
                break;
            case "lowercase":
                res = new ValueString( args.get(
                        0 ).evaluate().toString().toLowerCase() );
                break;
            case "uppercase":
                res = new ValueString( args.get(
                        0 ).evaluate().toString().toUpperCase() );
                break;
            case "split":
                res = splitString( args );
                break;
            case "mousex":
                res = new ValueNumber( Mouse.getX() );
                break;
            case "mousey":
                res = new ValueNumber( graphics.getScreenHeight() - Mouse.getY() );
                break;
            case "mousebuttondown":
                res = new ValueInteger( Mouse.isButtonDown( args.get( 0 ).evaluate().toInteger() ) ? 1 : 0 );
                break;
            case "windowclosed":
                res = windowClosed();
                break;
            default:  // user function call as a part of a expression;ie. print(a+ foo() +meh())
                if ( symbolTable.containsKey( funky ) ) {
                    res = executeUserFunction( funky, args );
                } else {
                    System.out.println( "Runtime ERROR: Function '" + funky + "' undefined." );
                    exit( 1 );
                }
                break;
        }


        return res;

    }


    // Executes user function as an Expression and not as a Statement
    // Used for things like a = foo();
    private Expression executeUserFunction( String funky,
                                            List<Expression> args ) {
        // Get block statements from function list
        // assign variables from function variablenames
        // so that arguments are assigned to function parameters
        if ( !symbolTable.containsKey( funky ) ) {
            System.out.println( "Runtime ERROR: Function '" + funky + "' undefined." );
            exit( 1 );
        }
        List<String> params = ( ( ValueFunction ) symbolTable.get( funky ) ).getVariables();

        if ( params.size() != args.size() ) {
            System.out.println( "Runtime ERROR: Argument and Parameter sizes in Function '" + funky + "' does not match." );
            exit( 1 );
        }
        for ( int i = 0; i < params.size(); i++ ) {
            assignVariable( params.get( i ), args.get( i ).evaluate() );
        }

        ( ( ValueFunction ) symbolTable.get( funky ) ).getBlock().execute();

        return retrieveVariable( "AnyaBASICfunctionReturn123456" );
    }

    public Expression executeFunctionDefinition( String funky,
                                                 List<String> params,
                                                 Statement block ) {

        if ( symbolTable.containsKey( funky ) ) {
            System.out.println( "Runtime ERROR: Function '" + funky + "' already defined." );
            exit( 1 );
        }

        symbolTable.put( funky, new ValueFunction( funky, params, block ) );
        for ( int i = 0; i < params.size(); i++ ) {
            defineVariable( params.get( i ), new ValueNumber( 0 ) );
        }

        return null;
    }

    public Expression executeFunctionCall( String funky,
                                           List<Expression> args ) {

        if ( !symbolTable.containsKey( funky ) ) {
            System.out.println( "Runtime ERROR: Function '" + funky + "' undefined." );
            exit( 1 );
        }
        List<String> params = ( ( ValueFunction ) symbolTable.get( funky ) ).getVariables();

        if ( params.size() != args.size() ) {
            System.out.println( "Runtime ERROR: Argument and Parameter sizes in Function '" + funky + "' does not match." );
            exit( 1 );
        }
        for ( int i = 0; i < params.size(); i++ ) {
            assignVariable( params.get( i ), args.get( i ).evaluate() );
        }
        ( ( ValueFunction ) symbolTable.get( funky ) ).getBlock().execute();

        return null;
    }

    // Executes Array definition
    // Used for things like
    // array = {1,2,3,4,5,6,7}
    // array[10]
    public Expression executeArrayDefinition( String name, List<Expression> args ) {
        if ( symbolTable.containsKey( name ) ) {
            System.out.println( "Runtime ERROR: Array '" + name + "' already defined." );
            exit( 1 );
        }

        List<Expression> items = new ArrayList<>( args.size() );

        for ( Expression i : args ) {
            items.add( i.evaluate() );
        }
        symbolTable.put( name, new ValueArray( name, items ) );
        return null;
    }

    // Executes Array definition
    // Used for things like
    // array[10][20]
    public Expression executeArray2dDefinition( String name,
                                                int rows,
                                                int columns,
                                                List<Expression> args ) {
        if ( symbolTable.containsKey( name ) ) {
            System.out.println( "Runtime ERROR: Array '" + name + "' already defined." );
            exit( 1 );
        }

        List<Expression> items = new ArrayList<>( args.size() );

        for ( Expression i : args ) {
            items.add( i.evaluate() );
        }
        symbolTable.put( name, new ValueArray( name, rows, columns, items ) );
        return null;
    }

    // Executes struct definition
    public Expression executeStructDefinition( String name,
                                               Statement block,
                                               Map<String, Value> fields ) {

        if ( symbolTable.containsKey( name ) ) {
            System.out.println( "Runtime ERROR: Type '" + name + "' already defined." );
            exit( 1 );
        }

        symbolTable.put( name, new ValueStruct( name, fields ) );
        block.execute();
        return null;
    }

    // Executes struct instantiation
    // Vector2D v
    // TypeName varname
    public Expression executeStructInstantiation( String structName,
                                                  String varName ) {

        if ( !symbolTable.containsKey( structName ) ) {
            System.out.println( "Runtime ERROR: Type '" + structName + "' undefined." );
            exit( 1 );
        }

        Map<String, Value> structfields =
                ( ( ValueStruct ) symbolTable.get( structName ) ).getFields();
        // Copy values from structfields to varfields
        Map<String, Value> varfields = new HashMap<>( structfields );
        Statement block = userDefinedStructs.get( structName );

        if ( symbolTable.containsKey( varName ) ) {
            System.out.println( "Runtime ERROR: Type Variable '" + varName + "' already defined." );
            exit( 1 );
        }
        symbolTable.put( varName, new ValueStruct( varName, varfields ) );
        block.execute();
        return null;
    }

    // Executes Array of structsdefinition
    // Used for things like
    // Vector2d array[10]
    public Expression executeArrayStructDefinition( String structName,
                                                    String arrayName,
                                                    Expression numElements ) {

        if ( !symbolTable.containsKey( structName ) ) {
            System.out.println( "Runtime ERROR: Type '" + structName + "' undefined." );
            exit( 1 );
        }

        List<Expression> items = new ArrayList<>();

        Map<String, Value> structfields =
                ( ( ValueStruct ) retrieveVariable( structName ) ).getFields();

        for ( int i = 0; i < ( int ) numElements.evaluate().toNumber(); i++ ) {
            Map<String, Value> varfields = new HashMap<>( structfields );
            items.add( new ValueStruct( arrayName, varfields ) );
        }

        if ( symbolTable.containsKey( arrayName ) ) {
            System.out.println( "Runtime ERROR: Array Type Variable '" + arrayName + "' already defined." );
            exit( 1 );
        }
        symbolTable.put( arrayName, new ValueArray( arrayName, items ) );
        return null;
    }

    // Executes 2d Array of structsdefinition
    // Used for things like
    // Vector2d array[10][20]
    public Expression executeArray2dStructDefinition( String structName,
                                                      String arrayName,
                                                      Expression numRows,
                                                      Expression numCols ) {

        if ( !symbolTable.containsKey( structName ) ) {
            System.out.println( "Runtime ERROR: Type '" + structName + "' undefined." );
            exit( 1 );
        }

        List<Expression> items = new ArrayList<>();

        Map<String, Value> structfields =
                ( ( ValueStruct ) retrieveVariable( structName ) ).getFields();

        int rows = numRows.evaluate().toInteger();
        int cols = numCols.evaluate().toInteger();

        int numElements = rows * cols;
        for ( int i = 0; i < numElements; i++ ) {
            Map<String, Value> varfields = new HashMap<>( structfields );
            items.add( new ValueStruct( arrayName, varfields ) );
        }

        if ( symbolTable.containsKey( arrayName ) ) {
            System.out.println( "Runtime ERROR: Array Type Variable '" + arrayName + "' already defined." );
            exit( 1 );
        }
        symbolTable.put( arrayName, new ValueArray( arrayName, rows, cols, items ) );
        return null;
    }

    public Expression executeMapDefinition( String name ) {
        if ( symbolTable.containsKey( name ) ) {
            System.out.println( "Runtime ERROR: Associative Array '" + name + "' already defined." );
            exit( 1 );
        }

        symbolTable.put( name, new ValueMap( name ) );
        return null;
    }

    public Expression executeFree( String name ) {
        if ( symbolTable.containsKey( name ) ) {
            symbolTable.remove( name );
        } else {
            System.out.println( "Runtime ERROR: Variable or Type '" + name + "' undefined." );
            exit( 1 );
        }
        if ( userDefinedStructs.containsKey( name ) ) {
            userDefinedStructs.remove( name );
        }

        return null;
    }

    void parse() {
        match( Token.Type.START_BLOCK );
        currentCodeLine++;
        while ( currentToken().getType() != Token.Type.END_BLOCK ) {
            identifyKeyword( currentToken().getType(), statements );  // load to main AST
        }
        match( Token.Type.END_BLOCK );
        currentCodeLine++;

    }

    // error helper
    private void printErrorLine() {

        String codeString = "";
        String caretString = "";
        int currentErrorTokenIndex = currentStatementIndex;
        while ( currentErrorTokenIndex != errorTokenIndex ) {
            codeString += tokens.get( currentErrorTokenIndex++ ).getTextOriginal() + " ";

        }
        for ( int i = 0; i < codeString.length() - 2; i++ ) {
            caretString += " ";
        }
        caretString += "  ^";

        int tokenCounter = 0;
        do {
            codeString += tokens.get( currentErrorTokenIndex++ ).getTextOriginal() + " ";
            if ( ( tokenCounter++ ) > 20 ) break;
        }
        while ( !isStartOfLine( tokens.get( currentErrorTokenIndex ).getType() ) );

        System.out.println( codeString );
        System.out.println( caretString );
    }

    // Helper function for error reporting
    private boolean isStartOfLine( Token.Type type ) {
        switch ( type ) {
            case START_BLOCK:
            case END_BLOCK:
            case PRINT:
            case WRITE:
            case DELAY:
            case WHILE:
            case IF:
            case FOR:
            case REPEAT:
            case FUNCTION:    // function definition
            case RETURN:    // function definition
            case SCREEN:    // function definition
            case SCREEN_CLEAR:    // function definition
            case SCREEN_FLIP:    // function definition
            case DRAW_LINE:    // function definition
            case DRAW_ELLIPSE:    // function definition
            case DRAW_STRING:    // function definition
            case SET_COLOR:    // function definition
            case SET_BLENDMODE:
            case LOAD_IMAGE:    // function definition
            case DRAW_IMAGE:    // function definition
            case DRAW_TRANSFORMED_IMAGE:    // function definition
            case DRAW_FANCY_LINE:
            case DRAW_SCROLLED_IMAGE:
            case GET_SUB_IMAGE:    // function definition
            case SOUND_INIT:
            case SOUND_LOAD:
            case SOUND_PAUSE:
            case SOUND_STOP:
            case SOUND_PLAY:
            case FREE:
            case SWAP:    // swap
            case EXIT:    // function definition
            case VAR:    // function definition
            case INPUT:    // function definition
            case BREAK:
                return true;
            case KEYWORD:
                if ( nextToken().getType() == Token.Type.ASSIGNMENT ) {
                    return true;
                } else if ( nextToken().getType() == Token.Type.LEFT_BRACKET ) {
                    return true;
                } else  //function call as a statement
                {
                    return true;
                }
        }

        return false;
    }

    // recognizers
    private boolean isAddOp( Token.Type type ) {
        return ( type == Token.Type.PLUS ) ||
                ( type == Token.Type.MINUS );
    }

    private boolean isMulOp( Token.Type type ) {
        return ( type == Token.Type.ASTERISK ) ||
                ( type == Token.Type.SLASH ) ||
                ( type == Token.Type.PERCENT );
    }

    //private boolean isUnaryOp(Token.Type type)
    //{
    //    return type == Token.Type.NOT;
    //}

    private boolean isLogicalOp( Token.Type type ) {
        return type == Token.Type.OR || type == Token.Type.AND;
    }

    private boolean isMultiDigitOp( Token.Type type ) {
        return type == Token.Type.LESSEQUAL || type == Token.Type.GREATEREQUAL;
    }

    private boolean isRelationalOp( Token.Type type ) {
        boolean lgOps = type == Token.Type.LESS ||
                type == Token.Type.GREATER;

        boolean eqOps = type == Token.Type.EQUAL ||
                type == Token.Type.NOTEQUAL;

        return eqOps || lgOps ||
                isMultiDigitOp( type ) ||
                isLogicalOp( type );
    }

    public void defineVariable( String varName, Value value ) {
        symbolTable.put( varName, value );
    }

    public void assignVariable( String varName, Value value ) {
        if ( !symbolTable.containsKey( varName ) ) {
            System.out.println( "Runtime ERROR: Variable '" + varName + "' undefined." );
            exit( 1 );
        }
        symbolTable.put( varName, value );
    }

    // Assign value to array
    public void assignArrayVariable( String varName, Expression index, Value value ) {
        if ( !symbolTable.containsKey( varName ) ) {
            System.out.println( "Runtime ERROR: Array variable '" + varName + "' undefined." );
            exit( 1 );
        }
        symbolTable.get( varName ).toArray().set( ( int ) index.evaluate().toNumber(),
                value );
    }

    // Assign value to 2d array
    public void assignArray2dVariable( String varName,
                                       Expression row,
                                       Expression column,
                                       Value value ) {
        if ( !symbolTable.containsKey( varName ) ) {
            System.out.println( "Runtime ERROR: Array variable '" + varName + "' undefined." );
            exit( 1 );
        }

        int r = row.evaluate().toInteger();
        int c = column.evaluate().toInteger();
        ValueArray array = ( ( ValueArray ) symbolTable.get( varName ) );
        array.setValue( r, c, value );

    }

    // Assign values to a field struct struct
    // v.x = 5
    public void assignStructFieldVariable( String structName,
                                           String fieldName,
                                           Value value ) {
        if ( !symbolTable.containsKey( structName ) ) {
            System.out.println( "Runtime ERROR: Type Variable '" + structName + "' undefined." );
            exit( 1 );
        }
        Map<String, Value> fields = ( ( ValueStruct ) symbolTable.get( structName ) ).getFields();
        if ( !fields.containsKey( fieldName ) ) {
            System.out.println( "Runtime ERROR: Field Variable '" +
                    structName + "." + fieldName + "' undefined." );
            exit( 1 );
        }

        fields.put( fieldName, value );
    }

    // Assign values to a struct field in an array element
    // v[10].x = 5
    public void assignArrayStructFieldVariable( String arrayName,
                                                Expression index,
                                                String fieldName,
                                                Value value ) {

        if ( !symbolTable.containsKey( arrayName ) ) {
            System.out.println( "Runtime ERROR: Array Variable '" + arrayName + "' undefined." );
            exit( 1 );
        }

        Map<String, Value> fields = ( ( ValueStruct ) symbolTable.get(
                arrayName ).toArray().get(
                ( int ) index.evaluate().toNumber() ) ).getFields();
        if ( !fields.containsKey( fieldName ) ) {
            String idx = "[" + index.evaluate().toNumber() + "]";
            System.out.println( "Runtime ERROR: Field Variable '" +
                    arrayName + idx + "." + fieldName + "' undefined." );
            exit( 1 );
        }

        fields.put( fieldName, value );
    }

    // Assign values to a struct field in an array element
    // v[10].x = 5
    public void assignArray2dStructFieldVariable( String arrayName,
                                                  Expression row,
                                                  Expression col,
                                                  String fieldName,
                                                  Value value ) {

        if ( !symbolTable.containsKey( arrayName ) ) {
            System.out.println( "Runtime ERROR: Array Variable '" + arrayName + "' undefined." );
            exit( 1 );
        }

        int r = row.evaluate().toInteger();
        int c = col.evaluate().toInteger();
        ValueArray array = ( ( ValueArray ) symbolTable.get( arrayName ) );

        Map<String, Value> fields = ( ( ValueStruct ) array.getValue( r, c ) ).getFields();
        if ( !fields.containsKey( fieldName ) ) {
            String idx = "[" + r + "]" + "[" + c + "]";
            System.out.println( "Runtime ERROR: Field Variable '" +
                    arrayName + idx + "." + fieldName + "' undefined." );
            exit( 1 );
        }

        fields.put( fieldName, value );
    }

    // Assign value to map
    public void assignMapVariable( String varName, Expression index, Value value ) {
        if ( !symbolTable.containsKey( varName ) ) {
            System.out.println( "Runtime ERROR: Map variable '" + varName + "' undefined." );
            exit( 1 );
        }
        ( ( ValueMap ) symbolTable.get( varName ) ).toMap().put( index.evaluate().toString(),
                value );
    }

    // Add a typedef with its block for execution later
    // used for
    // Vector2D v
    public void addUserStruct( String structName, Statement blockStatements ) {

        userDefinedStructs.put( structName, blockStatements );
    }

    public void incrementCodeLine() {
        currentCodeLine++;
    }

    public Expression retrieveVariable( String varName ) {
        //System.out.println("Variable name:" + symbolTable.get( varName ));
        if ( !symbolTable.containsKey( varName ) ) {
            System.out.println( "Runtime ERROR: variable '" + varName + "' undefined." );
            exit( 1 );
        }
        return symbolTable.get( varName );
    }

    public Expression retrieveArrayVariable( String varName, Expression index ) {
        //System.out.println("Variable name:" + symbolTable.get( varName ));
        if ( !symbolTable.containsKey( varName ) ) {
            System.out.println( "Runtime ERROR: variable '" + varName + "' undefined." );
            exit( 1 );
        }
        return symbolTable.get( varName ).toArray().get( ( int ) index.evaluate().toNumber() );
    }

    public Expression retrieveArray2dVariable( String varName,
                                               Expression row,
                                               Expression col ) {
        if ( !symbolTable.containsKey( varName ) ) {
            System.out.println( "Runtime ERROR: variable '" + varName + "' undefined." );
            exit( 1 );
        }
        int r = row.evaluate().toInteger();
        int c = col.evaluate().toInteger();
        ValueArray array = ( ( ValueArray ) symbolTable.get( varName ) );
        return array.getValue( r, c );
    }

    public Expression retrieveStructVariable( String varName,
                                              String fieldName ) {
        if ( !symbolTable.containsKey( varName ) ) {
            System.out.println( "Runtime ERROR: variable '" + varName + "' undefined." );
            exit( 1 );
        }
        return ( ( ValueStruct ) symbolTable.get( varName ) ).getFields().get( fieldName );
    }

    public Expression retrieveArrayStructFieldVariable( String varName,
                                                        Expression index,
                                                        String fieldName ) {
        if ( !symbolTable.containsKey( varName ) ) {
            System.out.println( "Runtime ERROR: variable '" + varName + "' undefined." );
            exit( 1 );
        }
        return ( ( ValueStruct ) symbolTable.get(
                varName ).toArray().get(
                index.evaluate().toInteger() ) ).getFields().get( fieldName );
    }

    public Expression retrieveArray2dStructFieldVariable( String varName,
                                                          Expression row,
                                                          Expression col,
                                                          String fieldName ) {
        if ( !symbolTable.containsKey( varName ) ) {
            System.out.println( "Runtime ERROR: variable '" + varName + "' undefined." );
            exit( 1 );
        }

        int r = row.evaluate().toInteger();
        int c = col.evaluate().toInteger();
        ValueArray array = ( ( ValueArray ) symbolTable.get( varName ) );
        return ( ( ValueStruct ) array.getValue( r, c ) ).getFields().get( fieldName );

    }

    public Expression retrieveMapVariable( String varName, Expression index ) {
        if ( !symbolTable.containsKey( varName ) ) {
            System.out.println( "Runtime ERROR: Associative Array variable '" + varName + "' undefined." );
            exit( 1 );
        }
        return ( ( ValueMap ) symbolTable.get( varName ) ).toMap().get( index.evaluate().toString() );
    }

    public Graphics getGraphics() {
        return graphics;
    }

    List<Statement> getStatements() {
        return statements;
    }

    void setTokens( List<Token> tokens ) {
        this.tokens = tokens;
        currentTokenIndex = 0;
    }

    public int getCurrentTokenIndex() {
        return currentTokenIndex;
    }

    public void setCurrentTokenIndex( int currentTokenIndex ) {
        this.currentTokenIndex = currentTokenIndex;
    }

    // function change local var
    public Token.Type reTokenizeLocalFunctionVariables( Token.Type type,
                                                        String functionName,
                                                        List<String> paramNames ) {
        Token.Type res;
        if ( type == Token.Type.KEYWORD ) {
            String text = currentToken().getText();
            if ( paramNames.contains( text ) ) {
                tokens.set( currentTokenIndex,
                        new Token( Token.Type.KEYWORD,
                                functionName + "zero_12345" + text ) );
            }
            res = currentToken().getType();
            match( Token.Type.KEYWORD );
        } else if ( currentToken().getType() == Token.Type.DOT ) {
            res = currentToken().getType();
            eatToken( 2 );
        } else {
            res = currentToken().getType();
            eatToken( 1 );
        }
        return res;
    }

}
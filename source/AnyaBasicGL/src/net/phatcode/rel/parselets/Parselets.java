package net.phatcode.rel.parselets;

import net.phatcode.rel.expressions.ExpressionMapVariable;
import net.phatcode.rel.main.Parser;
import net.phatcode.rel.main.Token;
import net.phatcode.rel.expressions.Expression;
import net.phatcode.rel.expressions.ExpressionFunction;
import net.phatcode.rel.values.Value;
import net.phatcode.rel.values.ValueNumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*********************************************************************************
 * Created by relminator
 * <p>
 * Richard Eric Lope BSN RN
 * http://rel.phatcode.net
 * Started: 4/10/2016
 * Ended: Ongoing
 * <p>
 * License MIT:
 * Copyright (c) 2016 Richard Eric Lope
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software. (As clarification, there is no
 * requirement that the copyright notice and permission be included in binary
 * distributions of the Software.)
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/

public class Parselets
{

    private List<Statement> statementBlock(Parser parser)
    {
        List<Statement> statementAST = new ArrayList<>();
        parser.match( Token.Type.START_BLOCK );
        parser.incrementCodeLine();
        while( parser.currentToken().getType() != Token.Type.END_BLOCK )
        {
            parser.identifyKeyword( parser.currentToken().getType(), statementAST );
        }
        parser.match( Token.Type.END_BLOCK );
        parser.incrementCodeLine();
        return statementAST;

    }

    public Statement statementAssign( List<Statement> statementAST, Parser parser )
    {
        Statement result;
        String varName = parser.match(Token.Type.KEYWORD).getText();
        parser.match( Token.Type.ASSIGNMENT );
        result = new StatementAssign( varName, parser.expression(), parser );
        statementAST.add( result );
        return result;
    }

    public Statement statementVarDefinition( List<Statement> statementAST, Parser parser )
    {
        Statement result;
        parser.match( Token.Type.VAR );
        String varName = parser.match(Token.Type.KEYWORD).getText();
        if( parser.currentToken().getType() != Token.Type.ASSIGNMENT )
        {
            result = new StatementVarDefinition( varName,
                    new ValueNumber(0), parser );
            statementAST.add( result );
            return result;
        }
        parser.match( Token.Type.ASSIGNMENT );
        result = new StatementVarDefinition( varName, parser.expression(), parser );
        statementAST.add( result );
        return result;
    }

    public Statement statementPrint( List<Statement> statementAST, Parser parser )
    {
        parser.match( Token.Type.PRINT );
        parser.match( Token.Type.LEFT_PAREN );
        Statement result = new StatementPrint( parser.expression() );
        statementAST.add( result );
        parser.match( Token.Type.RIGHT_PAREN );
        return result;
    }

    public Statement statementWrite( List<Statement> statementAST, Parser parser )
    {
        parser.match( Token.Type.WRITE );
        parser.match( Token.Type.LEFT_PAREN );
        Statement result = new StatementWrite( parser.expression() );
        statementAST.add( result );
        parser.match( Token.Type.RIGHT_PAREN );
        return result;
    }


    public Statement statementDelay( List<Statement> statementAST, Parser parser )
    {
        parser.match( Token.Type.DELAY );
        parser.match( Token.Type.LEFT_PAREN );
        Statement result = new StatementDelay( parser.expression() );
        statementAST.add( result );
        parser.match( Token.Type.RIGHT_PAREN );
        return result;
    }

    public Statement statementInput( List<Statement> statementAST, Parser parser )
    {
        parser.match( Token.Type.INPUT );
        parser.match( Token.Type.LEFT_PAREN );
        Statement result = new StatementInput( parser.currentToken().getText(),
                                               parser );
        statementAST.add( result );
        parser.match( Token.Type.KEYWORD );
        parser.match( Token.Type.RIGHT_PAREN );
        return result;
    }

    public Statement statementWhile( List<Statement> statementAST, Parser parser )
    {
        Expression condition;
        Statement blockStatements;
        List<Statement> blockAST;

        parser.match( Token.Type.WHILE );
        parser.match( Token.Type.LEFT_PAREN );
        condition = parser.expression();
        parser.match( Token.Type.RIGHT_PAREN );
        blockAST = statementBlock( parser );      // load all block parselets to while AST
        blockStatements = new StatementBlock( blockAST );   // setup
        Statement result =  new StatementWhile( condition, blockStatements, parser );   // add to while statement
        statementAST.add( result );
        return result;
    }

    public Statement statementIf( List<Statement> statementAST, Parser parser )
    {
        Expression condition;
        Statement thenStatements;
        Statement elseStatements = null;
        List<Statement> thenAST;
        List<Statement> elseAST;

        parser.match( Token.Type.IF );
        parser.match( Token.Type.LEFT_PAREN );
        condition = parser.expression();
        parser.match( Token.Type.RIGHT_PAREN );
        parser.match( Token.Type.THEN );

        if( parser.currentToken().getType() == Token.Type.START_BLOCK )    // block parselets
        {
            thenAST = statementBlock(parser);      // load all block parselets to for AST
        }
        else    // single line statement
        {
            thenAST = new ArrayList<>();
            parser.identifyKeyword( parser.currentToken().getType(), thenAST );
        }
        thenStatements = new StatementBlock( thenAST );   // setup

        // check if there's an else clause
        if( parser.currentToken().getType() == Token.Type.ELSE )
        {
            parser.match( Token.Type.ELSE );
            if( parser.currentToken().getType() == Token.Type.START_BLOCK )    // block parselets
            {
                elseAST = statementBlock(parser);      // load all block parselets to for AST
            }
            else    // single line statement
            {
                elseAST = new ArrayList<>();
                parser.identifyKeyword( parser.currentToken().getType(), elseAST );
            }
            elseStatements = new StatementBlock( elseAST );   // setup
        }
        Statement result =  new StatementIf( condition, thenStatements, elseStatements );   // add to IF statement
        statementAST.add( result );
        return result;
    }


    public Statement statementFor( List<Statement> statementAST, Parser parser )
    {
        Expression start;
        Expression end;
        Expression step;
        Statement blockStatements;
        List<Statement> blockAST;

        parser.match( Token.Type.FOR );
        parser.match( Token.Type.LEFT_PAREN );
        // inline the assignment statement since we need both varName and startValue as
        // arguments to FOR
        parser.match( Token.Type.VAR );
        String varName = parser.match(Token.Type.KEYWORD).getText();
        parser.match( Token.Type.ASSIGNMENT );
        start = parser.expression();   // startValue
        Statement res = new StatementVarDefinition( varName, start, parser );  // Assign global iterator to start
        statementAST.add( res );

        parser.match( Token.Type.TO );
        end = parser.expression();
        parser.match( Token.Type.STEP );
        step = parser.expression();
        parser.match( Token.Type.RIGHT_PAREN );
        if( parser.currentToken().getType() == Token.Type.START_BLOCK )    // block parselets
        {
            blockAST = statementBlock(parser);      // load all block parselets to for AST
        }
        else    // single line statement
        {
            blockAST = new ArrayList<>();
            parser.identifyKeyword( parser.currentToken().getType(), blockAST );
        }
        blockStatements = new StatementBlock( blockAST );   // setup
        Statement result =  new StatementFor( varName,
                end,
                step,
                blockStatements,
                parser );   // add to for statement
        statementAST.add( result );
        return result;
    }

    // repeat(start to end step stepsize)
    public Statement statementRepeat( List<Statement> statementAST, Parser parser )
    {
        Expression start;
        Expression end;
        Expression step;
        Statement blockStatements;
        List<Statement> blockAST;

        parser.match( Token.Type.REPEAT );
        parser.match( Token.Type.LEFT_PAREN );
        start = parser.expression();
        parser.match( Token.Type.TO );
        end = parser.expression();
        parser.match( Token.Type.STEP );
        step = parser.expression();
        parser.match( Token.Type.RIGHT_PAREN );
        if( parser.currentToken().getType() == Token.Type.START_BLOCK )    // block parselets
        {
            blockAST = statementBlock(parser);      // load all block parselets to for AST
        }
        else    // single line statement
        {
            blockAST = new ArrayList<>();
            parser.identifyKeyword( parser.currentToken().getType(), blockAST );
        }
        blockStatements = new StatementBlock( blockAST );   // setup
        Statement result =  new StatementRepeat( start,
                end,
                step,
                blockStatements,
                parser );   // add to repeat statement
        statementAST.add( result );
        return result;
    }


    public Statement statementReturn( List<Statement> statementAST, Parser parser )
    {
        parser.match( Token.Type.RETURN );
        Statement result = new StatementReturn( parser.expression(), parser );
        statementAST.add( result );
        return result;
    }

    public Statement statementFree( List<Statement> statementAST, Parser parser )
    {
        parser.match( Token.Type.FREE );
        parser.match( Token.Type.LEFT_PAREN );
        Statement result = new StatementFree( parser.currentToken().getText(), parser );
        parser.match( Token.Type.KEYWORD );
        statementAST.add( result );
        parser.match( Token.Type.RIGHT_PAREN );
        return result;
    }

    // array = {1,2,3,4,5}
    private Statement statementArrayBraceDefinition( String varName,
                                                Parser parser )
    {
        List<Expression> elements = new ArrayList<>();
        parser.match( Token.Type.LEFT_BRACE );

        if(parser.currentToken().getType() != Token.Type.RIGHT_BRACE)
        {
            elements.add( parser.expression() );
            while( parser.currentToken().getType() == Token.Type.COMMA )
            {
                parser.match( Token.Type.COMMA );
                elements.add( parser.expression() );
            }
        }
        parser.match( Token.Type.RIGHT_BRACE );
        return new StatementArrayBraceDefinition( varName,
                                    elements,
                                    parser );

    }

    // var array[expression] or
    // var array[axpression] = {...}
    public Statement statementArrayDefinition( List<Statement> statementAST,
                                               Parser parser )
    {
        Expression value;
        parser.match( Token.Type.VAR );
        String varName = parser.match(Token.Type.KEYWORD).getText();
        Statement result;

        parser.match( Token.Type.LEFT_BRACKET );

        // Check if it's a list initialization
        // var a[] = {1,2,3,4,5,6,7}
        if( parser.currentToken().getType() == Token.Type.RIGHT_BRACKET )
        {
            parser.match( Token.Type.RIGHT_BRACKET );
            parser.match( Token.Type.ASSIGNMENT );
            result = statementArrayBraceDefinition( varName, parser );
            statementAST.add( result );
            return result;
        }

        value = parser.expression();
        parser.match( Token.Type.RIGHT_BRACKET );

        // var a[20+i]
        if( parser.currentToken().getType() != Token.Type.LEFT_BRACKET )
        {
            result = new StatementArrayDefinition( varName,
                    value,
                    parser );

            statementAST.add( result );
            return result;
        }

        // var a[20+i][30]
        parser.match( Token.Type.LEFT_BRACKET );
        Expression value2 = parser.expression();
        result = new StatementArray2dDefinition( varName,
                        value, value2,
                        parser );

        parser.match( Token.Type.RIGHT_BRACKET );
        statementAST.add( result );
        return result;
    }

    // var array[expression] = .....
    public Statement statementArrayAssignment( List<Statement> statementAST,
                                               Parser parser )
    {
        Expression value;
        String varName = parser.match(Token.Type.KEYWORD).getText();
        Statement result;

        parser.match( Token.Type.LEFT_BRACKET );
        value = parser.expression();
        parser.match( Token.Type.RIGHT_BRACKET );

        // a[20+i] = .....  or
        // a[20+i].x = ......
        if( parser.currentToken().getType() != Token.Type.LEFT_BRACKET )
        {
            if( parser.currentToken().getType() == Token.Type.DOT )
            {  // a[20+i].x

                parser.match( Token.Type.DOT );
                String fieldName = parser.match( Token.Type.KEYWORD ).getText();
                parser.match( Token.Type.ASSIGNMENT );

                result = new StatementArrayAssignStructField( varName,
                        value,
                        fieldName,
                        parser.expression(),
                        parser );
            } else
            {
                // a[20+i] ie. array assignment
                parser.match( Token.Type.ASSIGNMENT );
                result = new StatementArrayAssign( varName,
                        value,
                        parser.expression(),
                        parser );

            }
        }
        else // 2d array
        {
            parser.match( Token.Type.LEFT_BRACKET );
            Expression column = parser.expression();
            parser.match( Token.Type.RIGHT_BRACKET );

            // a[20+i][10] = .....
            if( parser.currentToken().getType() != Token.Type.DOT )
            {
                parser.match( Token.Type.ASSIGNMENT );
                result = new StatementArray2dAssign( varName,
                        value,
                        column,
                        parser.expression(),
                        parser );
            }
            else  //a[20+i][10].x =
            {
                parser.match( Token.Type.DOT );
                String fieldName = parser.match( Token.Type.KEYWORD ).getText();
                parser.match( Token.Type.ASSIGNMENT );

                result = new StatementArray2dAssignStructField( varName,
                        value,
                        column,
                        fieldName,
                        parser.expression(),
                        parser );
            }

        }
        statementAST.add( result );
        return result;

    }


    public Statement statementScreen( List<Statement> statementAST, Parser parser )
    {
        List<Expression> args = new ArrayList<>();

        parser.match( Token.Type.SCREEN );

        parser.match( Token.Type.LEFT_PAREN );
        // Check for empty parameters
        if(parser.currentToken().getType() != Token.Type.RIGHT_PAREN)
        {
            args.add( parser.expression() );
            while( parser.currentToken().getType() == Token.Type.COMMA )
            {
                parser.match( Token.Type.COMMA );
                args.add( parser.expression() );
            }
        }
        parser.match( Token.Type.RIGHT_PAREN );
        Statement result =  new StatementScreen( args, parser );

        statementAST.add( result );
        return result;
    }

    public Statement statementScreenFlip( List<Statement> statementAST, Parser parser )
    {

        Statement result;
        parser.match( Token.Type.SCREEN_FLIP );

        parser.match( Token.Type.LEFT_PAREN );
        if( parser.currentToken().getType() != Token.Type.RIGHT_PAREN )
        {
            result =  new StatementScreenFlip( parser.expression(), parser );
            parser.match( Token.Type.RIGHT_PAREN );
        }
        else
        {
            parser.match( Token.Type.RIGHT_PAREN );
            result = new StatementScreenFlip( parser );
        }
        statementAST.add( result );
        return result;
    }

    public Statement statementScreenClear( List<Statement> statementAST, Parser parser )
    {
        List<Expression> args = new ArrayList<>();

        parser.match( Token.Type.SCREEN_CLEAR );

        parser.match( Token.Type.LEFT_PAREN );
        // Check for empty parameters
        if(parser.currentToken().getType() != Token.Type.RIGHT_PAREN)
        {
            args.add( parser.expression() );
            while( parser.currentToken().getType() == Token.Type.COMMA )
            {
                parser.match( Token.Type.COMMA );
                args.add( parser.expression() );
            }
        }
        parser.match( Token.Type.RIGHT_PAREN );
        Statement result =  new StatementScreenClear( args, parser );

        statementAST.add( result );
        return result;
    }

    // drawline(x1,y1,x2,y2)<+><space><+>
    // drawline(x1,y1,x2,y2,r,g,b,a)<+><space><+>
    // +   -> draw a box instead of a line
    // + + -> draw a filledbox instead of a line (there's a space between the '+')
    public Statement statementDrawLine( List<Statement> statementAST, Parser parser )
    {
        List<Expression> args = new ArrayList<>();

        Statement result;

        parser.match( Token.Type.DRAW_LINE );

        parser.match( Token.Type.LEFT_PAREN );
        args.add( parser.expression() );
        while( parser.currentToken().getType() == Token.Type.COMMA )
        {
            parser.match( Token.Type.COMMA );
            args.add( parser.expression() );
        }
        parser.match( Token.Type.RIGHT_PAREN );

        if( parser.currentToken().getType() == Token.Type.COMMA )
        {
            parser.match( Token.Type.COMMA );
        }
        boolean isBox = parser.currentToken().getType() == Token.Type.PLUS;
        if( !isBox )
        {
            result = new StatementDrawLine( args, parser );
        } else
        {
            parser.match( Token.Type.PLUS );
            boolean isFilledBox = parser.nextToken().getType() == Token.Type.PLUS;
            if( !isFilledBox )
            {
                result = new StatementDrawRect( args, parser );
            } else
            {
                result = new StatementDrawRectFilled( args, parser );
                parser.match( Token.Type.COMMA );
                parser.match( Token.Type.PLUS );
            }

        }
        statementAST.add( result );
        return result;
    }

    public Statement statementDrawEllipse ( List<Statement> statementAST, Parser parser )
    {
        List<Expression> args = new ArrayList<>();

        parser.match( Token.Type.DRAW_ELLIPSE );

        parser.match( Token.Type.LEFT_PAREN );
        args.add( parser.expression() );
        while( parser.currentToken().getType() == Token.Type.COMMA )
        {
            parser.match( Token.Type.COMMA );
            args.add( parser.expression() );
        }
        parser.match( Token.Type.RIGHT_PAREN );
        if( parser.currentToken().getType() ==Token.Type.COMMA )
        {
            parser.match( Token.Type.COMMA );
        }
        boolean isFilled = parser.currentToken().getType() == Token.Type.PLUS;
        Statement result;
        if( !isFilled )
        {
            result = new StatementDrawEllipse( args, parser );
        }
        else
        {
            parser.match( Token.Type.PLUS );
            result = new StatementDrawEllipseFilled( args, parser );

        }
        statementAST.add( result );
        return result;
    }

    // drawstring(x,y,text)
    public Statement statementDrawString( List<Statement> statementAST, Parser parser )
    {
        List<Expression> args = new ArrayList<>();

        parser.match( Token.Type.DRAW_STRING );

        parser.match( Token.Type.LEFT_PAREN );
        args.add( parser.expression() );
        while( parser.currentToken().getType() == Token.Type.COMMA )
        {
            parser.match( Token.Type.COMMA );
            args.add( parser.expression() );
        }
        parser.match( Token.Type.RIGHT_PAREN );
        Statement result =  new StatementDrawString( args, parser );

        statementAST.add( result );
        return result;
    }

    public Statement statementSetColor( List<Statement> statementAST, Parser parser )
    {
        List<Expression> args = new ArrayList<>();

        parser.match( Token.Type.SET_COLOR );

        parser.match( Token.Type.LEFT_PAREN );
        args.add( parser.expression() );
        while( parser.currentToken().getType() == Token.Type.COMMA )
        {
            parser.match( Token.Type.COMMA );
            args.add( parser.expression() );
        }
        parser.match( Token.Type.RIGHT_PAREN );
        Statement result =  new StatementSetColor( args, parser );

        statementAST.add( result );
        return result;
    }

    public Statement statementSetBlendMode( List<Statement> statementAST, Parser parser )
    {
        parser.match( Token.Type.SET_BLENDMODE );

        parser.match( Token.Type.LEFT_PAREN );
        Expression mode = parser.expression();

        parser.match( Token.Type.RIGHT_PAREN );
        Statement result =  new StatementSetBlendMode( mode, parser );
        statementAST.add( result );
        return result;
    }

    // loadimage(String a, String array, index )
    // a = filename
    // array = name of array
    // index = index on the array where to load the image
    public Statement statementLoadImage( List<Statement> statementAST, Parser parser )
    {
        List<Expression> args = new ArrayList<>();

        parser.match( Token.Type.LOAD_IMAGE );

        parser.match( Token.Type.LEFT_PAREN );
        args.add( parser.expression() );
        while( parser.currentToken().getType() == Token.Type.COMMA )
        {
            parser.match( Token.Type.COMMA );
            args.add( parser.expression() );
        }
        parser.match( Token.Type.RIGHT_PAREN );
        Statement result =  new StatementLoadImage( args, parser );

        statementAST.add( result );
        return result;
    }

    // drawImage(x,y,[r,g,b,a]),flipmode,image[index]
    public Statement statementDrawImage( List<Statement> statementAST, Parser parser )
    {
        List<Expression> args = new ArrayList<>();

        parser.match( Token.Type.DRAW_IMAGE );

        parser.match( Token.Type.LEFT_PAREN );
        args.add( parser.expression() );
        while( parser.currentToken().getType() == Token.Type.COMMA )
        {
            parser.match( Token.Type.COMMA );
            args.add( parser.expression() );
        }
        parser.match( Token.Type.RIGHT_PAREN );
        parser.match( Token.Type.COMMA );
        args.add( parser.expression() );  // flipmode
        parser.match( Token.Type.COMMA );
        args.add( parser.expression() );  // array[index]
        Statement result =  new StatementDrawImage( args, parser );

        statementAST.add( result );
        return result;
    }

    // DrawFancyLine(x1, y1, x2, y2, width, type )
    // DrawFancyLine(x1, y1, x2, y2, width, type, r, g, b, a )
    public Statement statementDrawFancyLine( List<Statement> statementAST, Parser parser )
    {
        List<Expression> args = new ArrayList<>();

        parser.match( Token.Type.DRAW_FANCY_LINE );

        parser.match( Token.Type.LEFT_PAREN );
        args.add( parser.expression() );
        while( parser.currentToken().getType() == Token.Type.COMMA )
        {
            parser.match( Token.Type.COMMA );
            args.add( parser.expression() );
        }
        parser.match( Token.Type.RIGHT_PAREN );
        Statement result =  new StatementDrawFancyLine( args, parser );
        statementAST.add( result );
        return result;
    }

    // drawTransformedImage(x,y,angle,scalex,scaley),flipmode,image[index]
    public Statement statementDrawTransformedImage( List<Statement> statementAST, Parser parser )
    {
        List<Expression> args = new ArrayList<>();

        parser.match( Token.Type.DRAW_TRANSFORMED_IMAGE );

        parser.match( Token.Type.LEFT_PAREN );
        args.add( parser.expression() );
        while( parser.currentToken().getType() == Token.Type.COMMA )
        {
            parser.match( Token.Type.COMMA );
            args.add( parser.expression() );
        }
        parser.match( Token.Type.RIGHT_PAREN );
        parser.match( Token.Type.COMMA );
        args.add( parser.expression() );  // flipmode
        parser.match( Token.Type.COMMA );
        args.add( parser.expression() );  // array[index]
        Statement result =  new StatementDrawTransformedImage( args, parser );

        statementAST.add( result );
        return result;
    }

    // DrawScrolledImage(x,y,scalex,scaley,u,v[,r,g,b,a]),flipmode,arrayIndex
    public Statement statementDrawScrolledImage( List<Statement> statementAST, Parser parser )
    {
        List<Expression> args = new ArrayList<>();

        parser.match( Token.Type.DRAW_SCROLLED_IMAGE );

        parser.match( Token.Type.LEFT_PAREN );
        args.add( parser.expression() );
        while( parser.currentToken().getType() == Token.Type.COMMA )
        {
            parser.match( Token.Type.COMMA );
            args.add( parser.expression() );
        }
        parser.match( Token.Type.RIGHT_PAREN );
        parser.match( Token.Type.COMMA );
        args.add( parser.expression() );  // flipmode
        parser.match( Token.Type.COMMA );
        args.add( parser.expression() );  // array[index]
        Statement result =  new StatementDrawScrolledImage( args, parser );

        statementAST.add( result );
        return result;
    }

    //"getsubimage(0,0, 200, 200,imageArray[0]),imageArray,2",
    public Statement statementGetSubImage( List<Statement> statementAST, Parser parser )
    {
        List<Expression> args = new ArrayList<>();

        parser.match( Token.Type.GET_SUB_IMAGE );

        parser.match( Token.Type.LEFT_PAREN );
        args.add( parser.expression() );
        while( parser.currentToken().getType() == Token.Type.COMMA )
        {
            parser.match( Token.Type.COMMA );
            args.add( parser.expression() );
        }
        parser.match( Token.Type.RIGHT_PAREN );
        parser.match( Token.Type.COMMA );
        args.add( parser.expression() );  // Dest name
        parser.match( Token.Type.COMMA );
        args.add( parser.expression() );  // Dest index
        Statement result =  new StatementGetSubImage( args, parser );

        statementAST.add( result );
        return result;
    }

    public Statement statementSoundInit( List<Statement> statementAST, Parser parser )
    {
        parser.match( Token.Type.SOUND_INIT );
        parser.match( Token.Type.LEFT_PAREN );
        parser.match( Token.Type.RIGHT_PAREN );

        Statement result =  new StatementSoundInit( parser );

        statementAST.add( result );
        return result;
    }

    public Statement statementSoundLoad( List<Statement> statementAST, Parser parser )
    {
        List<Expression> args = new ArrayList<>();
        parser.match( Token.Type.SOUND_LOAD );
        parser.match( Token.Type.LEFT_PAREN );
        args.add( parser.expression() );
        while( parser.currentToken().getType() == Token.Type.COMMA )
        {
            parser.match( Token.Type.COMMA );
            args.add( parser.expression() );
        }
        parser.match( Token.Type.RIGHT_PAREN );

        Statement result =  new StatementSoundLoad( args, parser );

        statementAST.add( result );
        return result;
    }

    public Statement statementSoundPlay( List<Statement> statementAST, Parser parser )
    {
        parser.match( Token.Type.SOUND_PLAY );
        parser.match( Token.Type.LEFT_PAREN );
        Expression exp =  parser.expression() ;
        parser.match( Token.Type.RIGHT_PAREN );

        Statement result =  new StatementSoundPlay( exp, parser );

        statementAST.add( result );
        return result;
    }

    public Statement statementSoundPause( List<Statement> statementAST, Parser parser )
    {
        parser.match( Token.Type.SOUND_PAUSE );
        parser.match( Token.Type.LEFT_PAREN );
        Expression exp =  parser.expression() ;
        parser.match( Token.Type.RIGHT_PAREN );

        Statement result =  new StatementSoundPause( exp, parser );

        statementAST.add( result );
        return result;
    }

    public Statement statementSoundStop( List<Statement> statementAST, Parser parser )
    {
        parser.match( Token.Type.SOUND_STOP );
        parser.match( Token.Type.LEFT_PAREN );
        Expression exp =  parser.expression() ;
        parser.match( Token.Type.RIGHT_PAREN );

        Statement result =  new StatementSoundStop( exp, parser );

        statementAST.add( result );
        return result;
    }


    public Statement statementSwap( List<Statement> statementAST, Parser parser )
    {
        Statement result;
        parser.match( Token.Type.SWAP );
        parser.match( Token.Type.LEFT_PAREN );
        String varNameA = parser.match(Token.Type.KEYWORD).getText();
        if( parser.currentToken().getType() != Token.Type.LEFT_BRACKET )
        {
            parser.match( Token.Type.COMMA );
            String varNameB = parser.match( Token.Type.KEYWORD ).getText();
            result = new StatementSwap( varNameA, varNameB, parser );
            statementAST.add( result );
            parser.match( Token.Type.RIGHT_PAREN );
        }
        else //swap(a[indexa],a[indexb])
        {
            parser.match( Token.Type.LEFT_BRACKET );
            Expression indexA = parser.expression();
            parser.match( Token.Type.RIGHT_BRACKET );
            parser.match( Token.Type.COMMA );
            String varNameB = parser.match( Token.Type.KEYWORD ).getText();
            parser.match( Token.Type.LEFT_BRACKET );
            Expression indexB = parser.expression();
            parser.match( Token.Type.RIGHT_BRACKET );
            result = new StatementSwapArrayElements( varNameA,
                                            varNameB,
                                            indexA,
                                            indexB,
                                            parser );
            statementAST.add( result );
            parser.match( Token.Type.RIGHT_PAREN );

        }
        return result;
    }

    public Statement statementExit( List<Statement> statementAST, Parser parser )
    {
        parser.match( Token.Type.EXIT );
        parser.match( Token.Type.LEFT_PAREN );
        Statement result = new StatementExit( parser.expression(), parser );
        statementAST.add( result );
        parser.match( Token.Type.RIGHT_PAREN );
        return result;
    }

    public Statement statementBreak( List<Statement> statementAST, Parser parser )
    {
        parser.match( Token.Type.BREAK );
        Statement result = new StatementBreak( parser );
        statementAST.add( result );
        return result;
    }

    // Type varName
    // start
    //      a = 0
    //      s = "rel"
    //      pi = 3.141593
    // end
    public Statement statementStruct( List<Statement> statementAST, Parser parser )
    {
        List<Statement> blockAST = new ArrayList<>();
        Map<String, Value> fields = new HashMap<>(); // field variables

        parser.match( Token.Type.TYPE );
        String structName = parser.match( Token.Type.KEYWORD ).getText();
        parser.match( Token.Type.START_BLOCK );
        String fieldName = parser.match( Token.Type.KEYWORD ).getText();
        fields.put( fieldName, null );    // null for now since we'd execute the assignments later
        parser.match( Token.Type.ASSIGNMENT );
        blockAST.add( new StatementStructFieldAssign( structName,
                fieldName,
                parser.expression(),
                parser ) ) ;
        while( parser.currentToken().getType() != Token.Type.END_BLOCK )
        {
            fieldName = parser.match( Token.Type.KEYWORD ).getText();
            fields.put( fieldName, null );    // null for now since we'd execute the assignments later
            parser.match( Token.Type.ASSIGNMENT );
            blockAST.add( new StatementStructFieldAssign( structName,
                    fieldName,
                    parser.expression(),
                    parser ) );
        }

        parser.match( Token.Type.END_BLOCK );

        Statement blockStatements = new StatementBlock( blockAST );

        Statement result = new StatementStructDefinition( structName,
                blockStatements, fields, parser );
        statementAST.add( result );

        // Add to userStruct table
        parser.addUserStruct( structName, blockStatements );

        return result;
    }

    // x.y =  "rel"
    // x.z =  20
    public Statement statementStructFieldAssign( List<Statement> statementAST, Parser parser )
    {
        String varName = parser.match( Token.Type.KEYWORD ).getText();
        parser.match( Token.Type.DOT );
        String fieldName = parser.match( Token.Type.KEYWORD ).getText();

        parser.match( Token.Type.ASSIGNMENT );

        Statement result = new  StatementStructFieldAssign( varName,
                                fieldName,
                                parser.expression(),
                                parser );
        statementAST.add( result );
        return result;
    }

    // Vector2D v
    public Statement statementStructInstantiation( List<Statement> statementAST, Parser parser )
    {
        String structName = parser.match( Token.Type.KEYWORD ).getText();
        String varName = parser.match( Token.Type.KEYWORD ).getText();

        Statement result = new StatementStructInstantiation( structName,
                varName,
                parser );
        statementAST.add( result );
        return result;
    }

    // Vector2D v[100]
    public Statement statementArrayStructInitialization( List<Statement> statementAST,
                                                         Parser parser )
    {
        Expression value;
        Statement result;
        String structName = parser.match( Token.Type.KEYWORD ).getText();
        String arrayName = parser.match(Token.Type.KEYWORD).getText();

        parser.match( Token.Type.LEFT_BRACKET );
        value = parser.expression();
        parser.match( Token.Type.RIGHT_BRACKET );

        // Vector2D v[100]
        if( parser.currentToken().getType() != Token.Type.LEFT_BRACKET )
        {
            result = new StatementArrayStructDefinition( structName,
                    arrayName,
                    value,
                    parser );
        }
        else
        {
            parser.match( Token.Type.LEFT_BRACKET );
            Expression value2 = parser.expression();
            parser.match( Token.Type.RIGHT_BRACKET );
            result = new StatementArray2dStructDefinition( structName,
                    arrayName,
                    value,
                    value2,
                    parser );
        }

        statementAST.add( result );
        return result;
    }


    private void appendFunctionName(String functionName,
                                    List<String> paramNames,
                                    Parser parser)
    {
        int currentIndex = parser.getCurrentTokenIndex();

        parser.match( Token.Type.START_BLOCK );

        int startTokens = 1;
        int endTokens = 0;
        do
        {
            Token.Type res = parser.reTokenizeLocalFunctionVariables(
                    parser.currentToken().getType(),
                    functionName,
                    paramNames );
            if( res == Token.Type.START_BLOCK )
                startTokens++;
            else if( res == Token.Type.END_BLOCK )
                endTokens++;
            //System.out.println("Starts:" +startTokens );
            //System.out.println("Ends:" + endTokens );
        } while( startTokens != endTokens );

        //parser.match( Token.Type.END_BLOCK );

        parser.setCurrentTokenIndex( currentIndex );

    }

    // function functionName(param1,param2...)
    // Handles function definition
    // ie. Function foo() start end
    public Statement statementFunctionDefinition( List<Statement> statementAST,
                                                  Parser parser )
    {
        List<Statement> blockAST;
        List<String> paramNames = new ArrayList<>();
        Statement blockStatements;

        parser.match( Token.Type.FUNCTION );

        String funkName = parser.match(Token.Type.KEYWORD).getText();
        parser.match( Token.Type.LEFT_PAREN );
        // Check for empty parameters
        if(parser.currentToken().getType() != Token.Type.RIGHT_PAREN)
        {
            paramNames.add( parser.currentToken().getText() );
            parser.match( Token.Type.KEYWORD );
            while( parser.currentToken().getType() == Token.Type.COMMA )
            {
                parser.match( Token.Type.COMMA );
                paramNames.add( parser.currentToken().getText() );
                parser.match( Token.Type.KEYWORD );
            }
        }
        parser.match( Token.Type.RIGHT_PAREN );

        appendFunctionName(funkName, paramNames, parser);

        //rename parameter names
        for( int i = 0; i < paramNames.size(); i++ )
        {
            paramNames.set( i, funkName + "zero_12345" + paramNames.get( i ) );
        }

        // add block to AST full of assignment statements
        blockAST = statementBlock( parser);
        blockStatements = new StatementBlock( blockAST );   // setup
        Statement result =  new StatementFunctionDefinition( funkName,
                paramNames,
                blockStatements,
                parser );

        statementAST.add( result );

        return result;
    }


    public Statement statementFunctionCall( List<Statement> statementAST, Parser parser )
    {


        Statement result;
        String funkName = parser.match(Token.Type.KEYWORD).getText();
        parser.match( Token.Type.LEFT_PAREN );
        List<Expression> args = new ArrayList<>();

        // Check for empty parameters
        if(parser.currentToken().getType() != Token.Type.RIGHT_PAREN)
        {
            args.add( parser.expression() );
            while( parser.currentToken().getType() == Token.Type.COMMA )
            {
                parser.match( Token.Type.COMMA );
                args.add( parser.expression() );
            }
        }
        parser.match( Token.Type.RIGHT_PAREN );

        result = new StatementFunctionCall(funkName, args, parser);
        statementAST.add( result );

        return result;
    }

    public Expression expressionFunction(Parser parser)
    {

        String functionName = parser.currentToken().getText();

        parser.match( Token.Type.KEYWORD );
        parser.match( Token.Type.LEFT_PAREN );
        List<Expression> args = new ArrayList<>();
        if( parser.currentToken().getType() != Token.Type.RIGHT_PAREN )
        {
            args.add( parser.expression() );
            while( parser.currentToken().getType() == Token.Type.COMMA )
            {
                parser.match( Token.Type.COMMA );
                args.add( parser.expression() );
            }
        }
        Expression result = new ExpressionFunction( functionName, args, parser );
        parser.match( Token.Type.RIGHT_PAREN );
        return result;
    }

    // Var map{:}
    public Statement statementMapDefinition( List<Statement> statementAST, Parser parser )
    {

        parser.match( Token.Type.VAR );
        String mapName = parser.match( Token.Type.KEYWORD ).getText();
        parser.match( Token.Type.LEFT_BRACE );
        parser.match( Token.Type.COLON );
        parser.match( Token.Type.RIGHT_BRACE );

        Statement result = new StatementMapDefinition( mapName,
                parser );
        statementAST.add( result );
        return result;
    }

    // map{key} = expression
    public Statement statementMapAssignment( List<Statement> statementAST, Parser parser )
    {
        String varName = parser.match( Token.Type.KEYWORD ).getText();
        parser.match( Token.Type.LEFT_BRACE );
        Expression key = parser.arithmeticExpression();  // Can't use expression() since < and > are also used in boolean expression
        parser.match( Token.Type.RIGHT_BRACE );

        parser.match( Token.Type.ASSIGNMENT );

        Statement result = new  StatementMapAssign( varName,
                key,
                parser.expression(),
                parser );
        statementAST.add( result );
        return result;
    }


}

package net.phatcode.rel.parselets;

import net.phatcode.rel.main.Parser;
import net.phatcode.rel.expressions.Expression;
import net.phatcode.rel.values.ValueNumber;
import net.phatcode.rel.values.ValueString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*********************************************************************************
 * Created by relminator
 * <p>
 * Richard Eric Lope BSN RN
 * http://rel.phatcode.net
 * Started: 4/21/2016
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

public class StatementInput implements Statement
{
    private String varName;
    private Parser parser;

    public StatementInput( String varName,
                           Parser parser )
    {
        this.varName = varName;
        this.parser = parser;
    }

    @Override
    public Expression execute()
    {
        BufferedReader lineRead = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        try
        {
            input = lineRead.readLine();
        } catch (IOException e)
        {
            System.out.println("Error in Input method" + e);
        }

        try
        {
            double value = Double.parseDouble(input);
            parser.defineVariable( varName, new ValueNumber( value ) );
        }
        catch( NumberFormatException e )
        {
            parser.defineVariable( varName, new ValueString( input ) );
        }
        return null;
    }
//    Expression varName;
//    private Parser parser;
//
//    public StatementInput( Expression varName, Parser parser )
//    {
//        this.varName = varName;
//        this.parser = parser;
//    }
//
//    @Override
//    public Expression execute()
//    {
//        String variable = varName.evaluate().toString();
//        BufferedReader lineRead = new BufferedReader(new InputStreamReader(System.in));
//        String input = "";
//        try
//        {
//            input = lineRead.readLine();
//        } catch (IOException e)
//        {
//            System.out.println("Error in Input method" + e);
//        }
//
//        try
//        {
//            double value = Double.parseDouble(input);
//            parser.defineVariable( varName.toString(), new ValueNumber( value ) );
//        }
//        catch( NumberFormatException e )
//        {
//            parser.defineVariable( varName.toString(), new ValueString( input ) );
//        }
//        return null;
//    }
}

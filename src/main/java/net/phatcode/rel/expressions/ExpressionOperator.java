package net.phatcode.rel.expressions;

import net.phatcode.rel.main.Token;
import net.phatcode.rel.values.Value;
import net.phatcode.rel.values.ValueNumber;
import net.phatcode.rel.values.ValueString;

/*********************************************************************************
 *
 *  Created by relminator
 *
 *  Richard Eric Lope BSN RN
 *  http://rel.phatcode.net
 *  Started: 4/4/2016
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

public class ExpressionOperator implements Expression
{
    private Expression left;
    private Token.Type operator;
    private Expression right;

    public ExpressionOperator( Expression left, Token.Type operator, Expression right )
    {
        this.left= left;
        this.operator = operator;
        this.right = right;
    }


    @Override
    public Value evaluate()
    {
        Value result = null;
        Value leftVal = left.evaluate();
        Value rightVal = right.evaluate();

        switch( operator )
        {
            case PLUS:
                if( leftVal instanceof ValueString )    // is it a string so concat
                {
                    result = new ValueString(leftVal.toString() +
                            rightVal.toString());
                }
                else  // number or int
                {
                    result = new ValueNumber(leftVal.toNumber() +
                            rightVal.toNumber());
                }
                break;
            case MINUS:
                result = new ValueNumber(leftVal.toNumber() - rightVal.toNumber());
                break;
            case ASTERISK:
                result = new ValueNumber(leftVal.toNumber() * rightVal.toNumber());
                break;
            case SLASH:
                result = new ValueNumber(leftVal.toNumber() / rightVal.toNumber());
                break;
            case PERCENT:
                result = new ValueNumber((int)leftVal.toNumber() % (int)rightVal.toNumber());
                break;
            case CARET:
                result = new ValueNumber(Math.pow( leftVal.toNumber(), rightVal.toNumber()));
                break;
            case OR:
                result = new ValueNumber( ((int)leftVal.toNumber() != 0) ||
                                          ((int)rightVal.toNumber() != 0) ?
                                          1 : 0 );
                break;
            case AND:
                result = new ValueNumber( ((int)leftVal.toNumber() != 0) &&
                                          ((int)rightVal.toNumber() != 0) ?
                                          1 : 0 );
                break;
            case EQUAL:
                if( leftVal instanceof ValueString )    // is it a string
                {
                    result = new ValueNumber( (leftVal.toString().compareTo(
                            rightVal.toString() ) == 0)  ? 1 : 0  );
                }
                else  // number
                {
                    result = new ValueNumber(leftVal.toNumber() == rightVal.toNumber() ? 1 : 0 );
                }
                break;
            case NOTEQUAL:
                if( leftVal instanceof ValueString )    // is it a string
                {
                    result = new ValueNumber( (leftVal.toString().compareTo(
                            rightVal.toString() ) != 0)  ? 1 : 0  );                }
                else  // number or int
                {
                    result = new ValueNumber(leftVal.toNumber() != rightVal.toNumber() ? 1 : 0 );
                }
                break;
            case LESS:
                if( leftVal instanceof ValueString )    // is it a String
                {
                    result = new ValueNumber( (leftVal.toString().compareTo(
                            rightVal.toString() ) < 0)  ? 1 : 0  );
                }
                else  // number or int
                {
                    result = new ValueNumber(leftVal.toNumber() < rightVal.toNumber() ? 1 : 0 );
                }
                break;
            case GREATER:
                if( leftVal instanceof ValueString )    // is it a string
                {
                    result = new ValueNumber( (leftVal.toString().compareTo(
                            rightVal.toString() ) > 0)  ? 1 : 0  );
                }
                else  // number or int
                {
                    result = new ValueNumber(leftVal.toNumber() > rightVal.toNumber() ? 1 : 0 );
                }
                break;
            case LESSEQUAL:
                if( leftVal instanceof ValueString )    // is it a string
                {
                    result = new ValueNumber( (leftVal.toString().compareTo(
                            rightVal.toString() ) <= 0)  ? 1 : 0  );
                }
                else  // number
                {
                    result = new ValueNumber(leftVal.toNumber() <= rightVal.toNumber() ? 1 : 0 );
                }
                break;
            case GREATEREQUAL:
                if( leftVal instanceof ValueString )    // is it a string
                {
                    result = new ValueNumber( (leftVal.toString().compareTo(
                            rightVal.toString() ) >= 0)  ? 1 : 0  );
                }
                else  // number or int
                {
                    result = new ValueNumber(leftVal.toNumber() >= rightVal.toNumber() ? 1 : 0 );
                }
                break;
            case UNKNOWN:
                break;
        }

        return result;
    }
}

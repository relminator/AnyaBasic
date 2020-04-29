package net.phatcode.rel.parselets;

import net.phatcode.rel.expressions.Expression;

/*********************************************************************************
 *
 *  Created by relminator
 *
 *  Richard Eric Lope BSN RN
 *  http://rel.phatcode.net
 *  Started: 4/6/2016
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
public class StatementIf implements Statement
{
    private Expression condition;
    private Statement thenBlock;
    private Statement elseBlock;

    public StatementIf( Expression condition, Statement thenBlock, Statement elseBlock )
    {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public Expression execute()
    {
        Expression result = null;
        if( elseBlock != null )    // There's an else clause
        {
            if( condition.evaluate().toBoolean() )
            {
                result = thenBlock.execute();   // should be a nested AST
            }
            else
            {
                result = elseBlock.execute();   // should be a nested AST
            }
        }
        else
        {
            if( condition.evaluate().toBoolean() )
            {
                result = thenBlock.execute();   // should be a nested AST
            }

        }
        return result;
    }
}

package net.phatcode.rel.expressions;

import net.phatcode.rel.main.Parser;
import net.phatcode.rel.values.Value;

/*********************************************************************************
 * Created by relminator
 * <p>
 * Richard Eric Lope BSN RN
 * http://rel.phatcode.net
 * Started: 4/27/2016
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

public class ExpressionArray2dVariable implements Expression
{
    private String name;
    private Expression row;
    private Expression col;
    private Parser parser;

    public ExpressionArray2dVariable( String name,
                                      Expression row,
                                      Expression col,
                                      Parser parser )
    {
        this.name = name;
        this.row = row;
        this.col = col;
        this.parser = parser;
    }
    @Override
    public Value evaluate()
    {
        return parser.retrieveArray2dVariable( name, row, col ).evaluate();
    }
}

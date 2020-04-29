package net.phatcode.rel.expressions;

import net.phatcode.rel.main.Parser;
import net.phatcode.rel.values.Value;
import net.phatcode.rel.values.ValueArray;
import net.phatcode.rel.values.ValueInteger;
import net.phatcode.rel.values.ValueNumber;
import net.phatcode.rel.values.ValueString;
import net.phatcode.rel.values.ValueStruct;

import java.util.List;

/**
 * Created by relminator on 4/7/2016.
 * todo -> done 04-07-16
 * multiple parameter support
 * TODO: 4/7/2016
 * args should be parameter type so that we could pass any data type
 * to the parameter list
 */
public class ExpressionFunction implements Expression
{
    private String name;
    private List<Expression> param;     // make this an array list for multiple parameter support
    private Parser parser;

    public ExpressionFunction( String name, List<Expression> param, Parser parser )
    {
        this.name = name;
        this.param = param;
        this.parser = parser;
    }

    @Override
    public Value evaluate()
    {
        Value v = parser.executeFunction( name, param ).evaluate();
        if( v instanceof ValueInteger )
        {
            return new ValueInteger( v.toInteger() );
        }
        else if( v instanceof ValueString )
        {
            return new ValueString( v.toString() );
        }
        else if( v instanceof ValueStruct )
        {
            return new ValueStruct( (ValueStruct)v.evaluate() );
        }
        else if( v instanceof ValueArray )
        {
            return new ValueArray( (ValueArray)v.evaluate() );
        }
        else
        {
            return new ValueNumber( v.evaluate().toNumber() );
        }

    }
}

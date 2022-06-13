package common.util;

import java.util.Stack;

public class Calculator
{


    /** 연산자. */
    private static String operators = "+-*/()&|<>^=!" ;


    /**
     * operator 를 return 을 한다.
     * @return
     */
    public static String getOperators()
    {
        return operators ;
    }

    public static double calculate( String input ) throws Exception
    {
        CalcTokenizer tokenizer = new CalcTokenizer( input ) ;

        Stack operator = new Stack() ;
        Stack operand = new Stack() ;



        while (tokenizer.hasMoreTokens())
        {
            String token = tokenizer.nextToken() ;

            if (isOperator(token))
            {
                Operator op = Operator.toOperator(token);


                if (op instanceof LeftParenOperator)
                {
                    operator.push( op );
                    continue;
                }

                else if( op instanceof RightParenOperator)
                {
                    calculateUntilLeftParen( operator , operand ) ;
                    continue ;
                }

                while( !operator.isEmpty() )
                {

                    Operator op2 = (Operator)operator.peek();

                    // 기존의 연산자 순위가 높으면 계산한다.
                    if (op.getPushPriority() <= op2.getPopPriority())
                    {
                        calculate( operator , operand );
                    }

                    else break ;

                }

                operator.push(op);

            }

            else if (isOperand(token))
            {
                operand.push(Double.valueOf(token));
            }

            else throw new NumberFormatException( "'" + token + "'") ;

        }

        calculateAll( operator , operand );


        return ( (Double) operand.pop()).doubleValue() ;

    }


        // 나머지 모두 계산


    /**
     * 연산자를 만족할 때 까지 계산한다.
     * @param op
     */
    protected static void calculate( Stack operator , Stack operand )
    {
        double result ;

        if (!operator.isEmpty())
        {

            Operator op = (Operator)operator.pop();

            if( op.isUnaryOperator()  )
            {
                double value1 = ((Double)operand.pop()).doubleValue();
                result = ((UnaryOperator)op).work( value1 ) ;
            }

            else
            {

                double value1 = ((Double)operand.pop()).doubleValue();
                double value2 = ((Double)operand.pop()).doubleValue();


                result = ((BinaryOperator)op).work(value2, value1);
            }

            operand.push(new Double(result));
        }
    }
    /**
     * 연산자를 만족할 때까지 계산한다.
     * @param op
     */
    protected static void calculateAll( Stack operator , Stack operand )
    {
        Operator op ;
        double result;

        while (!operator.isEmpty())
        {
            op = (Operator)operator.pop();

            if( op.isUnaryOperator()  )
            {
                double value1 = ((Double)operand.pop()).doubleValue();
                result = ((UnaryOperator)op).work( value1 ) ;
            }

            else
            {

                double value1 = ((Double)operand.pop()).doubleValue();
                double value2 = ((Double)operand.pop()).doubleValue();


                result = ((BinaryOperator)op).work(value2, value1);
            }

            operand.push(new Double(result));
        }
    }


    /**
     * 왼쪽 괄호가 나타날 때까지 계산한다.
     * @param op
     */
    protected static void calculateUntilLeftParen( Stack operator , Stack operand )
    {
        Operator op ;
        double result;

        while (!operator.isEmpty() && ! ( ( op = (Operator) operator.pop() ) instanceof LeftParenOperator ) )
        {

            if( op.isUnaryOperator()  )
            {
                double value1 = ((Double)operand.pop()).doubleValue();
                result = ((UnaryOperator)op).work( value1 ) ;
            }

            else
            {

                double value1 = ((Double)operand.pop()).doubleValue();
                double value2 = ((Double)operand.pop()).doubleValue();


                result = ((BinaryOperator)op).work(value2, value1);
            }

            operand.push(new Double(result));
        }
    }

    /**
     * token이 정의된 연산자인지 검사한다.
     *
     * @param token 검사할 토큰
     * @return
     */
    protected static boolean isOperator(String token )
    {
        if (token.equals("+") ||
            token.equals("*") || token.equals("/") ||
            token.equals("(") || token.equals(")") ||
            token.equals("<<") || token.equals(">>") ||
            token.equals("&") || token.equals("|" ) ||
            token.equals("^") || token.equals("-") || token.equalsIgnoreCase("round") || token.equals("<") || token.equals(">")  || 
            token.equals("%") || token.equalsIgnoreCase("pow") || token.equalsIgnoreCase("lg") || token.equals("==") || token.equals("!="))

        {
            return true;
        }


        return false;
    }

    /**
     * 수자(Double)인지 검사한다.
     * @param token
     * @return
     */
    protected static boolean isOperand(String token)
    {
        try
        {
            Double.parseDouble(token);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }
}

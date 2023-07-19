package common.util;

public abstract class Operator
{
	/** 스택으로 들어갈 때 순위 */
	protected int m_pushPriority;
	
	/** 스택 안에 있을 때 순위 */
	protected int m_popPriority;
	
	/**
	 * 새 연산자를 만든다.
	 * 
	 * @param token 문자열 
	 * @return 연산자
	 * @throws Exception 정의되지 않은 연산자
	 */
	public static Operator toOperator(String token) throws Exception
	{
		if (token.equals("+"))
			return new PlusOperator();
		if (token.equals("-"))
			return new MinusOperator();
		if (token.equals("*"))
			return new MultiOperator();
		if (token.equals("/"))
			return new DivOperator();
		if (token.equals("%"))
			return new RemainderOperator();
		if (token.equals("("))
			return new LeftParenOperator();
		if (token.equals(")"))
			return new RightParenOperator();	
		if (token.equals(">>"))
			return new RightShiftOperator() ;
		if (token.equals("<<")) 
			return new LeftShiftOperator() ;
		if (token.equals(">"))
			return new GreaterThan() ;
		if (token.equals("<")) 
			return new LessThan() ;
		if( token.equals("&"))
			return new BitAndOperator() ;
		if( token.equals("|"))
			return new BitOrOperator() ;
		if( token.equals("^"))
			return new XOrOperator() ;
		if( token.equalsIgnoreCase("round")) 
			return new RoundOperator() ;
		// 2011.11.24 kering
		// 밑이 2인 로그
		if (token.equalsIgnoreCase("lg")) 
		    return new LogOperator(2);
		// 자승
		if (token.equalsIgnoreCase("pow"))
            return new PowerOperator();
		if (token.equals("=="))
            return new Equal();
		if (token.equals("!="))
            return new NotEqual();
		throw new Exception("bad operator");
	}

	public static char getReverseOperator(char operator){
		if (operator == '+')
			return '-';
		
		if (operator == '-')
			return '+';
		
		if (operator == '*')
			return '/';
		
		if (operator == '/')
			return '*';
		
		if (operator == '>')
			return '<';
		
		if (operator == '<')
			return '>';
		
		return operator;
	}

	/**
	 * 
	 * @return
	 */
	public int getPushPriority()
	{
		return m_pushPriority;
	}

	/**
	 * @return
	 */
	public int getPopPriority()
	{
		return m_popPriority;
	}

	/**
	 * operand 를 하나만 가지는 operator냐 ? 
	 * @return
	 */
	public abstract boolean isUnaryOperator() ;
}


abstract class BinaryOperator extends Operator
{
	/**
	 * operand 를 하나만 가지는 operator냐 ? 
	 * @return
	 */
	public boolean isUnaryOperator()
	{
		return false ;
	}
	
	/**
	 * 연산자 계산을 한다.
	 * 
	 * @param value1 값 1
	 * @param value2 값 2
	 * @return 계산한 값
	 */	
	protected abstract double work(double value1, double value2);
}

abstract class UnaryOperator extends Operator
{
	/**
	 * operand 를 하나만 가지는 operator냐 ? 
	 * @return
	 */
	public boolean isUnaryOperator()
	{
		return true ;
	}
	
	/**
	 * 연산자 계산을 한다.
	 * 
	 * @param value1 값 1
	 * @return 계산한 값
	 */	
	protected abstract double work(double value1);
}

/**
 * + 연산자
 */
class PlusOperator extends BinaryOperator
{
	public PlusOperator()
	{
		m_pushPriority = 1;	
		m_popPriority = 1;	
	}
	
	protected double work(double value1, double value2)
	{
		return (value1 + value2);
	}
}

class MinusOperator extends BinaryOperator
{
	public MinusOperator()
	{
		m_pushPriority = 1;	
		m_popPriority = 1;	
	}
	protected double work(double value1, double value2)
	{
		return (value1 - value2);
	}
}

class MultiOperator extends BinaryOperator
{
	public MultiOperator()
	{
		m_pushPriority = 2;	
		m_popPriority = 2;	
	}
	protected double work(double value1, double value2)
	{
		return (value1 * value2);
	}
}

class DivOperator extends BinaryOperator
{
	public DivOperator()
	{
		m_pushPriority = 2;	
		m_popPriority = 2;	
	}
	protected double work(double value1, double value2)
	{
		if (value2 == 0)
			return Double.NaN;
			
		return (value1 / value2);
	}
	
	public static void main(String[] args)
	{
		DivOperator op = new DivOperator();
		
		System.out.println(op.work(1, 0));	
	}
}

class LeftParenOperator extends BinaryOperator
{
	public LeftParenOperator()
	{
		m_pushPriority = 10;	
		m_popPriority = 0;	
	}
	
	protected double work(double value1, double value2)
	{
		return 1;
	}
}

class RightParenOperator extends BinaryOperator
{
	public RightParenOperator()
	{
		m_pushPriority = 0;	
		m_popPriority = 10;	
	}
	
	protected double work(double value1, double value2)
	{
		return 1;
	}
	
}


class RightShiftOperator extends BinaryOperator
{
	
	public RightShiftOperator()
	{
		m_pushPriority = 4;	
		m_popPriority = 4;		
	}

	/** 
	 * @see com.onion.util.Operator#work(double, double)
	 */
	protected double work(double value1, double value2)
	{
		return (int) value1 >> (int) value2 ;
	}
	
}



class LeftShiftOperator extends BinaryOperator
{
	public LeftShiftOperator()
	{
		m_pushPriority = 4;	
		m_popPriority = 4;		
	}

	/** 
	 * @see com.onion.util.Operator#work(double, double)
	 */
	protected double work(double value1, double value2)
	{
		return (int) value1 << (int) value2 ;
	}
	
}

class BitOrOperator extends BinaryOperator
{
	
	public BitOrOperator()
	{
		m_pushPriority = 4;	
		m_popPriority = 4;		
	}

	/** 
	 * @see com.onion.util.Operator#work(double, double)
	 */
	protected double work(double value1, double value2)
	{
		return (int) value1 | (int) value2 ;
	}
	
}

class BitAndOperator extends BinaryOperator
{
	public BitAndOperator()
	{
		m_pushPriority = 4;	
		m_popPriority = 4;		
	}

	/** 
	 * @see com.onion.util.Operator#work(double, double)
	 */
	protected double work(double value1, double value2)
	{
		return (int) value1 & (int) value2 ;
	}
	
}

class GreaterThan extends BinaryOperator
{
	public GreaterThan()
	{
		m_pushPriority = 4;	
		m_popPriority = 4;		
	}

	/** 
	 * @see com.onion.util.Operator#work(double, double)
	 */
	protected double work(double value1, double value2)
	{
		return  value1 > value2  ? 1.0 : 0.0;
	}
}

class LessThan extends BinaryOperator
{
	public LessThan()
	{
		m_pushPriority = 4;	
		m_popPriority = 4;		
	}

	/** 
	 * @see com.onion.util.Operator#work(double, double)
	 */
	protected double work(double value1, double value2)
	{
		return  value1 < value2  ? 1.0 : 0.0;
	}
}

class Equal extends BinaryOperator
{
	public Equal()
	{
		m_pushPriority = 4;	
		m_popPriority = 4;		
	}

	/** 
	 * @see com.onion.util.Operator#work(double, double)
	 */
	protected double work(double value1, double value2)
	{
		return  (int) value1 == (int) value2  ? 1.0 : 0.0;
	}
}

class NotEqual extends BinaryOperator
{
	public NotEqual()
	{
		m_pushPriority = 4;	
		m_popPriority = 4;		
	}

	/** 
	 * @see com.onion.util.Operator#work(double, double)
	 */
	protected double work(double value1, double value2)
	{
		return  (int) value1 != (int) value2  ? 1.0 : 0.0;
	}
}

class XOrOperator extends BinaryOperator
{
	public XOrOperator()
	{
		m_pushPriority = 4;	
		m_popPriority = 4;		
	}

	/** 
	 * @see com.onion.util.Operator#work(double, double)
	 */
	protected double work(double value1, double value2)
	{
		return (int) value1 ^ (int) value2 ;
	}
	
}

class RemainderOperator extends BinaryOperator {

	public RemainderOperator()
	{
		m_pushPriority = 2 ;	
		m_popPriority = 2 ;		
	}
	
	protected double work(double value1, double value2) {
		return value1 % value2;
	}
	
}



class RoundOperator extends UnaryOperator {
	public RoundOperator() {
		m_pushPriority = 5 ;	
		m_popPriority = 5 ;		
	}
	
	/**
	 * operand 를 하나만 가지는 operator냐 ? 
	 * @return
	 */
	public boolean isUnaryOperator() {
		return true;
	}

	/**
	 * @see com.onion.util.UnaryOperator#work(double)
	 */
	protected double work(double value1) {
		return ( int ) value1;
	}
}


// 2011.11.24 kering
// 로그 연산자
class LogOperator extends UnaryOperator {
    private double base;    // 밑
    
    public LogOperator(int base) {
        this.base = base;
        m_pushPriority = 6;    
        m_popPriority = 6;     
    }
    
    /**
     * operand 를 하나만 가지는 operator냐 ? 
     * @return
     */
    public boolean isUnaryOperator() {
        return true;
    }

    /**
     * @see com.onion.util.UnaryOperator#work(double)
     */
    protected double work(double value) {
        return Math.log(value) / Math.log(base);
    }
}

// 자승 연산자
class PowerOperator extends BinaryOperator { 
    public PowerOperator() {
        m_pushPriority = 7 ;    
        m_popPriority = 7 ;
    }
    
    protected double work(double value1, double value2) {
        return Math.pow(value1, value2);
    }
}
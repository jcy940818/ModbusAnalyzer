package common.util;

public abstract class Operator
{
	/** �������� �� �� ���� */
	protected int m_pushPriority;
	
	/** ���� �ȿ� ���� �� ���� */
	protected int m_popPriority;
	
	/**
	 * �� �����ڸ� �����.
	 * 
	 * @param token ���ڿ� 
	 * @return ������
	 * @throws Exception ���ǵ��� ���� ������
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
		// ���� 2�� �α�
		if (token.equalsIgnoreCase("lg")) 
		    return new LogOperator(2);
		// �ڽ�
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
	 * operand �� �ϳ��� ������ operator�� ? 
	 * @return
	 */
	public abstract boolean isUnaryOperator() ;
}


abstract class BinaryOperator extends Operator
{
	/**
	 * operand �� �ϳ��� ������ operator�� ? 
	 * @return
	 */
	public boolean isUnaryOperator()
	{
		return false ;
	}
	
	/**
	 * ������ ����� �Ѵ�.
	 * 
	 * @param value1 �� 1
	 * @param value2 �� 2
	 * @return ����� ��
	 */	
	protected abstract double work(double value1, double value2);
}

abstract class UnaryOperator extends Operator
{
	/**
	 * operand �� �ϳ��� ������ operator�� ? 
	 * @return
	 */
	public boolean isUnaryOperator()
	{
		return true ;
	}
	
	/**
	 * ������ ����� �Ѵ�.
	 * 
	 * @param value1 �� 1
	 * @return ����� ��
	 */	
	protected abstract double work(double value1);
}

/**
 * + ������
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
	 * operand �� �ϳ��� ������ operator�� ? 
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
// �α� ������
class LogOperator extends UnaryOperator {
    private double base;    // ��
    
    public LogOperator(int base) {
        this.base = base;
        m_pushPriority = 6;    
        m_popPriority = 6;     
    }
    
    /**
     * operand �� �ϳ��� ������ operator�� ? 
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

// �ڽ� ������
class PowerOperator extends BinaryOperator { 
    public PowerOperator() {
        m_pushPriority = 7 ;    
        m_popPriority = 7 ;
    }
    
    protected double work(double value1, double value2) {
        return Math.pow(value1, value2);
    }
}
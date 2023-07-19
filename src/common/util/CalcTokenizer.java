package common.util;

public class CalcTokenizer
{
	
	/** ��ū�� ó���Ѱ� ���� ���ڿ� */
	String m_strRemaint = "";
	
	
	boolean beforeOperand = false ;
	
	
	/**
	 * @param strSource
	 * @param strToken
	 */
	public CalcTokenizer(
			String strSource )
	{
		m_strRemaint = strSource.trim()  ;	
	
	}
	
	/**
	 * ��ū���� �и��� ���� ���ڿ��� ��´�.
	 */
	public String nextToken()
	{
		if( ! hasMoreTokens() ) return null ;
		
		else
		{
			if( m_strRemaint.charAt(0) == '+' || m_strRemaint.charAt(0) == '*' || m_strRemaint.charAt(0) == '/' || 
					m_strRemaint.charAt(0) == '('  || 
					m_strRemaint.charAt(0) == '|' || m_strRemaint.charAt(0) == '&' ||
					m_strRemaint.charAt(0) == '^' || m_strRemaint.charAt(0) == '%')
			{
				beforeOperand = false ;
				return getOperator( 0 , 1) ;
			}
			
			else if ( m_strRemaint.charAt(0) == ')' )
			{
				beforeOperand = true ;
				return getOperator(0 , 1 ) ;
			}
				
			
			else if( m_strRemaint.charAt(0) == '<' && m_strRemaint.charAt(1) == '<' )
			{
				beforeOperand = false ;
				return getOperator( 0 , 2 ) ;
			}
			
			else if( m_strRemaint.charAt(0) == '<' )
			{
				beforeOperand = false ;
				return getOperator( 0 , 1 ) ;
			}
			
			else if( m_strRemaint.charAt(0) == '>' && m_strRemaint.charAt(1) == '>' )
			{
				beforeOperand = false ;
				return getOperator( 0 , 2 ) ;
			}
			
			else if( m_strRemaint.charAt(0) == '>' )
			{
				beforeOperand = false ;
				return getOperator( 0 , 1 ) ;
			}
		
			else if( m_strRemaint.charAt(0) == '-' )
			{
				if ( beforeOperand )
				{
					beforeOperand = false ;
					return getOperator( 0 , 1 ) ;
				}
				
				else return getOperand() ;
			}
			
			else if( m_strRemaint.toLowerCase().startsWith("round") )
			{
				beforeOperand = false ;
				return getOperator( 0 , 5 ) ;
			}
			
			// 2011.11.24 kering
			// ���� 2�� �α�
			else if (m_strRemaint.toLowerCase().startsWith("lg")) {
			    beforeOperand = false;
			    return getOperator(0, 2);
			}
			
			// �ڽ�
			else if (m_strRemaint.toLowerCase().startsWith("pow")) {
			    beforeOperand = false;
			    return getOperator(0, 3);
			}
			
			else if( m_strRemaint.charAt(0) == '=' && m_strRemaint.charAt(1) == '=' )
			{
				beforeOperand = false ;
				return getOperator( 0 , 2 ) ;
			}
			
			else if( m_strRemaint.charAt(0) == '!' && m_strRemaint.charAt(1) == '=' )
			{
				beforeOperand = false ;
				return getOperator( 0 , 2 ) ;
			}
			
			else return getOperand() ;
			
		}
	}
	
	
	/** token ���� operator �� �����Ѵ�. */
	private String getOperator( int begin ,int length )
	{
		return getToken( begin , length ) ;
		
	}
	
	
	/** token ���� operand �� �����Ѵ�. */
	private String getOperand()
	{
		int exponentialCount = 0 ;
		int dotCount = 0 ;
		beforeOperand = true ;
		
		for ( int i = 0 ; i < m_strRemaint.length() ; i ++)
		{
			if ( m_strRemaint.charAt(i) == '+' || m_strRemaint.charAt(i) == '*' || m_strRemaint.charAt(i) == '/' || 
					m_strRemaint.charAt(i) == '(' || m_strRemaint.charAt(i) == ')' || 
					m_strRemaint.charAt(i) == '|' || m_strRemaint.charAt(i) == '&' ||
					m_strRemaint.charAt(i) == '^' || m_strRemaint.charAt(i) == ' ' || m_strRemaint.charAt(i) == '\t' || 
					m_strRemaint.charAt(i) == '<' || m_strRemaint.charAt(i) == '>' || m_strRemaint.charAt(i) == '%')
						
				return getToken(0 , i ) ;
			
			
			else if( m_strRemaint.charAt(i) == '-' )
			{
				if( i == 0 || m_strRemaint.charAt(i-1) == 'e' || m_strRemaint.charAt(i-1) == 'E') ;
				else return getToken( 0 , i ) ;
			}
			
			else if( m_strRemaint.charAt(i) == 'e' || m_strRemaint.charAt(i) == 'E' )
			{
				if( exponentialCount > 0 ) throw new NumberFormatException( "'" + m_strRemaint + "'") ;
				else exponentialCount ++ ;
			}
			
			
			else if( m_strRemaint.charAt(i) == '.')
			{
				if( dotCount > 0 ) throw new NumberFormatException( "'" + m_strRemaint + "'") ;
				else dotCount ++ ;
			}
			
			else if(m_strRemaint.charAt(i)=='=' || m_strRemaint.charAt(i)=='!') {
				return getToken(0 , i);
			}
			
			else if( isDigit( m_strRemaint.charAt(i))) ;			
			
			else throw new NumberFormatException( "'" + m_strRemaint + "'") ;
		}
		
		return getToken( 0 , m_strRemaint.length() ) ;
	}
	
	
	
	/** value �� �����̳�? */
	private boolean isDigit( char value )
	{
		return ( value >= '0' && value <= '9' ) ;
	}
	
	
	/** begin ���� �����ϴ� length �� len �� token �� ��ȯ�Ѵ�. */
	private String getToken( int begin , int len )
	{
		String retval = m_strRemaint.substring( begin , len ) ;
		m_strRemaint = m_strRemaint.substring(  begin + len ).trim() ;
		
		return retval ;
	}
	
	
	/** �� �̻��� token �� �ִ°�? */
	public boolean hasMoreTokens()
	{
		return m_strRemaint.length() != 0 ;
	}

	
}

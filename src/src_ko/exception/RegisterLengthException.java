package src_ko.exception;

import src_ko.util.Util;

public class RegisterLengthException extends Exception {
	public RegisterLengthException(){
		super("�ùٸ� ������ ������ ���� ��Ŷ�� �ƴմϴ�" + Util.separator + Util.separator + "\n\n" + "���� ��Ŷ�� ������ ���̰� �ùٸ��� �ʽ��ϴ�\n");		
	}		
}	
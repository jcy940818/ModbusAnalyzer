package src_en.util;

public class Descriptor {
		
	public static void getFcDescription() {
		System.out.println("┌────────────────────────────────────────────────────────────────────┐");
		System.out.println("│                  Modbus FunctionCode Description                   │");
		System.out.println("│                                                                    │");
		System.out.println("│ 1. FC01 - Read Coil Status : 0~9999 DI Data(ON/OFF)                │");
		System.out.println("│                                                                    │");
		System.out.println("│                                                                    │");
		System.out.println("│ 2. FC02 - Read Input Status : 10001~19999 DI Data(ON/OFF)          │");
		System.out.println("│                                                                    │");
		System.out.println("│                                                                    │");
		System.out.println("│ 3. FC03 - Read Holding Registers : 40001~49999 Analog Data         │");
		System.out.println("│                                                                    │");
		System.out.println("│                                                                    │");
		System.out.println("│ 4. FC04 - Read Input Registers : 30001~39999 Analog Data           │");
		System.out.println("│                                                                    │");
		System.out.println("│                                                                    │");
		System.out.println("│ 5. FC05 - Force Single Coil : FC01(0~9999) Control                 │");
		System.out.println("│                                                                    │");
		System.out.println("│                                                                    │");
		System.out.println("│ 6. FC06 - Preset Single Register : FC03(40001~49999) Control       │");
		System.out.println("│                                                                    │");
		System.out.println("│                                                                    │");
		System.out.println("│ Info : Analog Data를 Bit연산하여 DI(ON/OFF)상태로도 표현합니다.               │");		
		System.out.println("│                                                                    │");
		System.out.println("└────────────────────────────────────────────────────────────────────┘");
	}
	
	public static void getAddrDescription() {
		// Register Address : Base 0
		// Modbus Address : Base 1
		// 위의 두 주소방식 차이에 대해서 설명
		
		// FunctionCode별 Modbus 주소 표기법 설명
	}
}

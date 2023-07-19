package common.util;

public class StringUtil {

	public static int[] getTargetIndex(String str, char find) {
		int[] targetIndex = null;
		int containsCount = getContainsCount(str, find);

		if (containsCount >= 1) {
			targetIndex = new int[containsCount];
			int index = 0;

			char[] cs = str.toCharArray();

			for (int i = 0; i < cs.length; i++) {
				if (cs[i] == find) {
					targetIndex[index++] = i;
				}
			}
		}

		return targetIndex;
	}

	public static int getContainsCount(String str, char find) {
		int count = 0;
		for (char c : str.toCharArray()) {
			if (c == find)
				count++;
		}
		return count;
	}

	public static void printString(String str) {
		System.out.println("String : " + str);
		char[] cs = str.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			System.out.println("char[" + i + "] : " + cs[i]);
		}
	}

}

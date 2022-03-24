package common.perf;

public class PerfLabelStatusBean implements Comparable {
	public int index;
	public int value;
	public String label;

	@Override
	public int compareTo(Object obj) {
		PerfLabelStatusBean bean = (PerfLabelStatusBean) obj;
		if (this.value < bean.value) {
			return -1;
		} else if (this.value == bean.value) {
			return 0;
		} else {
			return 1;
		}
	}
}

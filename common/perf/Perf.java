package common.perf;

public abstract class Perf implements Comparable {
	private int index;
	 
	public int getIndex() { return index; }
	public void setIndex(int index) { this.index = index; }
	public abstract String getDisplayName();
	public abstract String getCounter();
	public abstract int getInterval();
	public abstract String getMeasure();
	public abstract String getScaleFunction();
	public abstract int getDataFormat();
	public abstract String[] getBinLabel();
	public abstract PerfLabelStatusBean[] getStatusLabels();
	public abstract common.perf.FmsPerfItem.EventInfo[] getFmsEventInfo();
	public abstract common.perf.SnmpPerfItem.EventInfo[] getSnmpEventInfo();
	public abstract int getEnable();
	
	@Override
	public int compareTo(Object obj) {
		Perf bean = (Perf) obj;
		if (this.index < bean.index) {
			return -1;
		} else if (this.index == bean.index) {
			return 0;
		} else {
			return 1;
		}
	}
}

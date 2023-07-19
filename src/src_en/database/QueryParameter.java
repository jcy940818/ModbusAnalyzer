package src_en.database;

public class QueryParameter implements Comparable{
	
	private int index; // 파라미터 인덱스
	private String name; // 파라미터 이름
	private String value; // 파라미터 내용
	private String example; // 파라미터 내용 예시
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
	
	@Override
	public int compareTo(Object obj) {		
		QueryParameter param = (QueryParameter)obj;		
		
		if(this.index < param.index) {
			return -1;
		}else if(this.index == param.index) {
			return 0;
		}else {
			return 1;
		}		
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("index : %d / ", this.index));
		sb.append(String.format("name : %s / ", this.name));
		sb.append(String.format("value : %s", this.value));
		return sb.toString();
	}
	
}
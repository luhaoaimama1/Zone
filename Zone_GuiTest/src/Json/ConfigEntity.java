package Json;

public class ConfigEntity {
	private String packageName;
	private String firstClassName;
//	private String outSavePath;

	public ConfigEntity() {
	}

	public ConfigEntity(String packageName, String firstClassName) {
		this.packageName = packageName;
		this.firstClassName = firstClassName;
//		this.outSavePath = outSavePath;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getFirstClassName() {
		return firstClassName;
	}

	public void setFirstClassName(String firstClassName) {
		this.firstClassName = firstClassName;
	}

//	public String getOutSavePath() {
//		return outSavePath;
//	}
//
//	public void setOutSavePath(String outSavePath) {
//		this.outSavePath = outSavePath;
//	}

}

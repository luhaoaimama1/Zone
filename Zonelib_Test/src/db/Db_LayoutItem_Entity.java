package db;

public class Db_LayoutItem_Entity {
	private String tv1;
	private String tv2;
	private String tv3;
	
	private boolean checkStatu=false;
	
	public Db_LayoutItem_Entity() {
		// TODO Auto-generated constructor stub
	}
	public Db_LayoutItem_Entity(String[] s, int i) {
		// TODO Auto-generated constructor stub
		if (i == 1) {
			tv1 = s[0];
			tv2 = s[1];
			tv3 = s[2];
		} else {
			tv1 = s[0];
			tv2 = s[1];
			tv3 = s[2];
		}
	}

	public String getTv1() {
		return tv1;
	}

	public void setTv1(String tv1) {
		this.tv1 = tv1;
	}

	public String getTv2() {
		return tv2;
	}

	public void setTv2(String tv2) {
		this.tv2 = tv2;
	}

	public String getTv3() {
		return tv3;
	}

	public void setTv3(String tv3) {
		this.tv3 = tv3;
	}
	public boolean getCheckStatu() {
		return checkStatu;
	}
	public void setCheckStatu(boolean checkStatu) {
		this.checkStatu = checkStatu;
	}





}

package Android.Zone.Sqlite.Inner;

public class UpdateColumn{
	public String  column_old;//ע��
	public String column_target;//ע��  ����������ֶԱ�
	public String targetLength;
	public UpdateColumn() {
	}
	public UpdateColumn(String  column_old,String  column_target,String  targetLength) {
		this.column_old=column_old;
		this.column_target=column_target;
		this.targetLength=targetLength;
	}
}
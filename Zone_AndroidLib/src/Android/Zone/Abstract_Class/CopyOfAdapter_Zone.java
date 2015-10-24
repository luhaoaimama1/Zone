package Android.Zone.Abstract_Class;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * ��֧�಼ֶ��
 * @author Zone
 */
public abstract class CopyOfAdapter_Zone<T> extends BaseAdapter {
	private List<T> data;
	private int layout_id;
	private LayoutInflater mInflater;// �õ�һ��LayoutInfalter�����������벼��
	private int[] idArray;// �õ�һ��LayoutInfalter�����������벼��
	
	private List<Integer> idList=new ArrayList<Integer>();
	
	/**
	 * @param context
	 * @param data
	 * @param layout_id
	 */
	public CopyOfAdapter_Zone(Context context, List<T> data,int layout_id) {
		this.data = data;
		this.layout_id=layout_id;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int index) {
		return data.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			//convertView ���ظ�����  arg0�򲻻� �������� ÿ�ζ����ؾͺ��� ����  �й�convertView�Ķ��ǳ�ʼ����
			holder = new ViewHolder();
			convertView = mInflater.inflate(layout_id, null);
			//��id ���ҳ���
			if(!ViewGroup.class.isInstance(convertView)){
				if(convertView.getId()!=-1){
					idList.add(convertView.getId());
				}
			}else{
				if(convertView.getId()!=-1){
					idList.add(convertView.getId());
				}
				isVgComeOn(convertView);	
			}
			
			idArray=new int[idList.size()];
			for (int i = 0; i < idList.size(); i++) {
				idArray[i]=idList.get(i);
			}
			for (int i = 0; i < idArray.length; i++) {
				//�� convertView��ÿ���ؼ� ����map ������ȡ��
				holder.map.put(idArray[i], convertView.findViewById(idArray[i]));
				}
			//��View��Ӽ��� 
			convertView.setTag(holder);
		} else {
			holder = ((ViewHolder) convertView.getTag());
		}
		//���ֶ��Ѿ�Ū����  ������view�� ������   holder����view ��index �и��������
		T dataIndex = data.get(position);
		setData(holder.map,dataIndex,position);
		return convertView;
	}

	private void isVgComeOn(View convertView) {
		if(ViewGroup.class.isInstance(convertView)){
			ViewGroup vg = (ViewGroup)convertView;
			for (int i = 0; i < vg.getChildCount(); i++) {
				View child = vg.getChildAt(i);
				int cId = child.getId();
				if(cId!=-1){
					//����Դ�뷢��-1 ��û��id��˵
					idList.add(cId);
				}
				//������view�� viewGroup������
				isVgComeOn(child);
			}
			
		}
	}


	/**
	 * @author   �����Ǵ���ͼ��
	 * @author Map<String,View> map
	 */
	public  class ViewHolder {
		public	 Map<Integer,View> map=new  HashMap<Integer, View>();
	}

	/**
	 * 
	 * @param viewMap   װ��convertView����ͼ �ʴ���ȡ��Ȼ��ֵ����
	 * @param data     ��itemIndex�����ݼ��� �е�item...
	 * @param position		
	 */
	public abstract  void setData(Map<Integer, View> viewMap, T data, int position); //ע�����ֻ�����������������û�о���ʵ�֡�

}

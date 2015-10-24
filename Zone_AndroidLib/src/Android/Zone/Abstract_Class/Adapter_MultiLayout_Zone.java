package Android.Zone.Abstract_Class;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Android.Zone.Utils.ViewIDsUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * �಼��
 * @author Zone
 */
public abstract class Adapter_MultiLayout_Zone<T> extends BaseAdapter {
	private List<T> data;
	private int[] layout_ids;
	private LayoutInflater mInflater;// �õ�һ��LayoutInfalter�����������벼��
	private int[] idArray;// �õ�һ��LayoutInfalter�����������벼��
	private boolean log=false;
	/**
	 * @param context
	 * @param data
	 * @param layout_ids
	 */
	public Adapter_MultiLayout_Zone(Context context, List<T> data,int... layout_ids) {
		this.data = data;
		this.layout_ids=layout_ids;
		mInflater = LayoutInflater.from(context);
	}

	public int getItemViewType(int position) {
		List<Integer> layoutList=new ArrayList<Integer>();
		for (Integer item : layout_ids) {
			layoutList.add(item);
		}
		int temp=getItemViewType_Zone(position,layoutList);
		if(temp<0||temp>layout_ids.length){
			throw new IllegalStateException("return value must be 0-layout_ids.length!");
		}
		return temp;
	}


	public int getViewTypeCount() {
		return layout_ids.length;
	}
	/**
	 * 
	 * @param position
	 * @param layoutlist 
	 * @return  ����Ϊ layout_ids���ȷ�Χ��
	 */
	public abstract int getItemViewType_Zone(int position, List<Integer> layoutlist);
	
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
			convertView = mInflater.inflate(layout_ids[getItemViewType(position)], null);
			holder.layoutId=layout_ids[getItemViewType(position)];
			//��id ���ҳ���
			List<Integer> idList = ViewIDsUtils.getIDsByView(convertView);			
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
			  if (log) {
				System.out.println("��ʼ����position:" + position);
			}
		} else {
			holder = ((ViewHolder) convertView.getTag());
			if (log) {
				System.out.println("���� position:" + position);
			}
		}
		//���ֶ��Ѿ�Ū����  ������view�� ������   holder����view ��index �и��������
		T dataIndex = data.get(position);
		setData(holder.map,dataIndex,position,holder.layoutId);
		 if (log) {
				int type = getItemViewType(position);
				System.out.println("getView " + position + " " + convertView
						+ " type = " + type);
			}
		return convertView;
	}

	/**
	 * @author   �����Ǵ���ͼ��
	 * @author Map<String,View> map
	 */
	public  class ViewHolder {
		public	 Map<Integer,View> map=new  HashMap<Integer, View>();
		public int layoutId=-1;
	}
	/**
	 * 
	 * @param viewMap   װ��convertView����ͼ �ʴ���ȡ��Ȼ��ֵ����
	 * @param data     ��itemIndex�����ݼ��� �е�item...
	 * @param position		
	 * @param layoutId ��Ϊ����id 
	 */
	public abstract  void setData(Map<Integer, View> viewMap, T data, int position, int layoutId); //ע�����ֻ�����������������û�о���ʵ�֡�

}

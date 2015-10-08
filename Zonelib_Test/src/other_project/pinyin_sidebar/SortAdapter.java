package other_project.pinyin_sidebar;

import java.util.List;

import com.example.mylib_test.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SortAdapter extends BaseAdapter {
	
	private List<SortModel> data;
	private Context context;
	public SortAdapter(Context context,List<SortModel> data){
		this.context=context;
		this.data=data;
	}

	@Override
	public int getCount() {
		return data==null?0:data.size();
	}

	@Override
	public SortModel getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if (convertView == null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.layout_lv_item, null);
			holder=new ViewHolder();
			holder.sortLetter=(TextView) convertView.findViewById(R.id.word);
			holder.name=(TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		SortModel sortModel = data.get(position);
		int selection=getSelectionByPosition(position);	// ����ĸ�ַ�
		int index=getPositionBySelection(selection);
		if (position == index) {
			// ˵�������Ŀ�ǵ�һ������Ҫ��ʾ��ĸ
			holder.sortLetter.setVisibility(View.VISIBLE);
			holder.sortLetter.setText(sortModel.getSortLetter());
		}else{
			holder.sortLetter.setVisibility(View.GONE);
		}
		holder.name.setText(sortModel.getName());
		return convertView;
	}
	
	public int getSelectionByPosition(int position){
		return data.get(position).getSortLetter().charAt(0);
	}
	
	/**
	 * ͨ������ĸ��ȡ��ʾ������ĸ���������ˣ��磺C,����
	 * @author Xubin
	 *
	 */
	public int getPositionBySelection(int selection){
		for (int i = 0; i < getCount(); i++) {
			String sortStr=data.get(i).getSortLetter();
			char firstChar=sortStr.toUpperCase().charAt(0);
			if (firstChar == selection) {
				return i;
			}
		}
		return -1;
	}
	
	class ViewHolder{
		TextView sortLetter;
		TextView name;
	}
}

package other_project.pinyin_sidebar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.example.mylib_test.R;
import other_project.pinyin_sidebar.SideBar.OnLetterSelectedListener;
import other_project.pinyin_sidebar.pinyin.PinYin;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class SideBarActivity extends Activity {

	private ListView lv;
	private SideBar sideBar;
	private TextView mTextView;
	private PinYin pinYin;
	private List<SortModel> data;
	private SortAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_side_bar);

		initView();
	}

	/**
	 * @author Xubin Single QQ:215298766
	 */
	private void initView() {
		lv = (ListView) findViewById(R.id.main_lv);
		sideBar = (SideBar) findViewById(R.id.side_bar);
		mTextView = (TextView) findViewById(R.id.dialog);
		sideBar.setmTextView(mTextView);
		sideBar.setLetterSelectedListener(letterSelectedListener);
		// ��ȡ��������
		data=fillData(getResources().getStringArray(R.array.data));
		// ����ĸ����
		Collections.sort(data, new PinYinComparator());
		// ��������������ʾ��ListView��
		adapter=new SortAdapter(this, data);
		lv.setAdapter(adapter);
	}
	public List<SortModel> fillData(String[] names){
		List<SortModel> sortModels=new ArrayList<SortModel>();
		for (int i = 0; i < names.length; i++) {
			SortModel model=new SortModel();
			model.setName(names[i]);
			String py=pinYin.getPinYin(names[i]);
			String sortLetter=py.substring(0,1).toUpperCase();	// ��ȡ����ƴ��������ĸ��д
			model.setSortLetter(sortLetter);
			sortModels.add(model);
		}
		
		return sortModels;
	}
	
	private OnLetterSelectedListener letterSelectedListener=new OnLetterSelectedListener() {
		
		@Override
		public void onLetterSelected(String s) {
			int position=adapter.getPositionBySelection(s.charAt(0));
			lv.setSelection(position);
		}
	};
}

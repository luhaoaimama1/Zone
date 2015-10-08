package Android.Zone.Utils;

import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentSwitchUtils {
	/**
	 * ������Ļ���Ӧ�����ܺð�  ����switch ֧���ַ���ƥ��
	 */
	private Fragment nowFragment;
	private FragmentActivity frameActivity;
	private int frameId;
	private BackStatue allowBack=BackStatue.NO_BACK;
	private FragmentManager manager;
	
	public enum BackStatue{
		NO_BACK,BACK;
	}
	public Fragment getNowFragment(){
		return nowFragment;
	}
	public List<Fragment> getFragments(){
		if(manager.getFragments()!=null){
			return manager.getFragments();
		}
		return null;
	}

	/**
	 * 
	 * @param frameActivity
	 * @param frameId  ���滻��frame
	 * @param allowBack  �Ƿ��������
	 */
	public FragmentSwitchUtils(FragmentActivity frameActivity, int frameId,BackStatue allowBack) {
		this.frameActivity = frameActivity;
		this.frameId = frameId;
		this.allowBack=allowBack;
	}
	/**
	 * Ĭ�ϣ������� ���� 
	 * @param frameActivity
	 * @param frameId
	 */
	public FragmentSwitchUtils(FragmentActivity frameActivity, int frameId) {
		this(frameActivity,frameId,BackStatue.NO_BACK);
	}

	/**
	 * û�ж�����
	 * @param fragment
	 */
	public void switchPage(Class<?> fragment) {
		switchPage(fragment, -1, -1);
	}
	/**
	 * <strong>	tran.addToBackStack(null); �Ƿ�������˵���һ��fragment  ��д��ֱ���˳�activity</strong>
	 * nullò����tag  δ��֤
	 * Tag���� class.getName();
	 * @param fragment
	 * @param ani_in
	 * @param ani_out
	 */
	public void switchPage(Class<?> fragment,int ani_in,int ani_out) {
		if (!Fragment.class.isAssignableFrom(fragment)) {
			throw new IllegalArgumentException("���Ͳ���frament ����չʾ");
		}
		manager = frameActivity.getSupportFragmentManager();
		FragmentTransaction tran = manager.beginTransaction();
		if(ani_in!=-1){
			tran.setCustomAnimations(ani_in,ani_out);	
//			tran.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
		}
		String targetName = fragment.getName();
		if (nowFragment != null) {
			String nowName = nowFragment.getClass().getName();
			if (nowName.equals(targetName)) {
				// �������ҳ��
				return;
			}
			Fragment targetFm = null;
			if(manager.getFragments()!=null){
				for (Fragment item : manager.getFragments()) {
					if (item!=null&&targetName.equals(item.getTag())) {
						// �����ֱ�ӿ��� ����
						tran.hide(nowFragment);
						targetFm = manager.findFragmentByTag(targetName);
						tran.show(targetFm);
					}
				}
			}
			if (targetFm == null) {
				// û���� ���� Ȼ����ʾ����
				targetFm = Fragment.instantiate(frameActivity, targetName);
				tran.hide(nowFragment);
				tran.add(frameId, targetFm, targetFm.getClass().getName())
						.show(targetFm);
			}
			nowFragment = targetFm;
			
			//��һ�β����Ի���  ���ڶ����Ժ� ͨ��allowBack �Ƿ���ӻ���
			switch (allowBack) {
			case BACK:
				tran.addToBackStack(null);
				break;
			case NO_BACK:
				
				break;
			default:
				break;
			}
		} else {
			// ����һ�ε�ʱ�� ��now Ϊ�յ����
			nowFragment = Fragment.instantiate(frameActivity, targetName);
			tran.add(frameId, nowFragment, nowFragment.getClass().getName())
					.show(nowFragment);
		}
		tran.commit();
	}
}

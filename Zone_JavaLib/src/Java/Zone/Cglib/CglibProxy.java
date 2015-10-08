package Java.Zone.Cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 *  Aop cglib ��̬����  ��ʼ�������� ���޸�
 * @author zone
 *
 */
public class CglibProxy implements MethodInterceptor {

	/**
	 * Ҫ�����Ķ���ĸ������
	 */
    private Object target;

    /**
     * �����������
     * 
     * @param target
     * @return
     */
    public Object getInstance(Object target) {
        this.target = target;
     // ������Ҫ�����������
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        // �ص�����
        enhancer.setCallback(this);
        // �����������   ͨ���ֽ��뼼����̬��������ʵ��
        return enhancer.create();
    }

    // ���ظ������з����ĵ��� ���ԶԷ��صĽ�������޸ģ�����
    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
    	/**
    	 * �����������
    	 */
        Object result = null;
        System.out.println("���￪ʼ");
        // ͨ����������ø����еķ���
        result = methodProxy.invokeSuper(proxy, args);
        System.out.println("�������");
        //�Խ�������˸���  ����ţ�������
        result="2";
        return result;
    }

}
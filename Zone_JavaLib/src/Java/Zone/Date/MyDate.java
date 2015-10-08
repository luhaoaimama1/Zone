package Java.Zone.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * �������ʹ  ����temp.get(Calendar.YEAR) ����
 * @author a
 *
 */
public class MyDate {

	/**
	 * twoDateStringSubtract("20150424143650","20130122173850");
	 * <br> ��Сλ�ò�������
	 * @param DateString1 20150424143650
	 * @param DateString2
	 * @return  ����1��2��5��11ʱ12��13��
	 */
	public static String twoDateStringSubtract(String DateString1,String DateString2)
	{ 
//		Time:20150424143650 
		String finalStr="";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date date1 =sdf.parse(DateString1);
			Date date2 =sdf.parse(DateString2);
			
			Calendar calendar1 = Calendar.getInstance();
			Calendar calendar2 = Calendar.getInstance();
			calendar1.setTime(date1);
			calendar2.setTime(date2);
			finalStr=calendarToString(twoCalendarSubtract(calendar1,calendar2));
			System.out.println(finalStr);
			
		} catch (ParseException e) {
			e.printStackTrace();
			System.err.println("ת���쳣������");
		}
		return finalStr;
	}
	/**
	 * 
	 * @param calendar1
	 * @param calendar2
	 * @return  Calendar ���֮���ֵ
	 */
	public static Calendar twoCalendarSubtract(Calendar calendar1,Calendar calendar2)
	{
		Calendar temp=Calendar.getInstance();
		if (calendar1.getTimeInMillis() < calendar2.getTimeInMillis()) {
			//��calendar1�����
			temp = calendar1;
			calendar1 = calendar2;
			calendar2 = temp;
		}
		temp.set(calendar1.YEAR-calendar2.YEAR, calendar1.MONTH-calendar2.MONTH, calendar1.DATE-calendar2.DATE,
				calendar1.HOUR_OF_DAY-calendar2.HOUR_OF_DAY,calendar1.MINUTE-calendar2.MINUTE, calendar1.SECOND-calendar2.SECOND);
		return temp;
	}
	public static String calendarToString(Calendar calendar1)
	{
		String str=String.format("%d��%d��%d��%dʱ%d��%d��", calendar1.YEAR,calendar1.MONTH,calendar1.DATE,calendar1.HOUR_OF_DAY,calendar1.MINUTE,calendar1.SECOND);
		
		return str;
	}
	
}

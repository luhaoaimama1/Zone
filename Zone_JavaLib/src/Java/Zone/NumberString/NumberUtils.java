package Java.Zone.NumberString;

import java.text.DecimalFormat;

public class NumberUtils {
	/**
	 * 
	 * @param number
	 * @param dotNumber  ����1.1111 ��0��ʾ1  3��ʾ1.111   10����ʾ1.1111 ������û�µ�
	 * @return
	 */
	public static String numberDotFormat(double number,int dotNumber){
		StringBuilder sb=new StringBuilder();
		if(dotNumber==0){
			sb.append("#");
		}else{
			sb.append("#.");
			for (int i = 0; i < dotNumber; i++) {
				sb.append("#");
			}
		}
		DecimalFormat   df=new  DecimalFormat(sb.toString());  
		return df.format(number);
	}
}

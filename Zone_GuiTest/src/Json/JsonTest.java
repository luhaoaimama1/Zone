package Json;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 1.������ Ч�� ��֤��ȷ��  	JSONObject json = new JSONObject(jsonStr);
 * 2.{}����  [] ����  a.get("book")  ��ͷ��{ ����[ ������ת��
 * 3.���� ����
 * �Լ�д��������  
	 * 1.�ֶΣ�HashMap<String,"boolean">  key���ֶ�����   ����Ǹ���Ӧ����ö��  ��,String,Arr��list����
	 * 2.������ 
 * ��������������  ��һ��  root   root_str  root_str_str
 * ��ô��������Ĺ�ؾ���  List<�Լ�д��������> ����
 * 
 * @author Zone
 *
 */
public class JsonTest {
	
	public static void generateFile(String packageName,String firstClassName,String outSavePath,String jsonStr,JsonExceptionListener listener){
		List<OutPutEntity> outList=new ArrayList<OutPutEntity>();
		JSONObject json=null;
		try {
			json = new JSONObject(jsonStr);
		} catch (net.sf.json.JSONException e) {
			if(listener!=null){
				listener.errMsg(e.getMessage());
			}
			return ;
		}
		OutPutEntity classEntity=new OutPutEntity(firstClassName);
		getJsonString(json,classEntity, outList);
	
		if(writeEntityFile(outList, new File(outSavePath), packageName)){
			listener.successMsg();
		}
	}
	
	
	public static void main(String[] args) {
		/**
		 * ����Ҫ�Ĳ���
		 * ��һ��  ����  
		 * �ڶ���  ��һ���������
		 * ������ ������ļ���·��
		 */
		String packageName="json.EntityTest";
		String firstClassName="Root";
		String outSavePath="/";
		
		List<OutPutEntity> outList=new ArrayList<OutPutEntity>();
		File f = new File("D:/json.txt");
//		File f = new File("D:/json2.txt");
//		File f = new File("D:/json3.txt");
//		File f = new File("D:/json5.txt");
		
		String jsonStr = IOUtils.read(f, "gbk");
//		String jsonStr = IOUtils.read(f, "utf-8");
		
		JSONObject json = new JSONObject(jsonStr);
		OutPutEntity classEntity=new OutPutEntity(firstClassName);
		/**
		 * ���� getType 
		 */
//		System.out.println(getType(json, "store"));
//		System.out.println(getType(json.getJSONObject("store"), "book"));
//		System.out.println(getType(json.getJSONObject("store").getJSONObject("bicycle"), "color"));
//		
		/**
		 * �����Ĳ���
		 */
		getJsonString(json,classEntity, outList);
		System.out.println("��С��"+outList.size());
		for (OutPutEntity item : outList) {
			System.out.println("=====================�����֣�"+item.className+"=====================");
			for (Entry<String, FieldProperty> aa : item.field.entrySet()) {
				System.out.println("field����"+aa.getKey()+"\t type:____"+aa.getValue());
				if(aa.getValue()!=FieldProperty.STRING&&aa.getValue()!=FieldProperty.ARRAY){
					System.out.print("\t    �ֶ������֣�"+item.fieldClassName.get(aa.getKey()));	
				}
				System.out.println();
			}
		}
		writeEntityFile(outList, new File(outSavePath), packageName);
		
	}
	public static String hump(String str){
		String str1=str.substring(0, 1);
		String str2=str.substring(1);
		return  str1.toUpperCase()+str2;
	}
//	==================================д���ļ�======================================================
	public static boolean writeEntityFile(List<OutPutEntity> outList,File outFileFolder,String packageName){
		String nextLineStr="\r\n";
		
		for (OutPutEntity item : outList) {
			System.out.println("=====================�����֣�"+item.className+"=====================");
			//����java���ļ�   
			File classFile=new File(outFileFolder, item.className+".java");
			
			boolean haveListUtils=false;
			StringBuilder ksSb=new StringBuilder();
			StringBuilder filedSb=new StringBuilder();
			StringBuilder setGetSb=new StringBuilder();
			String endStr="}";
			for (Entry<String, FieldProperty> aa : item.field.entrySet()) {
				//TODO  ��java���ļ���д��  �����ֶ�
				System.out.println("field����"+aa.getKey()+"\t type:____"+aa.getValue());
				if(aa.getValue()!=FieldProperty.STRING&&aa.getValue()!=FieldProperty.ARRAY){
					System.out.print("\t    �ֶ������֣�"+item.fieldClassName.get(aa.getKey()));	
				}
				String humpStr = hump(aa.getKey());
				String classNameTemp = item.fieldClassName.get(aa.getKey());
				switch (aa.getValue()) {
				case STRING:
					filedSb.append("	private String "+aa.getKey()+";"+nextLineStr);
					
					setGetSb.append("	public String get"+humpStr+"() {"+nextLineStr);
					setGetSb.append("		return "+aa.getKey()+";"+nextLineStr);
					setGetSb.append("	}"+nextLineStr);
					setGetSb.append(nextLineStr);
					setGetSb.append("	public void set"+humpStr+"(String "+aa.getKey()+") {"+nextLineStr);
					setGetSb.append("		this."+aa.getKey()+" = "+aa.getKey()+";"+nextLineStr);
					setGetSb.append("	}"+nextLineStr);
					break;
				case ARRAY:
					haveListUtils=true;
					filedSb.append("	private List<String> "+aa.getKey()+"=new ArrayList<String>();"+nextLineStr);
					
					setGetSb.append("	public List<String> get"+humpStr+"() {"+nextLineStr);
					setGetSb.append("		return "+aa.getKey()+";"+nextLineStr);
					setGetSb.append("	}"+nextLineStr);
					setGetSb.append(nextLineStr);
					setGetSb.append("	public void set"+humpStr+"(List<String> "+aa.getKey()+") {"+nextLineStr);
					setGetSb.append("		this."+aa.getKey()+" = "+aa.getKey()+";"+nextLineStr);
					setGetSb.append("	}"+nextLineStr);
					break;
				case LIST:
					haveListUtils=true;
					filedSb.append("	private List<"+classNameTemp+"> "+aa.getKey()+"=new ArrayList<"+classNameTemp+">();"+nextLineStr);
					
					setGetSb.append("	public List<"+classNameTemp+"> get"+humpStr+"() {"+nextLineStr);
					setGetSb.append("		return "+aa.getKey()+";"+nextLineStr);
					setGetSb.append("	}"+nextLineStr);
					setGetSb.append(nextLineStr);
					setGetSb.append("	public void set"+humpStr+"(List<"+classNameTemp+"> "+aa.getKey()+") {"+nextLineStr);
					setGetSb.append("		this."+aa.getKey()+" = "+aa.getKey()+";"+nextLineStr);
					setGetSb.append("	}"+nextLineStr);
					break;
				case CLASS:
					filedSb.append("	private "+classNameTemp+" "+aa.getKey()+";"+nextLineStr);

					setGetSb.append("	public "+classNameTemp+" get"+humpStr+"() {"+nextLineStr);
					setGetSb.append("	return "+aa.getKey()+";"+nextLineStr);
					setGetSb.append("	}"+nextLineStr);
					setGetSb.append(nextLineStr);
					setGetSb.append("	public void set"+humpStr+"("+classNameTemp+" "+aa.getKey()+") {"+nextLineStr);
					setGetSb.append("	this."+aa.getKey()+" = "+aa.getKey()+";"+nextLineStr);
					setGetSb.append("	}"+nextLineStr);
					break;

				default:
					break;
				}
				setGetSb.append(nextLineStr);
			}
			if(haveListUtils){
				ksSb.append("package "+packageName+";"+nextLineStr);
				//====== ��ӵ���
				ksSb.append("import java.util.ArrayList;"+nextLineStr);
				ksSb.append("import java.util.List;"+nextLineStr);
				//======
				ksSb.append("public class "+item.className+" {"+nextLineStr);
			}else{
				ksSb.append("package "+packageName+";"+nextLineStr);
				ksSb.append("public class "+item.className+" {"+nextLineStr);
			}
			filedSb.append(nextLineStr);
			String fileWriteStr=ksSb.toString()+filedSb.toString()+setGetSb.toString()+endStr;
			if (!IOUtils.write(classFile, fileWriteStr, "gbk")) {
				return false;
			}
		}
		return true;
	}
	
//	==================================����======================================================

	public static void addOutPutEntity_NameUnique(OutPutEntity out,List<OutPutEntity> outList){
		for (OutPutEntity item : outList) {
			if(out.className.equals(item.className)){
				int index=outList.indexOf(item);
				outList.set(index, out);
				return;
			}
		}
		outList.add(out);
	}
	
	public enum FieldProperty{
		STRING,CLASS,LIST,ARRAY;
	}
	//��֤ JSONObject�� key ��ֵ�� {}����  [] ���� ���� �ַ����� 
	public static FieldProperty getType(JSONObject json,String key){
		String str = json.get(key).toString();
		System.out.println(str);
		if(str.startsWith("{")){
			return FieldProperty.CLASS;
		}
		if(str.startsWith("[{")){
			return FieldProperty.LIST;
		}
		if(str.startsWith("[")){
			//[] ��"[]"������  str����[] ��������һ����jsonArray һ��jsonObject����ֻ����ô����
			try {
				json.getJSONArray(key);
			} catch (net.sf.json.JSONException e) {
				//���ת�� �������쳣�� ����String ��"[]"
				return FieldProperty.STRING;
			}
			return FieldProperty.ARRAY;
		}
		return FieldProperty.STRING;
	}

	
	public static void getJsonString(JSON json, OutPutEntity classEntity,List<OutPutEntity> outList) {
		if (JSONObject.class.isInstance(json)) {
			JSONObject jsonTemp = (JSONObject) json;
			for (Iterator<String> iter = (jsonTemp).keys(); iter.hasNext();) {
				String key = iter.next();
				switch (getType(jsonTemp, key)) {
				case CLASS:
					classEntity.field.put(key, FieldProperty.CLASS);
					classEntity.fieldClassName.put(key, classEntity.className+"_"+key);
					/** �Ե�*/
					getJsonString(jsonTemp.getJSONObject(key), new OutPutEntity(classEntity.className+"_"+key), outList);
					break;
				case LIST:
					classEntity.field.put(key, FieldProperty.LIST);
					classEntity.fieldClassName.put(key, classEntity.className+"_"+key);
					/** �Ե�*/
					getJsonString(jsonTemp.getJSONArray(key), new OutPutEntity(classEntity.className+"_"+key), outList);
					break;
				case STRING:
					classEntity.field.put(key, FieldProperty.STRING);
					break;
				case ARRAY:
					String str = jsonTemp.get(key).toString();
					System.out.println("nimei:"+str);
					classEntity.field.put(key, FieldProperty.ARRAY);
					break;
				default:
					break;
				}
			}
			addOutPutEntity_NameUnique(classEntity, outList);
		}
		if (JSONArray.class.isInstance(json)) {
				JSONArray jsonTemp = (JSONArray) json;
				for (int i = 0; i < jsonTemp.length(); i++) {
					JSONObject jsonObj = jsonTemp.getJSONObject(i);
					/** �Ե�*/
					getJsonString(jsonObj, classEntity, outList);
				}
		}
	}
	
}

package com.hnsun.myaccount.util.data.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.hnsun.myaccount.util.ReflectUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.data.ApplicationDatas;
import com.hnsun.myaccount.util.log.LogFactory;

/**
 * DOM解析
 * @author hnsun
 * @date 2016/10/10
 */
public class XMLByDOM4j<E> {
	
	public XMLByDOM4j(E obj) { 
		if(UtilBoss.ObjUtil.isNull(obj)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
		this.obj = obj;
	}
	
	public XMLByDOM4j(List<E> objs) { 
		if(UtilBoss.ObjUtil.isNull(objs)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
		this.objs = objs;
	}
	
	public XMLByDOM4j(Class<E> objClazz, Object... params) {
		if(UtilBoss.ObjUtil.isNull(objClazz)) UtilBoss.ExceptionUtil.throwNullPointerArgument();
		this.objClazz = objClazz;
		this.params = params;
	}

	/**
	 * 将对象中的属性列转换成XML的元素树结构
	 * @param element 顶层元素
	 * @param obj 所要转换的对象
	 * @param elementNames 指定该对象需转换的属性名 为null时全选
	 * @param andId 是否有Id  若elementNames为空无效 否者为elementNames第一个
	 */
	public static <E> void bindElementFromObj(Element element, E obj, List<String> elementNames, boolean andId) {
		UtilBoss.ConditionUtil.n(element, obj);
		
		if(!UtilBoss.IfUtil.isEmpty(elementNames)) {
			int startIndex = 0; //开始位置
			if(andId) { //Id 成属性
				String str = UtilBoss.StrUtil.getByObj(ReflectUtil.getAttrValue(obj, elementNames.get(0)));
				if(UtilBoss.ObjUtil.isNotNull(str)) { //即使“”也要添加
					element.addAttribute(elementNames.get(0), str);
					startIndex = 1;
				}
			}

			for(int i = startIndex; i < elementNames.size(); i++) { //其他成子元素
				String str = UtilBoss.StrUtil.getByObj(ReflectUtil.getAttrValue(obj, elementNames.get(i)));
				if(UtilBoss.ObjUtil.isNotNull(str)) {
					Element child = element.addElement(elementNames.get(i));
					child.setText(str);
				}
			}
		} else if(UtilBoss.ObjUtil.isNull(elementNames)) {//所有元素
			Map<String, Object> map = ReflectUtil.getAttrValues(obj);
			Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<String, Object> entry = iter.next();
				String name = entry.getKey();
				if(ignore(name)) continue;

				String value = UtilBoss.StrUtil.getByObj(entry.getValue());
				if(UtilBoss.ObjUtil.isNotNull(value)) {
					Element child = element.addElement(name);
					child.setText(value);
				}
			}
		}
	}

	/**
	 * 将XML的元素树结构列转换成对象中的属性
	 * @param element 整个树结构
	 * @param obj 所要设置值的对象
	 * @param elementNames 指定该对象需转换的属性名 为null时全选
	 * @param andId 是否有Id  若elementNames为空无效 否者为elementNames第一个
	 * @param params 构建对象需要的参数
	 */
	public static <E> E bindObjFromElement(Element element, Class<E> objClazz, List<String> elementNames, boolean andId, Object... params) {
		E ret = null;
		UtilBoss.ConditionUtil.n(element, objClazz);
		
		ret = (E) ReflectUtil.newInstance(objClazz, params);
		if(!UtilBoss.IfUtil.isEmpty(elementNames)) {
			int startIndex = 0; //开始位置
			if(andId) { //获得Id
				String str = element.attributeValue(elementNames.get(0));
				if(UtilBoss.ObjUtil.isNotNull(str)) {
					ReflectUtil.setAttrValue(ret, elementNames.get(0), str);
					startIndex = 1;
				}
			}

			for(int i = startIndex; i < elementNames.size(); i++) { //其他对象元素
				Element child = element.element(elementNames.get(i));
				if(UtilBoss.ObjUtil.isNull(child)) continue;

				String str = child.getText();
				if(UtilBoss.ObjUtil.isNotNull(str)) ReflectUtil.setAttrValue(ret, child.getName(), str);
			}
		} else if(UtilBoss.ObjUtil.isNull(elementNames)) {//所有元素
			List<Element> children = element.elements();
			for(Element child : children) {
				String name = child.getName();
				if(ignore(name)) continue;

				String value = child.getText();
				if(UtilBoss.ObjUtil.isNotNull(value)) ReflectUtil.setAttrValue(ret, name, value);
			}
		}
		return ret;
	}

	/**
	 * 将内容写进xml的文件地址
	 * @param document 将要写出去的内容
	 * @param outPath 文件存储地址 eg: ../file.xml
	 */
	public static void generateXmlFile(Document document, String outPath) {
		UtilBoss.ConditionUtil.nt(document, outPath);
		
		XMLWriter xmlWriter = null;
		FileWriter writer;
		try {
			writer = new FileWriter(outPath); //封装通道
		
			OutputFormat xmlFormat = new OutputFormat(); // 输出转换对象
			xmlFormat.setEncoding(ApplicationDatas.APP_ENCODING); //文件编码
			
			xmlWriter = new XMLWriter(writer, xmlFormat); //创建写文件方法
			xmlWriter.write(document); //写出文件
		} catch (IOException e) {
			LogFactory.log().e(e, "文件没有找到");
		} finally {
			try {
				if(UtilBoss.ObjUtil.isNotNull(xmlWriter)) xmlWriter.close();
			} catch (IOException e) {
            	LogFactory.log().e(e, "输入输出有问题");
			}
		}
	}

	/**
	 * 从Xml文件中获得Document
	 * @param inPath
	 * @return
	 */
	public static Document fromXmlFile(String inPath) {
		UtilBoss.ConditionUtil.n(inPath);
		
		Document document = null;
		SAXReader reader = new SAXReader();
		try {
			File file = new File(inPath);
			if(file.exists()) document = reader.read(file);
		} catch (DocumentException e) {
			LogFactory.log().e(e, "XML文件转化为文档失败");
		}

		return document;
	}
	
	/**
	 * 忽略的字段
	 * @param elementName
	 * @return
	 */
	public static boolean ignore(String elementName) {
		boolean ret = false;
		UtilBoss.ConditionUtil.n(elementName);
		
		if(elementName.equals("serialVersionUID")) ret = true;
		
		return ret;
	}

	public Document toDocument() { //转化为Document
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement(ApplicationDatas.XML_ROOT_NAME); //头
		
		if(UtilBoss.ObjUtil.isNotNull(obj)) obj2Element(root, obj);
		else if(!UtilBoss.IfUtil.isEmpty(objs)) obj2Elements(root, objs);
		
		return document;
	}

	public List<E> fromDocument(Document document) { //params 构建对象的参数 转为对象
		UtilBoss.ConditionUtil.n(document);
		Element root = document.getRootElement(); //头
		return element2Objs(root);
	}

	public XMLByDOM4j<E> setElements(List<String> elementNames, boolean andId) { //导出对象参数
		this.elementNames = elementNames;
		this.andId = andId;
		return this;
	}

	private void obj2Element(Element root, E obj) { //将对象转换成元素对象
		UtilBoss.ConditionUtil.n(root, obj);
		Element child = root.addElement(obj.getClass().getSimpleName());//类名 即当前最外元素名
		bindElementFromObj(child, obj, elementNames, andId);
	}

	private void obj2Elements(Element root, List<E> objs) { //将List内对象转为元素对象
		UtilBoss.ConditionUtil.n(root, objs);
		for(E obj : objs) {
			obj2Element(root, obj);
		}
	}

	private E element2Obj(Element child) { //单个元素对象转化为对象
		UtilBoss.ConditionUtil.n(child);
		return bindObjFromElement(child, objClazz, elementNames, andId, params);
	}

	private List<E> element2Objs(Element root) { //整个List元素转化为对象
		List<E> ret = null;
		UtilBoss.ConditionUtil.n(root);
		
		List<Element> children = root.elements(); //单个实体
		ret = new ArrayList<E>();
		for(Element child : children) {
			ret.add(element2Obj(child));
		}
		return ret;
	}

	private E obj;
	private List<E> objs;
	private Class<E> objClazz;
	private Object[] params;

	private List<String> elementNames; //指定该对象需转换的属性名 为null时全选
	private boolean andId; //是否有Id  若elementNames为空无效 否者为elementNames第一个

	{
		elementNames = null;
		andId = false;
	}
}

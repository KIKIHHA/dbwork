package com.cubee.db;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.Properties;

/**动态代理(反射技术)*/
public class BeanFactory {

	static Properties prop = new Properties();
	static String path;
	static {
		try {

			Resource resource = new ClassPathResource("bean.properties");
			path=((ClassPathResource) resource).getPath();
			InputStream in=resource.getInputStream();
			//存疑,如何不适用springboot提供的resource,而是用传统的方法来读取文件
			/*InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("classpath:bean.properties");*/
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object getBean(String name,String path) {
		Object obj = null;
		try {

			Resource resource = new ClassPathResource(path);
			InputStream in=resource.getInputStream();
			//存疑,如何不适用springboot提供的resource,而是用传统的方法来读取文件
			/*InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("classpath:bean.properties");*/
			prop.load(in);
			obj=prop.getProperty(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static Object getBean(String name) {
		Object obj = null;
		try {
			//获取指定类的Class实例
			/*Class<?> c = Class.forName(prop.getProperty(name));
			//反射创建类的对象
			obj = c.newInstance();*/
			obj=prop.getProperty(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 *
	 * @param name 需要改变的key名
	 * @param value 改变后的值
	 */
	public static void setBean(String name,String value) {
		prop.setProperty(name,value);
		OutputStream out;
		try {
			System.out.println("path = " + path);
			out=new FileOutputStream("src/main/resources/bean.properties");
			prop.store(out,"someChanged");
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*return obj;*/
	}
}

package com.holly.util;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import lombok.extern.log4j.Log4j;

/**
 * SPRING工具类，获取spring bean ,控制spring 刷新，读取spring 状态
 * <ol>
 * <li>{@link ApplicationContext }</li>
 * <li>{@link ConfigurableApplicationContext }</li>
 * <li>{@link WebApplicationContextUtils }</li>
 * </ol>
 * 
 * @see
 *
 */
@Log4j
public class SpringUtil {
	public static final String BEAN_REF_FACTORY_FILE_NAME = "applicationContext.xml";
	private static ApplicationContext appCtx;
	private static boolean isInWebContext = true;

	public static void setApplicationContext(ApplicationContext appCtx1) {
		if (appCtx1 == null)
			return;
		isInWebContext = true;
		appCtx = appCtx1;
	}

	public static ApplicationContext getAppCtx() {
		if (appCtx == null) {
			isInWebContext = false;
			appCtx = createXmlAppCtx();
		}
		return appCtx;
	}

	public static ApplicationContext getWebAppCtx(ServletContext servletContext) {
		return WebApplicationContextUtils.getWebApplicationContext(servletContext);
	}

	public static void refresh() {
		ApplicationContext appCtx = getAppCtx();
		log.info("=========================>>>>" + getStatus());
		if (appCtx instanceof ConfigurableApplicationContext) {
			((ConfigurableApplicationContext) appCtx).refresh();
			log.info("=========================>>>>" + getStatus());
		}
	}

	/**
	 * 返回spring当前状态
	 * 
	 * @return
	 */
	public static String getStatus() {
		String appName = getAppCtx().getApplicationName();
		String format = " %s [ isRunning :  %s , isActive :  %s   , beanCnt : %s";
		return String.format(format,
				new Object[] { appName, isRunning(), isActive(), appCtx.getBeanDefinitionCount() });
	}

	public static void close() {
		ApplicationContext appCtx = getAppCtx();
		if (appCtx instanceof ConfigurableApplicationContext) {
			((ConfigurableApplicationContext) appCtx).close();
		}
	}

	public static boolean isRunning() {
		ApplicationContext appCtx = getAppCtx();
		if (appCtx instanceof ConfigurableApplicationContext) {
			return ((ConfigurableApplicationContext) appCtx).isRunning();
		}
		return false;
	}

	public static boolean isActive() {
		ApplicationContext appCtx = getAppCtx();
		if (appCtx instanceof ConfigurableApplicationContext) {
			return ((ConfigurableApplicationContext) appCtx).isActive();
		}
		return false;
	}

	protected static ApplicationContext createXmlAppCtx() {
		return new ClassPathXmlApplicationContext(new String[] { BEAN_REF_FACTORY_FILE_NAME }, true);
	}

	public static void setAppCtx(ApplicationContext webAppCtx) {
		if (webAppCtx == null)
			return;
		isInWebContext = true;
		appCtx = webAppCtx;
	}

	/**
	 * 根据beanName(对应于Bean配置的Id属性）获取Bean对象
	 * 
	 * @param beanName
	 * @return
	 */
	public final static Object getBean(String beanName) {
		return getAppCtx().getBean(beanName);
	}

	public final static Object getBean(String beanName, String className) throws ClassNotFoundException {
		Class clz = Class.forName(className);
		return getAppCtx().getBean(beanName, clz.getClass());
	}

	public final static <T> T getBean(String beanName, Class<T> clz) throws ClassNotFoundException {
		return getAppCtx().getBean(beanName, clz);
	}

	public final static <T> Map<String, T> getBeansOfType(Class<T> clz) {
		return appCtx.getBeansOfType(clz);
	}

	public static boolean isInWebContext() {
		return isInWebContext;
	}

	/**
	 * 获取类型为requiredType的对象
	 * 
	 * @param clz
	 * @return
	 * @throws org.springframework.beans.BeansException
	 * 
	 */
	public static <T> T getBean(Class<T> clz) throws BeansException {
		@SuppressWarnings("unchecked")
		T result = (T) appCtx.getBean(clz);
		return result;
	}

	/**
	 * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
	 * 
	 * @param name
	 * @return boolean
	 */
	public static boolean containsBean(String name) {
		return appCtx.containsBean(name);
	}

	/**
	 * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
	 * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
	 * 
	 * @param name
	 * @return boolean
	 * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
	 * 
	 */
	public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		return appCtx.isSingleton(name);
	}

	/**
	 * @param name
	 * @return Class 注册对象的类型
	 * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
	 * 
	 */
	public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		return appCtx.getType(name);
	}

	/**
	 * 如果给定的bean名字在bean定义中有别名，则返回这些别名
	 * 
	 * @param name
	 * @return
	 * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
	 * 
	 */
	public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
		return appCtx.getAliases(name);
	}

}

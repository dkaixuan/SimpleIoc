package com.simple;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kaixuan
 * @version 1.0
 * @date 23/3/2020 上午10:57
 */
public class SimpleIOC {


    Map<String, Object> beanMap = new HashMap<>();

    /**
     * 初始化bean
     *
     * @param location
     */
    public SimpleIOC(String location) throws Exception {
        loadsBeans(location);
    }


    private void loadsBeans(String location) throws Exception {
        //加载xml配置文件
        InputStream inputStream = new FileInputStream(location);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);
        Element root = document.getDocumentElement();
        NodeList nodes = root.getChildNodes();

        //遍历<bean>标签
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                String id = element.getAttribute("id");
                String className = element.getAttribute("class");
                //加载beanClass
                Class beanClass = null;
                try {
                    beanClass = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
                //创建bean
                Object bean = beanClass.newInstance();

                //遍历<property>标签
                NodeList propertyNodes = element.getElementsByTagName("property");
                for (int j = 0; j < propertyNodes.getLength(); j++) {
                    Node propertyNode = propertyNodes.item(j);
                    if (propertyNode instanceof Element) {
                        Element propertyElement = (Element) propertyNode;
                        String name = propertyElement.getAttribute("name");
                        String value = propertyElement.getAttribute("value");
                        //利用反射将bean的相关字段访问权限设为可访问
                        Field field = bean.getClass().getDeclaredField(name);
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        if (value != null && value.length() > 0) {
                            field.set(bean, value);
                        }else {
                            String ref = propertyElement.getAttribute("ref");
                            if (ref == null || ref.length() == 0) {
                                throw new IllegalArgumentException("ref config error");
                            }
                            //将引用填充到相关字段中
                            field.set(bean,getBean(ref));
                        }

                        //将bean注册导Bean容器中
                        registerBean(id, bean);
                    }

                }



            }

        }

    }


    public Object getBean(String name) {
        Object bean = beanMap.get(name);
        if (bean == null) {
            throw new IllegalArgumentException("there is no bean with name:" + name);
        }
        return bean;
    }


    public void registerBean(String id, Object bean) {
        beanMap.put(id, bean);
    }


}

package com.wmall.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * 封装了XML转换成object，object转换成XML的代码
 *
 * @author niexx
 */
public class XMLUtil {
    /**
     * JavaBean转换成xml 默认编码UTF-8
     *
     * @param obj
     * @return
     */
    public static String convertToXml(Object obj) {
        // return convertToXml(obj, "UTF-8");
        return convertToXml(obj, "GBK");
    }

    /**
     * JavaBean转换成xml
     *
     * @param obj
     * @param encoding
     * @return
     */
    public static String convertToXml(Object obj, String encoding) {
        String result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * xml转换成JavaBean
     *
     * @param xml
     * @param c
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertToJavaBean(String xml, Class<T> c) {
        T t = null;
        try {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }
}

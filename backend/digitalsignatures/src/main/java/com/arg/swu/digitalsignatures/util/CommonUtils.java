package com.arg.swu.digitalsignatures.util;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author sitthichaim
 *
 */
@Slf4j
public class CommonUtils  {

	
	public static final String STATUS = "status";
	public static final String MESSAGE = "message";
	public static final String DATA = "data";
	public static final String ENTRIES = "entries";
	public static final String CLASSPATH = "language/messages";
	/**
	 * 
	 * @param entries
	 * @param message
	 * @param addOn
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> response(
			Object entries,
			String message,
			Map<String, Object> addOn) {

		Map<String, Object> response = new HashMap<>();
		response.put(STATUS, 200);
		response.put(MESSAGE, message);
		response.put(ENTRIES, entries);
		if (null != addOn) {
			for (Map.Entry<String, Object> entry : addOn.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				response.put(key, value);
			}
		}
		return response;
	}

	/**
	 * 
	 * @param message
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> responseError(String message) {

		Map<String, Object> response = new HashMap<>();
		response.put(STATUS, 400);
		response.put(MESSAGE, message);

		return response;
	}
	
	/**
	 * 
	 * @param status
	 * @param message
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> responseByStatus(int status, String message) {

		Map<String, Object> response = new HashMap<>();
		response.put(STATUS, status);
		response.put(MESSAGE, message);

		return response;
	}
	
	/**
	 * i18n
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		try {
			
			ResourceBundle resourceBundle = ResourceBundle.getBundle(CLASSPATH);
			return resourceBundle.getString(key);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "";
	}
	
	/**
	 * concatLikeParam
	 * @param param
	 * @param start
	 * @param end
	 * @return String
	 */
	public static String concatLikeParam(String param, boolean start, boolean end) {

		if (StringUtils.isBlank(param)) return "";
		
		StringBuilder result = new StringBuilder();
		if (start) result.append("%");
		result.append(param);
		if (end) result.append("%");

		return result.toString();
	}
	
	/**
	 * convertDateSqlDate
	 * @param date
	 * @param isStart
	 * @param isEnd
	 * @return String
	 */
	public static String convertDateSqlDate(Date date, boolean isStart, boolean isEnd) {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (date == null) return null;
		if (isStart) {
			return formatDate.format(date) + " 00:00";
		} else if (isEnd) {
			return formatDate.format(date) + " 23:59";
		} else {
			return formatDateTime.format(date);
		}
	}

	/**
	 * joinParam
	 * @param objs
	 * @param otherParam
	 * @return Object[]
	 */
    public static Object[] joinParam(Object[] objs, Object... otherParam) {
        List<Object> params = new ArrayList<>();
        if(objs!=null){
            for(Object obj : objs) params.add(obj);
        }
        if(otherParam!=null && otherParam.length>0){
            for (Object obj : otherParam){
                params.add(obj);
            }
        }
        return params.toArray();
    }

    /**
     * joinParam
     * @param objs
     * @param obj2
     * @return Object[]
     */
    public static Object[] joinParam(List<Object> objs, List<Object> obj2) {
        List<Object> params = new ArrayList<>();
        if(objs!=null){
            for(Object obj : objs) params.add(obj);
        }
        if(obj2!=null){
            for (Object obj : obj2){
                params.add(obj);
            }
        }
        return params.toArray();
    }
    
    /**
     * copyObject
     * @param src
     * @param dest
     */
    public static void copyObject(Object src, Object dest) {

	    Field[] srcFields = src.getClass().getDeclaredFields();
	
	    for (Field srcField : srcFields) {
	        try {
	            Field destField = dest.getClass().getDeclaredField(srcField.getName());
				srcField.setAccessible(true);
	            destField.setAccessible(true);
	            destField.set(dest, srcField.get(src));
	        } catch (Exception e) {
                // Handle exception
	        }
	    }

    }
    
    /**
     * getIdByAnotation
     * @param entity
     * @return id
     */
    public static Long getIdByAnotation(Object entity) {
        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(jakarta.persistence.Id.class)) {
                field.setAccessible(true);
                try {
                    return (Long) field.get(entity);
                } catch (IllegalAccessException e) {
                }
            }
        }
        // If no @Id annotated field is found
        return null;
    }

    /**
     * hashSha256
     * @param value
     * @return
     */
    public static String hashSha256(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes());
            byte byteData[] = md.digest();
            // convert the byte to hex
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("==============[ hashSha256 ]==============");
            log.error(e.getMessage(), e);
            return null;
        }
    }
}

package com.arg.swu.commons;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author sitthichaim
 *
 */
public class CommonUtils implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger(CommonUtils.class);
	

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
			LOG.error(e.getMessage(), e);
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
            LOG.error("==============[ hashSha256 ]==============");
            LOG.error(e.getMessage(), e);
            return null;
        }
    }
    
    
    // Regular expression to match LDAP special characters
    private static final Pattern LDAP_SPECIAL_CHARS_PATTERN = Pattern.compile("[#\\\"+;<\\\\\\>]");

    /**
     * Escape LDAP special characters in a given string by replacing them with their ASCII hex values.
     *
     * @param input the input string to escape
     * @return the escaped string
     */
    public static String escapeLdapSpecialCharacters(String input) {
        Matcher matcher = LDAP_SPECIAL_CHARS_PATTERN.matcher(input);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, escapeLdapSpecialCharacter(matcher.group()));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String escapeLdapSpecialCharacter(String character) {
        byte[] bytes = character.getBytes();
        StringBuilder escaped = new StringBuilder();
        for (byte b : bytes) {
            escaped.append('\\');
            escaped.append(String.format("%02X", b));
        }
        return escaped.toString();
    }
    
    /**
     * formatDate
     * @param date
     * @param format
     * @param locale
     * @return
     */
    public static String formatDate(Date date, String format, Locale locale){
        if(date == null)
        {
            return "";
        }

        DateFormat dateFormat = new SimpleDateFormat(format, locale);
        return dateFormat.format(date);
    }
    	
    /**
     * 
     * longZeroToNull
     * @param src
     */
    public static void longZeroToNull(Object src) {
	    Field[] srcFields = src.getClass().getDeclaredFields();
	    for (Field srcField : srcFields) {
	    	
	        try {
	        	Field field = src.getClass().getDeclaredField(srcField.getName());
	        	field.setAccessible(true);
	        	if (field.getType() == Long.class && null != field.get(src)) {
	        		field.set(src, field.get(src).equals(0L) ? null : field.get(src));
	        	}
	        } catch (Exception e) {
                // Handle exception
	        }
	    }

    }
    

    /**
     * checkBetweenDate
     * @param activePeriodStart
     * @param activePeriodEnd
     * @param now
     * @return boolean
     */
    public static boolean checkBetweenDate(Date activePeriodStart, Date activePeriodEnd, Date now) {
        if (activePeriodStart == null) {
            return activePeriodEnd == null || now.compareTo(activePeriodEnd) < 0;
        } else if (activePeriodEnd == null) {
            return activePeriodStart.compareTo(now) < 0;
        } else {
            return activePeriodStart.compareTo(now) * now.compareTo(activePeriodEnd) > 0;
        }
    }
    
    
}

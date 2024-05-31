package com.arg.swu.services;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import com.arg.swu.commons.ApiConstant;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@RequiredArgsConstructor
public class LdapService implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(LdapService.class);

	@Qualifier("ldapTemplateDefault")
	private final LdapTemplate ldapTemplate;
	
	public boolean authenLdap(String username, String password) {
		try {
			
			if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
				return false;
			}
			
			String uuid = MessageFormat.format("(uid={0})", username);
			return ldapTemplate.authenticate("", uuid, password);
		} catch (Exception e) {
			LOG.error("=================[ authenLdap ]===================");
			LOG.error(e.getMessage(), e);
		}
		return false;
	}
	
	public boolean userIsExists(String uid) {
		try {
			String uuid = MessageFormat.format("(uid={0})", uid);
		    List<String> data = ldapTemplate.search("", uuid, (AttributesMapper<String>) attrs -> {
			    // LOG.info("userPassword -> {}", attrs);
		    	return (String) attrs.get("cn").get();
		    });
			return !data.isEmpty();
		} catch (Exception e) {
			LOG.error("=================[ userIsNotExists ]===================");
			LOG.error(e.getMessage(), e);
		}
		return false;
	}
	
}

package com.arg.swu.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.arg.swu.commons.ApiConstant;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * 
 * @author sitthichaim
 *
 */
@Component
public class FileTransferServiceImpl implements FileTransferService, ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(FileTransferServiceImpl.class);

	@Value("${sftp.buasri.host}")
	private String buasriHost;
	
	@Value("${sftp.buasri.port}")
	private Integer buasriPort;
	
	@Value("${sftp.buasri.username}")
	private String buasriUsername;
	
	@Value("${sftp.buasri.password}")
	private String buasriPassword;
	
	@Value("${sftp.buasri.sessionTimeout}")
	private Integer buasriSessionTimeout;
	
	@Value("${sftp.buasri.channelTimeout}")
	private Integer buasriChannelTimeout;
	
	@Value("${sftp.buasri.default.path}")
	private String buasriDefaultPath;
	
	@Override
	public boolean uploadFile(String localFilePath, String remoteFilePath) {
		
		ChannelSftp channelSftp = createChannelSftp();
		try {
			channelSftp.put(localFilePath, remoteFilePath);
			return true;
		} catch(SftpException ex) {
			LOG.error("========================[ uploadFile ]========================");
			LOG.error(ex.getMessage(), ex);
		} finally {
			disconnectChannelSftp(channelSftp);
		}
		
		return false;
	}
	
	private ChannelSftp createChannelSftp() {
		try {
			JSch jSch = new JSch();
			Session session = jSch.getSession(buasriUsername, buasriHost, buasriPort);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(buasriPassword);
			session.connect(buasriSessionTimeout);
			Channel channel = session.openChannel("sftp");
			channel.connect(buasriChannelTimeout);
			return (ChannelSftp) channel;
		} catch(JSchException ex) {
			LOG.error("========================[ createChannelSftp ]========================");
			LOG.error(ex.getMessage(), ex);
		}
		
		return null;
	}
	
	private void disconnectChannelSftp(ChannelSftp channelSftp) {
		try {
			if( channelSftp == null) 
				return;
			
			if(channelSftp.isConnected()) 
				channelSftp.disconnect();
			
			if(channelSftp.getSession() != null) 
				channelSftp.getSession().disconnect();
			
		} catch(Exception ex) {
			LOG.error("========================[ disconnectChannelSftp ]========================");
			LOG.error(ex.getMessage(), ex);
		}
	}
	
}

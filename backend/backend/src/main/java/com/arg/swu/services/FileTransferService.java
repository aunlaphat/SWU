package com.arg.swu.services;

/**
 * 
 * @author sitthichaim
 *
 */
public interface FileTransferService {

	boolean uploadFile(String localFilePath, String remoteFilePath);
	
}

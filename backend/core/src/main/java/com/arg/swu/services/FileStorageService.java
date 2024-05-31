package com.arg.swu.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.dto.UploadFileData;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
public class FileStorageService implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(FileStorageService.class);

	@Value("${default.path}")
	private String defaultPath;
	
	public UploadFileData saveFileToStorage(MultipartFile file, int pathModule) {
		if (null != file) {
			try {
				
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				String currentYears = sdf.format(new Date());
				
				String extensoin = (null == FilenameUtils.getExtension(file.getOriginalFilename()) ? "txt" : FilenameUtils.getExtension(file.getOriginalFilename()));
				String fileName = UUID.randomUUID().toString();

				String fullFile = fileName + FilenameUtils.EXTENSION_SEPARATOR + extensoin;

				String saveDirectory = concatnatePath(currentYears, pathModule, fullFile);

				File directory = new File(concatnatePath(currentYears, pathModule, null));

			    File parentFile = new File(defaultPath);

			    if (!directory.getCanonicalPath().startsWith(parentFile.getCanonicalPath())) {
		            throw new IOException("expanding " + directory.getName() + " would create file outside of " + parentFile);
		        }
			    
			    if (! directory.exists()){
			        directory.mkdirs();
			    }

				File fs = new File(saveDirectory);
				
				Files.write(fs.toPath(), file.getBytes());
				
				String base64 = null;
				if ("png".equals(extensoin) || "jpg".equals(extensoin) || "jpeg".equals(extensoin)) {
	                base64 = Base64.getEncoder().encodeToString(file.getBytes());
				}
				
				String fullpath = saveDirectory.replace(defaultPath, "");
				
				return UploadFileData
						.builder()
						.fullpath(fullpath)
						.filesize(file.getSize())
						.originalFilename(file.getOriginalFilename())
						.filename(fileName + FilenameUtils.EXTENSION_SEPARATOR + extensoin)
						.extension(extensoin)
						.base64(base64)
						.prefix(currentYears)
						.module((long) pathModule)
						.build();
				
			} catch (Exception e) {
	        	LOG.error("==================[ saveFileToStorage ]==================");
	            LOG.error(e.getMessage(), e);
			}
		}
		return null;
	}
	
	public UploadFileData saveByteArrayToStorage(byte[] fileByteArray, String filename, int pathModule) throws Exception {
		if (null != fileByteArray) {
			try {
				
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				String currentYears = sdf.format(new Date());
				
				String extensoin = (null == FilenameUtils.getExtension(filename) ? "txt" : FilenameUtils.getExtension(filename));
				String fileName = filename.split("\\.")[0];

				String fullFile = fileName + FilenameUtils.EXTENSION_SEPARATOR + extensoin;

				String saveDirectory = concatnatePath(currentYears, pathModule, fullFile);

				File directory = new File(concatnatePath(currentYears, pathModule, null));

			    File parentFile = new File(defaultPath);

			    if (!directory.getCanonicalPath().startsWith(parentFile.getCanonicalPath())) {
		            throw new IOException("expanding " + directory.getName() + " would create file outside of " + parentFile);
		        }
			    
			    if (! directory.exists()){
			        directory.mkdirs();
			    }

				File fs = new File(saveDirectory);
				
				Files.write(fs.toPath(), fileByteArray);
				
				String base64 = null;
				if ("png".equals(extensoin) || "jpg".equals(extensoin) || "jpeg".equals(extensoin)) {
	                base64 = Base64.getEncoder().encodeToString(fileByteArray);
				}
				
				String fullpath = saveDirectory.replace(defaultPath, "");
				return UploadFileData
						.builder()
						.fullpath(fullpath)
						.originalFilename(filename)
						.filename(fileName + FilenameUtils.EXTENSION_SEPARATOR + extensoin)
						.extension(extensoin)
						.base64(base64)
						.prefix(currentYears)
						.module((long) pathModule)
						.build();
				
			} catch (Exception e) {
	        	LOG.error("==================[ saveByteArrayToStorage ]==================");
	            LOG.error(e.getMessage(), e);
				throw e;
			}
		}
		return null;
	}

	public String concatnatePathByModel(UploadFileData data) throws IOException {
		if (null == data) {
			throw new IOException("Upload file data is nullable");
		}
		return concatnatePath(data.getPrefix(), data.getModule().intValue(), data.getFilename());
	}

	public String concatnatePath(String prefix, int module, String filename) {

		StringBuilder sb = new StringBuilder();

		sb.append(defaultPath);
		sb.append(File.separator);

		sb.append(prefix);
		sb.append(File.separator);

		sb.append(getModuleOfPath(module));
		sb.append(File.separator);

		if (StringUtils.isNoneBlank(filename)) {
			sb.append(filename);
		}
		
		return sb.toString();
	}
	
	public String getModuleOfPath(int pathModule) {
		String pathOfModule = "other";
		switch (pathModule) {
		case 1: {
			pathOfModule = PATH_BANNER;
			break;
		}
		case 2: {
			pathOfModule = PATH_COURSE;
			break;
		}
		case 3: {
			pathOfModule = PATH_COURSEPUBLIC;
			break;
		}
		case 4: {
			pathOfModule = PATH_MASTER_DOCUMENT;
			break;
		}
		case 5: {
			pathOfModule = PATH_MASTER_FINANCIAL;
			break;
		}
		case 6: {
			pathOfModule = PATH_MEMBER;
			break;
		}
		case 7: {
			pathOfModule = PATH_PERSONAL;
			break;
		}
		case 8: {
			pathOfModule = PATH_RECEIPT;
			break;
		}
		case 9: {
			pathOfModule = PATH_SYSTEM;
			break;
		}
                case 10:{
                        pathOfModule = PATH_NEWS;
                        break;
                }
		default:
			break;
		}
		return pathOfModule;
	}
	
	public boolean checkFileIsExists(String fullPath) {
		try {
			File directory = new File(fullPath);

			File parentFile = new File(defaultPath);

			if (!directory.getCanonicalPath().startsWith(parentFile.getCanonicalPath())) {
				throw new IOException("expanding " + directory.getName() + " would create file outside of " + parentFile);
			}

			if(directory.exists() && !directory.isDirectory()) { 
				return true;
			}
		} catch (Exception e) {
        	LOG.error("==================[ checkFileIsExists ]==================");
			LOG.error(e.getMessage(), e);
		}
		return false;
	}
	
	public boolean checkLinkFileIsExists(String link) {
		try {
			File directory = new File(defaultPath + link);

			File parentFile = new File(defaultPath);

			if (!directory.getCanonicalPath().startsWith(parentFile.getCanonicalPath())) {
				throw new IOException("expanding " + directory.getName() + " would create file outside of " + parentFile);
			}

			if(directory.exists() && !directory.isDirectory()) { 
				return true;
			}
		} catch (Exception e) {
        	LOG.error("==================[ checkLinkFileIsExists ]==================");
			LOG.error(e.getMessage(), e);
		}
		return false;
	}
	
	public boolean checkLinkFileIsExistsByModel(UploadFileData model) {
		
		if (StringUtils.isBlank(model.getPrefix()) || StringUtils.isBlank(model.getFilename()) || null == model.getModule()) {
			return false;
		}
       	
		try {
			String fullPath = concatnatePathByModel(model);
			File directory = new File(fullPath);

			File parentFile = new File(defaultPath);

			if (!directory.getCanonicalPath().startsWith(parentFile.getCanonicalPath())) {
				throw new IOException("expanding " + directory.getName() + " would create file outside of " + parentFile);
			}

			if(directory.exists() && !directory.isDirectory()) { 
				return true;
			}
		} catch (Exception e) {
        	LOG.error("==================[ checkLinkFileIsExistsByModel ]==================");
			LOG.error(e.getMessage(), e);
		}
		return false;
	}
    
    public UploadFileData getUploadFileData(UploadFileData model) {
        try {
			
			String fullPath = concatnatePathByModel(model);

            if (StringUtils.isNoneBlank(fullPath)) {

				File directory = new File(fullPath);

				File parentFile = new File(defaultPath);
	
				if (!directory.getCanonicalPath().startsWith(parentFile.getCanonicalPath())) {
					throw new IOException("expanding " + directory.getName() + " would create file outside of " + parentFile);
				}

                Path path = Paths.get(fullPath);
                byte[] data = java.nio.file.Files.readAllBytes(path);
                return UploadFileData.builder().base64(Base64.getEncoder().encodeToString(data)).build();
            }
                
        } catch (IOException e) {
        	LOG.error("==================[ getBase64FromPath ]==================");
            LOG.error(e.getMessage(), e);
        }
        return null;
    }
    
    public String getBase64ByModel(UploadFileData model) {
        try {
        	
			String fullPath = concatnatePathByModel(model);

            if (StringUtils.isNoneBlank(fullPath)) {

				File directory = new File(fullPath);

				File parentFile = new File(defaultPath);
	
				if (!directory.getCanonicalPath().startsWith(parentFile.getCanonicalPath())) {
					throw new IOException("expanding " + directory.getName() + " would create file outside of " + parentFile);
				}

                Path path = Paths.get(fullPath);
                byte[] data = java.nio.file.Files.readAllBytes(path);
                return Base64.getEncoder().encodeToString(data);
            }
                
        } catch (IOException e) {
        	LOG.error("==================[ getBase64FromPath ]==================");
            LOG.error(e.getMessage(), e);
        }
        return null;
    }
    
    public byte[] getContentfilePublic(UploadFileData model) {
        try {

			String fullPath = concatnatePathByModel(model);

            if (StringUtils.isNoneBlank(fullPath)) {

				File directory = new File(fullPath);

				File parentFile = new File(defaultPath);
	
				if (!directory.getCanonicalPath().startsWith(parentFile.getCanonicalPath())) {
					throw new IOException("expanding " + directory.getName() + " would create file outside of " + parentFile);
				}

                Path path = Paths.get(fullPath);
                return java.nio.file.Files.readAllBytes(path);
            }
                
        } catch (IOException e) {
        	LOG.error("==================[ getBase64FromPath ]==================");
            LOG.error(e.getMessage(), e);
        }
        return null;
    }
    
	public byte[] getContentfilePublic(String fullPath) {
        try {

			 LOG.info("##### FULL PATH ######");
			 String fullPathStr = defaultPath+fullPath;
			 LOG.info(fullPath);
			 
            if (StringUtils.isNoneBlank(fullPathStr)) {

				File directory = new File(fullPathStr);

				File parentFile = new File(defaultPath);
	
				if (!directory.getCanonicalPath().startsWith(parentFile.getCanonicalPath())) {
					throw new IOException("expanding " + directory.getName() + " would create file outside of " + parentFile);
				}

                Path path = Paths.get(fullPathStr);
                return java.nio.file.Files.readAllBytes(path);
            }
                
        } catch (IOException e) {
        	LOG.error("==================[ getBase64FromPath ]==================");
            LOG.error(e.getMessage(), e);
        }
        return null;
    }
    
	public String getFullFilePath(String filePath) {
		try {
			if (checkFileIsExists(defaultPath + filePath)) {
				return defaultPath + filePath;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
	
}

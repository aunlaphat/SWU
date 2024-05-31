package com.arg.swu.services;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.dto.BuasriGenerateData;
import com.arg.swu.dto.UploadFileData;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleGenerateBuasri implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(ScheduleGenerateBuasri.class);
	
	private final FileStorageService fileStorageService;
	private final FileTransferService fileTransferService;
	
	private final ScheduleService scheduleService;
	
	@Value("${default.path}")
	private String defaultPath;
	
	@Value("${sftp.buasri.default.path}")
	private String buasriDefaultPath;

	@Scheduled(cron = "0 55 23 * * *")
	@JmsListener(destination = "generatebuasrischedule?prioritizedMessages=true")
	public void generateBuasri() throws IOException {
		
		LOG.info("==================[ generatebuasrischedule ]==================");
		
		List<BuasriGenerateData> list = scheduleService.findBuasriListData(true);
		if (null != list && !list.isEmpty()) {
			List<String> lines = list.stream().map(o -> {
				StringJoiner joiner = new StringJoiner(":");
				joiner.add(Optional.ofNullable(o.getBuasriId()).orElse(""));
				joiner.add(Optional.ofNullable(o.getMemberFirstnameEn()).orElse(""));
				joiner.add(Optional.ofNullable(o.getMemberLastnameEn()).orElse(""));
				joiner.add(Optional.ofNullable(o.getMemberCardno()).orElse(""));
				joiner.add(Optional.ofNullable(o.getGidnumber()).orElse(""));
				joiner.add(Optional.ofNullable(o.getM100()).orElse(""));
				return joiner.toString();
			}).toList();
			
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String currentYears = sdf.format(new Date());
			
			final SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
			
			String filename = "userdata" + format.format(new Date()) + ".txt";
			
			UploadFileData model = UploadFileData
					.builder()
					.prefix(currentYears)
					.module(9l)
					.filename(filename)
					.build();
			
			String fullPath = fileStorageService.concatnatePathByModel(model);
			
			File directory = new File(fileStorageService.concatnatePath(currentYears, 9, null));

		    File parentFile = new File(defaultPath);

		    if (!directory.getCanonicalPath().startsWith(parentFile.getCanonicalPath())) {
	            throw new IOException("expanding " + directory.getName() + " would create file outside of " + parentFile);
	        }
		    
		    if (! directory.exists()){
		        directory.mkdirs();
		    }
		    
			Path file = Paths.get(fullPath);
			Files.write(file, lines, StandardCharsets.UTF_8);
			
			fileTransferService.uploadFile(fullPath, buasriDefaultPath);
			
			List<Long> memberIds = list.stream().map(BuasriGenerateData::getMemberId).toList();
			scheduleService.updateBuasriActiveStatus(memberIds, true);
		}
	}

	@Scheduled(cron = "0 56 23 * * *")
	public void deleteBuasri() throws IOException {
		
		List<BuasriGenerateData> list = scheduleService.findBuasriListData(false);
		if (null != list && !list.isEmpty()) {
			List<String> lines = list.stream().map(o -> {
				StringJoiner joiner = new StringJoiner(":");
				joiner.add(Optional.ofNullable(o.getBuasriId()).orElse(""));
				joiner.add(Optional.ofNullable(o.getMemberFirstnameEn()).orElse(""));
				joiner.add(Optional.ofNullable(o.getMemberLastnameEn()).orElse(""));
				joiner.add(Optional.ofNullable(o.getMemberCardno()).orElse(""));
				joiner.add(Optional.ofNullable(o.getGidnumber()).orElse(""));
				joiner.add(Optional.ofNullable(o.getM100()).orElse(""));
				return joiner.toString();
			}).toList();
			
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String currentYears = sdf.format(new Date());
			
			final SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
			
			String filename = "del" + format.format(new Date()) + ".txt";
			
			UploadFileData model = UploadFileData
					.builder()
					.prefix(currentYears)
					.module(9l)
					.filename(filename)
					.build();
			
			String fullPath = fileStorageService.concatnatePathByModel(model);
			
			File directory = new File(fileStorageService.concatnatePath(currentYears, 9, null));

		    File parentFile = new File(defaultPath);

		    if (!directory.getCanonicalPath().startsWith(parentFile.getCanonicalPath())) {
	            throw new IOException("expanding " + directory.getName() + " would create file outside of " + parentFile);
	        }
		    
		    if (! directory.exists()){
		        directory.mkdirs();
		    }
		    
			Path file = Paths.get(fullPath);
			Files.write(file, lines, StandardCharsets.UTF_8);
			
			fileTransferService.uploadFile(fullPath, buasriDefaultPath);

			List<Long> memberIds = list.stream().map(BuasriGenerateData::getMemberId).toList();
			scheduleService.updateBuasriActiveStatus(memberIds, false);
			
		}
	}
	
}

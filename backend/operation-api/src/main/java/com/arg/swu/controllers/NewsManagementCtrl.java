package com.arg.swu.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.CoursepublicGradeData;
import com.arg.swu.dto.MasOccupationSkillData;
import com.arg.swu.dto.MasWebsiteBannerData;
import com.arg.swu.dto.NewsInfoAttachData;
import com.arg.swu.dto.NewsInfoData;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.AutUserRole;
import com.arg.swu.models.AutUserRolePK;
import com.arg.swu.models.CoursepublicGrade;
import com.arg.swu.models.MasBanner;
import com.arg.swu.models.MasOccupationSkill;
import com.arg.swu.models.NewsInfo;
import com.arg.swu.models.NewsInfoAttach;
import com.arg.swu.repositories.AutUserRepo;
import com.arg.swu.repositories.NewsInfoAttachRepository;
import com.arg.swu.repositories.NewsInfoRepository;
import com.arg.swu.services.EntityMapperService;
import com.arg.swu.services.NewsManagementService;
import com.arg.swu.services.UtilityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Date;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author sitthichaim
 *
 */
@RestController
@RequestMapping("news-management")
@RequiredArgsConstructor
public class NewsManagementCtrl implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger(NewsManagementCtrl.class);

	private final NewsManagementService newsManagementService;
	private final NewsInfoAttachRepository newsInfoAttachRepository;
	private final NewsInfoRepository newsInfoRepository;
	private final UtilityService utilityService;
	private final EntityMapperService mapperService;

	
	@PostMapping("delete-news-info")
    public ResponseEntity<Map<String, Object>> deleteNewsInfo(HttpServletRequest request, HttpServletResponse response,@RequestBody NewsInfoData data) {
	try {
                    AutUser userAction = utilityService.getActionUser(request);
		Map<String, Object> result = newsManagementService.deleteNewsInfo(data,userAction);
		return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_DELETE_SUCCESS, null), HttpStatus.OK);
	} catch (Exception e) {
		LOG.error(e.getMessage(), e);
		return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
	}
}
	
	@PostMapping("delete-news-info-attach")
    public ResponseEntity<Map<String, Object>> deleteNewsInfoAttach(HttpServletRequest request, HttpServletResponse response,@RequestBody NewsInfoAttachData data) {
	try {
                    AutUser userAction = utilityService.getActionUser(request);
		Map<String, Object> result = newsManagementService.deleteNewsInfoAttach(data,userAction);
		return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_DELETE_SUCCESS, null), HttpStatus.OK);
	} catch (Exception e) {
		LOG.error(e.getMessage(), e);
		return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
	}
}
	

	@PostMapping("find-news-info-refmode")
	public ResponseEntity<Map<String, Object>> findNewsRefMode(HttpServletRequest request, HttpServletResponse response,
			@RequestBody NewsInfoData data) {
		try {

			Map<String, Object> result = newsManagementService.findNewsByCondition(data);

			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));

			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn),
					HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-news-info-attach-refmode")
	public ResponseEntity<Map<String, Object>> postNewsInfoAttachRefMode(HttpServletRequest request,
			HttpServletResponse response, @RequestBody NewsInfoAttachData criteria) {
		try {
			Map<String, Object> result = newsManagementService.findNewsAttachRefModeByCondition(criteria);
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn),
					HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping("news-info-refmode")
 	public ResponseEntity<Map<String, Object>> postNewsRefMode(HttpServletRequest request, HttpServletResponse response,
 			@RequestBody NewsInfoData data) {
 		try {
 			
 			AutUser userAction = utilityService.getActionUser(request);
 			
 			List<NewsInfoAttachData> temp = data.getNewsAttachDocList();
 			
 			
 			NewsInfo newsInfoData = mapperService.convertToEntity(data, NewsInfo.class);
 			newsInfoData.setCreateBy(userAction);
 			newsInfoData.setCreateDate(new Date());
 		
 			newsInfoRepository.save(newsInfoData);
 			data = mapperService.convertToEntity(newsInfoData, NewsInfoData.class);
 			
 			NewsInfoAttach newsInfoAttachData = mapperService.convertToEntity(temp, NewsInfoAttach.class);
 			newsInfoAttachData.setNewsId(newsInfoData.getNewsId());
 			newsInfoAttachData.setCreateBy(userAction);
 			newsInfoAttachData.setCreateDate(new Date());
 			newsInfoAttachData.setFilename(newsInfoData.getFilename());
 			newsInfoAttachData.setFileNameEn(newsInfoData.getFilename());
 			newsInfoAttachData.setFileNameTh(newsInfoData.getFilename());
 			newsInfoAttachData.setModule(newsInfoData.getModule());
 			newsInfoAttachData.setPrefix(newsInfoData.getPrefix());
 			newsInfoAttachData.setType("coverimage");
 			
 			if (null != temp && !temp.isEmpty()) {
 				
				List<NewsInfoAttach> occupationSkillList = temp.stream().map(o -> {
					NewsInfoAttach nia = mapperService.convertToEntity(o, NewsInfoAttach.class);
					if (null == nia.getNewsAttachId()) {
						nia.setNewsId(newsInfoData.getNewsId());
						nia.setCreateBy(userAction);
						nia.setCreateDate(new Date());
					} else {
						nia = newsInfoAttachRepository.findById(nia.getNewsAttachId()).orElse(null);
					}
					return nia;	
				}).toList();
				newsInfoAttachRepository.saveAll(occupationSkillList);
				
				List<NewsInfoAttachData> list = occupationSkillList.stream().map(o -> mapperService.convertToData(o, NewsInfoAttachData.class)).toList();
				data.setNewsAttachDocList(list);
				
			}
 			newsInfoAttachRepository.save(newsInfoAttachData);
 			
 			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
 		} catch (Exception e) {
 			LOG.error(e.getMessage(), e);
 			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
 		}
 	}
	
	@PostMapping("news-info-attach-refmode")
 	public ResponseEntity<Map<String, Object>> postNewsAttachRefMode(HttpServletRequest request, HttpServletResponse response,
 			@RequestBody NewsInfoAttachData data) {
 		try {
 			
 			AutUser userAction = utilityService.getActionUser(request);
 			
 			NewsInfoAttach newsInfoData = mapperService.convertToEntity(data, NewsInfoAttach.class);
 			newsInfoData.setCreateBy(userAction);
 			newsInfoData.setType("file");
 			newsInfoData.setCreateDate(new Date());
 			
 			newsInfoAttachRepository.save(newsInfoData);
 			
 			data = mapperService.convertToEntity(newsInfoData, NewsInfoAttachData.class);
 			
 			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
 		} catch (Exception e) {
 			LOG.error(e.getMessage(), e);
 			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
 		}
 	}

	@PutMapping("news-info-refmode/{newsId}")
	public ResponseEntity<Map<String, Object>> putNewsRefMode(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "newsId", required = true) Long newsId, @RequestBody NewsInfoData data) {
		try {

			NewsInfo newsInfoData = newsInfoRepository.findById(newsId).orElse(null);
			if (null == newsInfoData) {
				return new ResponseEntity<>(
						CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND),
						HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);

			NewsInfo update = mapperService.convertDataToEntity4Update(data, NewsInfo.class, newsInfoData, userAction);
			update.setNewsId(newsId);
			
//NewsInfoAttach newsInfoAttachData = newsInfoRepository.findById().orElse(null);
//			
//			List<NewsInfoAttachData> temp = data.getNewsAttachDocList();
//			
//			
//			NewsInfoAttach newsInfoAttachData = mapperService.convertToEntity(temp, NewsInfoAttach.class,);
// 			newsInfoAttachData.getNewsId(newsInfoData.getNewsId());
// 			newsInfoAttachData.setUpdateBy(userAction);
// 			newsInfoAttachData.setUpdateDate(new Date());
// 			newsInfoAttachData.setCreateDate(new Date());
// 			newsInfoAttachData.setFilename(newsInfoData.getFilename());
// 			newsInfoAttachData.setFileNameEn(newsInfoData.getFilename());
// 			newsInfoAttachData.setFileNameTh(newsInfoData.getFilename());
// 			newsInfoAttachData.setModule(newsInfoData.getModule());
// 			newsInfoAttachData.setPrefix(newsInfoData.getPrefix());
// 			newsInfoAttachData.setType("coverimage");
// 			
// 			newsInfoAttachRepository.save(newsInfoAttachData);
			

			newsInfoRepository.save(update);

			List<NewsInfoAttachData> responseList = new ArrayList<>();
			
			
			
			if (null != data.getNewsAttachDocList() && !data.getNewsAttachDocList().isEmpty()) {
				
				List<NewsInfoAttach> DocAttach = data.getNewsAttachDocList().stream().map(o -> {
					NewsInfoAttach newsInfoAttach = newsInfoAttachRepository.findById(o.getNewsAttachId()).orElse(null);
					if (null == newsInfoAttach) {
						newsInfoAttach.setNewsAttachId(null);
						newsInfoAttach.setCreateBy(userAction);
						newsInfoAttach.setCreateDate(new Date());
						newsInfoAttach.setType("file");
					} else {
						newsInfoAttach.setUpdateBy(userAction);
						newsInfoAttach.setUpdateDate(new Date());
						newsInfoAttach.setType("file");
					}
					newsInfoAttach.setNewsId(newsId);
					newsInfoAttach.setNewsAttachId(o.getNewsAttachId());
					return newsInfoAttach;
				}).toList();
				
				if (null != DocAttach && !DocAttach.isEmpty()) {
					newsInfoAttachRepository.saveAll(DocAttach);
					responseList = DocAttach.stream().map(o -> mapperService.convertToData(o, NewsInfoAttachData.class)).toList();
				}
				
			}else if (null != data.getFilename() && !data.getFilename().isEmpty()) {
				
				List<NewsInfoAttach> DocAttach = data.getNewsAttachDocList().stream().map(o -> {
					NewsInfoAttach newsInfoAttach = newsInfoAttachRepository.findById(o.getNewsAttachId()).orElse(null);
					if (null == newsInfoAttach) {
						newsInfoAttach.setNewsAttachId(null);
						newsInfoAttach.setCreateBy(userAction);
						newsInfoAttach.setCreateDate(new Date());
					} else {
						newsInfoAttach.setUpdateBy(userAction);
						newsInfoAttach.setUpdateDate(new Date());
						newsInfoAttach.setFilename(newsInfoData.getFilename());
						newsInfoAttach.setFileNameEn(newsInfoData.getFilename());
						newsInfoAttach.setFileNameTh(newsInfoData.getFilename());
						newsInfoAttach.setModule(newsInfoData.getModule());
						newsInfoAttach.setPrefix(newsInfoData.getPrefix());
						newsInfoAttach.setType("coverimage");
					}
					newsInfoAttach.setNewsId(newsId);
					newsInfoAttach.setNewsAttachId(o.getNewsAttachId());
					return newsInfoAttach;
				}).toList();
				
				if (null != DocAttach && !DocAttach.isEmpty()) {
					newsInfoAttachRepository.saveAll(DocAttach);
					responseList = DocAttach.stream().map(o -> mapperService.convertToData(o, NewsInfoAttachData.class)).toList();
				}
				
			}

			data = mapperService.convertToData(update, NewsInfoData.class);
			data.setNewsAttachDocList(responseList);
			
//			if (null !=newsInfoData.getFilename()) {
//				
//			}
			
			
			

			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("news-info-attach-refmode/{newsAttachId}")
	public ResponseEntity<Map<String, Object>> putNewsAttachRefMode(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "newsAttachId", required = true) Long newsAttachId, @RequestBody NewsInfoAttachData data) {
		try {

			NewsInfoAttach newsInfoattachData = newsInfoAttachRepository.findById(newsAttachId).orElse(null);
			if (null == newsInfoattachData) {
				return new ResponseEntity<>(
						CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND),
						HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);

			NewsInfoAttach update = mapperService.convertDataToEntity4Update(data, NewsInfoAttach.class, newsInfoattachData, userAction);
			update.setNewsId(newsAttachId);

			newsInfoAttachRepository.save(update);

			List<NewsInfoAttachData> responseList = new ArrayList<>();
			
			data = mapperService.convertToData(update, NewsInfoAttachData.class);

			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@GetMapping("news-info-refmode/{newsId}")
	public ResponseEntity<Map<String, Object>> getNewsRefMode(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "newsId", required = true) Long newsId) {
		try {

			NewsInfo newsInfoData = newsInfoRepository.findById(newsId).orElse(null);
			if (null == newsInfoData) {
				return new ResponseEntity<>(
						CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			NewsInfoData data = mapperService.convertToData(newsInfoData, NewsInfoData.class);

			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("news-info-attach-refmode/{newsAttachId}")
	public ResponseEntity<Map<String, Object>> getNewsAttachRefMode(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "newsAttachId", required = true) Long newsAttachId) {
		try {

			NewsInfoAttach newsInfoAttachData = newsInfoAttachRepository.findById(newsAttachId).orElse(null);
			if (null == newsInfoAttachData) {
				return new ResponseEntity<>(
						CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			NewsInfoAttachData data = mapperService.convertToData(newsInfoAttachData, NewsInfoAttachData.class);

			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	
}

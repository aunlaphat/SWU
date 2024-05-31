package com.arg.swu.services;

import com.arg.swu.commons.ApiConstant;
import static com.arg.swu.commons.ApiConstant.MSG_EDIT_DATA_NOTFOUND;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.CoursepublicGradeData;
import com.arg.swu.dto.CoursepublicMainData;
import com.arg.swu.dto.MasBankBranchData;
import com.arg.swu.dto.MasWebsiteBannerData;
import com.arg.swu.dto.NewsInfoAttachData;
import com.arg.swu.dto.NewsInfoData;
import com.arg.swu.dto.UploadFileData;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.CoursepublicGrade;
import com.arg.swu.models.NewsInfo;
import com.arg.swu.models.NewsInfoAttach;
import com.arg.swu.repositories.AutUserRepo;
import com.arg.swu.repositories.NewsInfoAttachRepository;
import com.arg.swu.repositories.NewsInfoRepository;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author kumpeep
 */
@Service
@RequiredArgsConstructor
public class NewsManagementService implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger(TeacherPortalService.class);

	private final JdbcTemplate jdbcTemplate;
	private final NewsInfoRepository newsInfoRepository;
	private final NewsInfoAttachRepository newsInfoAttachRepository;
	private final AutUserRepo autUserRepo;
	private final EntityMapperService mapperService;
	private final FileStorageService fileStorageService;

	public Map<String, Object> findNewsData(NewsInfoData criteria) {

		Map<String, Object> result = new HashMap<>();
		StringBuilder sb = new StringBuilder();
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		String orderBy = " order by ni.news_id asc ";

		if (criteria.getNewsStatus() != null) {
			conditions.append(" and ni.news_status = ? ");
			params.add(criteria.getNewsStatus());
		}
		if (StringUtils.isNotBlank(criteria.getNewsHeading())) {
			conditions.append(" and ni.news_heading ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getNewsHeading(), true, true));
		}
		if (criteria.getNewsStart() != null) {
			conditions.append(" and ni.news_start = ? ");
			params.add(criteria.getNewsStart());
		}
		if (criteria.getNewsEnd() != null) {
			conditions.append(" and ni.news_end = ? ");
			params.add(criteria.getNewsEnd());
		}
		if (criteria.getCreatedByNameTh() != null) {
			conditions.append(" and ni.create_by_id = ? ");
			params.add(criteria.getCreateById());
		}
		if (StringUtils.isNotBlank(criteria.getCreatedByNameTh())) {
			conditions.append(" and au.firstname_th ilike ? or au.lastname_th ilike ?");
			params.add(CommonUtils.concatLikeParam(criteria.getCreatedByNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getCreatedByNameTh(), true, true));
		}
		if (StringUtils.isNotBlank(criteria.getCreatedByNameEn())) {
			conditions.append(" and au.firstname_en ilike ? or au.lastname_en ilike ?");
			params.add(CommonUtils.concatLikeParam(criteria.getCreatedByNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getCreatedByNameTh(), true, true));
		}
		if (criteria.getNewsFormat() != null) {
			conditions.append(" and ni.news_format = ? ");
			params.add(criteria.getNewsFormat());
		}

		if (StringUtils.isNoneBlank(criteria.getMode())) {
			if ("shownews".equals(criteria.getMode())) {

				orderBy = " order by ni.news_hilight desc ";

				conditions.append(" and ni.active_flag = true ");
				conditions.append(" and ni.news_hilight in (1,2,3,4,5) ");
				conditions
						.append(" and ( ni.news_start <= now() and ( ni.news_end is null or now() < ni.news_end ) ) ");
			} else if ("allnews".equals(criteria.getMode())) {

				orderBy = " order by ni.news_start desc ";

				conditions.append(" and ni.active_flag = true ");
				conditions.append(" and ni.news_hilight in (1,2,3,4,5) ");
				conditions.append(" and ni.news_start <= now() and ni.news_end >= now() ");

			}
		}

		sb.append("select row_number() over (");
		sb.append(orderBy);
		sb.append(" ) row_num, ni.*, ");
		sb.append(
				" au.firstname_th, au.firstname_en, au.lastname_th, au.lastname_en, lc.name_en news_format_name_en, lc.name_lo as news_format_name_th ");
		sb.append(" from news_info ni ");
		sb.append(" join lookup_catalog lc on ni.news_format = lc.catalog_id ");
		sb.append(" join aut_user au on ni.create_by_id = au.user_id ");
		sb.append(WHERE);
		sb.append(conditions.toString());
		sb.append(orderBy);
		sb.append(LIMIT);
		List<NewsInfoData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(NewsInfoData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		result.put(ENTRIES, entries);

		StringBuilder count = new StringBuilder();
		count.append(" select count(*) from news_info ni join lookup_catalog lc on ni.news_format = lc.catalog_id ");
		count.append(" join aut_user au on ni.create_by_id=au.user_id ");
		count.append(WHERE);
		count.append(conditions.toString());

		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());
		result.put(TOTAL_RECORDS, totalRecords);

		return result;
	}

	public Map<String, Object> findNewsByCondition(NewsInfoData criteria) {

		Map<String, Object> result = new HashMap<>();

		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		String orderBy = " order by ni.news_hilight, ni.create_date  desc ";

		if (criteria.getNewsStatus() != null) {
			conditions.append(" and ni.news_status = ? ");
			params.add(criteria.getNewsStatus());
		}
		if (StringUtils.isNotBlank(criteria.getNewsHeading())) {
			conditions.append(" and ni.news_heading ilike ? ");
			params.add(CommonUtils.concatLikeParam(criteria.getNewsHeading(), true, true));
		}
		if (criteria.getNewsStart() != null) {
			conditions.append(" and ni.news_start = ? ");
			params.add(criteria.getNewsStart());
		}
		if (criteria.getNewsEnd() != null) {
			conditions.append(" and ni.news_end = ? ");
			params.add(criteria.getNewsEnd());
		}
		if (criteria.getCreatedByNameTh() != null) {
			conditions.append(" and ni.create_by_id = ? ");
			params.add(criteria.getCreateById());
		}
		if (StringUtils.isNotBlank(criteria.getCreatedByNameTh())) {
			conditions.append(" and au.firstname_th ilike ? or au.lastname_th ilike ?");
			params.add(CommonUtils.concatLikeParam(criteria.getCreatedByNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getCreatedByNameTh(), true, true));
		}
		if (StringUtils.isNotBlank(criteria.getCreatedByNameEn())) {
			conditions.append(" and au.firstname_en ilike ? or au.lastname_en ilike ?");
			params.add(CommonUtils.concatLikeParam(criteria.getCreatedByNameTh(), true, true));
			params.add(CommonUtils.concatLikeParam(criteria.getCreatedByNameTh(), true, true));
		}
		if (criteria.getNewsFormat() != null) {
			conditions.append(" and ni.news_format = ? ");
			params.add(criteria.getNewsFormat());
		}

		if (StringUtils.isNoneBlank(criteria.getMode())) {
			if ("shownews".equals(criteria.getMode())) {

				orderBy = " order by ni.news_hilight desc ";

				conditions.append(" and ni.active_flag = true ");
				conditions.append(" and ni.news_hilight in (1,2,3,4,5) ");
				conditions
						.append(" and ( ni.news_start <= now() and ( ni.news_end is null or now() < ni.news_end ) ) ");

			} else if ("allnews".equals(criteria.getMode())) {

				orderBy = " order by ni.news_start desc ";
				conditions.append(" and ni.active_flag = true ");
				conditions.append(" and ni.news_hilight in (1,2,3,4,5) ");
				conditions.append(" and ni.news_start <= now() and ni.news_end >= now() ");

			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(" select row_number() OVER ( ");
		sb.append(orderBy);
		sb.append(" ) row_num, ni.*, ");
		sb.append(
				" au.firstname_th, au.firstname_en, au.lastname_th, au.lastname_en, lc.name_en news_format_name_en, lc.name_lo as news_format_name_th ");
		sb.append(" from news_info ni ");
		sb.append(" join lookup_catalog lc on ni.news_format = lc.catalog_id ");
		sb.append(" join aut_user au on ni.create_by_id = au.user_id ");
		sb.append(WHERE);
		sb.append(conditions.toString());

		sb.append(orderBy);
		sb.append(LIMIT);

		List<NewsInfoData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(NewsInfoData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		if (null != entries && !entries.isEmpty()) {
			if (StringUtils.isNotBlank(criteria.getMode())) {
				if (MODE_SEARCH.equals(criteria.getMode())) {
					entries = entries.stream().map(o -> {
						UploadFileData c = UploadFileData.builder().filename(o.getFilename()).prefix(o.getPrefix())
								.module(o.getModule()).build();
						boolean fileIsExists = fileStorageService.checkLinkFileIsExistsByModel(c);
						if (fileIsExists) {
							String base64 = fileStorageService.getBase64ByModel(c);
							o.setBase64(base64);
							return o;
						}
						return o;
					}).toList();
				}
			}
		}

		StringBuilder count = new StringBuilder();
		count.append(" select count(*) from news_info ni join lookup_catalog lc on ni.news_format = lc.catalog_id ");
		count.append(" join aut_user au on ni.create_by_id=au.user_id ");
		count.append(WHERE);
		count.append(conditions.toString());
		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;
	}

	public Map<String, Object> findNewsAttachRefModeByCondition(NewsInfoAttachData criteria) {

		Map<String, Object> result = new HashMap<>();
		StringBuilder conditions = new StringBuilder();
		List<Object> params = new ArrayList<>();
		String orderBy = " order by nia.news_attach_id  asc ";

		if (null != criteria.getNewsId()) {
			conditions.append(" and ni.news_id = ? and nia.type = 'file'");
			params.add(criteria.getNewsId());
		}

		StringBuilder sb = new StringBuilder();

		sb.append(" select row_number() OVER ( ");
		sb.append(orderBy);
		sb.append(" ) AS row_num,nia.*  ");
		sb.append(" from news_info_attach nia ");
		sb.append(" left join news_info ni on nia.news_id = ni.news_id ");
		sb.append(WHERE);

		sb.append(conditions.toString());
		sb.append(orderBy);
		sb.append(LIMIT);

		List<NewsInfoAttachData> entries = jdbcTemplate.query(sb.toString(),
				BeanPropertyRowMapper.newInstance(NewsInfoAttachData.class),
				CommonUtils.joinParam(params.toArray(), criteria.getPageable()));
		if (null != entries && !entries.isEmpty()) {
			if (StringUtils.isNotBlank(criteria.getMode())) {
				if (MODE_SEARCH.equals(criteria.getMode())) {
					entries = entries.stream().map(o -> {
						UploadFileData c = UploadFileData.builder().filename(o.getFilename()).prefix(o.getPrefix())
								.module(o.getModule()).build();
						boolean fileIsExists = fileStorageService.checkLinkFileIsExistsByModel(c);
						if (fileIsExists) {
							String base64 = fileStorageService.getBase64ByModel(c);
							o.setBase64(base64);
							return o;
						}
						return o;
					}).toList();
				}
			}
		}

		StringBuilder count = new StringBuilder();
		count.append("select count(*) from news_info_attach nia ");
		count.append(" left join news_info ni on nia.news_id = ni.news_id where 1 = 1 ");
		count.append(conditions.toString());

		Long totalRecords = jdbcTemplate.queryForObject(count.toString(), Long.class, params.toArray());

		result.put(ENTRIES, entries);
		result.put(TOTAL_RECORDS, totalRecords);

		return result;
	}
	@Transactional
	public Map<String, Object> deleteNewsInfo(NewsInfoData data, AutUser userAction) {
	    Map<String, Object> result = new HashMap<>();

	    try {
	        NewsInfo obj = mapperService.convertToEntity(data, NewsInfo.class);
	        if (obj.getNewsId() != null) {
	            newsInfoAttachRepository.deleteByNewsId(obj.getNewsId());
	            newsInfoRepository.deleteById(obj.getNewsId());
	        }
	        result.put("message", "News deleted ");
	        return result;
	    } catch (Exception e) {
	        LOG.error("Error deleting news information", e);
	        result.put("error", e.getMessage());
	        return result;
	    }
	}
	
//	public Map<String, Object> deleteNewsInfo(NewsInfoData data, AutUser userAction) {
//		Map<String, Object> result = new HashMap<>();
//		NewsInfo obj = mapperService.convertToEntity(data, NewsInfo.class);
//		if (obj.getNewsId() != null) {
//			newsInfoAttachRepository.deleteByNewsId(obj.getNewsId());
//			newsInfoRepository.deleteById(obj.getNewsId());
//		}
//		return result;
//	}
	public Map<String, Object> deleteNewsInfoAttach(NewsInfoAttachData data, AutUser userAction) {
		Map<String, Object> result = new HashMap<>();
		NewsInfoAttach obj = mapperService.convertToEntity(data, NewsInfoAttach.class);
		if (obj.getNewsAttachId() != null) {
			newsInfoAttachRepository.deleteById(obj.getNewsAttachId());
		}
		return result;
	}

}

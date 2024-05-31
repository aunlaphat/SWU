package com.arg.swu.services;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.arg.swu.commons.CommonUtils;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.commons.FootprintInterfaces;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@RequiredArgsConstructor
public class EntityMapperService {

    private final ModelMapper modelMapper;
    
    public <D, T> D convertToData(T entity, Class<D> dtoClass) {
    	CommonUtils.longZeroToNull(entity);
        return modelMapper.map(entity, dtoClass);
    }

    public <D, T> D convertToEntity(T data, Class<D> entityClass) {
    	CommonUtils.longZeroToNull(data);
        return modelMapper.map(data, entityClass);
    }
    
    @SuppressWarnings("unchecked")
	public <D, T> D convertDataToEntity(T dto, Class<D> entityClass, AutUser userAction) {
    	CommonUtils.longZeroToNull(dto);
    	Object o = modelMapper.map(dto, entityClass);
    	FootprintInterfaces fp = (FootprintInterfaces) o;
    	fp.setCreateDate(new Date());
    	fp.setCreateBy(userAction);
    	return (D) o;
    }
    
    @SuppressWarnings("unchecked")
	public <D, T> D convertDataToEntity4Update(T dto, Class<D> entityClass, Object oldEntity, AutUser userAction) {
    	CommonUtils.longZeroToNull(dto);
    	Object o = modelMapper.map(dto, entityClass);

    	FootprintInterfaces oldFp = (FootprintInterfaces) oldEntity;
    	
    	FootprintInterfaces fp = (FootprintInterfaces) o;
    	fp.setCreateDate(oldFp.getCreateDate());
    	fp.setCreateBy(oldFp.getCreateBy());
    	fp.setUpdateDate(new Date());
    	fp.setUpdateBy(userAction);
    	
    	return (D) o;
    }
    
}
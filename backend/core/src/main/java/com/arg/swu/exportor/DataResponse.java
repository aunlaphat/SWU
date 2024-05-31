package com.arg.swu.exportor;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder 
public class DataResponse <T>{
    private List<T> datas;
    private List<Map<String,Object>> rowMap;
    private Long count;
    // private Footprint footprint;
    // private FileModel fileModel;
    // private FileCustomModel fileCustomModel;
    // private C control;
    private List<T> mockupDataTest;

}

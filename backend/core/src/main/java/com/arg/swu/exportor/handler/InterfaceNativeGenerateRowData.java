package com.arg.swu.exportor.handler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.poi.ss.usermodel.Sheet;

import com.arg.swu.exportor.DataResponse;

public abstract class InterfaceNativeGenerateRowData <T>{
   // public abstract List<Map<String, Object>> getData();

   public abstract DataResponse<T> getData() throws Exception;
   public abstract BodyWriteHandler<T> writeBodyHandler();
   public abstract HeaderHandler writeHeader();
   // public abstract UpdateSelfFileResultHeandler updateFileResultHandler();
   // public abstract UpdateSelfStatusHandler<K> updateStatusHandler();
   // public abstract UpdateSelfProgressHandler updateSelfProgressHandler();
   public abstract SheetNameHandler sheetNameHandler();

}

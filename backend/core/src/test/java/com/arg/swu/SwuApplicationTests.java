package com.arg.swu;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.arg.swu.report.AdminReport;
import com.arg.swu.report.JasperReportType;

@SpringBootTest
class SwuApplicationTests {
    @Test
    private void testreport()
    {
        // List<RssOutsourceJobSecurity> datas = new ArrayList<>();
        // RssOutsourceJobSecurity data = new RssOutsourceJobSecurity();
        // data.setVendorName("Report Test");
        // datas.add(data);

        // AdminReport core = new AdminReport.AdminReportBuilder(JasperReportType.PDF, "/msc/ktb/npa/report/example.jrxml")
        //                                                 .setConn(null)
        //                                                 // .addDatas(datas)
        //                                                 .addParam("reportDate", "20/08/2023")
        //                                                 .build();
        // byte[] result = core.execDatasource();
        // System.out.println(result);
    }
}

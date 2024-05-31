package com.arg.swu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.arg.swu.provider.FinancePaymentProvider;
import com.arg.swu.repositories.FinancePaymentRepository;
import com.arg.swu.services.FileStorageService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class BackendApplicationTests {

	 
	private FinancePaymentProvider testProvider;
	@Autowired
	private FinancePaymentRepository financePaymentRepo;
	@Autowired
	private FileStorageService mss;
	@Value("${default.path}")
	private String defaultPath;

	// @Test
	void contextLoads() {
	}

	// @Test
	void testSprit()
	{
		String t = "/2024/receipt/20240411-4.pdf";
		System.out.println(t.split("\\.").length);

		String t1 = "20240411-4.pdf";
		System.out.println(t1.split("\\.").length);
	}

	// @Test
	void testFilePath()
	{
		// String [] fileExt = this.fileNameScr.split(".");
        // this.fileNameDes = fileExt[0]+"-signed."+fileExt[1];

		FinancePaymentProvider p = FinancePaymentProvider.builder()
										.financePaymentRepo(financePaymentRepo)
										.financePaymentId(4L)
										.fss(mss)
										.defaultPath(defaultPath)
										.build();
										p.setModuleFile(8);
										
		p.builderData();

		try {
			log.info(p.srcFileBase64());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

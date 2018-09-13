package com.zr.testUtils;

import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.google.zxing.WriterException;
import com.zr.testUtils.service.IQRcodeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
	@Autowired
	private IQRcodeService qr;
	@Test
	public void testQr() throws IOException, WriterException{
		qr.refreshQRcode("1231");
	}

}

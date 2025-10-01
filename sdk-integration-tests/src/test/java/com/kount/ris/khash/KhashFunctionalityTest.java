package com.kount.ris.khash;

import com.kount.ris.util.Khash;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KhashFunctionalityTest {

	@Test
	public void testPaymentTokenHashingDefault() throws Exception {
		
		String output1 = Khash.getInstance().hashPaymentToken("4111111111111111");
		assertEquals("411111WMS5YA6FUZA1KC", output1);
		
		String output2 = Khash.getInstance().hashPaymentToken("5199185454061655");
		assertEquals("5199182NOQRXNKTTFL11", output2);
		
		String output3 = Khash.getInstance().hashPaymentToken("4259344583883");
		assertEquals("425934FEXQI1QS6TH2O5", output3);
	}
	
	@Test
	public void testGiftCardHashingDefault() throws Exception {
		
		String output4 = Khash.getInstance().hashGiftCard(666666, "4111111111111111");
		assertEquals("666666WMS5YA6FUZA1KC", output4);
		
		String output5 = Khash.getInstance().hashGiftCard(666666, "5199185454061655");
		assertEquals("6666662NOQRXNKTTFL11", output5);
		
		String output6 = Khash.getInstance().hashGiftCard(666666, "4259344583883");
		assertEquals("666666FEXQI1QS6TH2O5", output6);
	}
}

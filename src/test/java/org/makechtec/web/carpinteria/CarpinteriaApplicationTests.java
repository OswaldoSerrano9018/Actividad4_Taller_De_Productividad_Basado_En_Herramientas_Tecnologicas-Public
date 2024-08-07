package org.makechtec.web.carpinteria;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class CarpinteriaApplicationTests {

	@Test
	void contextLoads() {
		BigDecimal v = new BigDecimal("66583652073286629107");


		System.out.println(v.multiply(BigDecimal.valueOf(43)).toPlainString());
	}

}

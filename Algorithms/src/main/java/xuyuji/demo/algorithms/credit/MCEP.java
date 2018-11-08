package xuyuji.demo.algorithms.credit;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 ************************************************************
 * @类名 : MCEP.java
 *
 * @DESCRIPTION : 等额本金
 * @AUTHOR :  xuyuji
 * @DATE :  2018年8月9日
 ************************************************************
 */
public class MCEP {
    public void calc(BigDecimal prin, BigDecimal rate, int term) {
        BigDecimal unpaidAmount = prin;

        BigDecimal perAmt = prin.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);
        int i = 0;
        for (; i < term - 1; i++) {
            BigDecimal interest = unpaidAmount.multiply(rate);
            System.out.printf("第%d期,需偿还本金%.4f、利息%.4f,总偿还金额%.4f.\n", i + 1, perAmt, interest, perAmt.add(interest));
            unpaidAmount = unpaidAmount.subtract(perAmt);
        }
        BigDecimal interest = unpaidAmount.multiply(rate);
        System.out.printf("第%d期,需偿还本金%.4f、利息%.4f,总偿还金额%.4f.\n", i + 1, unpaidAmount, interest, unpaidAmount.add(interest));
    }

    public static void main(String[] args) {
        new MCEP().calc(BigDecimal.valueOf(2580000), 
            BigDecimal.valueOf(0.004491667)
            , 360);
    }
}

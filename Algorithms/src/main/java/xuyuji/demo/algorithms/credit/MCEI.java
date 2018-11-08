package xuyuji.demo.algorithms.credit;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 ************************************************************
 * @类名 : MCEI.java
 *
 * @DESCRIPTION : 等额本息息费计算
 * @AUTHOR : xuyuji
 * @DATE : 2018年8月9日
 ************************************************************
 */
public class MCEI {

    public void calc(BigDecimal prin, BigDecimal rate, int term) {
        BigDecimal rateAddOne = rate.add(BigDecimal.ONE);
        BigDecimal rateAddOnePowN = rateAddOne.pow(term);

        BigDecimal perAllAmt = prin.multiply(rate).multiply(rateAddOnePowN).divide(rateAddOnePowN.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
        System.out.println("每月需还款：" + perAllAmt);

        BigDecimal interest = BigDecimal.ZERO;
        BigDecimal lastPrin = prin;
        int i = 0;
        for (; i < term - 1; i++) {
            BigDecimal perPrin = prin.multiply(rate).multiply(rateAddOne.pow(i)).divide(rateAddOnePowN.subtract(BigDecimal.ONE), 2,
                RoundingMode.HALF_UP);
            lastPrin = lastPrin.subtract(perPrin);
            BigDecimal perInterest = perAllAmt.subtract(perPrin);
            interest = interest.add(perInterest);
            System.out.println("第" + (i + 1) + "期本金：" + perPrin + " 利息：" + perInterest);
        }
        BigDecimal lastInterest = perAllAmt.subtract(lastPrin);
        interest = interest.add(lastInterest);
        System.out.println("第" + (i + 1) + "期本金：" + lastPrin + " 利息：" + (perAllAmt.subtract(lastPrin)));
        System.out.println("总本金：" + prin + ",总利息：" + interest);
        System.out.println("总共还本付息：" + prin.add(interest));
        System.out.println("总共还本付息：" + perAllAmt.multiply(BigDecimal.valueOf(term)));
    }

    public static void main(String[] args) {
        new MCEI().calc(BigDecimal.valueOf(10000), BigDecimal.valueOf(0.36).setScale(8).divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP), 12);
        System.out.println("######");
        new MCEI().calc(BigDecimal.valueOf(100000), BigDecimal.valueOf(0.36).setScale(8).divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP), 6);
        System.out.println("######");
        new MCEI().calc(BigDecimal.valueOf(50000), BigDecimal.valueOf(0.001).multiply(BigDecimal.valueOf(30)).setScale(8), 12);
    }
}

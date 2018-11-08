package xuyuji.demo.algorithms.credit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import xuyuji.demo.algorithms.credit.domain.Schedule;

/**
 ************************************************************
 * @类名 : EPAI.java
 *
 * @DESCRIPTION : 等本等息
 * @AUTHOR : xuyuji
 * @DATE : 2018年8月9日
 ************************************************************
 */
public class EPAI {

    public List<Schedule> calc(BigDecimal prin, BigDecimal rate, int term) {
        List<Schedule> scheduleList = new ArrayList<Schedule>();

        BigDecimal perPrin = prin.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);
        BigDecimal perInterest = prin.multiply(rate).divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);

        int i = 0;
        for (; i < term - 1; i++) {
            scheduleList.add(new Schedule(i + 1).setPrincipal(perPrin).setInterest(perInterest).setTotalAmount(perPrin.add(perInterest)));
        }

        BigDecimal lastPrin = prin.subtract(perPrin.multiply(BigDecimal.valueOf(term - 1)));
        scheduleList.add(new Schedule(i + 1).setPrincipal(lastPrin).setInterest(perInterest).setTotalAmount(lastPrin.add(perInterest)));

        return scheduleList;
    }

    public static void main(String[] args) {
        List<Schedule> list = new EPAI().calc(BigDecimal.valueOf(10000), BigDecimal.valueOf(0.06), 3);
        for(Schedule s : list) {
            System.out.println(s);
        }
    }
}

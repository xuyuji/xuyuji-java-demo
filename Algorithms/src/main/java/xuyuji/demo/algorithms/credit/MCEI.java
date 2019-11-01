package xuyuji.demo.algorithms.credit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

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

    private BigDecimal stagingRate; // 每期利率

    private int term; // 期数

    private List<Schedule> scheduleList; // 还款计划

    private BigDecimal perPrincipalAndInterest; // 每期还本付息金额

    private BigDecimal totalInterest; // 总利息金额

    private BigDecimal totalPrincipalAndInterest; // 总还本付息金额

    public MCEI(BigDecimal stagingRate, int term) {
        this.stagingRate = stagingRate;
        this.term = term;
        init();
    }

    private void init() {
        this.scheduleList = new ArrayList<>();
        this.perPrincipalAndInterest = BigDecimal.ZERO;
        this.totalInterest = BigDecimal.ZERO;
        this.totalPrincipalAndInterest = BigDecimal.ZERO;
    }

    /**
     * 功能描述：计算等额本息
     * 
     * @param totalPrincipal void
     */
    public MCEI calc(BigDecimal totalPrincipal) {
        init();
        BigDecimal rateAddOne = stagingRate.add(BigDecimal.ONE);
        BigDecimal rateAddOnePowN = rateAddOne.pow(term);
        BigDecimal rateAddOnePowNSubOne = rateAddOnePowN.subtract(BigDecimal.ONE);

        perPrincipalAndInterest = totalPrincipal.multiply(stagingRate).multiply(rateAddOnePowN)
                .divide(rateAddOnePowNSubOne, 2, RoundingMode.HALF_UP);

        BigDecimal residualPrincipal = totalPrincipal;
        for (int i = 1; i < term; i++) {
            BigDecimal currentPrincipal =
                    totalPrincipal.multiply(stagingRate).multiply(rateAddOne.pow(i - 1))
                            .divide(rateAddOnePowNSubOne, 2, RoundingMode.HALF_UP);
            BigDecimal currentInterest = perPrincipalAndInterest.subtract(currentPrincipal);
            scheduleList.add(new Schedule(i, currentPrincipal, currentInterest));
            residualPrincipal = residualPrincipal.subtract(currentPrincipal);
            totalInterest = totalInterest.add(currentInterest);
        }
        BigDecimal lastInterest = perPrincipalAndInterest.subtract(residualPrincipal);
        scheduleList.add(new Schedule(term, residualPrincipal, lastInterest));
        totalInterest = totalInterest.add(lastInterest);
        totalPrincipalAndInterest = totalPrincipal.add(totalInterest);
        return this;
    }

    /**
     * 功能描述：获取还款计划
     * 
     * @return List<Schedule>
     */
    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    /**
     * 功能描述：获取每期还本付息金额
     * 
     * @return BigDecimal
     */
    public BigDecimal getPerPrincipalAndInterest() {
        return perPrincipalAndInterest;
    }

    /**
     * 功能描述：获取总利息金额
     * 
     * @return BigDecimal
     */
    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    /**
     * 功能描述：获取总本息金额
     * 
     * @return BigDecimal
     */
    public BigDecimal getTotalPrincipalAndInterest() {
        return totalPrincipalAndInterest;
    }

    class Schedule {
        private int currentTerm;
        private BigDecimal principal;
        private BigDecimal interest;

        public Schedule(int currentTerm, BigDecimal principal, BigDecimal interest) {
            this.currentTerm = currentTerm;
            this.principal = principal;
            this.interest = interest;
        }

        public BigDecimal getPrincipal() {
            return principal;
        }

        public void setPrincipal(BigDecimal principal) {
            this.principal = principal;
        }

        public BigDecimal getInterest() {
            return interest;
        }

        public void setInterest(BigDecimal interest) {
            this.interest = interest;
        }

        public int getCurrentTerm() {
            return currentTerm;
        }

        public void setCurrentTerm(int currentTerm) {
            this.currentTerm = currentTerm;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Schedule [currentTerm=");
            builder.append(currentTerm);
            builder.append(", principal=");
            builder.append(principal);
            builder.append(", interest=");
            builder.append(interest);
            builder.append("]");
            return builder.toString();
        }
    }

    public static void main(String[] args) {
        MCEI calc = new MCEI(BigDecimal.valueOf(0.03), 12);
        calc.calc(BigDecimal.valueOf(10000));

        List<MCEI.Schedule> list = calc.getScheduleList();
        for (MCEI.Schedule schedule : list) {
            System.out.println(schedule);
        }
        System.out.println("每期应还金额:" + calc.getPerPrincipalAndInterest());
        System.out.println("总利息金额:" + calc.getTotalInterest());
        System.out.println("应还总金额:" + calc.getTotalPrincipalAndInterest());
    }
}

package xuyuji.demo.algorithms.credit.domain;

import java.math.BigDecimal;

public class Schedule {

    private int term;
    
    private BigDecimal principal;
    
    private BigDecimal interest;
    
    private BigDecimal totalAmount;

    public Schedule(int term) {
        this.term = term;
    }
    
    public int getTerm() {
        return term;
    }

    public Schedule setTerm(int term) {
        this.term = term;
        return this;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public Schedule setPrincipal(BigDecimal principal) {
        this.principal = principal;
        return this;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public Schedule setInterest(BigDecimal interest) {
        this.interest = interest;
        return this;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Schedule setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[第").append(term).append("期还款计划]");
        builder.append("应还本金：").append(principal).append(",");
        builder.append("应还利息：").append(interest).append(",");
        builder.append("应还总额：").append(totalAmount).append(".");
        return builder.toString();
    }
}

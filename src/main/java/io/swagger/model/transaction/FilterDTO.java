package io.swagger.model.transaction;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

public final class FilterDTO {
    @NotNull
    private Integer page = 0;
    @NotNull
    private Integer pageSize = 10;
    private Date fromDate = null;
    private Date untilDate = null;
    @Pattern(regexp = "[A-Z]{2}[0-9]{2}[A-Z]{4}[0-9]{6,14}", message = "Iban of the from account was in invalid form.")
    private String fromIban = null;
    @Pattern(regexp = "[A-Z]{2}[0-9]{2}[A-Z]{4}[0-9]{6,14}", message = "Iban of the from account was in invalid form.")
    private String toIban = null;
    private String amountEqual = null;
    private String amountLowerThan = null;
    private String amountMoreThan = null;

    public FilterDTO() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(Date untilDate) {
        this.untilDate = untilDate;
    }

    public String getFromIban() {
        return fromIban;
    }

    public void setFromIban(String fromAccount) {
        this.fromIban = fromAccount;
    }

    public String getToIban() {
        return toIban;
    }

    public void setToIban(String toIban) {
        this.toIban = toIban;
    }

    public String getAmountEqual() {
        return amountEqual;
    }

    public void setAmountEqual(String amountEqual) {
        this.amountEqual = amountEqual;
    }

    public String getAmountLowerThan() {
        return amountLowerThan;
    }

    public void setAmountLowerThan(String amountLowerThan) {
        this.amountLowerThan = amountLowerThan;
    }

    public String getAmountMoreThan() {
        return amountMoreThan;
    }

    public void setAmountMoreThan(String amountMoreThan) {
        this.amountMoreThan = amountMoreThan;
    }
}

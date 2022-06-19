package io.swagger.model.transaction;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Constraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

public final class FilterParams {
    private final String dateRegex = "^([0-2][0-9]|(3)[0-1])(-)(((0)[0-9])|((1)[0-2]))(-)\\d{4}";
    private final String ibanRegex = "[A-Z]{2}[0-9]{2}[A-Z]{4}[0-9]{6,14}";

    @Pattern(regexp = dateRegex, message = "supplied from date was not valid use date format: (dd-mm-yyyy) e.g 10-06-2022")
    private String fromDate = null;

    @Pattern(regexp = dateRegex , message = "Supplied until date was not valid use date format: (dd-mm-yyyy) e.g 10-06-2022")
    private String untilDate = null;

    @Pattern(regexp = ibanRegex, message = "Iban of the from account was in invalid form.")
    private String fromIban = null;

    @Pattern(regexp = ibanRegex, message = "Iban of the to account was in invalid form.")
    private String toIban = null;

    @Min(value = 0, message = "Equals amount must be number and can not be lower than 0.")
    private String amountEqual = null;

    @Min(value = 0, message = "Lower than filter amount must be number and can not be lower than 0. ")
    private String amountLowerThan = null;

    @Min(value = 0, message = "More than amount filter must be a number and can not be lower than 0. ")
    private String amountMoreThan = null;

    public FilterParams() {
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(String untilDate) {
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

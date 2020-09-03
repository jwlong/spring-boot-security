package org.thinkingingis.dto;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.thinkingingis.enums.TaxType;
import org.thinkingingis.model.Tax;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class TaxDTO extends Tax {
    private String taxTypeDesc;
    private String amountStr;
    private String taxDateStr;

    public String getAmountStr() {
        if (getAmount() > 0) {
            DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.HALF_UP);
            return df.format(getAmount());
        }
        return amountStr;
    }

    public void setAmountStr(String amountStr) {
        this.amountStr = amountStr;
    }

    public String getTaxTypeDesc() {
        if (StringUtils.isNotEmpty(getType())) {
            for (TaxType type :TaxType.values()) {
                if (getType().equals(type.getCode())) {
                    return  type.getText();
                }
            }
            return  null;
        }
        return taxTypeDesc;
    }

    public String getTaxDateStr() {
        if (getTaxDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(getTaxDate());
        }
        return taxDateStr;
    }

    public void setTaxDateStr(String taxDateStr) {
        this.taxDateStr = taxDateStr;
    }

    public void setTaxTypeDesc(String taxTypeDesc) {
        this.taxTypeDesc = taxTypeDesc;
    }
}

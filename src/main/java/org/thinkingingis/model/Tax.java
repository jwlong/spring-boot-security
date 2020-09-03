package org.thinkingingis.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.thinkingingis.serializes.NumberSerializer;

import java.util.Date;
@TableName("tax")
public class Tax {
    @TableId(value = "tax_pk",type = IdType.AUTO)
    private Long taxPk;
    private String sid;
    private String type;
    private String taxCode;
    private String taxNo;
    @JsonSerialize(using = NumberSerializer.class)
    private Double amount;
    private String machineNo;
    private String addition;
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date taxDate;
    private Date recDate;
    private String used;

    public Long getTaxPk() {
        return taxPk;
    }

    public void setTaxPk(Long taxPk) {
        this.taxPk = taxPk;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public Date getRecDate() {
        return recDate;
    }

    public void setRecDate(Date recDate) {
        this.recDate = recDate;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public Date getTaxDate() {
        return taxDate;
    }

    public void setTaxDate(Date taxDate) {
        this.taxDate = taxDate;
    }
}

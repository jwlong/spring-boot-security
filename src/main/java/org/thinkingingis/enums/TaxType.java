package org.thinkingingis.enums;

public enum TaxType{
    NORMAL("04","普通发票"),
    ELECTRON("10","电子发票");
    private String code;
    private String text;
    TaxType(String code,String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}

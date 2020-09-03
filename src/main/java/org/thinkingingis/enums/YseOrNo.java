package org.thinkingingis.enums;

public enum YseOrNo {
    YES("Y","Yes"),
    NO("N","No");
    private String code;
    private String text;

    YseOrNo(String code, String text) {
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

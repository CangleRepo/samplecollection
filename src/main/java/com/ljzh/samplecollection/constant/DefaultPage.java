package com.ljzh.samplecollection.constant;

/***
 * @title DefaultPage
 * @description 默认分页参数
 * @author rubby
 * @version 1.0.0
 * @create 2023/06/09 上午 09:20
 **/
public enum DefaultPage {
    DEFAULT_PAGE_NUM(1),
    DEFAULT_PAGE_SIZE(10);

    private int value;

    DefaultPage(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

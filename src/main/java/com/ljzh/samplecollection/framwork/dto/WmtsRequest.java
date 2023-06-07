package com.ljzh.samplecollection.framwork.dto;

import lombok.Data;

@Data
public class WmtsRequest {
    private String layer;
    private String style;
    private String tilematrixset;
    private String Service;
    private String Request;
    private String Version;
    private String Format;
    private int TileMatrix;
    private int TileCol;
    private int TileRow;
}
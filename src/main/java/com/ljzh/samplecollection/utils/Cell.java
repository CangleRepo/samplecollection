package com.ljzh.samplecollection.utils;

import org.locationtech.jts.geom.*;


public class Cell {
    private Point localPoint;
    private Integer widthScales;
    private Integer heightScales;
    private MultiPolygon areas;

    public Cell() {
    }

    public Cell(Point localPoint, Integer widthScales,Integer heightScales) {
        this.localPoint = localPoint;
        this.widthScales = widthScales;
        this.heightScales = heightScales;
    }

    public Cell(Point localPoint, Integer widthScales,Integer heightScales, MultiPolygon areas) {
        this.localPoint = localPoint;
        this.widthScales = widthScales;
        this.heightScales = heightScales;
        this.areas = areas;
    }

    public LinearRing getCellRing(){
        double x = this.localPoint.getCoordinate().getX();
        double y = this.localPoint.getCoordinate().getY();
        Coordinate[] coords = new Coordinate[5];
        coords[0] = new Coordinate(x, y);
        coords[1] = new Coordinate(x+widthScales, y);
        coords[2] = new Coordinate(x+widthScales, y+heightScales);
        coords[3] = new Coordinate(x, y+heightScales);
        coords[4] = new Coordinate(x, y);
        return new GeometryFactory().createLinearRing(coords);
    }

    public Polygon getCellPolygon(){
        return new GeometryFactory().createPolygon(this.getCellRing(), null);
    }

    public MultiPolygon getCellMultiPolygon(){
        Polygon[] polygons = new Polygon[1];
        polygons[0] = this.getCellPolygon();
        return new GeometryFactory().createMultiPolygon(polygons);
    }

    public void calculateIntersection(MultiPolygon multiPolygon){
        Geometry intersection = getCellMultiPolygon().intersection(multiPolygon);
        if (intersection.isEmpty()){
            return;
        }
        if (intersection.getClass() == MultiPolygon.class){
            this.areas = (MultiPolygon) intersection;
        }
        if (intersection.getClass() == Polygon.class){
            Polygon[] polygons = new Polygon[1];
            polygons[0] = (Polygon) intersection;
            this.areas = new GeometryFactory().createMultiPolygon(polygons);
        }
    }

    public Point getLocalPoint() {
        return localPoint;
    }

    public void setLocalPoint(Point localPoint) {
        this.localPoint = localPoint;
    }

    public Integer getWidthScales() {
        return widthScales;
    }

    public void setWidthScales(Integer widthScales) {
        this.widthScales = widthScales;
    }

    public Integer getHeightScales() {
        return heightScales;
    }

    public void setHeightScales(Integer heightScales) {
        this.heightScales = heightScales;
    }

    public MultiPolygon getAreas() {
        return areas;
    }

    public void setAreas(MultiPolygon areas) {
        this.areas = areas;
    }
}

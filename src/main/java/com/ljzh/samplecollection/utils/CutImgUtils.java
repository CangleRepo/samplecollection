package com.ljzh.samplecollection.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CutImgUtils {

//    static String polygonSam1 = "[[321.85,1695.27],[497.26,1695.27],[497.26,1580.94],[321.85,1534.94],[321.85,1695.27]]";
//    static String polygonSam2 = "[[1037.64,502.48],[1196.39,502.48],[1196.39,346.25],[1037.64,346.25],[1037.64,502.48]]";

    public static List<Cell> calculate(BufferedImage in, int widthScales,int heightScales, MultiPolygon multiPolygon){
        int widthNum = (in.getWidth() / widthScales)+1;
        int heightNum = (in.getHeight() / heightScales)+1;
        List<Cell> cellList = new ArrayList<>();
        for (int i = 0; i < widthNum; i++) {
            for (int j = 0; j < heightNum; j++) {
                Point point = new GeometryFactory().createPoint(new Coordinate(i * widthScales, j*heightScales));
                Cell cell = new Cell(point,widthScales,heightScales);
                cell.calculateIntersection(multiPolygon);
                if (cell.getAreas()!=null){
                    cellList.add(cell);
                }
            }
        }
        for (Cell cell : cellList) {
            MultiPolygon areas = cell.getAreas();
            Coordinate[] coordinates = areas.getCoordinates();
            for (Coordinate coordinate : coordinates) {
                coordinate.setX(coordinate.getX()-cell.getLocalPoint().getX());
                coordinate.setY(coordinate.getY()-cell.getLocalPoint().getY());
            }
        }
        return cellList;
    }

    public static void cutImgToFile(Cell cell,BufferedImage bufferedImage, String outPath) throws IOException {
        int x = (int) cell.getLocalPoint().getX();
        int y = (int) cell.getLocalPoint().getY();
        Integer widthScales = cell.getWidthScales();
        Integer heightScales = cell.getHeightScales();
        BufferedImage subimage = bufferedImage.getSubimage(x, y, widthScales, heightScales);
        File file = new File(outPath);
        file.getParentFile().mkdirs();
        ImageIO.write(subimage,"PNG",file);
    }

    private static void writeXml(List<Cell> cells, String path) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        FileWriter fileWriter = new FileWriter(path);
        StringBuilder s = new StringBuilder();
        String fileName = file.getName().replace(".xml",".tif");
        s.append("<?xml version=\"1.0\" ?>\n" +
                "<annotation>\n" +
                "\t<folder>2xiao</folder>\n" +
                "\t<filename>");
        s.append(fileName).append("</filename>\n");
        s.append("\t<path>").append("../img/"+fileName).append("</path>\n");
        s.append("\t<source>\n" +
                "\t\t<database>Unknown</database>\n" +
                "\t</source>\n" +
                "\t<size>\n" +
                "\t\t<width>"+cells.get(0).getWidthScales()+"</width>\n" +
                "\t\t<height>"+cells.get(0).getHeightScales()+"</height>\n" +
                "\t\t<depth>3</depth>\n" +
                "\t</size>\n" +
                "\t<segmented>0</segmented>\n");
        int name = 0;
        for (Cell cell : cells) {
            int minX = (int) cell.getLocalPoint().getX();
            int minY = (int) cell.getLocalPoint().getY();
            s.append("\t<object>\n" +
                    "\t\t<name>"+name+"</name>\n" +
                    "\t\t<pose>Unspecified</pose>\n" +
                    "\t\t<truncated>0</truncated>\n" +
                    "\t\t<difficult>0</difficult>\n" +
                    "\t\t<bndbox>\n");
            // 正方形 1024*1024
            // 改矩形 参数 width * height
            // box.getMinX() - width
//            s.append("\t\t\t<xmin>").append(minX % cell.getWidthScales()).append("</xmin>\n");
//            s.append("\t\t\t<ymin>").append(minY % cell.getHeightScales()).append("</ymin>\n");
//            s.append("\t\t\t<xmax>").append(minX % cell.getWidthScales()+ cell.getWidthScales()).append("</xmax>\n");
//            s.append("\t\t\t<ymax>").append(minY % cell.getHeightScales()+ cell.getHeightScales()).append("</ymax>\n");
            Coordinate[] coordinates = cell.getAreas().getCoordinates();
            for (int i = 0; i < coordinates.length; i++) {
                s.append("\t\t\t<coordinate>\n");
                s.append("\t\t\t\t<x>").append(coordinates[i].getX()).append("</x>\n");
                s.append("\t\t\t\t<y>").append(coordinates[i].getY()).append("</y>\n");
                s.append("\t\t\t</coordinate>\n");
            }
            s.append("\t\t</bndbox>\n" +
                    "\t</object>\n");
            name ++;
        }
        s.append("</annotation>");

        fileWriter.write(s.toString());
        fileWriter.flush();
        fileWriter.close();
    }

//    public static MultiPolygon makeMultiPolygon(){
//        String[][] arr = JSON.parseObject(polygonSam1, String[][].class);
//        Coordinate[] coordinates = new Coordinate[arr.length];
//        for(int i=0;i<arr.length;i++){
//            double x = Double.parseDouble(arr[i][0]);
//            double y = Double.parseDouble(arr[i][1]);
//            coordinates[i] = new Coordinate(x,y);
//        }
//        Polygon p1 = new GeometryFactory().createPolygon(coordinates);
//        String[][] arr2 = JSON.parseObject(polygonSam2, String[][].class);
//        Coordinate[] coordinates2 = new Coordinate[arr2.length];
//        for(int i=0;i<arr2.length;i++){
//            double x = Double.parseDouble(arr2[i][0]);
//            double y = Double.parseDouble(arr2[i][1]);
//            coordinates2[i] = new Coordinate(x,y);
//        }
//        Polygon p2 = new GeometryFactory().createPolygon(coordinates2);
//        Polygon[] polygons = new Polygon[2];
//        polygons[0] = p1;
//        polygons[1] = p2;
//        return new GeometryFactory().createMultiPolygon(polygons);
//    }

//    public static void main(String[] args) throws IOException {
//        MultiPolygon multiPolygon = CutImgUtils.makeMultiPolygon();
//
//        BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\Administrator\\Desktop\\cut\\20230129144224.tif"));
//
//        List<Cell> calculate = CutImgUtils.calculate(bufferedImage, 200, 300, multiPolygon);
//        int i = 0;
//        for (Cell cell : calculate) {
//            CutImgUtils.cutImgToFile(cell,bufferedImage,"C:\\Users\\Administrator\\Desktop\\cut\\childpic\\"+i+".tif");
//            i++;
//        }
//        writeXml(calculate,"C:\\Users\\Administrator\\Desktop\\cut\\childpic\\"+"child"+".xml");
//    }
}


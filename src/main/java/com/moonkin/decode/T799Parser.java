package com.moonkin.decode;

import com.moonkin.util.FileUtil;
import ucar.ma2.*;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.Variable;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * T799Parser class
 *
 * @author xuduo
 * @date 2018/10/03
 */
public class T799Parser {

    public static void main(String[] args) {
        String filepath = "E:\\xuduo\\data\\ShuZhi\\ZongCan\\T799\\";
        String file_name = "zc_mwf_rh_925_201810122000.030";
        String outpath = "E:\\xuduo\\data\\after\\numeric\\t799\\";

        file_trans_to_nc(filepath, file_name, outpath);
    }

    public static void file_trans_to_nc(String filepath, String file_name, String outpath) {
        String[] name_list = file_name.split("_");
        String factor=name_list[2];
        String level =name_list[3];
        String report_time=name_list[4].substring(0, 12);
        String forecast_time=name_list[4].substring(13, 16);
        float lon0 = 0.0f;
        float lon1 = 0.0f;
        float lat0 = 0.0f;
        float lat1 = 0.0f;
        int lon_p = 1;
        int lat_p = 1;
        byte[] bs = FileUtil.readFileToBytes(filepath + file_name);
        ByteBuffer bf = ByteBuffer.wrap(bs);
        bf.order(ByteOrder.LITTLE_ENDIAN);
        bf.position(5);
        //104 106 112 113
        char[] ctype = new char[3];
        for(int i=0;i<3;i++) {
            ctype[i] = (char) bf.get();
        }
        String stype = String.valueOf(ctype);
        if("104".equals(stype)) {
            //起始经纬度信息
            bf.position(280);
            char[] cxy = new char[42];
            for(int i = 0;i < 42;i++) {
                cxy[i] = (char) bf.get();
            }
            String sxy = String.valueOf(cxy);
            String[] tmp = sxy.split("\\s+");
            lon0=Float.valueOf(tmp[3]);
            lon1=Float.valueOf(tmp[4]);
            lat0=Float.valueOf(tmp[5]);
            lat1=Float.valueOf(tmp[6]);
            lon_p=Integer.valueOf(tmp[1]);
            lat_p=Integer.valueOf(tmp[2]);
        }
        if ("106".equals(stype)) {
            //起始经纬度信息
            bf.position(214);
            char[] cxy = new char[24];
            for(int i = 0;i < 24;i++) {
                cxy[i] = (char) bf.get();
            }
            String sxy = String.valueOf(cxy);
            String[] tmp = sxy.split("\\s+");
            lon0=Float.valueOf(tmp[1]);
            lon1=Float.valueOf(tmp[2]);
            lat0=Float.valueOf(tmp[3]);
            lat1=Float.valueOf(tmp[4]);

            bf.position(280);
            cxy = new char[10];
            for(int i = 0;i < 10;i++) {
                cxy[i] = (char) bf.get();
            }
            sxy = String.valueOf(cxy);
            String[] tmp2 = sxy.split("\\s+");
            lon_p=Integer.valueOf(tmp2[1]);
            lat_p=Integer.valueOf(tmp2[2]);

        }
        if ("112".equals(stype) || "113".equals(stype)) {
            //起始经纬度信息
            bf.position(216);
            char[] cxy = new char[42];
            for(int i = 0;i < 42;i++) {
                cxy[i] = (char) bf.get();
            }
            String sxy = String.valueOf(cxy);
            String[] tmp = sxy.split("\\s+");
            lon0=Float.valueOf(tmp[1]);
            lon1=Float.valueOf(tmp[2]);
            lat0=Float.valueOf(tmp[3]);
            lat1=Float.valueOf(tmp[4]);
            lon_p=Integer.valueOf(tmp[5]);
            lat_p=Integer.valueOf(tmp[6]);
        }

        bf.position(360);
        float[][] fdata = new float[lon_p][lat_p];
        if (!factor.contains("wind")) {
           for(int i=0; i < lon_p; i++) {
               for(int j=0; j< lat_p; j++) {
                   fdata[i][j] = bf.getFloat();
               }
           }

            String file_out = report_time + "_" +forecast_time + ".nc";
            createNCfile(outpath + file_out,lon_p,lat_p,lon0, lat0, lon1, lat1, 1f, Float.valueOf(level), change_factor(factor),"要素",fdata);

        } else {
            for(int i=0; i < lon_p; i++) {
                for(int j=0; j< lat_p; j++) {
                    fdata[i][j] = bf.getFloat();
                }
            }
            String file_out = report_time + "_" +forecast_time + ".nc";
            createNCfile(outpath + file_out,lon_p,lat_p,lon0, lat0, lon1, lat1, 1f, Float.valueOf(level), factor + "-s","要素",fdata);
            for(int i=0; i < lon_p; i++) {
                for(int j=0; j< lat_p; j++) {
                    fdata[i][j] = bf.getFloat();
                }
            }
            createNCfile(outpath + file_out,lon_p,lat_p,lon0, lat0, lon1, lat1, 1f, Float.valueOf(level), factor + "-d","要素",fdata);


        }
    }

    /**
     *
     * @param filepath 创建的文件路径
     * @param width 网格横向长度
     * @param height 网格纵向高度
     * @param lonWest 网格左下角起始经度
     * @param latSouth 网格左下角起始维度
     * @param gridInterval 网格间距
     * @param elementName  要素变量名
     * @param elementDesc 要素变量描述
     * @param d d[i][j]为网格i和j处对应的数据
     * @throws IOException
     * @throws InvalidRangeException
     */
    public static void createNCfile(String filepath, int width, int height, float lonWest, float latSouth, float lonWest2, float latSouth2, float gridInterval, float lev, String elementName, String elementDesc, float d[][]) {
        int[] h = {10, 20, 30, 50, 70, 100, 200, 300, 500, 700, 850, 925};
        //创建netcdf3写入文件对象
        NetcdfFileWriter fileWriter;
        if (new File(filepath).exists()) {
            try {
                fileWriter = NetcdfFileWriter.openExisting(filepath);
                Variable v = fileWriter.findVariable(elementName);
                if (v != null) {
                    ArrayFloat values = (ArrayFloat) v.read();
                    //创建网格上的变量，D2代表二维，用(width, height)来描述他的定义域，这个顺序必须和ArrayList<Dimension> 加载的顺序一一对应，否则会出错
                    //ArrayFloat values = new ArrayFloat.D3(h.length, width, height);
                    Index index = values.getIndex();
                    for(int k=0; k < h.length; k++) {
                        if (h[k] == lev) {
                            for (int i = 0; i < width; i++) {
                                for (int j = 0; j < height; j++) {
                                    //将二维数组[i][j]处的数据写入网格的i,j处
                                    values.setFloat(index.set(k, i, j), d[i][j]);
                                }
                            }
                        }
                    }
                    fileWriter.write(v, values);
                    fileWriter.close();
                    System.out.println("文件已存入" + filepath);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidRangeException e) {
                e.printStackTrace();
            }
        } else {
            try {
                fileWriter = NetcdfFileWriter.createNew(NetcdfFileWriter.Version.netcdf3, filepath);
                //分别创建经度和纬度方向的维度，lon的宽度为width,lat高度为height,并命名为lon和lat
                Dimension zdim = fileWriter.addDimension(null, "lev", h.length);
                Dimension xdim = fileWriter.addDimension(null, "lon", width);
                Dimension ydim = fileWriter.addDimension(null, "lat", height);
                //通过一维组合成二维array list
                ArrayList<Dimension> dims = new ArrayList<>();
                //注意这里add的顺序很重要,一定要和数据对应，如果把list顺序调换，那么二维的维度也会不同，相当于数学中的坐标系，第一个是x坐标，第二个是y坐标，x宽度为width,y高度为height
                dims.add(zdim);
                dims.add(xdim);
                dims.add(ydim);

                Variable vz = fileWriter.addVariable(null, "lev", DataType.INT, "lev");
                fileWriter.addVariableAttribute(vz, new Attribute("long_name", "lev"));
                //给hy变量加上units，对应的高度层，如果不加units为hpa
                fileWriter.addVariableAttribute(vz, new Attribute("units", "hpa"));

                //创建名称为lon的变量,类型为folat，对应的维度为lon,对应Dimension里面定义的名称为"lon"的那个
                Variable vx = fileWriter.addVariable(null, "lon", DataType.FLOAT, "lon");
                //给vx变量加上数据说明，全程long_name
                fileWriter.addVariableAttribute(vx, new Attribute("long_name", "longitude"));
                //给vx变量加上units，对应的Degree_east，如果不加units为Degree_east(东经)，则不是地理坐标系，这个很重要
                fileWriter.addVariableAttribute(vx, new Attribute("units", "Degrees_east"));

                fileWriter.addVariableAttribute(vx, new Attribute("Lo1", Double.valueOf(lonWest)));
                fileWriter.addVariableAttribute(vx, new Attribute("Lo2", Double.valueOf(lonWest2)));
                fileWriter.addVariableAttribute(vx, new Attribute("Dx", 1.0d));
                fileWriter.addVariableAttribute(vx, new Attribute("La1", Double.valueOf(latSouth)));
                fileWriter.addVariableAttribute(vx, new Attribute("La2", Double.valueOf(latSouth2)));
                fileWriter.addVariableAttribute(vx, new Attribute("Dy", 1.0d));

                Variable vy = fileWriter.addVariable(null, "lat", DataType.FLOAT, "lat");
                fileWriter.addVariableAttribute(vy, new Attribute("long_name", "latitude"));
                //给vy变量加上units，对应的Degree_north，如果不加units为Degree_north(北纬)，则不是地理坐标系，这个很重要
                fileWriter.addVariableAttribute(vy, new Attribute("units", "Degrees_north"));

                fileWriter.addVariableAttribute(vy, new Attribute("Lo1", Double.valueOf(lonWest)));
                fileWriter.addVariableAttribute(vy, new Attribute("Lo2", Double.valueOf(lonWest2)));
                fileWriter.addVariableAttribute(vy, new Attribute("Dx", 1.0d));
                fileWriter.addVariableAttribute(vy, new Attribute("La1", Double.valueOf(latSouth)));
                fileWriter.addVariableAttribute(vy, new Attribute("La2", Double.valueOf(latSouth2)));
                fileWriter.addVariableAttribute(vy, new Attribute("Dy", 1.0d));

                //创建x和y方向上的变量。D1    代表一维
                ArrayInt zvalues = new ArrayInt.D1(h.length);
                ArrayFloat xvalues = new ArrayFloat.D1(width);
                ArrayFloat yvalues = new ArrayFloat.D1(height);

                for (int i = 0; i < width; i++) {
                    xvalues.setFloat(i, (float) (lonWest + gridInterval * i));
                }
                for (int j = 0; j < height; j++) {
                    yvalues.setFloat(j, (float) (latSouth + gridInterval * j));
                }

                for (int z = 0; z < h.length; z++) {
                    zvalues.setInt(z, h[z]);
                }

                String[] elementIds = {"HGT","TMP","RH","DIV","PV","W",
                        "PRMSL","TMP2M","TD2M","TCC","HCC","MCC",
                        "LCC","DP24","DH24","DT24","P3","P6","P12","P24", "wind-s","wind-d"};
                Map<Variable,ArrayFloat> map = new HashMap<>();
                for (String e : elementIds) {
                    //创建变量名称为var的变量，对应的维度为dims,该dims为上面定义的一个ArrayList<Diminsion>，该list包含2个维度，经度和维度
                    Variable v = fileWriter.addVariable(null, e, DataType.FLOAT, dims);
                    fileWriter.addVariableAttribute(v, new Attribute("long_name", "long_name"));
                    fileWriter.addVariableAttribute(v, new Attribute("units", "unit"));

                    //创建网格上的变量，D2代表二维，用(width, height)来描述他的定义域，这个顺序必须和ArrayList<Dimension> 加载的顺序一一对应，否则会出错
                    ArrayFloat values = new ArrayFloat.D3(h.length,width, height);
                    Index index = values.getIndex();
                    for(int k=0; k < h.length; k++) {
                        if (h[k] == lev && e.equals(elementName)) {
                            for (int i = 0; i < width; i++) {
                                for (int j = 0; j < height; j++) {
                                    //将二维数组[i][j]处的数据写入网格的i,j处
                                    values.setFloat(index.set(k, i, j), d[i][j]);
                                }
                            }
                        } else {
                            for (int i = 0; i < width; i++) {
                                for (int j = 0; j < height; j++) {
                                    //将二维数组[i][j]处的数据写入网格的i,j处
                                    values.setFloat(index.set(k, i, j), 9999f);
                                }
                            }
                        }
                    }
                    map.put(v,values);
                }
                //使用create()来创建该文件，只有create之后才能在文件夹中显示该文件，并且写入数据
                fileWriter.create();
                fileWriter.write(vx, xvalues);
                fileWriter.write(vy, yvalues);
                fileWriter.write(vz, zvalues);
                for(Map.Entry<Variable,ArrayFloat> entry : map.entrySet()) {
                    fileWriter.write(entry.getKey(),entry.getValue());
                }
                fileWriter.close();
                System.out.println("文件已存入" + filepath);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("文件创建失败，请检查路径");
            } catch (InvalidRangeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("文件写入错误，超出范围");
            }
        }
    }

    private static String change_factor(String factor_in) {
        Map<String,Object> map = new HashMap<>();
        map.put("height","HGT");
        map.put("height-p","HGT-p");
        map.put("temper","TMP");
        map.put("temper-p","TMP-p");
        map.put("rh","RH");
        map.put("rh-p","RH-p");
        map.put("wind","WIND");
        map.put("wind-p","WIND-p");
        map.put("div","DIV");
        map.put("div-p","DIV-p");
        map.put("vor","PV");
        map.put("vor-p","PV-p");
        map.put("omega","W");
        map.put("omega-p","W-p");
        map.put("slp","PRMSL");
        map.put("slp-p","PRMSL-p");
        map.put("t2","TMP2M");
        map.put("t2-p","TMP2M-p");
        map.put("td2","TD2M");
        map.put("td2-p","TD2M-p");
        map.put("nc","TCC");
        map.put("nc-p","TCC-p");
        map.put("hc","HCC");
        map.put("hc-p","HCC-p");
        map.put("mc","MCC");
        map.put("mc-p","MCC-p");
        map.put("lc","LCC");
        map.put("lc-p","LCC-p");
        map.put("dp24","DP24");
        map.put("dp24-p","DP24-p");
        map.put("dt24-p","DT24-p");
        map.put("dh24","DH24");
        map.put("dh24-p","DH24-p");
        map.put("dt24","DT24");
        map.put("rain3","P3");
        map.put("rain3-p","P3-p");
        map.put("rain6","P6");
        map.put("rain6-p","P6-p");
        map.put("rain12","P12");
        map.put("rain12-p","P12-p");
        map.put("rain24","P24");
        map.put("rain24-p","P24-p");
        String factor = (String) map.get(factor_in);
        return factor;
    }

}

package com.moonkin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author xuduo
 */
public class FileUtil {

    /**
     * 文件复制
     * @param src   源文件
     * @param dst   目标文件，目标目录必须存在
     * @throws IOException
     */
    public static void fileChannelCopy(File src, File dst) throws IOException {
        FileChannel inChannel =new FileInputStream(src).getChannel();
        FileChannel outChannel=new FileOutputStream(dst).getChannel();

        inChannel.transferTo(0, inChannel.size(), outChannel);

        inChannel.close();
        outChannel.close();
    }

    public static void main(String[] args) {
        File src = new File("/Users/xuduo/data/MODP_XZQX_EC_TP3H_AFXZ_000_DT_20180817080000_072-240.nc");
        File dst = new File("/Users/xuduo/datas/copy.nc");
        try {
            FileUtil.fileChannelCopy(src,dst);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

/*
* @class FileUtil
* @author xuduo
* @version 1.0
* @since 2018-09-25
* @copyright
*/
package com.moonkin.util;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能：文件操作工具类
 *
 * @author xuduo
 * @version 1.0
 * @since 2018-09-25
 */
public class FileUtil {

    /**
     * 判断文件的编码格式
     *
     * @param filePath 文件路径
     * @return 文件编码格式
     * @throws Exception
     */
    public static String getEnCode(String filePath) throws Exception {
        String code;
        BufferedInputStream bin = null;
        try {
            bin = new BufferedInputStream(new FileInputStream(filePath));
            int p = (bin.read() << 8) + bin.read();
            switch (p) {
                case 0xefbb:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                default:
                    code = "GBK";
            }
        } finally {
            if (null != bin) {
                bin.close();
            }
        }
        return code;
    }

    /**
     * 读远程文件内容保存到本地文件中，
     *
     * @param remotePath 远程挂载路径
     * @param localpath  本地路径
     * @return 本地路径的File对象
     * @throws IOException
     */
    public static File readFromRemote(String remotePath, String localpath) {
        File localfile = null;
        InputStream bis = null;
        OutputStream bos = null;
        try {
                File rmifile = new File(remotePath);
                String filename = rmifile.getName();
                bis = new BufferedInputStream(new FileInputStream(rmifile));
                localfile = new File(localpath + File.separator + filename);
                if (!localfile.getParentFile().exists()) {
                    localfile.getParentFile().mkdir();
                }
                bos = new BufferedOutputStream(new FileOutputStream(localfile));
                int length = (int) rmifile.length();
                byte[] buffer = new byte[length];
                Date date = new Date();
                bis.read(buffer);
                bos.write(buffer);
                Date end = new Date();
                int time = (int) ((end.getTime() - date.getTime()) / 1000);
                if (time > 0)
                    System.out.println("用时:" + time + "秒 " + "速度:" + length / time / 1024 + "kb/秒");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return localfile;
    }

    /**
     * 读取本地文件成byte[]
     *
     * @param filePath
     * @return
     */
    public static byte[] readLocalFile(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream is = new FileInputStream(file);
            byte[] buff = new byte[is.available()];
            is.read(buff);
            is.close();
            return buff;
        } catch (IOException e) {
            try {
                File file = new File(filePath);
                FileInputStream is = new FileInputStream(file);
                byte[] buff = new byte[is.available()];
                is.read(buff);
                is.close();
                return buff;
            } catch (IOException e1) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 目录不存在会自动创建目录
     *
     * @param path
     */
    public static void newFolder(String path) {
        String str = path.substring(path.lastIndexOf("/"), path.length());
        if (str.indexOf(".") != -1) {
            path = path.substring(0, path.lastIndexOf("/"));
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 拷贝文件
     *
     * @param inputDoc  输入文件本地路径
     * @param outputDoc 输出文件本地路径
     * @throws Exception
     */
    public static void copyFile(String inputDoc, String outputDoc) throws Exception {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel fci = null;
        FileChannel fco = null;
        try {
            fis = new FileInputStream(inputDoc);
            fos = new FileOutputStream(outputDoc);
            fci = fis.getChannel();
            fco = fos.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 4);
            while (fci.read(buffer) > -1) {
                buffer.flip();
                fco.write(buffer);
                buffer.clear();
            }
            fco.close();
            fci.close();
            fos.close();
            fis.close();
        } catch (Exception e) {
            Thread.sleep(500);
            fis = new FileInputStream(inputDoc);
            fos = new FileOutputStream(outputDoc);
            fci = fis.getChannel();
            fco = fos.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 4);
            while (fci.read(buffer) > -1) {
                buffer.flip();
                fco.write(buffer);
                buffer.clear();
            }
            fco.close();
            fci.close();
            fos.close();
            fis.close();
        } finally {
            if (null != fci)
                fci.close();
            if (null != fco)
                fco.close();
            if (null != fis)
                fis.close();
            if (null != fos)
                fos.close();
        }
    }

    /**
     * 强制删除文件
     *
     * @param file 文件对象
     * @return
     */
    public static boolean forceDelete(File file) {
        boolean result = false;
        int tryCount = 0;
        while (!result && tryCount++ < 10) {
            System.gc();
            result = file.delete();
        }
        return result;
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean result = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            result = forceDelete(file);
        }
        return result;
    }

    /**
     * 拷贝远程文件到本地目录文件中，并指定字符集
     *
     * @param smbpath   远程源路径(绝对路径+文件名)
     * @param srcCode   源文件编码
     * @param localpath 本地目标路径(绝对路径+文件名)
     * @param encode    目标文件编码
     * @return sucess/fail
     * @throws IOException
     */
    public static boolean copyLocalFileToSmb(String smbpath, String srcCode, String localpath, String encode) {
        try {
            String str = readFileToString(smbpath, srcCode);
            File localFile = new File(localpath);
            if (localFile.exists()) {
                localFile.delete();
                localFile.createNewFile();
            } else {
                new File(localFile.getParent()).mkdirs();
            }
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(localpath), encode);
            out.write(str.toString());
            out.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 复制文件,源和目标存储路径协议相同
     *
     * @param srcPath  源文件路径
     * @param destPath 目标文件路径
     * @param isDelete 是否删除原文件
     * @return
     */
    public static boolean copySmbToSmb(String srcPath, String destPath, boolean isDelete) {
        try {
                File file = new File(destPath);
                if (!file.exists()) {
                    if (!new File(file.getParent()).exists()) {
                        new File(file.getParent()).mkdirs();
                    }
                }
                File srcfile = new File(srcPath);
                FileInputStream in = new FileInputStream(srcfile);
                byte[] b = new byte[(int) srcfile.length()];
                in.read(b);
                in.close();
                FileOutputStream out = new FileOutputStream(file);
                out.write(b);
                out.close();
                if (isDelete) {
                    srcfile.delete();
                }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 读取本地目录下所有文件,过滤后缀名
     *
     * @param path   文件目录路径
     * @param suffix 文件后缀
     * @param result 返回文件对象列表
     * @throws Exception
     */
    public static void readFiles(String path, String suffix, List<File> result) throws Exception {
        File document = new File(path);
        if (!document.exists()) {
            return;
        }
        File[] list = document.listFiles();
        if (list.length > 0) {
            for (File file : list) {
                // 文件，直接添加到结果里
                if (file.isFile()) {
                    // 临时文件不需要
                    if (file.getName().startsWith("~")) {
                        continue;
                    }
                    // 为规定后缀名，添加到列表里
                    if (file.getName().endsWith(suffix)) {
                        result.add(file);
                    }
                }
                // 是文件夹，继续迭代
                if (file.isDirectory()) {
                    readFiles(path + "/" + file.getName(), suffix, result);
                }
            }
        }

    }

    /**
     * 按照生成时间排序
     *
     * @param result 文件对象列表
     */
    public static void fileSort(List<File> result) {
        result.stream().sorted(Comparator.comparing(File::lastModified));
    }


    /**
     * 写入文件
     *
     * @param filePath 文件全路径,不存在创建
     * @param str      内容
     * @param encode   编码
     */
    public static void write(String filePath, String str, String encode) {
        try {
                File file = new File(filePath);
                if (!file.exists()) {
                    if (!new File(file.getParent()).exists()) {
                        new File(file.getParent()).mkdirs();
                    }
                }
                FileOutputStream os = new FileOutputStream(file);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, encode));
                bw.write(str);
                bw.close();
                os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件内容
     *
     * @param filePath 文件路径
     * @param encode   编码
     * @return String
     */
    public static String readFileToString(String filePath, String encode) {
        String code;
        try {
            if (encode != null || !"".equals(encode)) {
                code = encode;
            } else {
                code = getEnCode(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
            try {
                if (!new File(filePath).exists()) {
                    return "";
                }
                InputStream in = new FileInputStream(filePath);
                BufferedReader br = new BufferedReader(new InputStreamReader(in, code));
                String resultstr = "";
                String line = "";
                while ((line = br.readLine()) != null) {
                    resultstr = resultstr + line + "\n";
                }
                br.close();
                in.close();
                return resultstr;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
    }

    /**
     * 读取文件内容
     *
     * @param filePath 文件路径
     * @return byte[] 字节流
     */
    public static byte[] readFileToBytes(String filePath) {
            try {
                File file = new File(filePath);
                FileInputStream is = new FileInputStream(file);
                byte[] buff = new byte[is.available()];
                is.read(buff);
                is.close();
                return buff;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
    }

    /**
     * 文件名解析
     *
     * @param params 参数（参数的键值用转换类型表示）
     * @param flg    是否严格解析
     * @return
     * @throws Exception
     */
    public static String parsePath(String src, Map<String, Object> params, boolean flg) throws Exception {
        String resultStr = "";
        StringBuffer sb = new StringBuffer();
        String tmpPattern = src;
        if (null != tmpPattern) {
            tmpPattern = tmpPattern.replaceAll("%", "%%");
        }
        List<Object> paramValues;
        String _regex = "#(0)?(\\d+)?(t?[a-zA-Z])";
        Pattern _pattern = Pattern.compile(_regex);
        Matcher m = _pattern.matcher(tmpPattern);
        paramValues = new ArrayList<Object>();
        int index = 0;
        for (int i = 0, len = tmpPattern.length(); i < len; ) {
            if (m.find(i)) {
                if (m.start() != i) {
                    sb.append(tmpPattern.substring(i, m.start()));
                }
                String flags = m.group(1);
                String width = m.group(2);
                String conversion = m.group(3);
                if (params.containsKey(conversion)) {
                    index++;
                    Object tmp = params.get(conversion);
                    if (null != width && tmp instanceof String) {
                        String s = (String) tmp;
                        int beginIndex = s.length() - Integer.parseInt(width);
                        beginIndex = beginIndex < 0 ? 0 : beginIndex;
                        s = s.substring(beginIndex);
                        tmp = Integer.parseInt(s);
                    }
                    paramValues.add(tmp);
                    sb.append("%").append(index).append("$");
                    if (null != flags) {
                        sb.append(flags);
                    }
                    if (null != width) {
                        sb.append(width).append("d");
                    } else {
                        sb.append("s");
                    }
                } else if (flg) {
                    throw new Exception("#" + conversion + "格式化异常");
                } else {
                    sb.append(m.group());
                }
                i = m.end();
            } else {
                sb.append(tmpPattern.substring(i));
                break;
            }
        }
        resultStr = String.format(sb.toString(), paramValues.toArray(new Object[0]));
        return resultStr;
    }

    /**
     * byte[] 保存成本地文件
     *
     * @param buffer   字节流
     * @param filePath 本地保存文件路径
     */
    public static void writeBytesToLocalFile(byte[] buffer, final String filePath) {
        File file = new File(filePath);
        OutputStream output = null;
        BufferedOutputStream bufferedOutput = null;
        try {
            output = new FileOutputStream(file);
            bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(buffer);
            bufferedOutput.close();
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bufferedOutput) {
                try {
                    bufferedOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * byte[] 保存成本地文件
     *
     * @param ins      输入流
     * @param filePath 本地保存文件路径
     */
    public static void writeInputStreamToLocalFile(InputStream ins, final String filePath) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File(filePath));
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != ins) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 分割文件方法
     * @param srcFile   源文件路径
     * @param desDir    生成目标目录
     * @param size      分割后单个文件大小 byte * 1024 * 1024 = 1M
     */
    public static void partitionFile(String srcFile, String desDir, int size){
        FileInputStream fis = null;
        File file;
        try {
            fis = new FileInputStream(srcFile);
            file = new File(srcFile);
            //创建规定大小的数组
            byte[] b = new byte[size];
            int len = 0;
            //name为以后的小文件命名做准备
            int num = 1;
            while ((len = fis.read(b)) != -1) {
                String fullName = file.getName();
                int lastIndexOf = fullName.lastIndexOf(".");
                String name = fullName.substring(0, lastIndexOf);
                String suffix = fullName.substring(lastIndexOf,fullName.length());
                FileOutputStream fos = new FileOutputStream(desDir + "/" + name + "_" + num + suffix);
                fos.write(b, 0, len);
                fos.close();
                num ++;
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fis != null){
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	/**
	 * 以字节形式读取二进制文件或文本文件
	 * @param path 文件绝对路径
	 * @return
	 * @throws IOException 
	 */
	public static byte[] readAsByte(String path) throws IOException {
		byte[] buf;
		buf = localReadAsByte(path);
		return buf;
	}

	/**
	 * 以指定字符编码读取文本文件
	 * @param path 文件绝对路径
	 * @param encoding 字符编码
	 * @return
	 * @throws IOException 
	 */
	public static String read(String path, String encoding) throws IOException {
		String content = "";
		content = localRead(path, encoding);
		return content;
	}

	/**
	 *  以指定字符编码读取文本文件
	 * @param file 文件
	 * @param encoding 字符编码
	 * @return
	 * @throws IOException 
	 */
	public static String read(File file, String encoding) throws IOException {
		String content = "";
		content = localRead(file, encoding);
		return content;
	}
	/**
	 * 读取本地文件内容(字节形式)
	 * 
	 * @param path
	 *            文件路径
	 * @return 文件内容
	 * @throws IOException 
	 */
	private static byte[] localReadAsByte(String path) throws IOException {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(path));
			int bufferSize = bis.available();
			byte[] buf = new byte[bufferSize];
			bis.read(buf);
			bis.close();
			return buf;
	}
	/**
	 * 读取本地文件内容(字节形式)
	 * 
	 * @param file
	 *            文件路径
	 * @return 文件内容
	 * @throws IOException 
	 */
	private static byte[] localReadAsByte(File file) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(
				new FileInputStream(file));
		int bufferSize = bis.available();
		byte[] buf = new byte[bufferSize];
		bis.read(buf);
		bis.close();
		return buf;
	}

	/**
	 * 以指定字符编码读取本地文本文件
	 * 
	 * @param path 文件路径
	 * @param encoding 字符编码
	 * @return 文件内容
	 * @throws IOException 
	 */
	public static String localRead(String path, String encoding) throws IOException {
		byte[] temp = localReadAsByte(path);
		String str = new String(temp, encoding);
		return str;
//		return new String(localReadAsByte(path),encoding);
	}
	/**
	 * 读取本地文件内容
	 * 
	 * @param file
	 *            文件
	 * @param encoding
	 *            字符编码
	 * @return 文件内容
	 * @throws IOException 
	 */
	public static String localRead(File file, String encoding) throws IOException {
		byte[] temp = localReadAsByte(file);
		String str = new String(temp, encoding);
		return str;
	}
	/**
	 * 按照little-endian 字节顺序写入浮点数
	 * @param dos
	 * @param value
	 */
	public static void writeFloatByLE (DataOutputStream dos, float value) throws IOException {
		ByteBuffer buff = ByteBuffer.allocate(4);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		buff.putFloat(value);
		dos.write(buff.array());
	}
	/**
	 * 按照little-endian 字节顺序写入短整数
	 * @param dos
	 * @param value
	 */
	public static void writeShortByLE (DataOutputStream dos, short value) throws IOException {
		ByteBuffer buff = ByteBuffer.allocate(2);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		buff.putShort(value);
		dos.write(buff.array());
	}
	/**
	 * 按照little-endian 字节顺序写入无符号短整数
	 * @param dos
	 * @param value
	 */
	public static void writeUnsignedShortByLE (DataOutputStream dos, int value) throws IOException {
		ByteBuffer buff = ByteBuffer.allocate(4);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		buff.putInt(value);
		dos.write(Arrays.copyOfRange(buff.array(), 0, 2));
	}
	/**
	 * 按照little-endian 字节顺序写入整数
	 * @param dos
	 * @param value
	 */
	public static void writeIntByLE (DataOutputStream dos, int value) throws IOException {
		ByteBuffer buff = ByteBuffer.allocate(4);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		buff.putInt(value);
		dos.write(buff.array());
	}

    /**
     * 文件复制
     * @param src   源文件
     * @param dst   目标文件，目标目录必须存在
     * @throws IOException
     */
    public static void fileChannelCopy(File src, File dst) throws IOException {
        if(!dst.getParentFile().exists()) {
            dst.getParentFile().mkdirs();
        }
        if(!dst.exists()) {
            dst.createNewFile();
        }
        FileChannel inChannel =new FileInputStream(src).getChannel();
        FileChannel outChannel=new FileOutputStream(dst).getChannel();

        inChannel.transferTo(0, inChannel.size(), outChannel);

        inChannel.close();
        outChannel.close();
    }

    /**
     * 删除指定文件夹内一段时间之前的文件
     */
    public static void fileDelete(File file, long seconds) {
        if (!file.exists()) {
            return;
        }
        if (file.lastModified() < seconds && (file.isFile() || file.list() == null)) {
            file.delete();
            System.out.println("删除了" + file.getName());
        } else {
            File[] files = file.listFiles(m -> {
                if (m.lastModified() < seconds) {
                    return true;
                } else {
                    return false;
                }
            });
            for (File f : files) {
                fileDelete(f, seconds);
            }
            if(file.lastModified() < seconds) {
                file.delete();
                System.out.println("删除了" + file.getName());
            }
        }
    }

    /**
     * 修改文件内容
     * @param fileName
     * @param oldstr
     * @param newStr
     * @return
     */
    public static boolean modifyFileContent(String fileName, String oldstr, String newStr) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(fileName, "rw");
            String line = null;
            long lastPoint = 0; //记住上一次的偏移量
            while ((line = raf.readLine()) != null) {
                final long ponit = raf.getFilePointer();
                if(line.contains(oldstr)){
                    String str=line.replace(oldstr, newStr);
                    raf.seek(lastPoint);
                    raf.writeBytes(str);
                }
                lastPoint = ponit;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}

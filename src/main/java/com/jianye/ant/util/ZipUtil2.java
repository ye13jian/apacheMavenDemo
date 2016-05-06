package com.jianye.ant.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import java.util.Enumeration;
import java.util.zip.ZipOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

/* *
 * 解压zip文件
 * 压缩zip文件
 * */


public class ZipUtil2 {

    /**
     * 创建目录
     *
     * @param path
     *            目录绝对路径名
     */
    static void createDir(String path) {
        File dir = new File(path);
        if (dir.exists() == false)
            dir.mkdir();
    }

    /**
     * 取得文件名,不包含后缀名
     *
     * @param name
     *            完整文件名
     * @return 文件名(不包含后缀名)
     */
    static String getSuffixName(String name) {
        return name.substring(0, name.lastIndexOf("."));
    }

    public static void main(String[] args) throws Exception {
//    unzip("d:/test/test.zip", "d:/test/");
//    System.out.println("ok!");

        /**
         * 压缩文件zip
         */
//        File[] files = new File[]{new File("d:/English"),new File("d:/发放数据.xls"),new File("d:/中文名称.xls")};
//        File zip = new File("d:/压缩.zip");
//        ZipFiles(zip,"abc",files);
        File[] files = new File[]{new File("d:/test")};
        File zip = new File("d:/压缩.zip");
        ZipFiles(zip,files);
        System.out.println("ok!");

    }

    /**
     * 解压zip文件
     *
     * @param zipFilePath
     *            zip文件绝对路径
     * @param unzipDirectory
     *            解压到的确
     * @throws Exception
     */
    public static void unzip(String zipFilePath, String unzipDirectory) throws Exception {
        // 创建文件对象
        File file = new File(zipFilePath);
        // 创建zip文件对象
        ZipFile zipFile = new ZipFile(file);
        // 创建本zip文件解压目录
        //File unzipFile = new File(unzipDirectory + "/" + getSuffixName(file.getName()));
        File unzipFile = new File(unzipDirectory);
        if (unzipFile.exists())
            unzipFile.delete();
        unzipFile.mkdir();
        // 得到zip文件条目枚举对象
        Enumeration zipEnum = zipFile.getEntries();
        // 定义输入输出流对象
        InputStream input = null;
        OutputStream output = null;
        // 定义对象
        ZipEntry entry = null;
        // 循环读取条目
        while (zipEnum.hasMoreElements()) {
            // 得到当前条目
            entry = (ZipEntry) zipEnum.nextElement();
            String entryName = new String(entry.getName());
            // 用/分隔条目名称
            String names[] = entryName.split("\\/");
            int length = names.length;
            String path = unzipFile.getAbsolutePath();
            for (int v = 0; v < length; v++) {
                if (v < length - 1) { // 最后一个目录之前的目录
                    path += "/" + names[v] + "/";
                    createDir(path);
                } else { // 最后一个
                    if (entryName.endsWith("/")) // 为目录,则创建文件夹
                        createDir(unzipFile.getAbsolutePath() + "/" + entryName);
                    else { // 为文件,则输出到文件
                        input = zipFile.getInputStream(entry);
                        output = new FileOutputStream(new File(unzipFile
                                .getAbsolutePath()
                                + "/" + entryName));
                        byte[] buffer = new byte[1024 * 8];
                        int readLen = 0;
                        while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1)
                            output.write(buffer, 0, readLen);
                        // 关闭流
                        input.close();
                        output.flush();
                        output.close();
                    }
                }
            }
        }
    }



    /**
     * 压缩文件-由于out要在递归调用外,所以封装一个方法用来
     * 调用ZipFiles(ZipOutputStream out,String path,File... srcFiles)
     * @param zip
     * @param srcFiles
     * @throws IOException
     * @author isea533
     */
    public static void ZipFiles(File zip,File... srcFiles) throws IOException{
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
        ZipUtil2.ZipFiles(out,srcFiles);
        out.close();
    }
    /**
     * 压缩文件-File
     * @param srcFiles 被压缩源文件
     * @author isea533
     */
    public static void ZipFiles(ZipOutputStream out,File... srcFiles){

        byte[] buf = new byte[1024];
        try {
            for(int i=0;i<srcFiles.length;i++){
                if(srcFiles[i].isDirectory()){
                    File[] files = srcFiles[i].listFiles();
                    String srcPath = srcFiles[i].getName();
                    srcPath = srcPath.replaceAll("\\*", "/");
                    if(!srcPath.endsWith("/")){
                        srcPath+="/";
                    }
                    out.putNextEntry(new ZipEntry(srcPath));
                    ZipFiles(out,files);
                }
                else{
                    FileInputStream in = new FileInputStream(srcFiles[i]);
                    System.out.println(srcFiles[i].getName());
                    out.putNextEntry(new ZipEntry(srcFiles[i].getName()));
                    int len;
                    while((len=in.read(buf))>0){
                        out.write(buf,0,len);
                    }
                    out.closeEntry();
                    in.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

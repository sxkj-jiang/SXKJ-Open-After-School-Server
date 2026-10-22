package com.sxkj.edu.common.utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

/**
 *@Description : 文件压缩和解压
 *@Author : jjj$
 *@Date : 2024$ 0929$
 *@Company : SXKJ
 *@Version : 1.0
 **/
public class FileZipUtil {

    /**
     * 压缩多个文件，压缩后的所有文件在同一目录下
     *
     * @param zipFileName 压缩后的文件名
     * @param files 需要压缩的文件列表
     * @throws IOException IO异常
     */
    public static void zipMultipleFiles(String zipFileName, File... files) throws IOException {
        ZipOutputStream zipOutputStream = null;
        try {
            // 输出流
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileName));
            // 遍历每一个文件，进行输出
            for (File file : files) {
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                FileInputStream fileInputStream = new FileInputStream(file);
                int readLen;
                byte[] buffer = new byte[1024];
                while ((readLen = fileInputStream.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, readLen);
                }
                // 关闭流
                fileInputStream.close();
                zipOutputStream.closeEntry();
            }
        } finally {
            if (null != zipOutputStream) {
                try {
                    zipOutputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 压缩多个文件，压缩后的所有文件在同一目录下
     *
     * @param zipFileName 压缩后的文件名
     * @param fileList 需要压缩的文件列表
     * @throws IOException IO异常
     */
    public static ZipOutputStream zipMultipleFiles(String dirName,String zipFileName, List<File> fileList) throws IOException {
        ZipOutputStream zipOutputStream = null;
        try {
            File destDirPath = new File(dirName);
            if (!destDirPath.exists()) {
                destDirPath.mkdir();
            }
            // 输出流
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileName));
            // 遍历每一个文件，进行输出
            for (File file : fileList) {
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                FileInputStream fileInputStream = new FileInputStream(file);
                int readLen;
                byte[] buffer = new byte[1024];
                while ((readLen = fileInputStream.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, readLen);
                }
                // 关闭流
                fileInputStream.close();
                zipOutputStream.closeEntry();
            }
        } finally {
            if (null != zipOutputStream) {
                try {
                    zipOutputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return zipOutputStream;
    }

    private static void addFileToZip(ZipOutputStream zipOut, String folderName, Path filePath) {
        Path relativePath = filePath;
        ZipEntry zipEntry = new ZipEntry(relativePath.toString());
        try {
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = Files.readAllBytes(filePath);
            zipOut.write(bytes, 0, bytes.length);
            zipOut.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压文件
     * @param zipFilePath
     * @param destDir
     * @throws IOException
     */
    public static void extract(String zipFilePath, String destDir) throws IOException {
        File destDirPath = new File(destDir);
        if (!destDirPath.exists()) {
            destDirPath.mkdir();
        }
        extract(Files.newInputStream(Paths.get(zipFilePath)),destDir);
    }

    /**
     * 解压文件
     * @param is
     * @param destDir
     * @throws IOException
     */
    public static void extract(InputStream is,String destDir) throws IOException {
        ZipInputStream zipIn = new ZipInputStream(is);
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {
            String filePath = destDir + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                extractFile(zipIn, filePath);
            } else {
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    /**
     * 递归删除目录及其内部所有文件、子目录
     *
     * @param dir 待删除的目录
     * @return true=全部删除成功
     */
    public static boolean deleteDirectory(File dir) {
        if (dir == null || !dir.exists()) {
            return true;
        }
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归删除子目录
                    deleteDirectory(file);
                } else {
                    if (!file.delete()) {
                        return false;
                    }
                }
            }
        }
        // 最后删除空目录本身
        return dir.delete();
    }

    /**
     * 递归删除目录及其内部所有文件、子目录
     *
     * @param dirPath 待删除的目录路径
     * @return true=全部删除成功
     */
    public static boolean deleteDirectory(String dirPath) {
        return deleteDirectory(new File(dirPath));
    }

    // 测试方法
    public static void main(String[] args) {
        try {
//            // 压缩示例
//            String srcFolder = "D:\\003_项目\\sxjypt\\模板\\录取审核申请模板.docx";
//            String srcFolder2 = "D:\\003_项目\\sxjypt\\模板\\test.txt";
//            String zipFileName = "D:\\003_项目\\sxjypt\\模板\\录取审核申请模板.zip";
//            zipMultipleFiles(zipFileName, new File(srcFolder),new File(srcFolder2));

            // 解压示例
            String zipFilePath = "D:\\003_项目\\sxjypt\\模板\\录取审核申请模板.zip";
            String destDir = "D:\\003_项目\\sxjypt\\模板\\解压测试";
            extract(zipFilePath, destDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

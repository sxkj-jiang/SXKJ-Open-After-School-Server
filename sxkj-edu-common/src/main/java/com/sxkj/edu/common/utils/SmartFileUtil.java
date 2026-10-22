package com.sxkj.edu.common.utils;
import com.sxkj.edu.common.base.domain.vo.DirAllFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;


/**
 *@Description : 文件名工具类
 *@Author : JIANGJJ
 *@Date : 2024-6-25 17:08
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Slf4j
public class SmartFileUtil {

    public static boolean isXmlFile(File file) {
        return "xml".equalsIgnoreCase(getFileExtension(file.getName()));
    }

    /**
     * 文件后缀名
     *
     * @param fullName
     * @return
     */
    public static String getFileExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    /**
     * 不带后缀名的文件名
     *
     * @param file
     * @return
     */
    public static String getNameWithoutExtension(String file) {
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    /**
     * 获取当前目录下的所有文件和子目录
     * @param dirPath
     * @return
     */
    public static DirAllFileVO getDirAllFile(String dirPath){
        DirAllFileVO dirAllFileVO = new DirAllFileVO();
        List<String> fileList = new ArrayList<>();
        List<String> dirFileList = new ArrayList<>();

        // 创建File对象表示当前目录
        File folder = new File(dirPath);

        // 获取当前目录下所有文件和文件夹
        File[] listOfFiles = folder.listFiles();

        // 检查文件数组是否为空（即确保目录存在且不为空）
        if (listOfFiles != null) {
            // 遍历文件数组
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    fileList.add(file.getName());
                    log.info("call getDirAllFile in fileName: " + file.getName());
                } else if (file.isDirectory()) {
                    dirFileList.add(file.getName());
                    log.info("call getDirAllFile in dirFileName: " + file.getName());
                }
            }
        }
        dirAllFileVO.setFileList(fileList);
        dirAllFileVO.setDirFileList(dirFileList);
        return dirAllFileVO;
    }

    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 验证文件是否存在，如果不存在则抛出异常
     *
     * @param filePath
     * @throws IOException
     */
    public static void isFileExistThrowException(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }
    }

    public static BufferedReader newBufferedReader(File file, Charset charset) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
    }

    public static BufferedWriter newBufferedWriter(File file, Charset charset) throws FileNotFoundException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
    }

    public static boolean createParentDirs(File file) throws IOException {
        File parent = file.getCanonicalFile().getParentFile();
        if (parent == null) {
            return false;
        }
        return parent.mkdirs();
    }

    public static boolean createNotExistParentDirFile(File file) throws IOException {
        boolean createParentDirsRes = createParentDirs(file);
        if (!createParentDirsRes) {
            throw new IOException("cannot create parent Directory of " + file.getName());
        }
        return file.createNewFile();
    }

    /**
     * 获取文件的MD5
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String getMD5Checksum(String filePath) throws Exception {
        InputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        byte[] b = complete.digest();

        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    /**
     * 获取文件的MD5
     * @param ism
     * @return
     * @throws Exception
     */
    public static String getMD5ChecksumByStream(InputStream ism) throws Exception {
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = ism.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        ism.close();
        byte[] b = complete.digest();

        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    /**
     * file 转MultipartFile
     * @param file
     * @return
     * @throws IOException
     */
    public static MultipartFile fileConvertMultipartFile(File file) throws IOException {
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", input);
        return multipartFile;
    }

    /**
     * file 字节数组 转MultipartFile
     * @param arr
     * @return
     * @throws IOException
     */
    public static MultipartFile fileByteConvertMultipartFile(byte[] arr) throws IOException {
        String fileName = UUIDUtils.randomNumber12()+".png";
        // 创建一个MockMultipartFile对象
        MultipartFile multipartFile = new MockMultipartFile("file", fileName, "text/plain", arr);
        return multipartFile;
    }

    public static void main(String[] args) {
        try {
            System.out.println(getDirAllFile("D:\\003_项目\\sxjypt\\模板\\解压测试"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.example.cugerhuo.tools;

/**
 * 获取从文件路径中文件名
 * @author 施立豪
 * @time 2023/4/9
 */
public class GetFileNameUtil {
    /**
     * 得到文件名
     * @param fileAbsolutePath 文件路径
      * @return   文件名
     */
    public static String getFileName(String fileAbsolutePath)
    {
        String filePath = fileAbsolutePath;
        int lastIndex = filePath.lastIndexOf("/");
        String fileName = filePath.substring(lastIndex
                + 1, filePath.length());
        return fileName;
    }
}

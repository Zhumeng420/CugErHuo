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

    public static String[] getUrls(String []oriurl)
    {
        return null;
    }

    /**
     * 得到分类
     * @param oricate 原始多个分类字符串|隔开
     * @return 第一个分类
     */
    public static String getCate(String oricate)
    { String filePath = oricate;
        int lastIndex = filePath.indexOf("|");
        if(lastIndex==-1)
        {
            return oricate;
        }
        String fileName = filePath.substring(0,lastIndex);
        return fileName;

    }
}

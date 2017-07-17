package com.wlwx.back.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作工具类
 * @author zjj
 * @date 2017年7月17日 下午2:25:53
 */
public class FileUtil {

	public static void main(String[] args) throws Exception {

		String filePath = "E://home//zhengjj";
		List<String> filenames = new ArrayList<>();
		filenames.add("911a83df6d94417ab160b1038c5fa393");
		filenames.add("95cf8c00893146038229cccc42fd3ace");
		getFiles(filePath,filenames);
	}

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
	
	/**
	 * 根据文件夹名称删除文件夹及子目录所有文件
	 * @date 2017年7月17日 下午3:06:12
	 * @param filePath
	 * @param fileNames
	 */
	public static void getFiles(String filePath,List<String> fileNames) {
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				if (fileNames.contains(file.getName())) {
					deleteDir(file);
					continue;
				}
				getFiles(file.getAbsolutePath(),fileNames);
			}
		}
	}
}

package com.uspacex.megatron.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.uspacex.megatron.model.FileModel;
import com.uspacex.megatron.util.FileUtils;
import com.uspacex.megatron.util.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileService {
    /**
     * 递归列出所有文件
     *
     * @param path     目录路径
     * @param fileList 目录下所有文件的列表
     * @param types    支持的文件类型，基于扩展名数组判断
     */
    public void listFiles(String path, List<FileModel> fileList, String[] types) {
        File f = new File(path);
        File[] l = f.listFiles();
        for (int i = 0; i < l.length; i++) {
            FileModel fileModel = new FileModel();
            if (l[i].isFile()) {
                if (ArrayUtils.contains(types, FileUtils.getFileType(l[i].getName()))) {
                    fileModel.setName(l[i].getName());
                    fileModel.setFingerprint(Md5Util.md5(l[i].getAbsolutePath()));
                    fileModel.setPath(l[i].getAbsolutePath());
                    fileModel.setType(FileUtils.getFileType(l[i].getName()));
                    fileList.add(fileModel);
                }
            } else if (l[i].isDirectory()) {
                listFiles(l[i].getAbsolutePath(), fileList, types);
            }
        }
    }

    /**
     * 生成文件树
     *
     * @param path      目录路径
     * @param fileModel 文件模型
     * @param types     支持的文件类型，基于扩展名数组判断
     * @param depth     递归深度
     */
    public void fileTree(String path, FileModel fileModel, String[] types, int depth) {
        File f = new File(path);
        if (f.isFile()) {
            log.warn(path + " is file.");
            return;
        }
        fileModel.setName(f.getName());
        fileModel.setPath(f.getAbsolutePath());
        fileModel.setDir(true);
        fileModel.setType("dir");
        fileModel.setFingerprint(Md5Util.md5(f.getAbsolutePath()));
        if ((depth--) == 0) {
            return;
        }
        List<FileModel> fileList = new ArrayList<>();
        fileModel.setFileList(fileList);
        File[] l = f.listFiles();
        for (int i = 0; i < l.length; i++) {
            FileModel fileModel1 = new FileModel();
            if (l[i].isFile()) {
                if (ArrayUtils.contains(types, FileUtils.getFileType(l[i].getName()))) {
                    fileList.add(fileModel1);
                    fileModel1.setDir(false);
                    fileModel1.setName(l[i].getName());
                    fileModel1.setFingerprint(Md5Util.md5(l[i].getAbsolutePath()));
                    fileModel1.setPath(l[i].getAbsolutePath());
                    fileModel1.setType(FileUtils.getFileType(l[i].getName()));
                }
            } else if (l[i].isDirectory()) {
                fileList.add(fileModel1);
                fileTree(l[i].getAbsolutePath(), fileModel1, types, depth);
            }
        }

    }
}

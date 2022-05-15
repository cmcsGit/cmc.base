package util;

import entity.BaseEntity;

import java.io.FileWriter;

/**
 * @author cmc
 */
public interface StatementUtil <T extends BaseEntity>{

    /**
     * 创建sql语句
     * @param writer 文件输出信息
     * @param entity 实体信息
     */
    void createStatement(FileWriter writer, T entity);
}

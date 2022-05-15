package util.impl;

import entity.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.StatementUtil;

import javax.persistence.Entity;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author cmc
 */
public class UpdateSqlStatement<T extends BaseEntity> implements StatementUtil<T> {

    private Logger logger = LoggerFactory.getLogger(InsertSqlStatement.class.getName());

    @Override
    public void createStatement(FileWriter writer, T entity) {
        String tableName = getTableName(entity);
        Map<String,Method> methodMap = ObjUtil.classToMethodMap(entity.getClass(),ObjUtil.GET);
        Map<String,String> columnMap = ObjUtil.GetColumnNameMap(entity.getClass());

        String statement = "update " + tableName;
        String value = "";
        int count = 0;
        for(String key: columnMap.keySet()){
            try {
                if(methodMap.get(key).invoke(entity) != null) {
                    if(count++ == 0) {
                        value += " set ";
                    }
                    else {
                        value += ",";
                    }
                    value += columnMap.get(key) + "=" +methodMap.get(key).invoke(entity);
                }
            } catch (Exception e) {
                logger.error("执行方法get" + ObjUtil.firstUpper(key) + "失败," + e.getMessage());
            }
        }
        try {
            if(!"".equals(value))
                writer.write(statement + value + "\n");
        } catch (IOException e) {
            logger.error("导出表" + tableName + "更新语句失败·····" + e.getMessage());
        }
    }

    private String getTableName(T entity) {
        Entity ent = entity.getClass().getAnnotation(Entity.class);
        if (ent != null) {
            return ent.name();
        } else {
            logger.error("获取类" + entity.getClass().getSimpleName() + "对应的表失败");
            return null;
        }
    }
}

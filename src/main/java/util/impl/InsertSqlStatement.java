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
public class InsertSqlStatement<T extends BaseEntity> implements StatementUtil<T> {

    private Logger logger = LoggerFactory.getLogger(InsertSqlStatement.class.getName());

    @Override
    public void createStatement(FileWriter writer, T entity) {
        String tableName = getTableName(entity);
        Map<String,Method> methodMap = ObjUtil.classToMethodMap(entity.getClass(),ObjUtil.GET);
        Map<String,String> columnMap = ObjUtil.GetColumnNameMap(entity.getClass());

        String frontState = "insert into " + tableName;
        StringBuilder statement = new StringBuilder(" ");
        StringBuilder value = new StringBuilder(" values");
        int count = 0;
        for(String key: columnMap.keySet()){
            if(count++ == 0) {
                statement.append("(");
                value.append("(");
            }
            else {
                statement.append(",");
                value.append(",");
            }
            statement.append(columnMap.get(key));
            try {
                value.append(methodMap.get(key).invoke(entity));
            } catch (Exception e) {
                logger.error("执行方法get" + ObjUtil.firstUpper(key) + "失败," + e.getMessage());
            }
        }
        if(statement.length() > 1) {
            statement.append(")");
            value.append(")");
        }
        try {
            writer.write(frontState + statement + value + "\n");
        } catch (IOException e) {
            logger.error("导出表" + tableName + "插入语句失败·····" + e.getMessage());
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

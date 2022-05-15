package util.impl;

import entity.BaseEntity;
import util.StatementUtil;

import java.io.FileWriter;

/**
 * @author cmc
 */
public class StatementFactory<T extends BaseEntity>{

    private FileWriter writer;

    private StatementUtil staUtil;

    public StatementFactory(FileWriter writer){
        this.writer = writer;
    }

    public Object create() {
        return null;
    }

    public Object create(String name) {
        return null;
    }

    public void create(String name,T entity) {
        switch (name){
            case "insert": staUtil = new InsertSqlStatement();break;
            default:staUtil = new UpdateSqlStatement();break;
        }

        staUtil.createStatement(writer,entity);
    }

    public Object create(Class cs) {
        return null;
    }
}

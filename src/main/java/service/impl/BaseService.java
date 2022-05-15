package service.impl;

import entity.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import response.FinalResponse;
import util.impl.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

/**
 * @author cmc
 */
public class BaseService<T extends BaseEntity>{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    EntityManagerUtil emu = new EntityManagerUtil();

    public <E extends BaseEntity> void batchUpdate(EntityManager em, List<E> lists, Boolean[] exists, FileWriter writer){
        emu.batchUpdate(em,lists,exists,writer);
    }

    public <E extends BaseEntity> void batchUpdate(EntityManager em, List<E> lists) {
        emu.batchUpdate(em, lists);
    }

    public <E extends BaseEntity> void batchInsert(EntityManager em, List<E> lists) {
        emu.batchInsert(em, lists);
    }

    public <E extends BaseEntity> void singleInsert(EntityManager em, E entity){
        emu.singleInsert(em, entity);
    }

    public <E extends BaseEntity> void singleUpdate(EntityManager em, E entity){
        emu.singleUpdate(em, entity);
    }

    public FinalResponse delete(Integer id){
        return new FinalResponse();
    }

    public FinalResponse save(T entity){
        return new FinalResponse();
    }

    public FinalResponse get(Integer id){
        return new FinalResponse();
    }

    public void logInfo(String... messages){
        logger.info("!-----------信息提示----------!");
        Arrays.asList(messages).forEach((message)->logger.info("!---" + message));
        logger.info("!-----------信息结束----------!");
    }

    public void logError(String... messages){
        logger.error("!-----------信息提示----------!");
        Arrays.asList(messages).forEach((message)->logger.error("!---" + message));
        logger.error("!-----------信息结束----------!");
    }

    public void logWarn(String messages){
        logger.warn("!-----------信息提示----------!");
        Arrays.asList(messages).forEach((message)->logger.warn("!---" + message));
        logger.warn("!-----------信息结束----------!");
    }
}

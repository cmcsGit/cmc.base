package util.impl;

import entity.BaseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.FileWriter;
import java.util.List;

/*
 * @author by cmc
 * @date 2019/3/20 19:58
 */
public class EntityManagerUtil {

    @Transactional(rollbackFor = Exception.class)
    public <T extends BaseEntity> void batchUpdate(EntityManager em, List<T> lists, Boolean[] exists, FileWriter writer) {
        int length = lists.size();
        StatementFactory<T> factory = new StatementFactory<>(writer);

        for(int i=0;i<length;i++){
            if(exists[i]){
                em.merge(lists.get(i));
                factory.create("update", lists.get(i));
            }
            else {
                em.persist(lists.get(i));
                factory.create("insert", lists.get(i));
            }
            if(i%30 == 0){
                em.flush();
                em.clear();
            }
        }
        em.flush();
        em.clear();
    }

    @Transactional(rollbackFor = Exception.class)
    public <T extends BaseEntity> void batchUpdate(EntityManager em, List<T> lists) {
        int length = lists.size();

        for(int i=0;i<length;i++){
            em.merge(lists.get(i));
            if(i%30 == 0){
                em.flush();
                em.clear();
            }
        }
        em.flush();
        em.clear();
    }

    @Transactional(rollbackFor = Exception.class)
    public <T extends BaseEntity> void batchInsert(EntityManager em, List<T> lists) {
        int length = lists.size();
        for(int i=0;i<length;i++){
            em.persist(lists.get(i));
            if(i%30 == 0){
                em.flush();
                em.clear();
            }
        }
        em.flush();
        em.clear();
    }

    @Transactional(rollbackFor = Exception.class)
    public <T extends BaseEntity> void singleInsert(EntityManager em, T entity){
        em.persist(entity);
        em.flush();
        em.clear();
    }

    @Transactional(rollbackFor = Exception.class)
    public <T extends BaseEntity> void singleUpdate(EntityManager em, T entity){
        em.merge(entity);
        em.flush();
        em.clear();
    }
}

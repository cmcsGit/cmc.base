package service;

import entity.BaseEntity;
import response.FinalResponse;

import java.io.FileWriter;
import java.util.List;

/**
 * @author cmc
 */
public interface IBaseService<T extends BaseEntity> {

    void batchUpdate(List<T> lists, Boolean[] exists, FileWriter writer);

    void batchUpdate(List<T> lists);

    void batchInsert(List<T> lists);

    void singleInsert(T entity);

    void singleUpdate(T entity);

    FinalResponse delete(Integer id);

    FinalResponse save(T entity);

    FinalResponse get(Integer id);
}

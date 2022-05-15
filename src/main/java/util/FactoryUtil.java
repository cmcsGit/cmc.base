package util;

import entity.BaseEntity;

/**
 * @author cmc
 */
public interface FactoryUtil <T extends BaseEntity>{

    /**
     * 创建新实例
     * @return
     */
    Object create();

    /**
     * 根据名称创建新实例
     * @param name
     * @return
     */
    Object create(String name);

    /**
     * 根据实体创建实例
     * @param cs
     * @return
     */
    Object create(Class cs);

    /**
     * 根据
     * @param name
     * @param entity
     * @return
     */
    Object create(String name, T entity);
}

package com.luolei.template.module.dynamic.repository;

import com.luolei.template.module.dynamic.domain.MetaObject;
import com.luolei.template.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 罗雷
 * @date 2018/5/8 0008
 * @time 16:07
 */
public interface MetaObjectRepository extends BaseRepository<MetaObject, Long> {

    /**
     * 根据对象名查找
     * @param objectName
     * @return
     */
    List<MetaObject> findAllByObjectName(String objectName);

    /**
     * 根据自然名查找
     * @param objectNaturalName
     * @return
     */
    List<MetaObject> findAllByObjectNaturalName(String objectNaturalName);

    /**
     * 根据租户查找
     * @param id
     * @return
     */
    @Query("FROM MetaObject o where o.tenant.id = :id")
    List<MetaObject> findAllByTenant(@Param("id") Long id);
}

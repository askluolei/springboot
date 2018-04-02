package com.luolei.template.config.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luolei.template.domain.AbstractAuditingEntity;
import com.luolei.template.domain.EntityAuditEvent;
import com.luolei.template.repository.EntityAuditEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 异步实体审计写入
 *
 * @author luolei
 * @createTime 2018-03-28 23:03
 */
@Slf4j
@Component
public class AsyncEntityAuditEventWriter {

    private final EntityAuditEventRepository auditingEntityRepository;

    /**
     * FIXME 这里使用公用的 还是 单独new 一个
     */
    private final ObjectMapper objectMapper; //Jackson object mapper

    public AsyncEntityAuditEventWriter(EntityAuditEventRepository auditingEntityRepository, ObjectMapper objectMapper) {
        this.auditingEntityRepository = auditingEntityRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * 异步写入实体审计到数据库
     */
    @Async
    public void writeAuditEvent(AbstractAuditingEntity target, EntityAuditAction action) {
        log.debug("-------------- Post {} audit  --------------", action.name());
        try {
            EntityAuditEvent auditedEntity = prepareAuditEntity(target, action);
            if (auditedEntity != null) {
                auditingEntityRepository.save(auditedEntity);
            }
        } catch (Exception e) {
            log.error("Exception while persisting audit entity for {} error: {}", target, e);
        }
    }

    /**
     * 创建实体
     *
     * @param entity
     * @param action
     * @return
     */
    private EntityAuditEvent prepareAuditEntity(final AbstractAuditingEntity entity, EntityAuditAction action) {
        EntityAuditEvent auditedEntity = new EntityAuditEvent();
        Class<?> entityClass = entity.getClass(); // Retrieve entity class with reflection
        auditedEntity.setAction(action.name());
        auditedEntity.setEntityType(entityClass.getName());
        Long entityId;
        String entityData;
        log.trace("Getting Entity Id and Content");
        try {
            entityId = entity.getId();
            //FIXME 需要将关联关系序列化吗？
            entityData = objectMapper.writeValueAsString(entity);
        } catch (IOException e) {
            log.error("Exception while getting entity ID and content {}", e);
            return null;
        }
        auditedEntity.setEntityId(entityId);
        auditedEntity.setEntityValue(entityData);
        if (EntityAuditAction.CREATE.equals(action)) {
            auditedEntity.setModifiedBy(entity.getCreatedBy());
            auditedEntity.setModifiedDate(entity.getCreatedDate());
            auditedEntity.setCommitVersion(1);
        } else {
            auditedEntity.setModifiedBy(entity.getLastModifiedBy());
            auditedEntity.setModifiedDate(entity.getLastModifiedDate());
            calculateVersion(auditedEntity);
        }
        log.trace("Audit Entity --> {} ", auditedEntity.toString());
        return auditedEntity;
    }

    /**
     * 计算 version
     * @param auditedEntity
     */
    private void calculateVersion(EntityAuditEvent auditedEntity) {
        log.trace("Version calculation. for update/remove");
        Integer lastCommitVersion = auditingEntityRepository.findMaxCommitVersion(auditedEntity
                .getEntityType(), auditedEntity.getEntityId());
        log.trace("Last commit version of entity => {}", lastCommitVersion);
        if(lastCommitVersion!=null && lastCommitVersion != 0){
            log.trace("Present. Adding version..");
            auditedEntity.setCommitVersion(lastCommitVersion + 1);
        } else {
            log.trace("No entities.. Adding new version 1");
            auditedEntity.setCommitVersion(1);
        }
    }
}

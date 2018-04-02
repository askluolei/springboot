package com.luolei.template.config.audit;

import com.luolei.template.domain.AbstractAuditingEntity;
import com.luolei.template.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

/**
 * 事件监听
 * 在插入，更新，删除之前做审计记录
 *
 * @author luolei
 * @createTime 2018-03-28 23:21
 */
@Slf4j
public class EntityAuditEventListener {

    @PostPersist
    public void onPostCreate(AbstractAuditingEntity target) {
        try {
            AsyncEntityAuditEventWriter asyncEntityAuditEventWriter = SpringContextUtils.getBean(AsyncEntityAuditEventWriter.class);
            asyncEntityAuditEventWriter.writeAuditEvent(target, EntityAuditAction.CREATE);
        } catch (NoSuchBeanDefinitionException e) {
            log.error("No bean found for AsyncEntityAuditEventWriter");
        } catch (Exception e) {
            log.error("Exception while persisting create audit entity {}", e);
        }
    }

    @PostUpdate
    public void onPostUpdate(AbstractAuditingEntity target) {
        try {
            AsyncEntityAuditEventWriter asyncEntityAuditEventWriter = SpringContextUtils.getBean(AsyncEntityAuditEventWriter.class);
            asyncEntityAuditEventWriter.writeAuditEvent(target, EntityAuditAction.UPDATE);
        } catch (NoSuchBeanDefinitionException e) {
            log.error("No bean found for AsyncEntityAuditEventWriter");
        } catch (Exception e) {
            log.error("Exception while persisting update audit entity {}", e);
        }
    }

    @PostRemove
    public void onPostRemove(AbstractAuditingEntity target) {
        try {
            AsyncEntityAuditEventWriter asyncEntityAuditEventWriter = SpringContextUtils.getBean(AsyncEntityAuditEventWriter.class);
            asyncEntityAuditEventWriter.writeAuditEvent(target, EntityAuditAction.DELETE);
        } catch (NoSuchBeanDefinitionException e) {
            log.error("No bean found for AsyncEntityAuditEventWriter");
        } catch (Exception e) {
            log.error("Exception while persisting delete audit entity {}", e);
        }
    }
}

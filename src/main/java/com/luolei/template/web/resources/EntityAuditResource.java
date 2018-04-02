package com.luolei.template.web.resources;

import com.luolei.template.domain.EntityAuditEvent;
import com.luolei.template.repository.EntityAuditEventRepository;
import com.luolei.template.support.R;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实体审计controller
 *
 * @author luolei
 * @createTime 2018-03-28 23:30
 */
@Slf4j
@RestController
@RequestMapping(path = {"/audits/entity"}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@Timed(value = "entityAuditResource")
public class EntityAuditResource {

    private final EntityAuditEventRepository entityAuditEventRepository;

    public EntityAuditResource(EntityAuditEventRepository entityAuditEventRepository) {
        this.entityAuditEventRepository = entityAuditEventRepository;
    }

    /**
     * 审计的实体类型，根据数据来的
     * @return
     */
    @GetMapping("/types")
    public R getAuditedEntities() {
        return R.ok(entityAuditEventRepository.findAllEntityTypes());
    }

    /**
     * 查询实体改变
     * @param entityType
     * @param limit
     * @return
     */
    @GetMapping("/changes")
    public R getChanges(@RequestParam(value = "entityType") String entityType, @RequestParam(value = "limit") int limit) {
        log.debug("REST request to get a page of EntityAuditEvents");
        Pageable pageRequest = PageRequest.of(0, limit);
        Page<EntityAuditEvent> page = entityAuditEventRepository.findAllByEntityType(entityType, pageRequest);
        return R.ok(page);
    }

    /**
     * 查询指定实体的前一个版本的情况
     * @param qualifiedName
     * @param entityId
     * @param commitVersion
     * @return
     */
    @GetMapping("/previous")
    public R getPrevVersion(@RequestParam(value = "qualifiedName") String qualifiedName,
                            @RequestParam(value = "entityId") Long entityId,
                            @RequestParam(value = "commitVersion") Integer commitVersion) {
        EntityAuditEvent prev = entityAuditEventRepository.findOneByEntityTypeAndEntityIdAndCommitVersion(qualifiedName, entityId, commitVersion);
        return R.ok(prev);
    }
}

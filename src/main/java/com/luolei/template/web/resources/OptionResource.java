package com.luolei.template.web.resources;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Preconditions;
import com.luolei.template.domain.Option;
import com.luolei.template.repository.OptionRepository;
import com.luolei.template.security.support.HasRole;
import com.luolei.template.support.Constants;
import com.luolei.template.support.R;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author luolei
 * @createTime 2018-04-14 23:44
 */
@HasRole({ Constants.ROLE_ADMIN })
@RestController
@RequestMapping(path = {"/api/option"}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class OptionResource {

    private final OptionRepository optionRepository;

    public OptionResource(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    /**
     * 新增配置
     * @param option
     * @return
     */
    @PostMapping
    public R createOption(@RequestBody Option option) {
        Preconditions.checkArgument(StrUtil.isNotBlank(option.getKey()), "key 为空");
        Preconditions.checkArgument(option.getKey().length() < 128, "key 的长度小于 128");
        Preconditions.checkArgument(StrUtil.isNotBlank(option.getKey()), "value 为空");
        Preconditions.checkArgument(!optionRepository.findOneByKey(option.getKey()).isPresent(), "该key已经存在");
        option = optionRepository.save(option);
        return R.ok(option);
    }

    /**
     * 主键删除配置
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public R deleteOption(@PathVariable(name = "id") Long id) {
        Preconditions.checkArgument(Objects.nonNull(id), "id 为null");
        optionRepository.deleteById(id);
        return R.ok();
    }

    /**
     * 修改配置
     * @param option
     * @return
     */
    @PutMapping
    public R modifyOption(@RequestBody Option option) {
        Option local = null;
        if (Objects.nonNull(option.getId())) {
            local = optionRepository.findById(option.getId()).orElseThrow(() -> new IllegalArgumentException("id:" + option.getId() + " 不存在"));
        }
        if (StrUtil.isNotBlank(option.getKey())) {
            local = optionRepository.findOneByKey(option.getKey()).orElseThrow(() -> new IllegalArgumentException("key:" + option.getKey() + " 不存在"));
        }
        if (StrUtil.isNotBlank(option.getValue())) {
            local.setValue(option.getValue());
        }
        if (StrUtil.isNotBlank(option.getExplanation())) {
            local.setExplanation(option.getExplanation());
        }
        local = optionRepository.save(local);
        return R.ok(local);
    }

    /**
     * 查询
     * @param id
     * @param key
     * @param pageable
     * @return
     */
    @GetMapping
    public R findOptions(@RequestParam(required = false) Long id, @RequestParam(required = false) String key, Pageable pageable) {
        if (Objects.nonNull(id)) {
            return R.ok(optionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id:" + id + " 不存在")));
        }
        if (StrUtil.isNotBlank(key)) {
            return R.ok(optionRepository.findOneByKey(key).orElseThrow(() -> new IllegalArgumentException("key:" + key + " 不存在")));
        }
        Page<Option> page = optionRepository.findAll(pageable);
        return R.ok(page);
    }
}

package com.luolei.template.domain;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author luolei
 * @createTime 2018-04-14 22:33
 */
@Slf4j
@Getter
@Setter
@Entity
@Table(name = "t_option")
public class Option extends AbstractAuditingEntity {

    private static final byte[] AES_KEY = "Handsome guy1111".getBytes();
    private static final byte[] SECURITY_KEY = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), AES_KEY).getEncoded();

    @Column(name = "c_key", nullable = false, unique = true, length = 32)
    private String key;

    @Column(name = "c_value", nullable = false)
    private String value;

    @Column(name = "c_explanation")
    private String explanation;

    @Column(name = "c_encrypted")
    private Boolean encrypted = false;

    // 如果加解密就使用下面的两个方法
    @JsonIgnore
    public String getEncryptedValue() {
        if (StrUtil.isNotBlank(value) && Objects.nonNull(encrypted) && encrypted) {
            try {
                return SecureUtil.aes(SECURITY_KEY).decryptStrFromBase64(value);
            } catch (Exception e) {
                // 防御性容错，为了防止设置了 encrypted = true，但是直接调用了setValue
                log.warn("解密失败，返回远字符串", e);
                return this.value;
            }
        }
        return this.value;
    }

    /**
     * 注意，要先 setEncrypted
     * @param value
     */
    public void setEncryptedValue(String value) {
        if (StrUtil.isNotBlank(value) && Objects.nonNull(encrypted) && encrypted) {
            this.value = SecureUtil.aes(SECURITY_KEY).encryptBase64(value);
        } else {
            this.value = value;
        }
    }
}

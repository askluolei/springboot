package com.luolei.template;

/**
 * 测试的时候发现的一个问题
 * 由于在开发模式（dev profile）下，数据库里面加入了一些数据
 * 在测试的时候有激活了 dev profile
 * 导致之前的单元测试部分失败了
 *
 * 为了单元测试的独立性，不要在单元测试的时候引入其他profile
 *
 * @author luolei
 * @createTime 2018-04-02 21:19
 */
public class ApplicationTest {
}

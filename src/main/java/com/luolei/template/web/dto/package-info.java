/**
 * DTO 数据传输对象
 * 用来接收请求数据的
 * 通过处理，转换成领域对象（业务没有这么复杂，压根没领域对象的概念，就是数据库实体），使用领域对象做业务
 * 然后返回 VO 对象（但是如果不是很复杂，直接返回 DTO 对象也行）
 * 譬如，在数据库保存的性别里面0是男，在页面上要显示男，实际上这个操作在前端也可以做，
 * 当然，如果这样的字段多了，直接在后台处理了，封装成功 VO 对象返回也行
 *
 * 总而言之，简单应用直接跳过 VO 使用 DTO
 * 为啥不使用数据库实体？因为使用 JPA ，实体上已经很多注解了，要是再加上校验注解，那就乱了
 *
 * @author luolei
 * @createTime 2018-04-02 23:20
 */
package com.luolei.template.web.dto;
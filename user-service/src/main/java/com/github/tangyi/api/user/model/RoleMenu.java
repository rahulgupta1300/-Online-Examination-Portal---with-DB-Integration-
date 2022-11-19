package com.github.tangyi.api.user.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 角色菜单关联
 *
 * @author tangyi
 * @date 2018/8/26 22:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_role_menu")
public class RoleMenu extends BaseEntity<RoleMenu> {

	@Column(name = "role_id")
	@JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

	@Column(name = "menu_id")
	@JsonSerialize(using = ToStringSerializer.class)
    private Long menuId;
}

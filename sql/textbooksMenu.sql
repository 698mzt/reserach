-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教材软著', '2039', '1', '/system/textbooks', 'C', '0', 'system:textbooks:view', '#', 'admin', sysdate(), '', null, '教材软著菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教材软著查询', @parentId, '1',  '#',  'F', '0', 'system:textbooks:list',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教材软著新增', @parentId, '2',  '#',  'F', '0', 'system:textbooks:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教材软著修改', @parentId, '3',  '#',  'F', '0', 'system:textbooks:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教材软著删除', @parentId, '4',  '#',  'F', '0', 'system:textbooks:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教材软著导出', @parentId, '5',  '#',  'F', '0', 'system:textbooks:export',       '#', 'admin', sysdate(), '', null, '');

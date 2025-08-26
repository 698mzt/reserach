-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教材软著', '3', '1', '/system/jiaocairuanzhu', 'C', '0', 'system:jiaocairuanzhu:view', '#', 'admin', sysdate(), '', null, '教材软著菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教材软著查询', @parentId, '1',  '#',  'F', '0', 'system:jiaocairuanzhu:list',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教材软著新增', @parentId, '2',  '#',  'F', '0', 'system:jiaocairuanzhu:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教材软著修改', @parentId, '3',  '#',  'F', '0', 'system:jiaocairuanzhu:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教材软著删除', @parentId, '4',  '#',  'F', '0', 'system:jiaocairuanzhu:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教材软著导出', @parentId, '5',  '#',  'F', '0', 'system:jiaocairuanzhu:export',       '#', 'admin', sysdate(), '', null, '');

-- 添加数据同步定时任务（每分钟执行一次）
-- 后续可修改为每天晚上12点执行：0 0 0 * * ?
INSERT INTO `sys_job` VALUES (4, '数据同步任务', 'SYSTEM', 'dataSynchronizeTask.synchronizeAll()', '0 * * * * ?', '3', '1', '0', 'admin', sysdate(), '', null, '数据同步任务，每分钟执行一次');

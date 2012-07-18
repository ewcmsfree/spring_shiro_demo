insert into acct_user(id, e_mail, login_name, real_name, pass_word, status, salt) values(1,'','admin','管理员','6e7a1b75ef4d24a32d83c62a71dfad44606c342c',FALSE,'b10b376b8efb5339');

insert into acct_role(id, role_name, caption) values(1, 'ROLE_ADMIN', '管理员');

insert into acct_permission(id, caption, expression, name) values(1, '所有权限', '*', 'ALL');
insert into acct_permission(id, caption, expression, name) values(2, '查看用户', 'user:view', 'USER_VIEW');
insert into acct_permission(id, caption, expression, name) values(3, '修改用户', 'user:edit','USER_EDIT');
insert into acct_permission(id, caption, expression, name) values(4, '删除用户', 'user:delete', 'USER_DELETE');
insert into acct_permission(id, caption, expression, name) values(5, '查看角色', 'group:view', 'GROUP_VIEW');
insert into acct_permission(id, caption, expression, name) values(6, '修改角色', 'group:edit', 'GROUP_EDIT');
insert into acct_permission(id, caption, expression, name) values(7, '删除角色', 'group:delete', 'GROUP_DELETE');
insert into acct_permission(id, caption, expression, name) values(8, '查看权限', 'permission:view','PERMISSION_VIEW')

insert into acct_user_role(user_id, role_id) values(1, 1);

insert into acct_user_permission(user_id, permission_id) values(1, 1);

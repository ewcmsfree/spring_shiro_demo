<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>用户管理</title>	
		<%@ include file="../../taglibs.jsp" %>
		<script type="text/javascript">
			$(function(){
				<%@ include file="../../alertMessage.jsp" %>

				$('#tt').propertygrid({
			        width:500,
			        url:"<c:url value='/security/userdetail/query'/>?id=${id}",
			        showGroup:true,
			        scrollbarSize:0,
			        singleSelect:false,
			        frozenColumns:[[
						{field:"ck",checkbox:true}
					]],
			        columns:[[
			            {field:'name',title:'名称',width:150},
			            {field:"value",title:'描述',width:260}
			        ]]
				});
				$("#tb-add").click(function(){
					$.ewcms.add({
						title:"添加 - 角色/权限",
						width:525,
						height:310
					});
				});
	            $("#tb-remove").click(function(){
					var rows = $("#tt").propertygrid("getSelections");
					if (rows.length == 0){
						$.messager.alert("提示","请选择移除项","info");
						return;
					}
					var parameter = "id=${id}&isRemove=true";
					for (var i = 0; i < rows.length; i++){
						if (rows[i].group == "角色"){
							parameter = parameter + "&roleNames=" + rows[i].name;
						}else{
							parameter = parameter + "&permissionNames=" + rows[i].name;
						}
					}
					$.post("<c:url value='/security/userdetail/editRoleAndPermission'/>", parameter, function(data){
						if (data == "success"){
							$('#edit-window').window('close');
							$("#tt").propertygrid("reload");
							$.messager.alert("提示","移除成功","info");
						}
					});
	            });
	            $("#tb-query").click(function(){
	            	$.ewcms.query();
	            });
	            $('#permission-tt').datagrid({
	                width:500,
	                pageSize:5,
	                nowrap: false,
	                rownumbers:true,
	                idField:'name',
	                pagination:true,
	                pageList:[5],
	                url: "<c:url value='/security/permission/query'/>",
	                frozenColumns:[[
	                     {field:'ck',checkbox:true},
	                     {field:"name",title:'权限名称',width:200}
	                ]],
	                columns:[[
	                     {field:"caption",title:'说明',width:230}
	                ]]
	            });
	            
	            $("#role-tt").datagrid({
	                width:500,
	                idField:'roleName',
	                pageSize:5,
	                pagination:true,
	                nowrap: false,
	                rownumbers:true,
	                pageList:[5],
	                url: "<c:url value='/security/role/query'/>",
	                frozenColumns:[[
	                     {field:'ck',checkbox:true},
	                     {field:"roleName",title:'用户组名称',width:200}
	                ]],
	                columns:[[
	                     {field:"caption",title:'说明',width:230}
	                ]]
	            });
	        });
			function addRoleAndPermission(){
				var roleRows = $("#role-tt").propertygrid("getSelections");
				var permissionRows = $("#permission-tt").propertygrid("getSelections");
				var parameter = "id=${id}&isRemove=false";
				for (var i = 0; i < roleRows.length; i++) {
					parameter = parameter + "&roleNames=" + roleRows[i].roleName;
				}
				for (var i = 0; i < permissionRows.length; i++){
					parameter = parameter + "&permissionNames=" + permissionRows[i].name;
				}
				$.post("<c:url value='/security/userdetail/editRoleAndPermission'/>", parameter, function(data){
					if (data == "success"){
						$('#edit-window').window('close');
						$("#tt").propertygrid("reload");
						$.messager.alert("提示","添加成功","info");
					}
				});
			}
			function queryNews(selections){
				$.ewcms.query({
					selections:selections
				});
			}
		</script>		
	</head>
	<body class="easyui-layout">
		<div region="center" style="padding:2px;" border="false">
	 		 <table id="tt" toolbar="#tb" fit="true"></table>
             <div id="tb" style="padding:5px;height:auto;">
               <div class="toolbar" style="margin-bottom:2px">
               		<shiro:hasPermission name="user:edit">
						<a id="tb-add" href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="user:delete">
						<a id="tb-remove" href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true">移除</a>
					</shiro:hasPermission>
				</div>
                <div style="padding-left:5px;">
                   <form id="queryform"  style="padding: 0;margin: 0;">
                   		 名称：<input type="text" name="realName" style="width:80px"/>&nbsp;
                   		 邮箱：<input type="text" name="email" style="width:120px"/>&nbsp;
                    	<a href="#" id="tb-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                   </form>
               </div>
            </div>	
	 	</div>
        <div id="edit-window" icon="icon-winedit" closed="true" class="easyui-window" title="" style="display:none;">
            <div class="easyui-tabs" border="false" fit="true">
                <div title="用户组" style="padding: 5px;">
                    <table id="role-tt" toolbar="#role-tb" ></table>
                    <div id="role-tb" style="padding: 5px; height: auto; display: none;">
                        <div style="padding-left: 5px;">
                            <form id="role-queryform" style="padding: 0; margin: 0;">
				                                    名称: <input type="text" name="name" style="width: 100px" />&nbsp; 
				                                    描述: <input type="text" name="remark" style="width: 150px" />&nbsp;
                                <a href="#" id="role-toolbar-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                            </form>
                        </div>
                    </div>
                </div>
                <div title="权限" style="padding: 5px;">
                    <table id="permission-tt" toolbar="#auth-tb"></table>
                    <div id="auth-tb" style="padding: 5px; height: auto; display: none;">
                        <div style="padding-left: 5px;">
                            <form id="auth-queryform" style="padding: 0; margin: 0;">
				                                    名称: <input type="text" name="name" style="width: 100px" />&nbsp; 
				                                    描述: <input type="text" name="remark" style="width: 150px" />&nbsp;
                                <a href="#" id="auth-toolbar-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div style="width:511px;height:16px;line-height:28px;text-align:center;background-color:#f6f6f6;position:absolute;height:28px;line-height:28px;bottom:7px;">
            	<a class="easyui-linkbutton" icon="icon-save" href="javascript:void(0);" onclick="javascript:addRoleAndPermission();">提交</a>
            	<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);" onclick="javascript:$('#edit-window').window('close');">关闭</a>
            </div>
        </div>
        <input type="hidden" id="id" name="id" value="${id}"/>
	</body>
</html>
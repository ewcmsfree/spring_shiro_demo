<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>权限管理</title>	
		<%@ include file="../../taglibs.jsp" %>
		<script type="text/javascript">
			$(function(){
				<%@ include file="../../alertMessage.jsp" %>

				$('#tt').datagrid({
					nowrap:true,
				    pagination:true,
				    rownumbers:true,
				    singleSelect:false,
				    pageSize:20,
					frozenColumns:[[
					    {field:'ck',checkbox:true},
					    {field:'id',title:'序号',width:50,hidden:true}
					]],
					columns:[[
					    {field:'name',title:'名称',width:100},
					    {field:"caption",title:'描述',width:200}
					]],
			        url:"<c:url value='/security/roledetail/query'/>?id=${id}",
				});
				$("#tb-add").click(function(){
					$.ewcms.add({
						title:"添加 - 权限",
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
						parameter = parameter + "&permissionIds=" + rows[i].id;
					}
					$.post("<c:url value='/security/roledetail/editPermission'/>", parameter, function(data){
						if (data == "success"){
							$('#edit-window').window('close');
							$("#tt").propertygrid("reload");
							$.messager.alert("提示","移除成功","info");
						}else{
							$.messager.alert("提示", data, "info");
							return;
						}
					});
	            });
	            $("#tb-query").click(function(){
	            	$.ewcms.query();
	            });
	            $('#permission-tt').datagrid({
	                width:511,
	                pageSize:5,
	                nowrap: false,
	                rownumbers:true,
	                idField:'id',
	                pagination:true,
	                pageList:[5],
	                url: "<c:url value='/security/permission/query'/>",
	                frozenColumns:[[
	                     {field:'ck',checkbox:true},
	                     {field:'id',title:"编号",width:50,hidden:true}
	                ]],
	                columns:[[
						 {field:"name",title:'权限名称',width:200},
	                     {field:"caption",title:'说明',width:230}
	                ]]
	            });
	        });
			function addPermission(){
				var permissionRows = $("#permission-tt").propertygrid("getSelections");
				var parameter = "id=${id}&isRemove=false";
				for (var i = 0; i < permissionRows.length; i++){
					parameter = parameter + "&permissionIds=" + permissionRows[i].id;
				}
				$.post("<c:url value='/security/roledetail/editPermission'/>", parameter, function(data){
					if (data == "success"){
						$('#edit-window').window('close');
						$("#tt").propertygrid("reload");
						$.messager.alert("提示","添加成功","info");
					}else{
						$.messager.alert("提示", data, "info");
						return;
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
            <div style="width:511px;height:16px;line-height:28px;text-align:center;background-color:#f6f6f6;position:absolute;height:28px;line-height:28px;bottom:7px;">
            	<a class="easyui-linkbutton" icon="icon-save" href="javascript:void(0);" onclick="javascript:addPermission();">提交</a>
            	<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);" onclick="javascript:$('#edit-window').window('close');">关闭</a>
            </div>
        </div>
        <input type="hidden" id="id" name="id" value="${id}"/>
	</body>
</html>
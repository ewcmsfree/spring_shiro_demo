<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
    	<title>权限组编辑</title>
      	<%@ include file="../../taglibs.jsp"%>
      	<link href="<c:url value='/static/jquery-validation/1.9.0/validate.css'/>" type="text/css" rel="stylesheet" />
		<script src="<c:url value='/static/jquery-validation/1.9.0/jquery.validate.min.js'/>" type="text/javascript"></script>
		<script src="<c:url value='/static/jquery-validation/1.9.0/messages_cn.js'/>" type="text/javascript"></script>
		<script type="text/javascript">
			$(function(){
				<%@ include file="../../alertMessage.jsp" %>

				if ("${close}" == "true"){
					parent.$('#edit-window').window('close');
				}
				//聚焦第一个输入框
				$("#name").focus();
				//为inputForm注册validate函数
				$("#inputForm").validate({
					rules: {
						roleName: {
							remote: "<c:url value='/security/role/checkRoleName'/>?oldRoleName=" + encodeURIComponent('${role.roleName}')
						}
					},
					messages: {
						roleName: {
							remote: "权限组名已存在"
						}
					},
					errorPlacement: function(error, element) {
						error.insertAfter( element );
					}
				});
				var selections = [];
				$.each($('input[name="selections"]'),function(index,o){
					selections[index] = $(o).val();
				});
				parent.queryNews(selections);
			});
		</script>
		<style type="text/css">
			.title {
			  font-size: 110%;
			  font-family: 宋体, Tahoma, SimSun, sans-serif;
			  margin:6px 5px 8px 2px;
			  padding:2px 3px 0 8px;
			  border-bottom: 1px solid #667788;
			}
		</style>
	</head>
	<body>
		<div style="width:100%;height:100%;overflow:auto">
	    <h1 class="title">权限组编辑</h1>
		<form:form id="inputForm" action="save" method="post" modelAttribute="role" class="form-horizontal">
			<fieldset>
			<table class="formtable">
				<tr>
					<td>名称：</td>
					<td ><input type="text" id="roleName" name="roleName" size="30" value="${role.roleName}" class="required"/></td>
				</tr>
				<tr>
					<td>说明：</td>
					<td><input type="text" id="caption" name="caption" size="30" value="${role.caption}"/></td>
				</tr>
			</table>
			<input type="hidden" id="id" name="id" value="${role.id}"/>
			<c:forEach var="selection" items="${selections}">
				<input type="hidden" name="selections" value="${selection}" />
			</c:forEach>
			</fieldset>
		</form:form>
		</div>
	    <div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	    	<a class="easyui-linkbutton" icon="icon-save" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	        <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	    </div>
	</body>
</html>
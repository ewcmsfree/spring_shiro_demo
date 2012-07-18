<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>权限组管理</title>	
		<%@ include file="../../taglibs.jsp" %>
		<script type="text/javascript" src="<c:url value='/static/easyui/ext/datagrid-detailview.js'/>"></script>
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
			        {field:'roleName',title:'角色名称',width:150, sortable:true},
			        {field:'caption',title:'说明',width:400}
	            ]],
	            url:"<c:url value='/security/role/query'/>",
		        view : detailview,
		    	detailFormatter : function(rowIndex, rowData) {
		    		return '<div id="ddv-' + rowIndex + '"></div>';
		    	},
		    	onExpandRow: function(rowIndex, rowData){
		    		var content = '<iframe src="<c:url value="/security/roledetail/index"/>?id=' + rowData.id + '" frameborder="0" width="100%" height="350px" scrolling="auto"></iframe>';
		    			
		    		$('#ddv-' + rowIndex).panel({
		    			border : false,
		    			cache : false,
		    			content : content,
		    			onLoad : function(){
		    				$('#tt').datagrid('fixDetailRowHeight',rowIndex);
		    			}
	    			});
	    			$('#tt').datagrid('fixDetailRowHeight',rowIndex);
	    		}
			});
            $("#tb-add").click(function(){
				$.ewcms.add({
					title:'新增'
				});
			});
            $("#tb-edit").click(function(){
				$.ewcms.edit({
					title:'修改'
				});
			});
            $("#tb-remove").click(function(){
            	$.ewcms.remove({
            		title:'删除'
            	});
            });
            $("#tb-query").click(function(){
            	$.ewcms.query();
            });
		});
		
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
               		<shiro:hasPermission name="role:edit">
						<a id='tb-add' href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
						<a id='tb-edit' href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="role:delete">
						<a id='tb-remove' href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
					</shiro:hasPermission>
				</div>
                <div style="padding-left:5px;">
                   <form id="queryform"  style="padding: 0;margin: 0;">
                   		 名称：<input type="text" name="name" style="width:80px"/>&nbsp;
                   		 说明：<input type="text" name="caption" style="width:120px"/>&nbsp;
                    	<a href="#" id="tb-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                   </form>
               </div>
            </div>	
	 	</div>
        <div id="edit-window" class="easyui-window" closed="true" icon="icon-winedit" title="" style="display:none;">
            <div class="easyui-layout" fit="true">
                <div region="center" border="false">
                   <iframe id="editifr"  name="editifr" class="editifr" frameborder="0" scrolling="no"></iframe>
                </div>
            </div>
        </div>	
	</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <title>EWCMS 站群内容管理平台</title>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<meta http-equiv="Cache-Control" content="no-store" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />       
		<%@ include file="taglibs.jsp" %>
        <link rel="stylesheet" type="text/css" href="<c:url value='/static/views/home.css'/>"/>
        <script type="text/javascript" src="<c:url value='/static/views/home.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/static/fcf/js/FusionCharts.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/static/views/ewcms.pubsub.js'/>"></script>
        <script type="text/javascript">
            var _home = new home();
            $(function(){
            	var clock = new Clock();
            	clock.display(document.getElementById("clock"));
            });
        </script>
    </head>
    <body class="easyui-layout">
        <div region="north" split="true" class="head">
			<div id="top">
				<img src="<c:url value='/static/image/top_bg_ewcms.gif'/>" height="35px" border="0" style="border:0;padding-left:4px;padding-top:13px;"/> | 企业网站站群内容管理系统V2.0
			    <div id="toppiz">
			   	<div class="huanying">
			       <span style="font-size:13px;font-weight: bold;"><span id="user-name"><shiro:principal property="realName"/></span> 欢迎你</span> | <span id="clock"></span>
			   	</div>
			   <div style="float:right;margin-top:5px;margin-right:10px;">
		           <a id="button-main" href="#" style="border:0;padding:0;"><img src="<c:url value='/static/image/exit.png'/>" width="17" height="17" style="border:0;"/></a>
		       </div>
			   	<div class="anniu">
			   		<div class="bs">
						<a class="styleswitch a1" style="cursor: pointer" title="谈黄色" rel="sunny"></a>
						<a class="styleswitch a2" style="cursor: pointer" title="浅蓝色" rel="cupertino"></a>
						<a class="styleswitch a4" style="cursor: pointer" title="黑色" rel="dark-hive"></a>	
						<a class="styleswitch a5" style="cursor: pointer" title="灰色" rel="pepper-grinder"></a>		
					</div>
			   </div>
					<div style="float:right;padding-top:42px;margin-right:10px;">
                        <span id="tipMessage" style="color:red;font-size:13px;width:100px;"></span>
                    </div>
			 </div>
             <div id="mm" class="easyui-menu" style="width:120px;display:none;">
                <div  id="switch-menu" iconCls="icon-switch">站点切换</div>
                <div class="menu-sep"></div>
                <div id="user-menu" iconCls="icon-edit">修改用户信息</div>
                <div id="password-menu" iconCls="icon-password">修改密码</div>
                <div class="menu-sep"></div>
                <div id="progress-menu">发布进度</div>
                <div class="menu-sep"></div>
                <div id="exit-menu" iconCls="icon-exit">退出</div>
             </div>
             </div>
        </div>
        <div region="south" style="height:2px;background:#efefef;overflow:hidden;"></div>
        <div region="west" split="true" title="EWCMS平台菜单" style="width:180px;padding:1px;overflow:hidden;">
            <div id="mainmenu" class="easyui-accordion" fit="true" border="false">
                  <div title="权限管理" style="overflow:auto;">
                 	<shiro:hasPermission name="user:view">
                    <div class="nav-item">
                        <a href="javascript:$.ewcms.openTab({title:'用户管理',src:'security/user/index'})">
							<img src="<c:url value='/static/image/user.png'/>" style="border:0;"></img><br/>
							<span>用户管理</span>
						</a>
                    </div>
                    </shiro:hasPermission>	
                    <shiro:hasPermission name="group:view">
                    <div class="nav-item">
                        <a href="javascript:$.ewcms.openTab({title:'角色管理',src:'security/role/index'})">
							<img src="<c:url value='/static/image/group.png'/>" style="border:0;"></img><br/>
							<span>角色管理</span>
						</a>
                    </div>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="permission:view">
                    <div class="nav-item">
                        <a href="javascript:$.ewcms.openTab({title:'权限列表',src:'security/permission/index'})">
                            <img src="<c:url value='/static/image/role.png'/>" style="border:0;"/><br/>
                            <span>权限列表</span>
                        </a>
                   </div>
                   </shiro:hasPermission>
                    <div class="nav-item">
                        <a  href="javascript:_home.addTab('退出登录','logout')"> 
                             <img src="<c:url value='/static/image/scheduling_jobclass.png'/>" style="border: 0" /><br/>
                             <span>退出登录</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
        <div region="center" style="overflow:hidden;">
			<div class="easyui-tabs"  id="systemtab" fit="true" border="false">
				<div title="Tab1" style="padding:20px;overflow:hidden;"> 
					<div style="margin-top:20px;">
						<h3>jQuery EasyUI framework help you build your web page easily.</h3>
						<li>easyui is a collection of user-interface plugin based on jQuery.</li> 
						<li>using easyui you don't write many javascript code, instead you defines user-interface by writing some HTML markup.</li> 
						<li>easyui is very easy but powerful.</li> 
					</div>
				</div>
			</div>
		</div>
    </body>
</html>
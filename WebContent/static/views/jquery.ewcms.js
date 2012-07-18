/**
 * jQuery ewcms 1.0
 * 
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 * 
 * author wangwei [hhywangwei@gmail.com]
 */
(function($){
	$.fn.serializeObject = function() {
		  var arrayData, objectData;
		  arrayData = this.serializeArray();
		  objectData = {};

		  $.each(arrayData, function() {
		    var value;

		    if (this.value != null) {
		      value = this.value;
		    } else {
		      value = '';
		    }

		    if (objectData[this.name] != null) {
		      if (!objectData[this.name].push) {
		        objectData[this.name] = [objectData[this.name]];
		      }

		      objectData[this.name].push(value);
		    } else {
		      objectData[this.name] = value;
		    }
		  });
		  return objectData;
	};
	function hasElementFor(id){
		var ids = $.isArray(id) ? id : [id];
		$.each(ids,function(index,i){
			if($(i).length == 0){
				alert(i + '编号的元素不存在');
				return false;
			}
		});
		return true;
	};
	
	$.extend({
		ewcms:{
			openTab : function (options){
				var defaults = {
					id : '#systemtab',
					src : '#',
					refresh : false
				};
				var opts = $.extend({}, defaults, options); 
			    if(!opts.content){
			    	opts.content = '<iframe src="' + opts.src + '" width=100% height=100% frameborder=0/>'; 
			    }
			    if(!hasElementFor(opts.id)){
			    	return ;
			    }
			    var t = $(opts.id);
			    var title = opts.title;
			    if (!t.tabs('exists', title)) {
			        t.tabs('add', {
			            title : title,
			            content : opts.content,
			            closable : true
			        });
			        return;
			    }
			    t.tabs('select', title);
		    	if(opts.refresh){
		  	        t.tabs('update', {
		  	            tab : t.tabs("getTab", title),
		  	            options : {
		  	                content : opts.content
		  	            }
		  	        });
		    	}
			},
			openWindow : function(options){
				var defaults = {
						title : "新窗口",
						width : 800,
						height : 500,
						modal:true,
						maximizable:false,
						minimizable:true,
						onClose:function(windowId){
							$(windowId).find("iframe").attr('src','about:blank');
					    }
				};
				var opts = $.extend({}, defaults, options);  
				if(!hasElementFor(opts.windowId)){
						return;
				}
				$(opts.windowId).removeAttr("style");
				$(opts.windowId).window({
					   title: opts.title,
					   width: opts.width,
					   height: opts.height,
					   left:(opts.left ? opts.left : ($(window).width() - opts.width)/2),
					   top:(opts.top ? opts.top : ($(window).height() - opts.height)/2),
					   modal: opts.modal,
					   maximizable:opts.maximizable,
					   minimizable:opts.minimizable,
					   onClose:opts.onClose(opts.windowId)
				});
				if(opts.src){
					opts.iframeId ? 
							$(opts.iframeId).attr('src',opts.src) 
							:$(opts.windowId).find('iframe').attr('src',opts.src);
				}
				$(opts.windowId).window('open');
			},
			query : function(options) {
				var defaults = {
						datagridId : '#tt',
						formId : '#queryform',
						url: "query",
						selections : []
				};
				var opts = $.extend({}, defaults, options);
				if(!hasElementFor(opts.datagridId)){
					return ;
				}
				$(opts.datagridId).datagrid({
					onBeforeLoad:function(param){
						if(opts.selections.length > 0){
							$.each(opts.selections,function(i,v){
								param['selections[' + i + ']'] = v;
							});
						}else{
							param['parameters']=$(opts.formId).serializeObject();
						}
					}
				});
				$(opts.datagridId).datagrid('load');
			},
			save :function(options){
				var defaults = {
					iframeId : '#editifr'
				};
				var opts = $.extend({}, defaults, options);
				$(opts.iframeId).contents().find('form').submit();

			},
			add : function(options){
				var defaults = {
						src: "edit",
					    windowId:"#edit-window"
				};
				var opts = $.extend({}, defaults, options);
				if(!hasElementFor(opts.windowId)){
					return;
				}
				$.ewcms.openWindow(opts);	
			},
			edit:function(options){
				var defaults = {
						src: "edit",
						datagridId : '#tt',
						windowId:'#edit-window',
						getId : function(row){
							return row.id;
						}
				};
				var opts = $.extend({}, defaults, options);
				if(!hasElementFor([opts.datagridId,opts.windowId])){
					return;
				}
				
			    var rows = $(opts.datagridId).datagrid('getSelections');
			    if(rows.length == 0){
			        $.messager.alert('提示','请选择修改记录','info');
			        return;
			    }
			    
			    var src = (( opts.src.indexOf("?") == -1) ? opts.src + '?' : opts.src + '&');
			    $.each(rows,function(index,row){
			    	src += 'selections=' + opts.getId(row) +'&';
			    });
			    opts.src = src;
			    $.ewcms.openWindow(opts);
			},
			remove:function(options){
				var defaults = {
						src: "delete",
						datagridId : '#tt',
						windowId:'#edit-window',
						getId : function(row){
							return row.id;
						}
				};
				var opts = $.extend({}, defaults, options);
				if(!hasElementFor([opts.datagridId,opts.windowId])){
					return;
				}
				
			    var rows = $(opts.datagridId).datagrid('getSelections');
			    if(rows.length == 0){
			        $.messager.alert('提示','请选择删除记录','info');
			        return;
			    }
			    
			    var src = (( opts.src.indexOf("?") == -1) ? opts.src + '?' : opts.src + '&');
			    $.each(rows,function(index,row){
			    	src += 'selections=' + opts.getId(row) +'&';
			    });
			    opts.src = src;
			    $.messager.confirm("提示","确定要删除所选记录吗?",function(r){
			        if (r){
			            $.post(opts.src,{},function(data){
			            	$.messager.alert('成功','删除成功','info');
			            	$(opts.datagridId).datagrid('clearSelections');
			                $(opts.datagridId).datagrid('reload');              	
			            });
			        }
			    });
			}
		}
	});
})(jQuery);

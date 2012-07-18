/*!
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

var login = function(checkCodeUrl){
    this._checkCodeUrl = checkCodeUrl;
}

login.prototype.init = function(){
    var _login = this;
    $('#id_login_btn').click(function() {
        _login.submit();
    });
    $('input[name=username]').keypress(function(event) {
        if (event.which == '13') {
            $('input[name=password]').focus();
        }
    });
    $('input[name=password]').keypress(function(event) {
        if (event.which == '13') {
            $('input[name=captcha]').focus();
        }
    });
    $('input[name=captcha]').keypress(function(event) {
        if (event.which == '13') {
            _login.submit();
        }
    });
    $('#id_checkcode').click(function() {
        this.src = _login._checkCodeUrl + '?nocache=' + Math.random();
        $('input[name=captcha]').val("").focus();
    });
    
    if (this.isEmpty($('input[name=username]'))) {
        $('input[input[name=username]').focus();
        return ;
    }
    if (this.isEmpty($('input[name=password]'))) {
        $('input[name=password]').focus();
        return ;
    }
   $('input[name=captcha]').focus();
}

login.prototype.isEmpty = function(input){
    if (input.val() && $.trim(input.val()) != "") {
        return false;
    } else {
        input.focus();
        return true;
    }
}

login.prototype.showErrorMessage = function(msg){
    $('#id_error_msg').html(msg);
}

login.prototype.submit = function(){
    if (this.isEmpty($('input[name=username]'))) {
        this.showErrorMessage('用户名不能为空。');
        return;
    }
    if (this.isEmpty($('input[name=password]'))) {
        this.showErrorMessage('密码不能为空。');
        return;
    }
    if (this.isEmpty($('input[name=captcha]'))) {
        this.showErrorMessage('验证码不能为空。');
        return;
    }
    $('form')[0].submit();
}
     
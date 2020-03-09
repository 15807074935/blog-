function submits() {
    $.ajax({
        url:'/security/login',
        data:$('.user_msg').serialize(),                 //将表单数据序列化，格式为name=value
        type:'POST',
        dataType:'json',
        success:function(data){
            console.log("in here success");
            window.location.href="/page/main.html";
        },
        error:function(){
            console.log("in here error");
            window.alert("用户名或密码错误");
        },
    });
}
$("#username").blur(function () {
var name=$("#username").val();
if(name.length!=0){
    $.ajax({
        type: "GET",
        url: "/register/"+name,
        contentType: "application/json",
        async: true,
        cache: false,
        success: function (result) {
            if(result.data==true){
                $(".tip2").css("display","none")
            }else{
                $(".tip2").css("display","block")
            }
        }
    });
}
});
function getValid() {
    var email = $("#useremail").val();
    $.ajax({
        type: "GET",
        url: "/register/email?email="+email,
        contentType: "application/json",
        async: true,
        cache: false,
        success: function (result) {
            if(result.data==true){
                $(".tip3").css("display","none")
                $.ajax({
                    type: "GET",
                    url: "/register/valid/"+email,
                    contentType: "application/json",
                    async: true,
                    cache: false,
                    success: function (result) {
                        if(result.code==0){
                            window.alert(result.data);
                        }else{
                            $(".avlid").text("已发送")
                        }
                    }
                });
            }else{
                $(".tip3").css("display","block")
            }
        }
    });
}
$("#useremail").blur(function () {
    $(".tip3").css("display","none")
})
var token;
$("#uservalid").blur(function () {
   var valid= $("#uservalid").val();
   var email= $("#useremail").val();
   if(valid.length==6){
       var data = {
           email:email,
           valid:valid
       }
       $.ajax({
           type: "POST",
           url: "/register/valid",
           data: JSON.stringify(data),//必要
           dataType:"json",
           contentType: "application/json",
           async: true,
           cache: false,
           success: function (result) {
               if(result.code==1){
                   $(".tip4").css("display","none")
                   token = result.data.token
               }else{
                   $(".tip4").css("display","block")
               }
           }
       })
   }
})
function submit() {
    var email= $("#useremail").val();
    var name=$("#username").val();
    var password=$("#userpassword").val();
    var image=$('#userimg').attr('src');
    var data = {
        userName:name,
        email:email,
        password:password,
        token:token,
        image:image
    }
    $.ajax({
        type: "POST",
        url: "/register",
        data: JSON.stringify(data),//必要
        dataType:"json",
        contentType: "application/json",
        async: true,
        cache: false,
        success: function (result) {
            if(result.code==1){
                window.alert("注册成功")
            }else{
                window.alert(result.msg)
            }
        }
    })
}
function changeimage() {
    $("#upload").click(); //隐藏了input:file样式后，点击头像就可以本地上传
    $("#upload").on("change", function() {
        var objUrl = getObjectURL(this.files[0]); //获取图片的路径，该路径不是图片在本地的路径
        if (objUrl) {
            // $("#userimg").attr("src", objUrl); //将图片路径存入src中，显示出图片
            upimg();
        }
    });
}
//建立一?可存取到?file的url
function getObjectURL(file) {
    var url = null;
    if (window.createObjectURL != undefined) { // basic
        url = window.createObjectURL(file);
    } else if (window.URL != undefined) { // mozilla(firefox)
        url = window.URL.createObjectURL(file);
    } else if (window.webkitURL != undefined) { // webkit or chrome
        url = window.webkitURL.createObjectURL(file);
    }
    return url;
}
//上传头像到服务器
function upimg() {
    var pic = $('#upload')[0].files[0];
    var file = new FormData();
    file.append('file', pic);
    $.ajax({
        url: "/write/image",
        type: "post",
        data: file,
        cache: false,
        contentType: false,
        processData: false,
        success: function(result) {
            $("#userimg").attr("src", result.data);
        }
    });
}
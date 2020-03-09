var E = window.wangEditor
var editor = new E('#editor')
editor.customConfig.uploadImgServer = '/write/image';
editor.customConfig.uploadFileName = 'file';
editor.customConfig.uploadImgHooks = {
    before : function(xhr, editor, files) {

    },
    success : function(xhr, editor, result) {
        console.log("上传成功");
    },
    fail : function(xhr, editor, result) {
        console.log("上传失败,原因是"+result);
    },
    error : function(xhr, editor) {
        console.log("上传出错");
    },
    timeout : function(xhr, editor) {
        console.log("上传超时");
    },
    customInsert: function (insertImg, result, editor) {
        // 图片上传并返回结果，自定义插入图片的事件（而不是编辑器自动插入图片！！！）
        // insertImg 是插入图片的函数，editor 是编辑器对象，result 是服务器端返回的结果

        // 举例：假如上传图片成功后，服务器端返回的是 {url:'....'} 这种格式，即可这样插入图片：
        var url = result.data;
        console.log(url)
        editor.cmd.do ('insertHtml', '<img src="' + url + '" style="max-width:600px;"/>')
        // result 必须是一个 JSON 格式字符串！！！否则报错
    }
}
editor.customConfig.uploadImgMaxSize = 10 * 1024 * 1024;
editor.customConfig.showLinkImg = false;
editor.customConfig.menus = [
    'head',  // 标题
    'bold',  // 粗体
    'fontSize',  // 字号
    'fontName',  // 字体
    'italic',  // 斜体
    'underline',  // 下划线
    'strikeThrough',  // 删除线
    'foreColor',  // 文字颜色
    'backColor',  // 背景颜色
    'link',  // 插入链接
    'list',  // 列表
    'justify',  // 对齐方式
    'quote',  // 引用
    'image',  // 插入图片
    'table',  // 表格
    'video',  // 插入视频
    'code',  // 插入代码
    'undo',  // 撤销
    'redo'  // 重复
]
editor.create()
// editor.$textElem.attr('contenteditable', false) 禁用编辑

var result;
var url=window.location.search; //获取url中"?"符后的字串
if(url.indexOf("?")!=-1){
    result = url.substr(url.indexOf("=")+1);
}
document.getElementById('btn1').addEventListener('click', function() {
    if(result==undefined){
        var data = {
            withcode:editor.txt.html(),
            detail:editor.txt.text(),
            title:$(" input[ name='title' ] ").val()
        }
        $.ajax({
            type:"POST",
            url:"/write/save",
            data: JSON.stringify(data),//必要
            dataType:"json",
            contentType:"application/json",
            async: true,
            cache:false,
            success:function(msg){
                if(msg.code==1){
                    $("#btn1").text("已保存");
                    result=msg.data;
                }else{
                    window.alert("保存失败");
                }
            }
        });
    }else{
        var data = {
            withcode:editor.txt.html(),
            detail:editor.txt.text(),
            title:$(" input[ name='title' ] ").val(),
            id:result
        }
        $.ajax({
            type:"PUT",
            url:"/write/update",
            data: JSON.stringify(data),//必要
            dataType:"json",
            contentType:"application/json",
            async: true,
            cache:false,
            success:function(result){
                if(result.code==1){
                    $("#btn1").text("已保存");
                }else{
                    window.alert("保存失败");
                }
            }
        });
    }
}, false)
$(function(){
    if(result!=undefined){
        $.ajax({
            type:"GET",
            url:"/write/getsave?id="+result,
            contentType:"application/json",
            async: true,
            cache:false,
            success:function(msg){
                if(msg.code==1){
                    editor.cmd.do ('insertHtml',msg.data.withcode);
                    $('.title').val(msg.data.title);
                    $.ajax({
                        type: "GET",
                        url: "/article/ispublish?id="+result,
                        contentType: "application/json",
                        async: true,
                        cache: false,
                        success: function (result) {
                            if(result.code==1){
                               $("#btn2").text("取消发布");
                            }else{
                                $("#btn2").text("发布");
                            }
                        }
                    });
                }else{
                    window.alert("error")
                }
            }
        });
    }
})
document.getElementById('btn2').addEventListener('click', function(){
    if(result==undefined){
        if($("#btn2").text()=="发布"){
            var data = {
                withcode:editor.txt.html(),
                detail:editor.txt.text(),
                title:$(" input[ name='title' ] ").val()
            }
            $.ajax({
                type:"POST",
                url:"/write/publish",
                data: JSON.stringify(data),//必要
                dataType:"json",
                contentType:"application/json",
                async: true,
                cache:false,
                success:function(msg){
                    if(msg.code==1){
                        $("#btn2").text("取消发布");
                        result=msg.data;
                    }else{
                        window.alert("发布失败");
                    }
                }
            });
        }
    }else{
        if($("#btn2").text()=="发布"){
            var data = {
                withcode:editor.txt.html(),
                detail:editor.txt.text(),
                title:$(" input[ name='title' ] ").val(),
                id:result
            }
            $.ajax({
                type:"PUT",
                url:"/write/publish",
                data: JSON.stringify(data),//必要
                dataType:"json",
                contentType:"application/json",
                async: true,
                cache:false,
                success:function(result){
                    if(result.code==1){
                        $("#btn2").text("取消发布");
                    }else{
                        window.alert("发布失败");
                    }
                }
            });
        }
        if($("#btn2").text()=="取消发布") {
            var data={
                id:result
            }
            $.ajax({
                type: "PUT",
                url: "/cancelpublish",
                data: JSON.stringify(data),//必要
                dataType: "json",
                contentType: "application/json",
                async: true,
                cache: false,
                success: function (msg) {
                    if(msg.code==1){
                        $("#btn2").text("发布");
                    }
                }
            })
        }
    }
})
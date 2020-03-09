$(".user").mouseover(function() {
    $(".user-nav").css("display", "block")
    $(".user").css("background", "rgba(216, 214, 214, 0.918)")
});
$(".user").mouseout(function() {
    $(".user-nav").css("display", "none")
    $(".user").css("background", "#fff")
});
var id;
$.ajax({
    type:"GET",
    url:"/user/",
    contentType:"application/json",
    async: true,
    cache:false,
    success:function(result) {
        if (result.code == 1) {
            id=result.data.id;
            $('#userimage').attr('src',result.data.image);
        }
    }
});

$(function(){
        $.ajax({
            type:"GET",
            url:"/mainpage/get",
            contentType:"application/json",
            async: true,
            cache:false,
            success:function(result){
                if(result.code==1){
                   var articles = result.data;
                   for(var i=0;i<articles.length;i++){
                       var as = $("<div class=\"content-left-left\">\n" +
                           "                    <div class=\"title\">\n" +
                           "                        <a href=\"javascript:void(0)\" onclick=\"getdetail("+articles[i].id+")\" class=\"title-a\">"+articles[i].title+"</a> </div>\n" +
                           "                    <div class=\"content\">"+articles[i].detail+"</div>\n" +
                           "                    <div class=\"more\">\n" +
                           "                        <div class=\"view\"><span>阅读：</span>"+articles[i].view+"</div>\n" +
                           "                        <div class=\"author\"><span>作者：</span><a href=\"javascript:void(0)\" onclick='ohp("+articles[i].authorId+")' class=\"author-a\">"+articles[i].authorName+"</a></div>\n" +
                           "                        <div class=\"comment\"><span>评论：</span>"+articles[i].comments.length+"</div>\n" +
                           "                        <div class=\"like\"><span>点赞：</span>"+articles[i].like+"</div>\n" +
                           "                    </div>\n" +
                           "                </div>\n" +
                           "                <div class=\"content-left-right\">\n" +
                           "                    <img class=\"content-left-right-img\" src="+articles[i].mainImage+" alt=\"\">\n" +
                           "                </div>")
                       $(".contents-left").append(as);
                   }
                }else{
                    console.log("error")
                }
            }
        });
});
$.ajax({
    type:"GET",
    url:"/user/recommend",
    contentType:"application/json",
    async: true,
    cache:false,
    success:function(result) {
        if (result.code == 1) {
            var users = result.data;
           for(var i=0;i<users.length;i++){
               var count;
               $.ajax({
                   type: "GET",
                   url: "/article/count?id=" + users[i].id,
                   contentType: "application/json",
                   async: false,
                   cache: false,
                   success: function (result) {
                       if(result.code==1){
                           count=result.data;
                           var as = $("<div class=\"bottom\">" +
                               "<div class=\"img\">\n" +
                               "                        <img class=\"img-img\" src="+users[i].image+" alt=\"\">\n" +
                               "                    </div>\n" +
                               "                    <div class=\"center\">\n" +
                               "                        <a class=\"users\" href=\"#\">"+users[i].userName+"</a>\n" +
                               "                        <div class=\"totle\"><span>发布了</span>"+count+"<span>篇文章</span></div>\n" +
                               "                    </div>\n" +
                               "                    <div class=\"add\">\n" +
                               "                        <a href=\"javascript:void(0)\" onclick=\"focusornot("+users[i].id+")\" class=\"add-a add-"+users[i].id+"\">关注</a>\n" +
                               "                    </div>" +
                               " </div>");
                           $(".content-right").append(as);

                           $.ajax({
                               type: "GET",
                               url: "/social/isfocus?id="+users[i].id,
                               contentType: "application/json",
                               async: false,
                               cache: false,
                               success: function (result) {
                                   if(result.code==1){
                                       $(".add-"+users[i].id).text("已关注");
                                       $(".add-"+users[i].id).css("background-color","#999");
                                   }else{
                                       $(".add-"+users[i].id).text("关注");
                                       $(".add-"+users[i].id).css("background-color","#42c02e");
                                   }
                               }
                           });
                       }
                   }
               });
           }
        }
    }
});
function getdetail(id){
    // window.location.href="/page/article.html?id="+id;
    window.open("/page/article.html?id="+id,'_blank')
}
function searchmore() {
    var text = $(".search-text").val();
    // window.location.href="/page/searchresult.html?text="+text;
    window.open("/page/searchresult.html?text="+text,'_blank')
}
function backmain() {
    window.location.href="/page/main.html";
}
function uhp() {
    // window.location.href="/page/homepage.html?id="+id;
    window.open("/page/homepage.html?id="+id,'_blank')
}
function ohp(oid) {
    // window.location.href="/page/homepage.html?id="+oid;
    window.open("/page/homepage.html?id="+oid,'_blank')
}
function focusornot(id) {
    var data = {
        id:id,
    }
    if($(".add-"+id).text()=="已关注") {
        $.ajax({
            type: "DELETE",
            url: "/social/focus",
            data: JSON.stringify(data),//必要
            dataType:"json",
            contentType: "application/json",
            async: true,
            cache: false,
            success: function (result) {
                console.log(result);
                if (result.code == 1) {
                    $(".add-"+id).text("关注");
                    $(".add-"+id).css("background-color","#42c02e");
                }
            }
        });
    }else{
        $.ajax({
            type: "POST",
            url: "/social/focus",
            data: JSON.stringify(data),//必要
            dataType:"json",
            contentType: "application/json",
            async: true,
            cache: false,
            success: function (result) {
                console.log(result);
                if(result.code==1){
                    $(".add-"+id).text("已关注");
                    $(".add-"+id).css("background-color","#999");
                }
            }
        });
    }
}
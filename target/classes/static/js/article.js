var result;
var url=window.location.search; //获取url中"?"符后的字串
if(url.indexOf("?")!=-1){
    result = url.substr(url.indexOf("=")+1);
}

var authorId;
var userId;
$.ajax({
    type:"GET",
    url:"/user/",
    contentType:"application/json",
    async: false,
    cache:false,
    success:function(result) {
        if (result.code == 1) {
            var userImage = result.data.image;
            userId =result.data.id;
            $('#userimage').attr('src',userImage);
        }
    }
});

$.ajax({
    type:"GET",
    url:"/article/randomarticle",
    contentType:"application/json",
    async: true,
    cache:false,
    success:function(result) {
        if (result.code == 1) {
           var articles = result.data;
           for(var i=0;i<articles.length;i++){
               var as = $(" <div class=\"title\">\n" +
                   "                    <div class=\"describe\"><a class=\"art_link\" onclick='getarticle("+articles[i].id+")' href=\"javascript:void(0)\">"+articles[i].title+"</a></div>\n" +
                   "                    <div class=\"watch\">阅读 "+articles[i].view+"</div>\n" +
                   "                </div>")
               $("#rdm").append(as);
           }
        }
    }
});

$.ajax({
    type:"GET",
    url:"/mainpage/getone?id="+result,
    contentType:"application/json",
    async: false,
    cache:false,
    success:function(result){
        if(result.code==1){
            authorId=result.data.authorId;
            var as =  $("     <div class=\"title\">"+result.data.title+"</div>\n" +
                "                <div class=\"note\">\n" +
                "                    <div class=\"user_img\">\n" +
                "                        <img class=\"img\" src="+result.data.authorImage+">\n" +
                "                    </div>\n" +
                "                    <div class=\"info\">\n" +
                "                        <div class=\"user_info\">\n" +
                "                            <a class=\"user_name\" href=\"javascript:void(0)\" onclick='ohp("+authorId+")'>"+result.data.authorName+"</a>\n" +
                "                            <a href=\"javascript:void(0)\"  onclick=\"myfocus()\" class=\"add\">关注</a></div>\n" +
                "                        <div class=\"article_info\">\n" +
                "                            <span class=\"article_time\">"+datetimeFormat(result.data.updateTime)+"</span>\n" +
                "                            <span class=\"article_words\">字数 "+result.data.detail.length+"</span class=\"article_time\">\n" +
                "                            <span class=\"article_watch\">阅读 "+result.data.view+"</span class=\"article_time\">\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                    <div class=\"ac\">\n" +
                "                        <a href=\"javascript:void(0)\" onclick='getdetail("+result.data.id+")'>编辑</a>\n" +
                "                    </div>" +
                "                </div>\n" +
                "                <div class=\"main\">"+result.data.withcode+" \n" +
                "                </div>\n" +
                "                <div class=\"btn\">\n" +
                "                        <div class=\"top\">\n" +
                "                            <div class=\"left\"><span class=\"style3\">标签：</span>\n" +
                "                            <a class=\"tip\" href=\"#\">"+result.data.label+"</a>\n" +
                "                            <div class=\"right\">\n" +
                "                                <img src=\"http://116.62.172.100:80/appr2.png\" onclick=\"like("+result.data.id+")\" class=\"appr\">\n" +
                "                                <span class=\"style\">"+result.data.like+"</span>\n" +
                "                                <span class=\"style2\">人点赞</span>\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                        <div class=\"bottom\">" +
                "<span>“小礼物走一走，来blog支持我”</span>\n" +
                "                            <a href=\"javascript:void(0)\" onclick=\"rl()\" >赞赏支持</a>" +
                "</div>\n" +
                "                    </div>\n" +
                "                </div>" +
                "       <div class=\"comment\">\n" +
                "                    <textarea class=\"up\" placeholder=\"写下你的评论\"></textarea>\n" +
                "                    <a class=\"up2\" onclick=\"upcomment()\"  href=\"javascript:void(0)\">发布</a>\n" +
                "                </div>\n" +
                "                </div>");
            $(".article-top").append(as);
        }else{
            window.alert("加载失败")
        }
    }
});

function datetimeFormat(timestamp) {
    var date;
    if(timestamp.toString().length==10){
       date  = new Date(timestamp * 1000);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    }else{
        date = new Date(timestamp);
    }
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    var D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' ';
    var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    var m = (date.getMinutes() <10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
    var s = (date.getSeconds() <10 ? '0' + date.getSeconds() : date.getSeconds());
    return Y+M+D+h+m+s;
}
window.onload=function (){
    $.ajax({
        type: "GET",
        url: "/social/islike?articleId="+result,
        contentType: "application/json",
        async: true,
        cache: false,
        success: function (result) {
            if(result.code==1){
                $('.appr').attr('src','http://116.62.172.100:80/appr.png');
            }
        }
    })
    $.ajax({
        type: "GET",
        url: "/social/me?id="+authorId,
        contentType: "application/json",
        async: true,
        cache: false,
        success: function (result) {
            if(result.code==0){
                $(".add").css("display","none");
            }
        }
    })
    $.ajax({
        type: "GET",
        url: "/social/isfocus?id="+authorId,
        contentType: "application/json",
        async: true,
        cache: false,
        success: function (result) {
            if(result.code==1){
                $(".add").text("已关注");
            }
        }
    })
    if(authorId==userId){
        $(".ac").css("display","block")
    }
}

$.ajax(
    {
        type: "GET",
        url: "/social/comment?id="+result,
        contentType: "application/json",
        async: true,
        cache: false,
        success: function (result) {
            if(result.code==1){
                var tip1 =$("<div class=\"allcomment\">\n" +
                    "                    <span>全部评论</span>\n" +
                    "</div>");
                $(".article-bottom").append(tip1);
                var comments = result.data;
                for(var i=0;i<comments.length;i++){
                    var tip2 =$( "                    <div class=\"comment-user\">\n" +
                        "                        <img src="+comments[i].userImage+" />\n" +
                        "                        <div class=\"user-info\">\n" +
                        "                            <div class=\"username\">"+comments[i].username+"</div>\n" +
                        "                            <div class=\"time\"> "+datetimeFormat(comments[i].updateTime)+"</div>\n" +
                        "                        </div>\n" +
                        "                    </div>\n" +
                        "                    <div class=\"acomment\">\n" +
                        "                       "+comments[i].comment+" \n" +
                        "                    </div>")
                    $(".allcomment").append(tip2);
                }
            }
        }
    });

function like(id){
    var data = {
        id:id
    }
    var x = $('.style').text();
    if($('.appr').attr('src')=='http://116.62.172.100:80/appr2.png'){
        $.ajax({
            type: "PUT",
            url: "/social/like",
            data: JSON.stringify(data),//必要
            dataType:"json",
            contentType: "application/json",
            async: true,
            cache: false,
            success: function (result) {
               if(result.code==1){
                   $('.appr').attr('src','http://116.62.172.100:80/appr.png');
                   $('.style').html(parseInt(x)+1);
               }
            }
        })
    }else{
        $.ajax({
            type: "DELETE",
            url: "/social/dislike",
            data: JSON.stringify(data),//必要
            dataType:"json",
            contentType: "application/json",
            async: true,
            cache: false,
            success: function (result) {
                if(result.code==1){
                    $('.appr').attr('src','http://116.62.172.100:80/appr2.png');
                    $('.style').html(parseInt(x)-1);
                }
            }
        });
    }
}
function upcomment() {
    var c = $(".up").val();
    var data ={
        comment:c,
        articleId:result
    }
    $.ajax({
        type: "POST",
        url: "/social/comment",
        data: JSON.stringify(data),//必要
        dataType:"json",
        contentType: "application/json",
        async: true,
        cache: false,
        success: function (result) {
            if(result.code==1){
                location.reload();
            }
        }
    });
}
function myfocus() {
    var data = {
        id:authorId,
    }
    if($(".add").text()=="已关注"){
        $.ajax({
            type: "DELETE",
            url: "/social/focus",
            data: JSON.stringify(data),//必要
            dataType:"json",
            contentType: "application/json",
            async: true,
            cache: false,
            success: function (result) {
                if(result.code==1){
                    $(".add").text("关注");
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
                if(result.code==1){
                    $(".add").text("已关注");
                }
            }
        });
    }
}
function getarticle(id){
    // window.location.href="/page/article.html?id="+id;
    window.open("/page/article.html?id="+id,'_blank')
}
$(".user").mouseover(function() {
    $(".user-nav").css("display", "block")
    $(".user").css("background", "rgba(216, 214, 214, 0.918)")
});
$(".user").mouseout(function() {
    $(".user-nav").css("display", "none")
    $(".user").css("background", "#fff")
});
function uhp() {
    // window.location.href="/page/homepage.html?id="+userId;
    window.open("/page/homepage.html?id="+userId,'_blank')
}
function searchmore() {
    var text = $(".search-text").val();
    window.location.href="/page/searchresult.html?text="+text;
}
function rl() {
    window.location.href="/page/reward.html?id="+result;
}
function ohp(oid) {
    window.location.href="/page/homepage.html?id="+oid;
}
function getdetail(id){
    window.location.href="/page/wangeditor.html?id="+id;
}
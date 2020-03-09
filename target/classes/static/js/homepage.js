var result;
var url=window.location.search; //获取url中"?"符后的字串
if(url.indexOf("?")!=-1){
    result = url.substr(url.indexOf("=")+1);
}
var other;
var focusandfans;
var articles;
$.ajax({
    type: "GET",
    url: "/user",
    contentType: "application/json",
    async: true,
    cache: false,
    success: function (result) {
        if(result.code==1){
            $('#userimage').attr('src',result.data.image);
        }
    }
});
$.ajax({
    type: "GET",
    url: "/other?id="+result,
    contentType: "application/json",
    async: false,
    cache: false,
    success: function (result) {
        if(result.code==1){
           other=result.data;
        }
    }
});
$.ajax({
    type: "GET",
    url: "/social/focusandfans?id="+result,
    contentType: "application/json",
    async: true,
    cache: false,
    success: function (result) {
        if(result.code==1){
            focusandfans = result.data;
            var as1 = $(" <div class=\"user_img\">\n" +
                "                        <img class=\"img\" src="+other.image+" alt=\"\">\n" +
                "                    </div>\n" +
                "                    <div class=\"user_dsc\">\n" +
                "                        <div class=\"name\">"+other.userName+"</div>\n" +
                "                        <div class=\"more\">\n" +
                "                            <span>关注:</span>\n" +
                "                            <a class=\"focus\" href=\"javascript:void(0)\" onclick=\"getfocus("+other.id+")\">"+focusandfans.focuss+"</a>\n" +
                "                            <span style=\"margin-left: 10px;\">粉丝:</span>\n" +
                "                            <a class=\"fan\" href=\"#\" onclick=\"getfans("+other.id+")\">"+focusandfans.fans+"</a>\n" +
                "                        </div>\n" +
                "                    </div>")
            $(".user_info").append(as1);
        }
    }
});
$.ajax({
    type: "GET",
    url: "/user/article?userId="+result,
    contentType: "application/json",
    async: true,
    cache: false,
    success: function (result) {
        if(result.code==1){
            articles = result.data;
            for(var i=0;i<articles.length;i++){
                var as2 = $("    <div class=\"content-left-left\">\n" +
                    "                        <div class=\"title\">\n" +
                    "                            <a href=\"javascript:void(0)\" onclick=\"getdetail("+articles[i].id+")\" class=\"title-a\">"+articles[i].title+"</a> </div>\n" +
                    "                        <div class=\"content\">"+articles[i].detail+"</div>\n" +
                    "                        <div class=\"more\">\n" +
                    "                            <div class=\"view\"><span>阅读：</span>"+articles[i].view+"</div>\n" +
                    "                            <div class=\"author\"><span>作者：</span><a href=\"javascript:void(0)\" class=\"author-a\">"+articles[i].authorName+"</a></div>\n" +
                    "                            <div class=\"comment\"><span>评论：</span>"+articles[i].comments.length+"</div>\n" +
                    "                            <div class=\"like\"><span>点赞：</span>"+articles[i].like+"</div>\n" +
                    "                        </div>\n" +
                    "                    </div>\n" +
                    "                    <div class=\"content-left-right\">\n" +
                    "                        <img class=\"content-left-right-img\" src="+articles[i].mainImage+" alt=\"\">\n" +
                    "                    </div>")
                $(".content-left").append(as2);
            }
        }
    }
});
function getfocus(userId) {
    window.location.href="/page/focus.html?id="+userId;
}
function getfans(userId) {
    window.location.href="/page/fans.html?id="+userId;
}
function getdetail(id){
    // window.location.href="/page/article.html?id="+id;
    window.open("/page/article.html?id="+id,'_blank')
}
function searchmore() {
    var text = $(".search-text").val();
    // window.location.href="/page/searchresult.html?text="+text;
    window.open("/page/searchresult.html?text="+text,'_blank')
}
function uhp() {
    // window.location.href="/page/homepage.html?id="+result;
    window.open("/page/homepage.html?id="+result,'_blank')
}
function uhp2() {
    // window.location.href="/page/homepage2.html?id="+result;
    window.open("/page/homepage2.html?id="+result,'_blank')
}
$(".user").mouseover(function() {
    $(".user-nav").css("display", "block")
    $(".user").css("background", "rgba(216, 214, 214, 0.918)")
});
$(".user").mouseout(function() {
    $(".user-nav").css("display", "none")
    $(".user").css("background", "#fff")
});
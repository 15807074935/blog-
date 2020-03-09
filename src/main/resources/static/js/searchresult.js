var text;
var url=window.location.search; //获取url中"?"符后的字串
if(url.indexOf("?")!=-1){
    text = url.substr(url.indexOf("=")+1);
    $(".search-text").attr("value",text);
}
$.ajax({
    type:"GET",
    url:"/users?username="+text,
    contentType:"application/json",
    async: true,
    cache:false,
    success:function(result) {
        if (result.code == 1) {
           var users = result.data;
           if(users.length!=0){
               if(users.length<5){
                   for(var i=0;i<users.length;i++){
                       var count;
                       $.ajax({
                           type: "GET",
                           url: "/article/count?id="+users[i].id,
                           contentType: "application/json",
                           async: false,
                           cache: false,
                           success: function (result) {
                               if(result.code==1){
                                   count=result.data;
                               }
                           }
                       });
                       var as = $(" <div class=\"usero\">\n" +
                           "                            <img src="+users[i].image+" alt=\"\" class=\"user-img\">\n" +
                           "                            <div class=\"usero-info\">\n" +
                           "                                <div class=\"usero-name\">"+users[i].userName+"</div>\n" +
                           "                                <div class=\"article-title\">写了"+count+"篇</div>\n" +
                           "                            </div>\n" +
                           "                        </div>")
                       $(".part-user").append(as);
                   }
               }else{
                   for(var i=0;i<5;i++){
                       var count;
                       $.ajax({
                           type: "GET",
                           url: "/article/count?id="+users[i].id,
                           contentType: "application/json",
                           async: false,
                           cache: false,
                           success: function (result) {
                               if(result.code==1){
                                   count=result.data;
                               }
                           }
                       });
                       var as = $(" <div class=\"usero\">\n" +
                           "                            <img src="+users[i].image+" alt=\"\" class=\"user-img\">\n" +
                           "                            <div class=\"usero-info\">\n" +
                           "                                <div class=\"usero-name\">"+users[i].userName+"</div>\n" +
                           "                                <div class=\"article-title\">写了"+count+"篇</div>\n" +
                           "                            </div>\n" +
                           "                        </div>")
                       $(".part-user").append(as);
                   }
               }
           }
        }
    }
});
$.ajax({
    type: "GET",
    url: "/search/articles?dt=" + text,
    contentType: "application/json",
    async: true,
    cache: false,
    success: function (result) {
        var articles = result.data;
        if (articles.length != 0) {
            $(".article-sum").html(articles.length+"个结果");
            for(var i=0;i<articles.length;i++){
                var as = $("<div class=\"content-left-left\">\n" +
                    "                        <div class=\"title\">\n" +
                    "                            <a href=\"#\" class=\"title-a\">"+articles[i].title+"</a> </div>\n" +
                    "                        <div class=\"content\">"+articles[i].detail+"</div>\n" +
                    "                        <div class=\"more\">\n" +
                    "                            <div class=\"view\"><span>阅读：</span>"+articles[i].view+"</div>\n" +
                    "                            <div class=\"author\"><span>作者：</span><a href=\"#\" class=\"author-a\">"+articles[i].authorName+"</a></div>\n" +
                    "                            <div class=\"comment\"><span>评论：</span>"+articles[i].comments.length+"</div>\n" +
                    "                            <div class=\"like\"><span>点赞：</span>"+articles[i].like+"</div>\n" +
                    "                        </div>\n" +
                    "                    </div>\n" +
                    "                    <div class=\"content-left-right\">\n" +
                    "                        <img class=\"content-left-right-img\" src="+articles[i].mainImage+" alt=\"\">\n" +
                    "                    </div>");
                $(".content-left").append(as);
            }
        }
    }
});
$.ajax({
    type:"GET",
    url:"/user/",
    contentType:"application/json",
    async: true,
    cache:false,
    success:function(result) {
        if (result.code == 1) {
            $('#userimage').attr('src',result.data.image);
        }
    }
});
function searchmore() {
    var text = $(".search-text").val();
    // window.location.href="/page/searchresult.html?text="+text;
    window.open("/page/searchresult.html?text="+text,'_blank')
}
function moreusers() {
    window.location.href="/page/allusers.html?text="+text;
}

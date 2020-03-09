$.ajax({
    type: "GET",
    url: "/social/thinkgoods",
    contentType: "application/json",
    async: true,
    cache: false,
    success: function (result) {
       if(result.code==1){
           articles = result.data;
           for(var i =0;i<articles.length;i++){
               var as =$(" <div class=\"articles\">\n" +
                   "                <div class=\"content-left-left\">\n" +
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
                   "                </div>\n" +
                   "            </div>")
               $(".contents").append(as);
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
function ohp(oid) {
    // window.location.href="/page/homepage.html?id="+oid;
    window.open("/page/homepage.html?id="+oid,'_blank')
}
function uhp() {
    // window.location.href="/page/homepage.html?id="+id;
    window.open("/page/homepage.html?id="+id,'_blank')
}
function getdetail(id){
    // window.location.href="/page/article.html?id="+id;
    window.open("/page/article.html?id="+id,'_blank')
}
function searchmore() {
    var text = $(".search-text").val();
    // window.location.href="/page/searchresult.html?text="+text;
    // window.open("/page/searchresult.html?text="+text,'_blank')
    window.open("/page/searchresult.html?text="+text,'_blank')
}
$(".user").mouseover(function() {
    $(".user-nav").css("display", "block")
    $(".user").css("background", "rgba(216, 214, 214, 0.918)")
});
$(".user").mouseout(function() {
    $(".user-nav").css("display", "none")
    $(".user").css("background", "#fff")
});
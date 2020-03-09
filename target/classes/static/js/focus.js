var result;
var url=window.location.search; //获取url中"?"符后的字串
if(url.indexOf("?")!=-1){
    result = url.substr(url.indexOf("=")+1);
}
var user;
var other;
var focusandfans;
$.ajax({
    type: "GET",
    url: "/user",
    contentType: "application/json",
    async: true,
    cache: false,
    success: function (result) {
        if(result.code==1){
            user=result.data;
            $('#userimage').attr('src',result.data.image);
        }
    }
});
$.ajax({
    type: "GET",
    url: "/other?id="+result,
    contentType: "application/json",
    async: true,
    cache: false,
    success: function (result) {
        if(result.code==1){
            other=result.data;
            dt();
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
                "                            <a class=\"focus\" href=\"javascript:void(0)\" onclick=\"getdetail()\">"+focusandfans.focuss+"</a>\n" +
                "                            <span style=\"margin-left: 10px;\">粉丝:</span>\n" +
                "                            <a class=\"fan\" href=\"#\">"+focusandfans.fans+"</a>\n" +
                "                        </div>\n" +
                "                    </div>")
            $(".user_info").append(as1);
        }
    }
});
function dt(){
    $.ajax({
        type: "GET",
        url: "/social/focus?userId="+other.id,
        contentType: "application/json",
        async: true,
        cache: false,
        success: function (result) {
            if(result.code==1){
                var users = result.data;
                $(".sum-sum").html(users.length);
                for(var i=0;i<users.length;i++){
                    $.ajax({
                        type: "GET",
                        url: "/article/count?id="+users[i].id,
                        contentType: "application/json",
                        async: false,
                        cache: false,
                        success: function (result) {
                            if(result.code==1){
                                var count=result.data;
                                var as = $("<div class=\"users\">\n" +
                                    "                <div class=\"img\">\n" +
                                    "                    <img class=\"img-img\" src="+users[i].image+" alt=\"\">\n" +
                                    "                </div>\n" +
                                    "                <div class=\"center\">\n" +
                                    "                    <a class=\"user\" onclick='ohp("+users[i].id+")' href=\"javascript:void(0)\">"+users[i].userName+"</a>\n" +
                                    "                    <div class=\"totle\"><span>发布了</span>"+count+"<span>篇文章</span></div>\n" +
                                    "                </div>\n" +
                                    "                <div class=\"add\">\n" +
                                    "                    <a href=\"javascript:void(0)\" onclick=\"focusornot("+users[i].id+")\" class=\"add-a add-"+users[i].id+"\"></a>\n" +
                                    "                </div>\n" +
                                    "            </div>")
                                $(".content").append(as);

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
function ohp(oid) {
    // window.location.href="/page/homepage.html?id="+oid;
    window.open("/page/homepage.html?id="+oid,'_blank')
}

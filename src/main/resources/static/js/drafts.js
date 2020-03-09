$(function() {
    $.ajax({
        type: "GET",
        url: "/write/save",
        contentType: "application/json",
        async: true,
        cache: false,
        success: function(result) {
            if(result.code==1){
                var articles = result.data;
                for (var i=0;i<articles.length;i++)
                {
                  var as = $("<div class=\"articles\">\n" +
                        "                <div class=\"content-left-left\">\n" +
                        "                    <div class=\"title\">\n" +
                        "                        <a href=\"javascript:void(0)\" onclick=\"getdetail("+articles[i].id+")\"  class=\"title-a\">"+articles[i].title+"</a> </div>\n" +
                        "                    <div class=\"content\">"+articles[i].detail+"</div>\n" +
                        "                </div>\n" +
                        "                <div class=\"content-left-right\">\n" +
                        "                    <img class=\"content-left-right-img\" src="+articles[i].mainImage+" alt=\"\">\n" +
                        "                </div>\n" +
                        "                    <div id=\"del\" onclick=\" deleteone("+articles[i].id+")\" >\n" +
                        "                        x </div>\n" +
                        "            </div>");
                    $(".contents").append(as);
            }

        }else{

            }
        }
    })
});
function getdetail(id){
    // window.location.href="/page/wangeditor.html?id="+id;
    window.open("/page/wangeditor.html?id="+id,'_blank')
}
function deleteone(id){
   var data={
        id:id
    }
    $.ajax({
        type: "delete",
        url: "/write/delete",
        data:JSON.stringify(data),
        dataType: 'json',
        contentType: "application/json",
        async: true,
        cache: false,
        success: function(result) {
            if (result.code == 1) {
                window.location.reload()
            }else{
                window.alert(result.data)
            }
        }
    });
}
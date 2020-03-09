var result;
var rNo;
var url=window.location.search; //获取url中"?"符后的字串
if(url.indexOf("?")!=-1){
    result = url.substr(url.indexOf("=")+1);
}
window.onload=function(){
    order(1)
}
function order(m){
    var data = {
    articleId:result,
    payment:m
    }
    $.ajax({
        type: "POST",
        url: "/reward",
        data: JSON.stringify(data),//必要
        dataType:"json",
        contentType: "application/json",
        async: true,
        cache: false,
        success: function (result) {
            if(result.code==1){
                rNo=result.data;
              if(precreate(rNo)==true){
                  return true;
              }
            }else{
                window.alert("error");
                return false;
            }
        }
    });
}
function precreate(rewardNo) {
    $.ajax({
        type: "GET",
        url: "/alipay/precreate?rewardNo="+rewardNo,
        contentType: "application/json",
        async: true,
        cache: false,
        success: function (result) {
            if(result.code==1){
                $('.payment_img').attr('src',result.data);
                return true;
            }else{
                window.alert("error");
                return false;
            }
        }
    });
}
var refreshid;
function rm(index,money) {
    clearInterval(refreshid);
        for(var i=1;i<9;i++){
            if(i!=index){
                $(".m"+i).css("border-color","#969696");
            }else{
                $(".m"+i).css("border-color","#ec7259");
            }
        }
        $(".pmoney").html("支付￥"+money);
        order(money)
       refreshid = setInterval("query()",3000);
}
function query() {
    $.ajax({
        type: "GET",
        url: "/alipay/query?orderNo="+rNo,
        contentType: "application/json",
        async: true,
        cache: false,
        success: function (result) {
            if(result.code==1){
               window.alert("支付成功");
               clearInterval(refreshid);
            }
        }
    });
}
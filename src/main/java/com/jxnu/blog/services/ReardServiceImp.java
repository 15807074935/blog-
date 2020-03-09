package com.jxnu.blog.services;

import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.google.common.collect.Lists;
import com.jxnu.blog.common.OrderStatus;
import com.jxnu.blog.common.ServerResponse;
import com.jxnu.blog.pojo.payInfo;
import com.jxnu.blog.pojo.reward;
import com.jxnu.blog.reporsity.payInfoReposity;
import com.jxnu.blog.reporsity.rewardReposity;
import com.jxnu.blog.utils.FTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ReardServiceImp implements RewardService {
    @Autowired
    rewardReposity rewardReposity;
    @Autowired
    private AlipayTradeService alipayTradeService;
    @Autowired
    payInfoReposity payInfoReposity;
    @Override
    public ServerResponse<String> createOrder(int articleId, int userId,long payment) {
        reward reward = new reward();
        reward.setArticleId(articleId);
        reward.setPayment(payment);
        reward.setStatus(OrderStatus.NoPAY.getCode());
        reward.setRewardNo(String.valueOf(System.currentTimeMillis()+new Random().nextInt(1000)));
        reward.setUserId(userId);
        reward save = rewardReposity.save(reward);
        if(save!=null){
            return ServerResponse.createBySuccess(save.getRewardNo());
        }else{
            return ServerResponse.createByError(null);
        }
    }
    @Override
    public ServerResponse<String> pay(Integer userId, String rewardNo, String path) {
        reward reward = rewardReposity.findByUserIdAndRewardNo(userId,rewardNo);

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        String outTradeNo = rewardNo;

        // (必填) 订单标题，粗略描述用户的支付目的。如“喜士多（浦东店）消费”
        String subject = new StringBuilder().append("blog的文章打赏,订单号：").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = String.valueOf(reward.getPayment());

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单：").append(outTradeNo).append("花费了").append(totalAmount).append("元").toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，线下扫码交易定义为5分钟
        String timeoutExpress = "5m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<>();
        GoodsDetail goods1 = GoodsDetail.newInstance(String.valueOf(reward.getArticleId()), "文章"+reward.getArticleId()+"的打赏",reward.getPayment(), 1);
        goodsDetailList.add(goods1);

        AlipayTradePrecreateRequestBuilder builder =new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject)
                .setTotalAmount(totalAmount)
                .setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount)
                .setSellerId(sellerId)
                .setBody(body)
                .setOperatorId(operatorId)
                .setExtendParams(extendParams)
                .setStoreId(storeId)
                .setTimeoutExpress(timeoutExpress)
                //支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setNotifyUrl("http://116.62.172.100:8080/alipay/notify")
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = alipayTradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                System.out.println("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                File folder = new File(path);
                if(!folder.exists()){
                    folder.setWritable(true);
                    folder.mkdirs();
                }
                // 需要修改为运行机器上的路径
                String qrPath = String.format(path+"/qr-%s.png", response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png",response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(),256,qrPath);
                File targetFile = new File(path,qrFileName);
                try {
                    FTPUtil.uploadFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String qrurl ="http://116.62.172.100:80/"+targetFile.getName();
                targetFile.delete();
                //                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                return ServerResponse.createBySuccess(qrurl);

            case FAILED:
                System.out.println("支付宝预下单失败!!!");
                return ServerResponse.createBySuccess(null);

            case UNKNOWN:
                System.out.println("系统异常，预下单状态未知!!!");
                return ServerResponse.createBySuccess(null);

            default:
                System.out.println("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createBySuccess(null);
        }
    }
    public ServerResponse callback(Map<String,String> param){
        Long rewardNo = Long.parseLong(param.get("out_trade_no"));
        String tradeNo = param.get("trade_no");
        String tradeStatus = param.get("trade_status");
        reward reward = rewardReposity.findByRewardNo(String.valueOf(rewardNo));
        if(reward==null){
            return ServerResponse.createByError("订单异常");
        }
        if(reward.getStatus()>= OrderStatus.PAID.getCode()){
            return ServerResponse.createByError("支付宝重复调用");
        }
        System.out.println("tradeStatus is--------------------------------"+tradeStatus);
        if("TRADE_SUCCESS".equals(tradeStatus)){
            rewardReposity.statusUpdateById(OrderStatus.PAID.getCode(),reward.getId());
        }
        payInfo payInfo = new payInfo();
        payInfo.setRewardNo(reward.getRewardNo());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);
        payInfo payInfo1 = payInfoReposity.save(payInfo);
        if(payInfo1!=null){
            return ServerResponse.createBySuccess("success");
        }
        return ServerResponse.createByError("error");
    }

    @Override
    public ServerResponse query(String orderNo) {
        AlipayTradeQueryRequestBuilder builder = new AlipayTradeQueryRequestBuilder()
                .setOutTradeNo(orderNo);
        AlipayF2FQueryResult result = alipayTradeService.queryTradeResult(builder);
        System.out.println("result.getResponse().getBody()"+result.getResponse().getBody());
        switch (result.getTradeStatus()) {
            case SUCCESS:
                System.out.println("查询返回该订单支付成功: )");

                AlipayTradeQueryResponse resp = result.getResponse();
                System.out.println(resp.getTradeStatus());
//                log.info(resp.getFundBillList());
                return ServerResponse.createBySuccess(null);

            case FAILED:
                System.out.println("查询返回该订单支付失败!!!");
                return ServerResponse.createByError(null);

            case UNKNOWN:
                System.out.println("系统异常，订单支付状态未知!!!");
                return ServerResponse.createByError(null);

            default:
                System.out.println("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByError(null);
        }
    }
}

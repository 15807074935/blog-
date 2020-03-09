package com.jxnu.blog.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.jxnu.blog.common.ServerResponse;
import com.jxnu.blog.dto.RewardDto;
import com.jxnu.blog.pojo.user;
import com.jxnu.blog.reporsity.UserReposity;
import com.jxnu.blog.services.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
public class RewardController {
    @Autowired
    RewardService rewardServiceImp;
    @Autowired
    UserReposity userReposity;
    @PostMapping("/reward")
    public ServerResponse createOrder(@RequestBody RewardDto rewardDto, Principal principal){
        user user = userReposity.findByUserName(principal.getName());
        return rewardServiceImp.createOrder(rewardDto.getArticleId(),user.getId(),rewardDto.getPayment());
    }

    /**
     * 当面付-扫码付
     *
     * 扫码支付，指用户打开支付宝钱包中的“扫一扫”功能，扫描商户针对每个订单实时生成的订单二维码，并在手机端确认支付。
     *
     * 发起预下单请求，同步返回订单二维码
     *
     * 适用场景：商家获取二维码展示在屏幕上，然后用户去扫描屏幕上的二维码
     * @return
     * @throws AlipayApiException
     */
    @GetMapping("/alipay/precreate")
    public ServerResponse<String> precreate(Principal principal,String rewardNo,HttpServletRequest request) {
        user user = userReposity.findByUserName(principal.getName());
        String path = request.getSession().getServletContext().getRealPath("upload");
        return rewardServiceImp.pay(user.getId(),rewardNo,path);
    }
    /**
     * 扫码付异步结果通知
     * https://docs.open.alipay.com/194/103296
     * @param request
     */
    @RequestMapping("/alipay/notify")
    public String notify(HttpServletRequest request) throws AlipayApiException {
        // 一定要验签，防止黑客篡改参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((key, value) -> System.out.println(key+"="+value[0]));

        // https://docs.open.alipay.com/54/106370
        // 获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        AlipaySignature.rsaCheckV1(params,
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoPsXsnGPzXFPNRkDT5ZbsdB2RLgpcx5+hXxfrf2RfkYtkqnpBfbOClnzmr/I4vqHRRBEuOTKX9o3kPt8yhz99hwQb7m4WpWRzBmyrBkI+Z9cQAJNJjl/05so+2mjuipF74X5E10qR7JEGsVB1ePLpKmutcO6Da/euJPtO+iASDkfyzvD5Mqr8py/MzpOMdCE51DH122XG1CdCvKtW5SAECGvfka/UnCKtS1+tPW9HdYRRx/WZhMRQIh3AqRVZD7OFZzg68tLqxltAjgffNBU6ioQPm3HItYclaPrOn8AqL+EsezFEoBi2vgZBEJdoJnHhSJs7ZBMvld3PfFTMaAomwIDAQAB",
                "utf-8",
                "RSA2");
        ServerResponse response = rewardServiceImp.callback(params);
        if (response.isSuccess()) {
            /**
             * TODO 需要严格按照如下描述校验通知数据的正确性
             *
             * 商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
             * 并判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
             * 同时需要校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
             *
             * 上述有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
             * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
             * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
             */

            return "success";
        }

        return "failed";
    }
    /**
     * 订单查询(最主要用于查询订单的支付状态)
     * @param orderNo 商户订单号
     * @return
     */
    @GetMapping("/alipay/query")
    public ServerResponse query(String orderNo){
        return   rewardServiceImp.query(orderNo);
    }
}

package com.jxnu.blog.config;

import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfiguration {
    @Bean
    public AlipayTradeService alipayTradeService() {
        return new AlipayTradeServiceImpl.ClientBuilder()
                .setGatewayUrl("https://openapi.alipaydev.com/gateway.do")
                .setAppid("2016101500688492")
                .setPrivateKey("MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCb9zndtJVxWNDcFjeC5xPbxCUflpb+gNNumziuojkKhC6ACI8+ra83/i9qjYL7wajcvnWOK5Cl9J4Npracr27Wav2KxCpwZN8to0izQo6kzUPFnLsvnyAbMAY7iQ/GhbQIxEV4S6xJPHGWrr/tHHaafZIs9SrQYbs0SXxnrQdRxnawolOr6y+fd8/t0U/3J09aRh44CC/6H8Ur3VYSMdifN12E436+kgZKHxXSuiOvyK1XOd7qAG2BbMj23uwRBKbd4JYpWERdw+JTF/98Csb0bX67eXXpASGeO77V9kKM4kS+5Izgb4uz7F6QAv6Elj/Xanu8soIh7jVi02qe5ZTbAgMBAAECggEAHY1w14OaNYoAuVM0XYdvidrWte+Q2NSMO48J5H00lbuKHBSDH/tFEjUFwwjhYtbFKl6kY7X5iJibQjbury3GsLZBKIQXDojuiPVBl0XrRWrnhpA2H8XlDYHCDJHh/VnkTSzzhOHB2Xash0LBdypHhup4QjCKVkXOdQ/flL2Cf4nxq+sOIplC71GCjpkkXSFVCqQe35YOZBtNQAtkk6FXkbUCPYRWEXfvqrR8iNhF2dz0JiaHlQQ+E4tfa5DLjD22bUYwpxYjSSaznBp+IQ1FCbSj+EpNR49Zw2CE8IbZJ8CswJyXUG72VTjzw0GxtB8QGpHaGLm8nSJjfLnfBv4dsQKBgQDSc5d9EDWocmfPD5+7xdTsUyJJe7uk+OaYTHcja1b+MQudXjm3Y/RCaDLSokKuXoUl+kWwOp6J9ibprcuSUYKeFPmKl/LNLA5m5X3789PUfumS1ft4bndoeLYjXp9LSKwyzLUnM82eSVIR+8GT17jJ9/QFdJVfTjv+buif8mT/tQKBgQC9uMKPaQydzLKectXx52bf9O7xO1omRn8yqIynMHFR2niwD3BFUSylhg7pZzQ8/mpI//cS8kMGorQFKCtS4ejO4Hk//J7ZynfTr5wHf+YChNGGHtaP5BMzF1/ATf1re5ZVNSgu8L1UcpMv622OA3l4zs55sSGaQO+tLcbYA8N8TwKBgFkBJA9zfI4KHJc4z2B1sbBlFHoph1mlHrlIlZZ4PilxCq16fURvVI2FTgFBOJxQQ1WlfDWlBQnp/dkUdKUkLe63hjY0Py7AtDKWWmrlWzBioFxGfX0Ykw2vB7D7wrjvOkydwPuccBjCf6A1blAdivx4Dk8EWGwfRUY9F8sVk9uFAoGAaHkqooBNk0PahIhmKfysWN1vCuYNIZCmkMXiFlsFu/ZbiVAnGfS2ajvlUEAlswTCkIpri0JfCHNmQIS7PrvqUKmp+0NoUjQ9QhOtDzAt/+sV2g53RutoazUO5D3ONHbs9OT9yMOG7s7dIIE7Zf2h0ZXIe5jAEUp68VmI6wE/j98CgYB+eskubikhCKE+gjvpGuid1kjfNJbUjAkvVi3pqsJKTGUXGicmlbeUJJXCkWgjlmUZ+SgpzSdhdUbfW6EowavBfJFHzUmDZMGjQB5vliV9gQTbrHz4pMcpXH7fbX/su/zfuqaBTSibZ7qqfFVZK4E0/B3IFOUqwx7LL0KWKJ1RGQ==")
                .setAlipayPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoPsXsnGPzXFPNRkDT5ZbsdB2RLgpcx5+hXxfrf2RfkYtkqnpBfbOClnzmr/I4vqHRRBEuOTKX9o3kPt8yhz99hwQb7m4WpWRzBmyrBkI+Z9cQAJNJjl/05so+2mjuipF74X5E10qR7JEGsVB1ePLpKmutcO6Da/euJPtO+iASDkfyzvD5Mqr8py/MzpOMdCE51DH122XG1CdCvKtW5SAECGvfka/UnCKtS1+tPW9HdYRRx/WZhMRQIh3AqRVZD7OFZzg68tLqxltAjgffNBU6ioQPm3HItYclaPrOn8AqL+EsezFEoBi2vgZBEJdoJnHhSJs7ZBMvld3PfFTMaAomwIDAQAB")
                .setSignType("RSA2")
                .build();
    }
}

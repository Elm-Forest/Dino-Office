package com.dino.socket.gpt.config;

import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Zhang Jinming
 */
@Configuration
public class OpenAiClientConfig {

    @Value("${open.ai.key}")
    private List<String> openAiKey;

    @Value("${open.ai.proxy.host}")
    private String host;

    @Value("${open.ai.proxy.port}")
    private int port;

    @Bean
    public OpenAiClient penAiClientV2() {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        //！！！！千万别再生产或者测试环境打开BODY级别日志！！！！
        //！！！生产或者测试环境建议设置为这三种级别：NONE,BASIC,HEADERS,！！！
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .proxy(proxy)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new OpenAiResponseInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        return OpenAiClient.builder()
                .apiKey(openAiKey)
                .okHttpClient(okHttpClient)
                .build();
    }

    @Bean
    public OpenAiStreamClient openAiStreamClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .proxy(new Proxy(Proxy.Type.HTTP,
                        new InetSocketAddress(host, port)))
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new OpenAiResponseInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .build();
        return OpenAiStreamClient.builder()
                .apiKey(openAiKey)
                .okHttpClient(okHttpClient)
                .build();
    }
}

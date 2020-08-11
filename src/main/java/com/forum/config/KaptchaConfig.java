package com.forum.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {

    @Bean
    public Producer kaptchaProducer(){

        /**
         * 设置验证码图片属性
         */
        Properties properties = new Properties();

        properties.setProperty("kaptcha.image.width", "100");

        properties.setProperty("kaptcha.image.height", "40");

        properties.setProperty("kaptcha.textproducer.font.size", "32");

        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0");

        properties.setProperty("kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        properties.setProperty("kaptcha.textproducer.char.length", "4");

        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");

        /**
         * 新建验证码对象
         */
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();

        /**
         * 设置验证码图片性质
         */
        Config config = new Config(properties);

        defaultKaptcha.setConfig(config);

        return defaultKaptcha;

    }

}

package com.forum;

import com.forum.entity.DiscussPost;
import com.forum.entity.User;
import com.forum.mapper.DiscussPostMapper;
import com.forum.mapper.UserMapper;
import com.forum.util.MailClient;
import com.forum.util.SensitiveFilter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@SpringBootTest
//导入 main 包的配置类 , 以 ForumApplication.class 为配置类
@ContextConfiguration(classes = ForumApplication.class)
//implements 得到spring容器
class ForumApplicationTests {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    public void testKafka() throws InterruptedException {

        kafkaProducer.sendMessage("1", "你好");
        kafkaProducer.sendMessage("2", "nice to meet you");

        Thread.sleep(1000 * 10);



    }

//    @Autowired
//    private SensitiveFilter sensitiveFilter;
//
//    @Test
//    void contextLoads() {
//
//        String text = "*赌_博* , *嫖_娼 , *吃饭* , *吸毒* , *吃饭*";
//
//        text = sensitiveFilter.filter(text);
//
//        System.out.println(text);
//
//    }


//    @Autowired
//    private TemplateEngine templateEngine;
//
//    @Autowired
//    private MailClient mailClient;
//
//
//    @Test
//    void contextLoads() {
//
//        Context context = new Context();
//
//        context.setVariable("username", "bxybug");
//
//        String content = templateEngine.process("mail/demo", context);
//
//        System.out.println(content);
//
//        mailClient.sendMail("bxybug@qq.com", "HTML", content);
//
//    }

//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private DiscussPostMapper discussPostMapper;

//    @Test
//    void contextLoads() {
//        System.out.println(userMapper.selectUserByUserId(1));
//        for (DiscussPost discussPost : discussPostMapper.selectDiscussPosts(0, 0, 10)) {
//            System.out.println(discussPost);
//        }
//    }


//    @Test
//    void contextLoads() {
//        AlphaDao alphaDao;
//       /* 有多个类时 , 应该在 impl 类上标注 @Primary 注解 , 否则会发生 NoUniqueBeanDefinitionException 异常
//        No qualifying bean of type 'com.forum.dao.AlphaDao' available:
//            expected single matching bean but found alphaHibernateDaoImpl,alphaMybatisDaoImpl
//        applicationContext.getBean(AlphaDao.class).select();*/
//        alphaDao = (AlphaDao) applicationContext.getBean("alphaMybatisDaoImpl");
//        alphaDao.select();
//        alphaDao = (AlphaDao) applicationContext.getBean("alphaHibernateDaoImpl");
//        alphaDao.select();
//        alphaDao = (AlphaDao) applicationContext.getBean("alphaMybatisDaoImpl", AlphaDao.class);
//        alphaDao.select();
//        alphaDao = (AlphaDao) applicationContext.getBean("alphaHibernateDaoImpl", AlphaDao.class);
//        alphaDao.select();
//        String format = applicationContext.getBean(SimpleDateFormat.class).format(new Date());
//        System.out.println(format);
//    }

}

@Component
class KafkaProducer{
    private KafkaTemplate kafkaTemplate;
    @Autowired
    public void setKafkaTemplate(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String content){
        kafkaTemplate.send(topic, content);
    }
}

@Component
class KafkaConsumer{
    @KafkaListener(topics = {"test"})
    public void handleMessage(ConsumerRecord record){
        System.out.println(record.value());
    }
}
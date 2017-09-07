package com.github.esbatis.test;

import com.alibaba.fastjson.JSON;
import com.github.esbatis.session.Configuration;
import com.github.esbatis.session.DefaultSessionFactory;
import com.github.esbatis.session.Session;
import com.github.esbatis.session.SessionFactory;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

public class DemoTest {

    @Test
    public void test() {
        Configuration configuration = new Configuration("http://10.101.91.60:9200/");
        configuration.addResource("DemoDao.xml");

        SessionFactory sessionFactory = new DefaultSessionFactory(configuration);
        Session session = sessionFactory.openSession();
        DemoDao demoDao = configuration.getMapper(DemoDao.class, session);
        Demo demo = new Demo();
        demo.setId(3L);
        demo.setCheckType(0);
        demo.setCheckId(1L);
        demo.setDataDt("2017-09-03");
        demo.setCreatedAt(LocalDate.now().toString());
        demo.setUpdatedAt(LocalDate.now().toString());
        String json = "{\\\"compareInfo\\\":[[{\\\"name\\\":\\\"GMV\\\",\\\"value\\\":\\\"53,275,333\\\",\\\"rate\\\":\\\"1.15%\\\"}],[{\\\"name\\\":\\\"餐厅量\\\",\\\"value\\\":\\\"72,358\\\",\\\"rate\\\":\\\"1.77%\\\"},{\\\"name\\\":\\\"交易餐厅\\\",\\\"value\\\":\\\"43,436\\\",\\\"rate\\\":\\\"1.81%\\\"}],[{\\\"name\\\":\\\"取消订单量\\\",\\\"value\\\":\\\"7,499\\\",\\\"rate\\\":\\\"0.00%\\\"}],[{\\\"name\\\":\\\"订单量\\\",\\\"value\\\":\\\"978,472\\\",\\\"rate\\\":\\\"0.87%\\\"}],[{\\\"name\\\":\\\"交易用户\\\",\\\"value\\\":\\\"800,828\\\",\\\"rate\\\":\\\"1.53%\\\"}],[{\\\"name\\\":\\\"运单量\\\",\\\"value\\\":\\\"747,912\\\",\\\"rate\\\":\\\"1.22%\\\"},{\\\"name\\\":\\\"超时订单量\\\",\\\"value\\\":\\\"34,005\\\",\\\"rate\\\":\\\"-11.83%\\\"},{\\\"name\\\":\\\"超时率\\\",\\\"value\\\":\\\"4.55%\\\",\\\"rate\\\":\\\"-12.89%\\\"}],[{\\\"name\\\":\\\"平均客单价\\\",\\\"value\\\":\\\"54\\\",\\\"rate\\\":\\\"0.27%\\\"}],[{\\\"name\\\":\\\"平均取单时间\\\",\\\"value\\\":\\\"3'50\\\",\\\"rate\\\":\\\"0.7%\\\"}]], \\\"byParam\\\":[{\\\"indicator\\\":\\\"订单量\\\",\\\"competitor\\\":\\\"22,897,719\\\",\\\"ele\\\":\\\"29,960,665\\\"},{\\\"indicator\\\":\\\"GMV\\\",\\\"competitor\\\":\\\"999,808,321\\\",\\\"share\\\":\\\"59.42%\\\",\\\"ele\\\":\\\"1,464,235,427\\\"},{\\\"indicator\\\":\\\"客单价\\\",\\\"competitor\\\":43.664,\\\"ele\\\":48.872},{\\\"indicator\\\":\\\"平均配送费\\\",\\\"competitor\\\":4,\\\"ele\\\":7},{\\\"indicator\\\":\\\"平均配送时长\\\",\\\"competitor\\\":\\\"40'55\\\",\\\"ele\\\":\\\"33'45\\\"},{\\\"indicator\\\":\\\"餐厅量\\\",\\\"competitor\\\":\\\"61,328\\\",\\\"ele\\\":\\\"72,688\\\",\\\"possessionRate\\\":\\\"75.79%\\\"}], \\\"uv\\\":{\\\"午餐\\\":\\\"637,593\\\",\\\"用户量\\\":\\\"1,301,312\\\",\\\"下午茶\\\":\\\"240,386\\\",\\\"晚餐\\\":\\\"570,049\\\"}}";
        demo.setCheckValueJson(json);

        demoDao.index(demo);
    }

    @Test
    public void test2() {
        Configuration configuration = new Configuration("http://10.101.91.60:9200");
        configuration.addResource("DemoDao.xml");

        SessionFactory sessionFactory = new DefaultSessionFactory(configuration);
        Session session = sessionFactory.openSession();
        DemoDao userDao = configuration.getMapper(DemoDao.class, session);
        List<Demo> list = userDao.findDemo("2017-08-31", 0, 1L);
        System.out.println(JSON.toJSON(list));
    }
}

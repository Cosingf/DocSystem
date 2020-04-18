package cn.xmu.edu.legaldocument;


import cn.xmu.edu.legaldocument.entity.Discuss;
import cn.xmu.edu.legaldocument.mapper.DiscussMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = LegaldocumentApplication.class)
//@Sql("/init-schema.sql")
public class InitDatabaseTests {

    @Autowired
    DiscussMapper discussMapper;

    @Test
    public void contextLoads() {

//        for (int i = 0; i < 11; ++i) {
//            Discuss discuss = new Discuss();
//            discuss.setCommentCount(i);
//            Date date = new Date();
//            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
//            discuss.setCreatedDate(date);
//            discuss.setUserId(i + 1);
//            discuss.setTitle(String.format("TITLE{%d}", i));
//            discuss.setContent(String.format("Balaababalalalal Content %d", i));
//            discussMapper.addDiscuss(discuss);
//        }
        List<Discuss> discussList=discussMapper.selectLatestDiscuss(1,0,10);
        for(Discuss discuss:discussList){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date d=discuss.getCreatedDate();
            System.out.println("格林威治时间:"+d);
            String s=df.format(d);
            System.out.println("格式化后的时间:"+s);
        }

    }

    /*
    @Test
    public void testSensitive() {
        String content = "discuss content <img src=\"https:\\/\\/baidu.com/ff.png\">色情赌博";
        String result = sensitiveUtil.filter(content);
        System.out.println(result);
    }*/
}

package com.xuecheng.freemarker.test;

import com.xuecheng.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author chenjia
 * @version 1.0
 * @date 2020/5/7 11:07
 * @desc
 */
public class TestFreemarker {

    @Test
    public void generateToFile() throws IOException, TemplateException {
        // 获取配置对象
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        // 获取classpath路径
        String classPath = this.getClass().getResource("/").getPath();

        // 设置模板存放的路径
        configuration.setDirectoryForTemplateLoading(new File(classPath + "/templates/"));

        // 获取模板
        Template template = configuration.getTemplate("test.ftl");

        // 获取模型数据
        Map model = getMap();

        // 生成内容
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        // 将内容输出到文件中
        try (FileWriter fos = new FileWriter(new File(classPath + "/templates/test.html"))) {
            fos.write(content);
            fos.flush();
        }
    }

    @Test
    public void generateToFileByString() throws IOException, TemplateException {
        // 获取配置对象
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        // 获取模板内容
        String templateStr = getTemplateStr();

        // 使用StringTemplateLoader 构建Template对象
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", templateStr);
        // 将模板加载器绑定到配置对象上
        configuration.setTemplateLoader(stringTemplateLoader);
        // 获取模板
        Template template = configuration.getTemplate("template");

        // 获取模型数据
        Map model = getMap();

        // 生成内容
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        // 获取classpath路径
        String classPath = this.getClass().getResource("/").getPath();
        // 将内容输出到文件中
        try (FileWriter fos = new FileWriter(new File(classPath + "/templates/testByString.html"))) {
            fos.write(content);
            fos.flush();
        }
    }


    public Map getMap() {
        Map map = new HashMap();
        map.put("name", "freemarker");
        Student stu1 = new Student();
        stu1.setName("小明");
        stu1.setAge(18);
        stu1.setBirthday(new Date());
        stu1.setMoney(1000.0D);
        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setAge(16);
        stu2.setBirthday(new Date());
        stu2.setMoney(200.0D);
        stu1.setBestFriend(stu2);
        List<Student> stus = new ArrayList<Student>();
        stus.add(stu1);
        stus.add(stu2);
        map.put("stu1", stu1);
        map.put("stu2", stu2);
        map.put("stus", stus);
        Map<String, Student> stuMap = new HashMap<String, Student>();
        stuMap.put("stu1", stu1);
        stuMap.put("stu2", stu2);
        map.put("stuMap", stuMap);
        return map;
    }

    public String getTemplateStr() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\"/>\n" +
                "    <title>freemarker测试</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "hello ${name}!<br/>\n" +
                "<br/>\n" +
                "测试获取对象内的数据<br/>\n" +
                "姓名:${stu1.name}<br/>\n" +
                "年龄:${stu1.age}<br/>\n" +
                "姓名:${stu1[\"name\"]}<br/>\n" +
                "年龄:${stu1[\"age\"]}<br/>\n" +
                "<br/>\n" +
                "获取遍历list<br/>\n" +
                "list的大小:${stus?size}<br/>\n" +
                "<table>\n" +
                "    <tr>\n" +
                "        <td>序号</td>\n" +
                "        <td>名称</td>\n" +
                "        <td>年龄</td>\n" +
                "        <td>生日(?date)</td>\n" +
                "        <td>生日(?string(\"yyyy年MM月dd日\"))</td>\n" +
                "    </tr>\n" +
                "    <#list stus as stu>\n" +
                "        <tr>\n" +
                "            <td <#if stu_index gt 0> style=\"background: #8b0a60;\"</#if> >${stu_index + 1}</td>\n" +
                "            <td <#if stu.name == \"小明\"> style=\"background: #8b8930;\"</#if> >${stu.name}</td>\n" +
                "            <td>${stu.age}</td>\n" +
                "            <td>${stu.birthday?date}</td>\n" +
                "            <td>${stu.birthday?string(\"yyyy年MM月dd日\")}</td>\n" +
                "        </tr>\n" +
                "    </#list>\n" +
                "</table>\n" +
                "<br/>\n" +
                "遍历map,因为freemarker没有直接提供遍历map的标签，所以通过遍历map中的key<br/>\n" +
                "<#-- 非空判断 -->\n" +
                "<#if stuMap??>\n" +
                "    <#list stuMap?keys as key>\n" +
                "        姓名:${stuMap[key].name}    年龄:${stuMap[key].age}<br/>\n" +
                "    </#list>\n" +
                "</#if>\n" +
                "</body>\n" +
                "</html>";
    }
}

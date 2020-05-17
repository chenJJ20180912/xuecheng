<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>freemarker测试</title>
</head>
<body>
hello ${name}!<br/>
<br/>
测试获取对象内的数据<br/>
姓名:${stu1.name}<br/>
年龄:${stu1.age}<br/>
姓名:${stu1["name"]}<br/>
年龄:${stu1["age"]}<br/>
<br/>
获取遍历list<br/>
list的大小:${stus?size}<br/>
<table>
    <tr>
        <td>序号</td>
        <td>名称</td>
        <td>年龄</td>
        <td>生日(?date)</td>
        <td>生日(?string("yyyy年MM月dd日"))</td>
    </tr>
    <#list stus as stu>
        <tr>
            <td <#if stu_index gt 0> style="background: #8b0a60;"</#if> >${stu_index + 1}</td>
            <td <#if stu.name == "小明"> style="background: #8b8930;"</#if> >${stu.name}</td>
            <td>${stu.age}</td>
            <td>${stu.birthday?date}</td>
            <td>${stu.birthday?string("yyyy年MM月dd日")}</td>
        </tr>
    </#list>
</table>
<br/>
遍历map,因为freemarker没有直接提供遍历map的标签，所以通过遍历map中的key<br/>
<#-- 非空判断 -->
<#if stuMap??>
    <#list stuMap?keys as key>
        姓名:${stuMap[key].name}    年龄:${stuMap[key].age}<br/>
    </#list>
</#if>
</body>
</html>
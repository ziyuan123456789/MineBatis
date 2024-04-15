# MineBatis 
简易的半自动Orm框架Demo
![Java17](https://img.shields.io/badge/JDK-17+-success.svg)
![License](https://img.shields.io/npm/l/mithril.svg)
## 注意事项:
- 编译结束后方法形参名称可能不再保留,可以加入编译参数解决


## 打个广告:
- 大四没事干,软件园日企招打螺丝的一个月实习都4000了,大连hr有没有想联系我的 邮箱:3139196862@qq.com,再找不到活真去打螺丝了


## 项目描述:
- 仿照Mybatis写的一个简单的Orm框架用来练手

## 已经实现:
- 简易的Class映射,仅可以处理单个对象的查询


## 备注:
- 曾经在AutumnMvc框架中写过一版,但是由于AutumnMvc框架的设计问题,导致Orm框架的设计不够完善,所以重新写了一个,日后会迁移回AutumnMvc
- 之前那一版放在AutumnFrameworkOldMineBatis软件包中保存,以后可能开一个新的分支进行归档处理

## 代码示范
### Mapper
```java
@MyMapper
public interface UserMpper {
    @MySelect("select * from user where UserId = #{userId} and UserName = #{userName}")
    User getOneUser(Integer userId,String userName);
}

```
### 感谢:
- 没有Gpt4写代码寸步难行
- 感谢Jetbrains提供的开源支持idea/pycharm/rider/clion license
- 感谢GitHub提供的学生免费copilot


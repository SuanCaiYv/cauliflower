# cauliflower
## 介绍
这是一个技术整合项目，通过模仿Twitter来把自己知道的技术塞进去并跑起来。

Cauliflower中文为花菜，叫这个一是因为我喜欢吃花菜，疫情在家期间每天都吃，吃了小半年；二是因为这个项目本身属于社交平台后端，社交网就像花菜的花一样，一个一个延伸出去，每一个花菜上面又有小的花菜，于是起名为Cauliflower。
## 用到的技术栈
### Spring全家桶
#### SpringCloudEureka
服务发现注册代理
#### SpringCloudConfig
统一配置管理
#### SpringCloudGateway

实际使用到的：SpringWebFlux+Sureness+MyBatisPlus+Resilience4J+Redis+Guava

功能：网关拦截，权限验证，请求转发
#### SpringWebFlux+Sureness

实际使用到的：SpringWebFlux+Sureness+R2DBC+ReactiveRedis+ReactiveJPA

功能：统一注册，授权中心
#### SpringCloud+Resilience4J

实际使用到的：Resilience4J+SpringBoot

功能：熔断+限流+重试+隔离
#### SpringCloudStream

实际使用到的：KafkaBinder+SpringCloudStream

功能：流式处理，消息推送，系统通知，削峰限流
### Redis
#### SpringBoot+Redis/ReactiveRedis

功能：可能认识的人，共同好友，关注列表，点赞管理，实时热榜。
### Zookeeper
功能：分布式锁

### Kafka
功能：消息推送，消息订阅，分布式(流)处理，请求限流

## 部分实现
## 总结

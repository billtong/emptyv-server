# empty-video-microService Structure
## one for all


### asset CRUD with the message listerner in services
all the services are using the diagram below.

![ad](img/asset-operation-diagram.png)  

Every Asset Operations are "abstracted" as below json template.
```javascript
{
  "userId": "xxx",                  //操作请求用户
  "operation": "create a comment",  //操作描述
  "object": comment                 //操作的资源实例
}
```
### 视频数据统计(弹幕，评论和收藏数)
在用户进行完comment，dan，favlist服务的添加，或favlist的改变值操作后，需要通过消息队列向video服务发布数据改变的消息，而video会做出相应的改动
![flow](img/video-count-diagram.png)
### 视频标签统计
在用户进行完video服务的添加或更改操作后，需要把把这个操作通过消息队列发布给tag服务。
![flow](img/video-tag-diagram.png)

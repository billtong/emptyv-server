# 消息队列
## Topic:"operation" - 以下为全部的资源操作消息，以及相关操作
- login  （auth login）  
消息服务: n/a  
历史服务: 记录该操作  
积分服务: 增加分数  
- activate a user （auth activate）  
消息服务: 发给改user  
历史服务: 记录该操作  
积分服务: 初始化分数信息  
- update a user  （patch user）  
消息服务: 发给改user  
历史服务: 记录该操作  
积分服务: 增加分数  
- create a comment  （post comment）  
消息服务: 发给@的人, 没有@的人不做处理  
历史服务: 记录该操作两次，创造的人和被@的人  
积分服务: 增加分数(判断单日上限)  
- like a comment  （patch comment）  
消息服务: 发给被喜欢的人  
历史服务: 记录该操作两次，主动方和被动方  
积分服务: 双方增加分数  
- delete a comment   （delete comment）  
消息服务: n/a  
历史服务: 记录该操作  
积分服务: n/a  
- like a video  （patch video）  
消息服务: n/a   
历史服务: 记录该操作  
积分服务: 增加分数(判断单日上限)
- unlike a video  （patch video）  
消息服务: n/a  
历史服务: 记录该操作   
积分服务: 增加分数(判断单日上限) 
- cancel unlike a video  （patch video）    
消息服务: n/a 
历史服务: 记录该操作  
积分服务: n/a   
- fav a video  （post favlist，patch favlist）    
消息服务: n/a 
历史服务: 记录该操作  
积分服务: 增加分数(判断单日上限)  
- send a message  （post message）  
消息服务: 发给被发送者   
历史服务: n/a  
积分服务: n/a
- get X point  （post point）  
消息服务: 发给获得积分者   
历史服务: 记录该操作  
积分服务: n/a  
- cancel like a video  （patch video）  
消息服务: n/a 
历史服务: 记录该操作  
积分服务: n/a   
- cancel like a comment  （patch comment）  
消息服务: n/a  
历史服务: 记录该操作两次，主动方和被动方  
积分服务: n/a  
## topic:"video-count"-视频数据统计消息，以及相关操作
- add a comment （post comment）   
视频服务：评论数加一
- add to favlist  （post favlist，patch favlist）  
视频服务  收藏数加一
- add a dan  （post dan）  
视频服务：收藏数加一
## topic:"tag"-标签操作消息，以及相关操作
- add video to tag （post video，patch video） 

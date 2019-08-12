# Message Broker Summary
all the topics list below
- "notification" -> notification service
- "history" -> history service
- "point" -> point service
- "video-count" -> video-service
- "video-tag" -> tag service
## operations as well as service subscriber
#### login (auth login)
- history service: record this operation
- point service: add to user's point
#### activate a user (auth activate)
- notifi service: send to this user  
- history service: record this operation  
- point service: initialize user's point  
#### update a user (patch user)
- notifi service: send to this use-r
- history service: record this operation
- point service: add to user's point
#### create a comment (post comment)
- notifi service: send to @user, or do nothing
- history service: record this operation
- point service: add to user's point(upder daily poinnt limit)
- video-service： comment count++
#### like a comment (patch comment)
- notifi service: send to that user  
- history service: record this operation  
- point service: add to users' point
#### delete a comment (delete comment)  
- history service: record this operation  
#### like a video (patch video)   
- history service: record this operation
- point service: add to user's point(upder daily poinnt limit)
#### unlike a video (patch video)
- history service: record this operation
- point service: add to user's point(upder daily poinnt limit)
#### cancel unlike a video (patch video)   
- history service: record this operation
#### fav a video (post favlist，patch favlist)
- history service: record this operation
- point service: add to user's point(upder daily poinnt limit)
- video-service：fav count++
#### cancel fav a video (patch favlist)
- history service: delete last add operation record
- video-service: fav coutn--
#### tag a video (post video, patch video)
- history service: record this operation
- tag service： add this video to the tag, or create a new tag pojo
#### send a message (post message)
- notifi service: send to message receiver
#### get X point (post point)
- notifi service: send to point reciever
- history service: record this operation
#### cancel like a video (patch video)
- history service: record this operation
#### cancel like a comment (patch comment)
- history service: record operations
#### post a dan (post dan)
- video-service：dan count++
- history service: record this operation
- point service: add to user's point(upder daily poinnt limit)
#### post a video
- history service: record this operation
- tag service：add this video to the tag, or create a new tag pojo
## summary by topics
### topic: "notification", subscriber: notification-service
### topic: "history", subscriber: history service
### topic: "point", subscriber: point service
### topic:"video-count", subscriber: video-service
- add a comment(post comment)
comment count++
- add to favlist(post favlist，patch favlist) 
fav count++
- add a dan（post dan)
danmu count++
### topic:"tag", subscriber: tag service
- tag a video （post video，patch video)
- post a video

## about history topic()
对比operation值，资源id和userid三者，来进行重复history文档的删减
- like a video vs cancel like a video
- unlike a video vs cancel unlike a video
- fav a video vs cancel fav a video
- like a comment vs cancel like a comment
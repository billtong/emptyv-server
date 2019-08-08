# EVAG (Empty Video API Gateway)[8000]
## user-service [8001]
- GET(/user-service/user/{id})
- GET(/user-service/user/email/{email})
- GET(/user-service/users)
    - queryparam: ids(id1,id2,id3)
- POST(/user-service/user)
    - body: user(json)
- PATCH(/user-service/user)
    - bearer token
    - body user(json), 只能改变profile部分
- POST(/user-service/auth/login)
    - basic auth
- GET(/user-service/auth/user)
- GET(/user-service/auth/active/{sessionID})
## comment-service [8002]
- GET(/comment-service/comment/{id})
- GET(/comment-service/comment/video/{id})
- PATCH(/comment-service/commment/{id}/like)
- POST(/comment-service/comment)
- DELETE(/comment-service/comment/{id})
## dan-service [8003]
- GET(/dan-service/api/dan/{videoId})
- POST(/dan-service/api/dan)
## video-service [8004]
- GET(/video-service/video/{id})
- GET(/video-service/videos/random)
- GET(/video-service/videos/search) `(future)`
- PATCH(/video-service/video/{id}/{operation})
    - queryparam: operation, tag
- POST(/video-service/video)
    - body: video(json)
## fav-list-servic [8005]
- GET(/fav-list-servic/favlist)
- GET(/fav-list-servic/favlist/{id})
- GET(/fav-list-servic/favlist/search)
- POST(/fav-list-servic/favlist) 
    - body: favlist(json)
- PATCH(/fav-list-servic/favlist/{id})
    - queryparam: operation, videoId
- DELETE(/fav-list-servic/favlist/{id})
## tag-service [8006]
- GET(/tag-service/tag/{name})
- GET(/tag-servic/tags/all)
## message-service [8007]
- ...
## notification-service [8008]
- GET(/notification-service/notification/{userId})
## history-serivce [8009]
- GET(/notification-service/history)
## point-service [8010]
- ...

# Outside of the EVAG
## oauth2-service [8011] 
- GET(/) OAuth主页
    - queryparams: clientId=1&secret=1&error=
- POST(/oauth/code) OAuth主页提交后处理endpoint
- GET(/oauth/userInfo) OAuth客户端需要callback请求的路径
    - bearer token
- POST(/oauth/client)
    - body: OAuthCient(json)
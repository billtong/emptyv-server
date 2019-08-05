# API Gateway [8000]
## user-service
- GET(/user-service/user/{id})
- POST(/user-service/user)
    - basic auth
- PATCH(/user-service/user)
    - bearer token
    - body user(json), 只能改变profile部分
- POST(/user-service/auth/login)
    - body: user(json)
- GET(/user-service/auth/user)
- GET(/user-service/auth/active/{sessionID})
## comment-service
- GET(/comment-service/comment/{id})
- GET(/comment-service/comment/video/{id})
- PATCH(/comment-service/commment/{id}/like)
- POST(/comment-service/comment)
- DELETE(/comment-service/comment/{id})
## dan-service
- GET(/dan-service/api/dan/{videoId})
- POST(/dan-service/api/dan)
## video-service
- GET(/video-service/video/{id})
- GET(/video-service/videos/random)
- GET(/video-service/videos/search) `(future)`
- PATCH(/video-service/video/{id}/{operation})
    - queryparam: operation, tag
- POST(/video-service/video)
    - body: video(json)
## fav-list-servic
- GET(/fav-list-servic/favlist)
- GET(/fav-list-servic/favlist/{id})
- GET(/fav-list-servic/favlist/search)
- POST(/fav-list-servic/favlist) 
    - body: favlist(json)
- PATCH(/fav-list-servic/favlist/{id})
    -queryparam: operation, videoId
- DELETE(/fav-list-servic/favlist/{id})
## tag-service
- GET(/tag-service/tag/{name})
- GET(/tag-servic/tags/all)
## message-service
- ...
## notification-service
- GET(/notification-service/notification/{userId})
## history-serivce
- GET(/notification-service/history)
## point-service
- ...
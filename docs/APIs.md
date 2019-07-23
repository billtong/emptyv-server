# APIs
## user-service [8001:]
- GET(/api/user/{id})
- POST(/api/user)
- PATCH(/api/user)
- POST(/auth/login)
- GET(/auth/user)
- GET(/auth/active/{sessionID})
## comment-service [8002:]
- GET(/api/comment/{id})
- GET(/api/comment/video/{id})
- PATCH(/api/commment/{id}/like)
- POST(/api/comment")
- DELETE(/api/comment/{id})
## dan-service [8003:]
- GET(/api/dan/{videoId})
- POST(/api/dan)
## video-service [8004:]
- GET(/api/video/{id})
- GET(/api/video/random)
- GET(/api/video/search)
- PATCH(/api/video/{id})
- POST(/api/video)
## fav-list-servic[8005:]
- GET(/api/favlist)
- GET(/api/favlist/{id})
- GET(/api/favlist/search)
- POST(/api/favlist)
- PATCH(/api/favlist/{id})
- DELETE(/api/favlist/{id})
## tag-service [8006:]
- GET(/api/tag/{name})
- GET(/api/tag/all)
## message-service [8007:]
- ...
## notification-service [8008:]
- GET(/api/notification/{userId})
## history-serivce [8009:]
- ...
## point-service [8010:]
- ...
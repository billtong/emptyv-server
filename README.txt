empty-server
一个(视频)(分享)(弹幕)平台后端
目前技术支持：ssm+mysql+tomcat

开发计划now(#1)
#1.基本表（用户，视频，评论）+事务操作
#2.历史表+事务操作拦截获取用户浏览记录
#3.弹幕表+事务操作


目前开放的endpoint
----------------------------------
用户
api/user/login
api/user/signUp
api/user/activated
api/user/getUser
----------------------------------
评论
api/comment/load
api/comment/write
api/comment/delete
----------------------------------
视频
api/video/search
api/vidoe/view
----------------------------------

目标-->
普通网友能够：查找（视频资源+评论资源+弹幕资源）
用户能够：查找/添加/更改/删除（视频资源+评论资源+弹幕资源+用户资源）

理想目标---->
用户量增长后的目标（广告盈利后）
响应式开发
人工智能推荐视频

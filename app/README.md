局域网文件传输Demo需求描述：
注：定义被动接受上传/下载请求的为server，主动发起上传/下载请求的为client。
1、	每个app进程既为client又为server；
2、	server既支持上传请求也支持下载请求；
3、	每个client可以同时连接多个server，每个server可以同时接受多个client的请求，多个上传下载任务并发执行；
4、	server端收到client请求时需界面友好提示用户，用户允许方可开始此次上传/下载会话；
5、	client/server任意方均可暂停/取消任务，暂停后点击“继续”按钮可继续下载；
6、	支持断点续传；
7、	能处理好各种异常事件如超时、断网、断电等并给出友好提示，恢复正常环境后任务能继续正常进行；
8、	能方便的管理上传/下载的文件；
9、	设计文档；

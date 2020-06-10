## 启动容器命令
docker run -dit --net=host --name eureka01 --add-host eureka01:192.168.99.100 --add-host eureka02:192.168.99.100 -e JAVA_OPTIONS='-DPORT=50101 -DSERVER_URL=http://user:123@eureka02:50102/eureka/ -DDOMAIN_NAME=eureka01' xc-govern-center:0.1
docker run -dit --net=host --name eureka02 --add-host eureka01:192.168.99.100 --add-host eureka02:192.168.99.100 -e JAVA_OPTIONS='-DPORT=50102 -DSERVER_URL=http://user:123@eureka01:50101/eureka/ -DDOMAIN_NAME=eureka02' xc-govern-center:0.1

 命令详解
   --dit 以守护线程可交互式启动
   --net=host  使用主机模式，容器和宿主机共用一个Ip 此时也就不存在端口转发的问题了
   --add-host eureka01:192.168.99.100 对于容器添加一个本地的域名解析 相当于直接修改Host文件
   // -e 设置环境变量
   -e JAVA_OPTIONS='-DPORT=50102 -DSERVER_URL=http://user:123@eureka01:50101/eureka/ -DDOMAIN_NAME=eureka02'
   --name eureka01 对容器取名
   
   
   
1. 需要修改host文件 添加dns 
    sudo vi /etc/hosts 
    
  
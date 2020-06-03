1. docker中安装fastdfs
docker run -ti -d --name tracker -v ~/tracker_data:/fastdfs/tracker/data --net=host season/fastdfs tracker

docker run -tid --name storage -v ~/storage_data:/fastdfs/storage/data -v ~/store_path:/fastdfs/store_path --net=host -e TRACKER_SERVER:192.168.99.100:22122 season/fastdfs storage
2.  修改storage中tracker的ip
    2.1 将配置文件拷贝到宿主机
        docker cp storage:/fdfs_conf/storage.conf ~/
    2.2 然后通过vi 修改下面这个数据
        tracker_server=实际的tracker:22122
    2.3 将文件拷贝到容器中
        docker cp ~/storage.conf storage:/fdfs_conf/
    2.4 重启容器 
       docker stop storage
       docker start storage
 

3. 
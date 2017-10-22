# Demo 使用说明 
 
## 文件说明 
 
```$xslt 
config/SetipPerl.pl     ip地址申请脚本 
config/iplist_xx.json   ip地址配置文件 
 
Demo/TaskBase/ 
    NIO_more_Client     接收方 
    NIO_more_Server     视频发送方 
``` 
 
## 使用方法 
 
1. 确保作为服务器的机器有摄像头可用 
2. 确保通信双方可以通过ip地址ping通(多个ip) 
 
### ip环境配置 
 
在配置文件 `iplist_xx.json` 中写入双方可用的ip地址 
 
```$xslt 
{ 
  "server": [ 
    "192.168.43.150", 
    "192.168.43.151", 
    "192.168.43.152", 
    "192.168.43.153", 
    "192.168.43.154", 
    "192.168.43.155", 
    "192.168.43.156", 
    "192.168.43.157", 
    "192.168.43.158", 
    "192.168.43.159" 
  ], 
  "client": [ 
    "192.168.43.170", 
    "192.168.43.171", 
    "192.168.43.172", 
    "192.168.43.173", 
    "192.168.43.174", 
    "192.168.43.175", 
    "192.168.43.176", 
    "192.168.43.177", 
    "192.168.43.178", 
    "192.168.43.179" 
  ] 
} 
``` 
 
然后根据上面的地址为 对应服务器, 客户机申请地址(windows 直接添加) 
 
如果使用 `Linux` 系统,可以执行`SetipPerl.pl` 或 `SetipPy.py` 脚本 快速申请ip地址 

> 若使用perl脚本初始化环境出现模块依赖问题，可以使用 python3脚本
 
```$xslt 
sudo perl SetipPerl.pl 配置文件所路径   网卡   类型[server | client] 
 
sudo perl SetipPerl.pl iplist_MP.json wlan0 server  # 给服务器初始化地址 
sudo perl SetipPerl.pl iplist_MP.json wlan1 client  # 给客户机初始化地址 

sudo python3 SetipPy.pl 配置文件所路径   网卡   类型[server | client] 
sudo python3 SetipPy.pl iplist_MP.json wlan0 server  # 给服务器初始化地址 
sudo python3 SetipPy.pl iplist_MP.json wlan1 client  # 给客户机初始化地址 
``` 
 
然后双方通过时间服务器同步一下时间, `Linux` 系统可以使用如下命令 
 
```$xslt 
sudo ntpdate 202.120.2.101 
``` 
 
### 运行Demo 
 
先修改  
 
```java 
//Demo/TaskBase/NIO_more_Client.java 
.... 
public static void main(String[] args) { 
        InetAddrCreater ic = new InetAddrCreater("./config/iplist_MP.json"); 
.... 
 
 
//Demo/TaskBase/NIO_more_Server.java 
.... 
public static void main(String[] args) { 
        InetAddrCreater ic = new InetAddrCreater("./config/iplist_MP.json"); 
 
.... 
``` 
 
把上述路径改为第一步配置的json文件路径 
 
运行,可以发现视频传输正常 
 
可以开启Ddos 对单一端口进行攻击,并不会影响视频传输质量

>2017.1.24

# 第一次跳变方法总结

## 第一次跳变方法的思路：

1. 开启一个子线程，然后子线程进行同步
2. 主线程只是调用send，等函数发送信息

## 情况

无法正常通信，而且出现了资源占用过高情况

## 失败原因猜想：

1. 子线程同步时候多次绑定一个相同的端口，直接导致了通信失败（端口被断开后有可能不能马上再次被使用）
2. 子线程负责同步，主线程负责IO，但是两个线程同步问题没有考虑进去


******************************

> 2017.1.23 11:31

没有尝试修复前面的问题，而是接着开发测试代码，查看一下端口重复绑定会不会影响

结果是无法正常通信，同步策略需要更改

******************************

> 2017.1.23 11:14

跳变类依然出现端口重复绑定错误

```
java.net.SocketException: Already bound
	at sun.nio.ch.Net.translateToSocketException(Net.java:125)
	at sun.nio.ch.DatagramSocketAdaptor.bind(DatagramSocketAdaptor.java:93)
	at Transport.Jump_UDP.run(Jump_UDP.java:110)
	at java.lang.Thread.run(Thread.java:745)
```

测试发现虽然get到了不同的value值，但是通过这个值算出来的端口，ip都没有改变，因此出现了重复绑定的问题
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
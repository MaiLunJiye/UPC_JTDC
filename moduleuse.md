# 模块使用文档

## 简单开始

首先双方都有一个ip端口地址表用于存可用于跳变通信的IP,端口

```
InetSocketAddress[] Alise_addrs = {
        new InetSocketAddress("102.168.1.100", 1000),
        new InetSocketAddress("102.168.1.100", 1001),
        new InetSocketAddress("102.168.1.100", 1002),
        new InetSocketAddress("102.168.1.100", 1003),
        ........
        new InetSocketAddress("102.168.1.122", 1003),
        ......
};


InetSocketAddress[] Bob_addrs = {
        new InetSocketAddress("102.168.2.100", 1000),
        new InetSocketAddress("102.168.2.100", 1001),
        new InetSocketAddress("102.168.2.100", 1002),
        new InetSocketAddress("102.168.2.100", 1003),
        ........
        new InetSocketAddress("102.168.2.122", 1003),
        ......
};
```

这两张张表顺序敏感,双方必须顺序一致

然后双方需要一个同步的KEY 假设为字符串`Alise_key` 和字符串`Bob_key`

然后开启同步服务

```
Task_TP_control Alise_IO = 
    new Task_TP_control(
        Alise_addrs, Bob_addrs,
        "Alise_key", "Bob_key"
);
```

同样的对方也需要开启相应的同步服务


当Alise 需要发送给 Bob报文

```
Bytebuffer sendsomething = new Bytebuffer.allocate(40000);
.... //write something in the buffer

Alise_IO.writeData(sendsomething);
```

当Alise 需要读取信道的信息时

```
Bytebuffer readbuf = new Bytebuffer.allocate(40000);
Alise_IO.readData(readbuf);
```

## 自定义同步策略

默认的同步策略是 **基于时间的 10Hz 的加盐MD5 算法**,如果需要自定义同步策略,那么只需要实现接口 `CanCountJumpValue`

```
public class My_Counter implements CanCountJumpValue{
    @Override
    public int countJumpValue(String key) {
        ........
        // 返回大于0的整数
        return  Math.abs(retvalue);
    }
}

//然后使用自己定义的跳变规则
Task_TP_control Alise_IO = 
    new Task_TP_control(
        Alise_addrs, Bob_addrs,
        "Alise_key", "Bob_key"
);

Alise_IO.setJumpValueCounter(
    new My_Counter()
);
```

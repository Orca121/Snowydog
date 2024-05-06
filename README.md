# Snowydog
**一个mini-tomcat**

- 仅一个HTTP Connector，不支持HTTPS；
- 仅支持挂载到`/`的一个Context，不支持多个Host与多个Context。
- 基于Servlet 6.0规范

因为只有一个Context，所以也只能有一个Web App。Jerrymouse Server的架构如下：

```ascii
  ┌───────────────────────────────┐
  │       Snowydog Server       │
  │                 ┌───────────┐ │
  │  ┌─────────┐    │  Context  │ │
  │  │  HTTP   │    │┌─────────┐│ │
◀─┼─▶│Connector│◀──▶││ Web App ││ │
  │  └─────────┘    │└─────────┘│ │
  │                 └───────────┘ │
  └───────────────────────────────┘
```

- 要运行多个Web App怎么办？

运行多个Snowydog Server就可以运行多个Web App了。

- 只支持HTTP，如果一定要使用HTTPS怎么办？

HTTPS可以部署在网关，通过网关将HTTPS请求转发为HTTP请求给Snowydog Server即可。部署一个Nginx就可以充当网关：

```ascii
               ┌───────────────────────────────┐
               │       Snowydog Server       │
               │ ┌─────────────────────────────┴─┐
               │ │       Snowydog Server       │
    ┌───────┐  │ │ ┌─────────────────────────────┴─┐
    │       │◀─┼─│ │       Snowydog Server       │
    │       │  │ │ │                 ┌───────────┐ │
◀──▶│ Nginx │◀─┼─┼─│  ┌─────────┐    │  Context  │ │
    │       │  └─┤ │  │  HTTP   │    │┌─────────┐│ │
    │       │◀───┼─┼─▶│Connector│◀──▶││ Web App ││ │
    └───────┘    └─┤  └─────────┘    │└─────────┘│ │
                   │                 └───────────┘ │
                   └───────────────────────────────┘
```

此外，Nginx还可以定义多个Host，根据Host转发给不同的Snowydog Server，所以，我们专注于实现一个仅支持HTTP、仅支持一个Web App的Web服务器，把HTTPS、HTTP/2、HTTP/3、Host、Cluster（集群）等功能全部扔给Nginx即可。

# jsql
java sql database like mysql
# 2017/8/23更新
* 之前用的是java。后面联系kotlin。全部用kotlin重写。
* 数据库引擎可以用自己开发的kv存储，但是这个项目采用了更成熟的开源存储。
* 前端实现mysql通信协议，用netty框架。
* 中间sql解析用到了druid。
* 前面只是静态解析。还需要原数据目录来判断语法树的合法性。
* 所有检查通过以后，再翻译成底层的引擎api
* 分布式用hazelcast。同步原数据，分布式队列等等。
* 客户端负载均衡需要其他的专门的软件。
* 审计，用elasticsearch数据库做审计数据库，why？几乎实时的分析审计数据，实时的图形化展示。
* 修改elasticseach源码，只能增加数据，不能删除和修改数据
* 审计前端用了grafana。当然也可以用kabana，只是前面的更好看点，，

### 用完key要remove掉！
```
    while (iterator.hasNext()){
        SelectionKey key=iterator.next();
        //一定要remove！
        iterator.remove();
        log.debug("key:{}",key);
        ...
        ...
    }
```
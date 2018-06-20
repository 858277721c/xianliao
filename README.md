# About
闲聊sdk封装

# Gradle
[![](https://jitpack.io/v/zj565061763/xianliao.svg)](https://jitpack.io/#zj565061763/xianliao)

# 使用
1. 配置appid
在项目的[string.xml](https://github.com/zj565061763/xianliao/blob/master/lib/src/main/res/values/strings.xml)中配置appid
```xml
<!DOCTYPE resources [<!ENTITY sg_appid "你的appid">]>
```

2. 初始化
```java
SGManager.getInstance().init(Context context);
```

3. 获得ISGAPI对象
```java
SGManager.getInstance().getSGAPI();
```

4. 分享图片
```java
SGManager.getInstance().shareImage(Bitmap bitmap);
```

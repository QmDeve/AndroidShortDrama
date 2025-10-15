<div align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java" alt="Java 21"/>
  <img src="https://jitpack.io/v/QmDeve/AndroidShortDrama.svg" alt="Java 21"/>

  <br>

 <h2>七猫短剧 河马短剧 百度短剧API封装为Android Lib</h2>

  <br>

  <a href="#快速集成">快速集成</a> • 
  <a href="#七猫短剧">七猫短剧</a> • 
  <a href="#河马短剧">河马短剧</a> • 
  <a href="#百度短剧">百度短剧</a> • 

</div>
<div align="center">

[问题反馈](https://github.com/QmDeve/AndroidShortDrama/issues) • 
[QQ交流群](https://qm.qq.com/q/P1TTAiSN6k) • 
[Telegram群](https://t.me/QmDeves)
</div>

---

## 快速集成

#### 1. 添加仓库配置：

<br>

在项目根目录的 settings.gradle 中添加：

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
       mavenCentral()
       maven { url 'https://jitpack.io' }
  }
}
```

<br>

#### 2. 添加依赖：
<br>
在模块的 build.gradle 中添加：

```gradle
dependencies {
   implementation 'com.github.QmDeve:AndroidShortDrama:v1.0.1'
}
```

<br>

## 七猫短剧
### 创建实例

```java
QimaoShortDrama qimaoShortDrama = new QimaoShortDrama();
```

### 短剧推荐

```java
// 传入页码,回调监听器
qimaoShortDrama.recommendDrama("1", new QimaoShortDrama.OnResultListener() {
    
    // 当推荐成功时的回调方法
    @Override
    public void onSuccess(String result) {
     
    }

    // 当推荐失败时的回调方法
    @Override
    public void onFailure(Exception e) {
       
    }
});
```

### 短剧搜索

```java
// 传入短剧名,页数,回调监听器
qimaoShortDrama.searchDrama("短剧名", "1", new QimaoShortDrama.OnResultListener() {
        @Override
        public void onSuccess(String result) {
          
        }

        @Override
        public void onFailure(Exception e) {
          
        }
    }
);
```

### 获取短剧详情

```java
// 传入短剧id,回调监听器
qimaoShortDrama.getDramaInfo("10000", new QimaoShortDrama.OnResultListener() {
        @Override
        public void onSuccess(String result) {
          
        }

        @Override
        public void onFailure(Exception e) {
          
        }
    }
);
```

<br>

## 河马短剧
### 获取实例

```java
HemaShortDrama hemaShortDrama = HemaShortDrama.getInstance();
```

### 短剧搜索

```java
// 传入短剧名,回调监听器
hemaShortDrama.searchListDrama("短剧名", new HemaShortDrama.OnResultListener<String>() {
    @Override
    public void onSuccess(String result) {
        
    }
    
    @Override
    public void onFailure(String errorMsg) {
     
    }
});
```

### 获取剧集列表

```java
// 传入短剧ID,回调监听器
hemaShortDrama.episodeListDrama("123221", new HemaShortDrama.OnResultListener<String>() {
    @Override
    public void onSuccess(String result) {
       
    }
    
    @Override
    public void onFailure(String errorMsg) {
     
    }
});
```

### 获取播放链接

```java
ArrayList<String> chapterIds = new ArrayList<>();
chapterIds.add("12345678");
chapterIds.add("123456");

// 传入短剧ID,剧集ID,剧集ID列表,回调监听器
hemaShortDrama.playListDrama("123456", "123567", chapterIds, new HemaShortDrama.OnResultListener<String>() {
    @Override
    public void onSuccess(String result) {
       
    }
    
    @Override
    public void onFailure(String errorMsg) {
      
    }
});
```

### 获取分类标签

```java
hemaShortDrama.typeDrama(new HemaShortDrama.OnResultListener<String>() {
    @Override
    public void onSuccess(String result) {
        
    }
    
    @Override
    public void onFailure(String errorMsg) {
        
    }
});
```

### 获取分类推荐列表

```java
//传入分类ID,分类名称,页码,回调监听器
hemaShortDrama.typeListDrama("1", "爱情", "1", new HemaShortDrama.OnResultListener<String>() {
    @Override
    public void onSuccess(String result) {
        
    }
    
    @Override
    public void onFailure(String errorMsg) {
       
    }
});
```

<br>

## 百度短剧
### 创建实例

```java
BaiduShortDrama baiduDrama = new BaiduShortDrama();
```

### 短剧搜索

```java
// 传入短剧名,页码,回调监听器
baiduDrama.searchDrama("短剧名", 1, new BaiduShortDrama.OnResultListener<>() {
    @Override
    public void onSuccess(SearchResponse result) {
       
    }
    
    @Override
    public void onFailure(String errorMsg) {
       
    }
});
```

### 获取剧集列表

```java
// 传入短剧ID,回调监听器
baiduDrama.getEpisodeList("4409265997409778202", new BaiduShortDrama.OnResultListener<EpisodeListResponse>() {
    @Override
    public void onSuccess(EpisodeListResponse result) {
    
    }
    
    @Override
    public void onFailure(String errorMsg) {
       
    }
});
```

### 获取剧集详情

```java
// 传入剧集视频ID,回调监听器
baiduDrama.getVideoDetail("video_123456", new BaiduShortDrama.OnResultListener<VideoDetailResponse>() {
    @Override
    public void onSuccess(VideoDetailResponse result) {
     
    }
    
    @Override
    public void onFailure(String errorMsg) {
      
    }
});
```

---

<div align="center">
如果这个项目对您有帮助，请给个⭐️星标支持！

[问题反馈](https://github.com/QmDeve/AndroidShortDrama/issues) • 
[QQ交流群](https://qm.qq.com/q/P1TTAiSN6k) • 
[Telegram群](https://t.me/QmDeves)

</div>

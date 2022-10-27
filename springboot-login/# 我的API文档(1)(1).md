# 我的API文档

## 接口


### 作品(work类型)
- (POST)    /work  上传新的作品
- (PUT)     /work   更新已有的作品
- (DELETE)  /work/{workId}  删除对应id的作品
- (GET)     /work/{Name}    查找对应名称的作品及作品报表
- ps：作品报表要一个数据结构
<!-- - (GET)     /work/{Name}     -->

### 用户(user类型)
- (POST)    /user   创建新用户
- (PUT)     /user   修改用户信息
- (GET)     /user/login 用户登录
- (GET)     /user/logout 用户登出
- (DELETE)  /user/{username} 注销用户
<!-- - 用户充值付费 -->
- (GET)     /user/{username}    搜索用户
- (GET)     /user/{username}/findWorks     返回用户全部作品
## 模型

``` javascript
User{
    id	        integer($int64)
    username	string
    firstName	string
    lastName	string
    email	    string
    password	string
    phone	    string
    belongings  [..Workid]
    userStatus	{
        vip         bool
        endtime     string($date-time)
        balcklist   bool
        logon       boolean     
    }
                // User Status
},
ApiResponse{
    code	    integer($int32)
    type	    string
    message	    string
},
work{//作品信息
    id	        integer($int64)//
    category	Category{//
        id	    integer($int64)
        name    string
    }\
    fingerprint{
        value   bool
        date    string($date-time)
    }
    watermark{
        value   bool
        date    string($date-time)
    }
    owner       string
    time        Time{//
        upload      string($date-time)//上传
        check       string($date-time)//上次检查
        interval    string($date-time)//周期
        // period      string($date-time)
    }
    name*	    string                //作品名称
    photoUrls*	[                     //封面
    //xml: OrderedMap { "wrapped": true }
    string
    xml: OrderedMap { "name": "photoUrl" }//看不懂
    xml:
        name: photoUrl]
//     tags	[
//     xml: OrderedMap { "wrapped": true }
//     Tag{
//         id	integer($int64)
//         name	string
// }]
    status	string  //作品阶段

    //pet status in the store
        Enum:
        [ uploading, checking, passed ]//上传状态
    count   Count{
        censord         integer($int32)//审查次数
        affendingnum    integer($int32)//侵权作品数
    }       
    keyword     [..string]//关键词
    abstract    string//摘要
    workfile    string//这是作品的下载地址
    urllist     [..string]//侵权网址
},

```

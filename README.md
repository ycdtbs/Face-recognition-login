#### 百度云Api与SpringBoot实现的人脸识别登录功能
**任何问题都可通过bilibli私信我，虽然我很菜，但是很乐意解答**  
[bilibli](https://space.bilibili.com/97483909)  
项目中非常重要的gettoken的方法中AK与SK需要自己从百度云人脸识别库获取（代码中删去了这部分）
#### 1、项目简介
项目中使用了SpringBoot+Thymleaf+百度云的API接口实现的人脸识别功能，其中融合了人脸库的管理功能，实现对人脸的增删改查。  
对于原生的百度云API文档可以点击下载   
[点击下载百度云API官方文档--32M](https://githubpicture.oss-cn-beijing.aliyuncs.com/FACE.pdf)   
项目演示和流程介绍可参考视频   
[视频演示](https://www.bilibili.com/video/av93519949#reply2469727748)
![image](https://githubpicture.oss-cn-beijing.aliyuncs.com/QQ%E6%88%AA%E5%9B%BE20200304214346.png)
![image](https://githubpicture.oss-cn-beijing.aliyuncs.com/QQ%E6%88%AA%E5%9B%BE20200304214430.png)
![image](https://githubpicture.oss-cn-beijing.aliyuncs.com/QQ%E6%88%AA%E5%9B%BE20200304215800.png)
![image](https://githubpicture.oss-cn-beijing.aliyuncs.com/QQ%E6%88%AA%E5%9B%BE20200304214510.png)
#### 2、接口方法
Face包下的的BaiduAiFace类是整个核心功能的接口  
**FaceRegistration** 方法为人脸注册   
**FaceUpdate** 方法为人脸更新  
**FindPersonFaceList** 方法为查询人脸信息
**FindGroupList** 为查询本组的面部信息
**DelPersonFace** 为删除人脸   
**FaceSearch**  为查找人脸
#### 3、接口参数
对于每个参数的具体含义与提要提供的参数参考百度云官方文档
```java
@Component
public class Setingmodel {
    private String imgpath;
    private String GroupID;
    private String UserID;
    private String Quality_Control;
    private String Image_Type;
    private String Liveness_Control;
    private String Userinf;

    public String getUserinf() {
        return Userinf;
    }

    public void setUserinf(String userinf) {
        Userinf = userinf;
    }

    public Setingmodel() {
        /**
         * 图片类型
         * BASE64:图片的base64值，base64编码后的图片数据，编码后的图片大小不超过2M；
         * URL:图片的 URL地址( 可能由于网络等原因导致下载图片时间过长)；
         * FACE_TOKEN：人脸图片的唯一标识，调用人脸检测接口时
         * 会为每个人脸图片赋予一个唯一的FACE_TOKEN
         * 同一张图片多次检测得到的FACE_TOKEN是同一个。
         */
        this.Image_Type = "BASE64";
        /**
         * 图片质量控制
         * NONE: 不进行控制
         * LOW:较低的质量要求
         * NORMAL: 一般的质量要求
         * HIGH: 较高的质量要求
         * 默认 NONE
         */
        this.Quality_Control = "NONE";
        /**
         * 活体检测控制
         * NONE: 不进行控制
         * LOW:较低的活体要求(高通过率 低攻击拒绝率)
         * NORMAL: 一般的活体要求(平衡的攻击拒绝率, 通过率)
         * HIGH: 较高的活体要求(高攻击拒绝率 低通过率)
         * 默认NONE
         */
        this.Liveness_Control = "NONE";
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getQuality_Control() {
        return Quality_Control;
    }

    public void setQuality_Control(String quality_Control) {
        Quality_Control = quality_Control;
    }

    public String getImage_Type() {
        return Image_Type;
    }

    public void setImage_Type(String image_Type) {
        Image_Type = image_Type;
    }

    public String getLiveness_Control() {
        return Liveness_Control;
    }

    public void setLiveness_Control(String liveness_Control) {
        Liveness_Control = liveness_Control;
    }
}

```


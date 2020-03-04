package com.example.facelogin.Face;

import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.util.Base64Util;
import com.example.facelogin.SetingModel.Setingmodel;
import com.example.facelogin.Utils.GetToken;
import com.example.facelogin.Utils.GsonUtils;
import com.example.facelogin.Utils.HttpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BaiduAIFace {

    private static String token = GetToken.getAuth();
    /**
     * 人脸注册
     */
    public   Map FaceRegistration(Setingmodel facaddseting) throws IOException {
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
        Map resultmap = FaceAddAndUpdate(facaddseting, url);
        return resultmap;

    }

    /**
     * 人脸更新
     * @param facaddseting 参数设置
     * @return 返回信息map
     * @throws IOException
     */
    public  Map FaceUpdate(Setingmodel facaddseting) throws IOException {
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/update";
        Map resultmap = FaceAddAndUpdate(facaddseting, url);
        return resultmap;

    }
    private Map FaceAddAndUpdate(Setingmodel facaddseting, String url) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(facaddseting.getImgpath()));
        String imagebase64 = Base64Util.encode(bytes);
        Map<String, Object> map=new HashMap<>();
        try {
            map.put("image", imagebase64);
            map.put("group_id", facaddseting.getGroupID());
            map.put("user_id", facaddseting.getUserID());
            map.put("liveness_control", facaddseting.getLiveness_Control());
            map.put("image_type", facaddseting.getImage_Type());
            map.put("quality_control", facaddseting.getQuality_Control());
            String param = GsonUtils.toJson(map);
            String result = HttpUtil.post(url, token, "application/json", param);
            Map resultmap = GsonUtils.fromJson(result, Map.class);
            return resultmap;
        } catch (Exception e) {
            System.out.println("失败");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询这个人的面部信息
     * @param setingmodel
     * @return map
     */
    public Map FindPersonFaceList(Setingmodel setingmodel){
        String url="https://aip.baidubce.com/rest/2.0/face/v3/faceset/face/getlist";
        Map<String, Object> map=new HashMap<>();
        if(!map.isEmpty()){
            map.clear();
        }
        map.put("group_id", setingmodel.getGroupID());
        map.put("user_id", setingmodel.getUserID());
        String param = GsonUtils.toJson(map);

        try {
            String result = HttpUtil.post(url, token, "application/json", param);
            Map resultmap = GsonUtils.fromJson(result, Map.class);
            return resultmap;
        } catch (Exception e) {
            System.out.println("查询失败");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询本组的面部信息
     * @param setingmodel
     * @return
     */
    public Map FindGroupList(Setingmodel setingmodel) {
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/group/getusers";
        Map<String, Object> map=new HashMap<>();
        map.put("group_id",setingmodel.getGroupID());
        String param = GsonUtils.toJson(map);
        try {
            String result = HttpUtil.post(url, token, "application/json", param);
            Map resultmap = GsonUtils.fromJson(result, Map.class);
            return resultmap;

        } catch (Exception e) {
            System.out.println("未查询到人脸信息");
            e.printStackTrace();
        }
        return null;
    }
    public Map DelPersonFace(Setingmodel setingmodel){
        String url="https://aip.baidubce.com/rest/2.0/face/v3/faceset/face/delete";
        Map map = FindPersonFaceList(setingmodel);
        Object result = map.get("result");
        String s = GsonUtils.toJson(result);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String face_token = jsonObject.getString("face_list");
        String substring = face_token.substring(2, face_token.length() - 2);
        String[] split = substring.split("\"");
        face_token=split[7];
        System.out.println(face_token);
        map.put("group_id", setingmodel.getGroupID());
        map.put("user_id", setingmodel.getUserID());
        map.put("face_token",face_token);
        String param = GsonUtils.toJson(map);
        try {
            String result2 = HttpUtil.post(url, token, "application/json", param);
            Map resultmap = GsonUtils.fromJson(result2, Map.class);
            return resultmap;
        } catch (Exception e) {
            System.out.println("删除失败");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 人脸查找
     * @return
     */
    public Map FaceSearch(Setingmodel setingmodel) throws IOException {
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/search";
//        byte[] bytes = Files.readAllBytes(Paths.get(setingmodel.getImgpath()));
//        String imagebase64 = Base64Util.encode(bytes);
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("image", setingmodel.getImgpath());
            map.put("liveness_control", setingmodel.getLiveness_Control());
            map.put("group_id_list", setingmodel.getGroupID());
            map.put("image_type", setingmodel.getImage_Type());
            map.put("quality_control", setingmodel.getQuality_Control());
            String param = GsonUtils.toJson(map);
            String result = HttpUtil.post(url, token, "application/json", param);
            Map<String,Object> resultmaps = GsonUtils.fromJson(result, Map.class);
            System.out.println("cuowudaima"+resultmaps.get("error_code"));
            if(!resultmaps.get("error_code").toString().equals("222202.0")){
                String resultlist = resultmaps.get("result").toString();
                String substring = resultlist.substring(1, resultlist.length() - 1);
                String regEx="[\n`~!@#$%^&*()+|{}':;'\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
                String aa = "";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(substring);//这里把想要替换的字符串传进来
                String newString = m.replaceAll(aa).trim();
                String[] split = newString.split(",");
                split[1]=split[1].substring(10, split[1].length());
                String face_token=split[0].substring(11,split[0].length());
                String group_id=split[1].substring(9,split[1].length());
                String user_id=split[2].substring(8,split[2].length());
                String user_info=split[3].substring(10,split[3].length());
                String score=split[4].substring(6,split[4].length());
                System.out.println(face_token);
                resultmaps.put("face_token",face_token);
                resultmaps.put("group_id",group_id);
                resultmaps.put("user_id",user_id);
                resultmaps.put("user_info",user_info);
                resultmaps.put("score",score);
                return resultmaps;

            }else {
                System.out.println("失败分支");
                return resultmaps;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

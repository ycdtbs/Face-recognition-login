package com.example.facelogin.Service;

import com.example.facelogin.Face.BaiduAIFace;
import com.example.facelogin.SetingModel.Setingmodel;
import com.example.facelogin.Utils.GsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class LoginService {
    @Autowired
    BaiduAIFace faceapi;
    @Autowired
    Setingmodel setingmodel;
    public Map<String,Object> searchface(StringBuffer imagebase64) throws IOException {
        String substring = imagebase64.substring(imagebase64.indexOf(",")+1, imagebase64.length());
        setingmodel.setImgpath(substring);
        setingmodel.setGroupID("StRoot");
        System.out.println(substring);
        Map map = faceapi.FaceSearch(setingmodel);
        return map;

    }
}

package com.itheima.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;


public class QiNiuTest {

    @Test
    public void test1(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "GVsQgX2B88lNZsp0tm-bF9xFAizX-h8Hvgyhtwlo";
        String secretKey = "r5vtKItyJ15sccLzUZ6HNzuyFSPhmA8GX6rY6M-0";
        String bucket = "bingo666healthcast";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "D:\\javalearn\\学习资料\\资料-传智健康项目\\day04\\资源\\图片资源\\03a36073-a140-4942-9b9b-712cecb144901.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "abc.jpg";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    @Test
    public void test2(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释
        String accessKey = "access key";
        String secretKey = "secret key";
        String bucket = "bucket name";
        String key = "file key";
//过期天数，该文件10天后删除
        int days = 10;
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
//            bucketManager.deleteAfterDays(bucket, key, days);
            bucketManager.delete(bucket,key);
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
    }
}

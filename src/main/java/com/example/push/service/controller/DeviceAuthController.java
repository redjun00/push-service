package com.example.push.service.controller;

import com.example.push.service.domain.Device;
import com.example.push.service.repo.DeviceRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class DeviceAuthController {

    @Autowired
    private DeviceRepo deviceRepo;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public boolean saveDeviceToken(@RequestBody Device device){
        boolean isSaved = false;
        log.info("try to save device token. device={}", device);
        device.setStartedAt(DateTime.now().toDate());
        log.info("device={}", device.toString());
        try {
            deviceRepo.save(device);
            isSaved=true;
        }catch (Exception e){
            log.info("already registered device token.");
        }
        return isSaved;
    }

    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public void pushMessageToAll(@RequestBody String messageToPush){
        log.info("try to push message. message={}", messageToPush);
        deviceRepo.findAll().forEach(device -> {
            try {
                askToSendMessage(device, messageToPush);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    public static final String SERVER_KEY = "AAAAmSwlYYY:APA91bEQnHqo4fm94ThHXYdOAYODffDQ0YARyjosoYUeUmBI7VFRz1In_2GBlmRFwSM7KRmFtNmzfur4rCv87htNALvvVieiEehdU_X0bNvo-ShHRghi6ghazlgDFJncNo4uYPtUdDUD";
    private void askToSendMessage(Device device, String messageToPush) throws JsonProcessingException {

        Map<String, String> message = new HashMap<>();
        message.put("message", messageToPush);

        Map<String, Object> jsonBody = new HashMap<>();
        jsonBody.put("to", "/topics/news");
        jsonBody.put("data", message);

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(jsonBody);
        log.info("body={}", body);

        //build request
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("fcm.googleapis.com")
                .addPathSegment("fcm")
                .addPathSegment("send")
                .build();

        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(null, body);

        Request request = new Request.Builder()
                .url(httpUrl)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "key="+SERVER_KEY)
                .build();

        log.info("***** request={} headers={} body={}", request, request.headers(), request.body());
        OkHttpClient client = new OkHttpClient();


        Callback callBackAfterRequest = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                log.info("call={}, response={}, body={}", call, response, response.body().string());
            }
        };
        client.newCall(request).enqueue(callBackAfterRequest);
    }

    //jmeter test용
    /*
    curl \
      -F "file=@/home/nrkim/tool/jmeter/temp/json_request.txt" \
      http://localhost:8080/fileupload
     */
    @RequestMapping(value = "/fileupload", method = RequestMethod.POST)
    public String addFile(@RequestPart MultipartFile file){
        String originalFileName = file.getOriginalFilename();
        log.info("this is add file. file name={} file type={}", originalFileName, file.getContentType());
        log.info("new file path={}", this.getClass().getResource("/").toString());

        String dirPath = System.getProperty("user.dir") + "/build/temporary/";
        new File(dirPath).mkdir();
        File destinationDir = new File(dirPath + originalFileName);
        try {
            file.transferTo(destinationDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destinationDir.getAbsolutePath();
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void testGet(){
        log.info("this is test");
    }
}
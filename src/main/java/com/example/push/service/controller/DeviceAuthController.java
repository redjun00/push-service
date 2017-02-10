package com.example.push.service.controller;

import com.example.push.service.domain.Device;
import com.example.push.service.repo.DeviceRepo;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/push")
public class DeviceAuthController {

    @Autowired
    private DeviceRepo deviceRepo;

    @RequestMapping(value = "/{deviceToken}", method = RequestMethod.POST)
    public void saveDeviceToken(@PathVariable Long deviceToken){
        log.info("try to save device token. deviceToken={}", deviceToken);
        Device device = new Device();
        device.setDeviceToken(deviceToken);
        device.setStartedAt(DateTime.now().toDate());
        log.info("device={}", device.toString());
        deviceRepo.save(device);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void testGet(){
        log.info("this is test");
    }
}

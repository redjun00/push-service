package com.example.push.service.repo;

import com.example.push.service.domain.Device;
import org.springframework.data.repository.CrudRepository;

public interface DeviceRepo extends CrudRepository<Device, Long>{
}

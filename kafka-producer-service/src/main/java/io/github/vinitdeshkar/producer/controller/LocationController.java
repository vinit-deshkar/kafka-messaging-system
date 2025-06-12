package io.github.vinitdeshkar.producer.controller;


import io.github.vinitdeshkar.producer.model.DriverLocation;
import io.github.vinitdeshkar.producer.service.LocationPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LocationController {

    @Autowired
    private LocationPublisherService locationPublisherService;

    @PostMapping("/updateLocation")
    public String updateLocation(@RequestBody DriverLocation location) {
        locationPublisherService.publishLocation(location);
        return "Location update published successfully.";
    }
}

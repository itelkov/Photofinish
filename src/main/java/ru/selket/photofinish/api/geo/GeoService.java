package ru.selket.photofinish.api.geo;


import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.selket.photofinish.api.ApiClient;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Log
@Component
public class GeoService {

    @Autowired
    private ApiClient apiClient;

    private volatile Map<Long,String> geoMap = new HashMap<>();

    @PostConstruct
    public void init() {
        log.info("init geo");
        refreshGeo();
    }

    public String getGeoName(long id){
        return geoMap.get(id);
    }

    private void refreshGeo(){
        geoMap.clear();
        apiClient.getGeoList().getData().forEach(geo -> geoMap.put(geo.getId(), geo.getName()));
    }

    @Scheduled(fixedRate = /*1 **/ 3600 * 1000, initialDelay = 2 * 1000)
    public void scheduledRefreshGeo(){
        refreshGeo();
    }
}

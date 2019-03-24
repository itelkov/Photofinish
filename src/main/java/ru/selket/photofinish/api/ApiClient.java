package ru.selket.photofinish.api;

import feign.*;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.selket.photofinish.api.geo.GeoData;
import ru.selket.photofinish.api.log.ApiLog;
import ru.selket.photofinish.api.log.ApiLogDecoder;
import ru.selket.photofinish.api.log.ApiLogService;
import ru.selket.photofinish.api.log.ApiLogThreadLocal;
import ru.selket.photofinish.api.photo.PhotoRequestPost;
import ru.selket.photofinish.api.photo.PhotoRequestResponse;
import ru.selket.photofinish.api.race.RaceData;
import ru.selket.photofinish.api.race.RaceListData;
import ru.selket.photofinish.api.race.RaceDataMeta;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;


@Component
public class ApiClient {

    @Value("${api.host}")
    private String host;

    @Value("${api.port}")
    private String port;

    @Value("${api.path}")
    private String path;

    @Value("${api.token}")
    private String token;

    private Api api;

    @Autowired
    private ApiLogService apiLogService;

    @PostConstruct
    public void init(){
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                if (token != null && !token.isEmpty()){
                    requestTemplate.header("Authorization", "Bearer " + token);
                }

                ApiLog apiLog = new ApiLog();
                apiLog.setPath(requestTemplate.path());
                apiLog.setRequest(requestTemplate.requestBody().bodyTemplate());
                apiLog.setRequestDateTime(LocalDateTime.now());
                apiLogService.set(apiLog);
                ApiLogThreadLocal.set(apiLog);
                requestTemplate.header("Request-id", apiLog.getId());
            }
        };

        api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new ApiLogDecoder(apiLogService))
                .requestInterceptor(requestInterceptor)
                //.logger(new Logger.ErrorLogger())
                //.logLevel(Logger.Level.FULL)
                .target(Api.class, "http://" + host + "/" + path);
    }

    public GeoData getGeoList(){
        return api.geoList();
    }

    public RaceData getRace(long raceId){
        return api.race(raceId);
    }

    public RaceListData getRaceList(int page){
        return api.raceList(page);
    }

    public RaceListData getRaceListNext(RaceDataMeta meta){
        RaceListData data = null;
        if (meta.getCurrentPage() < meta.getLastPage()){
            data = api.raceList(meta.getCurrentPage() + 1);
        }

        return data;
    }

    public RaceListData getRaceListPrev(RaceDataMeta meta){
        RaceListData data = null;
        if (meta.getCurrentPage() > 1){
            data = api.raceList(meta.getCurrentPage() - 1);
        }

        return data;
    }

    public RaceListData findRaceList(String query){
        return api.raceList(query);
    }

    public PhotoRequestResponse setRequest(PhotoRequestPost photoRequestPost){
        return api.setRequest(photoRequestPost).getData();
    }

    public PhotoRequestResponse getRequest(Long requestId){
        return api.getRequest(requestId).getData();
    }
}

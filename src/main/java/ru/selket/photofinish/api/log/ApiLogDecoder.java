package ru.selket.photofinish.api.log;

import feign.Response;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import lombok.extern.java.Log;

import java.io.IOException;
import java.lang.reflect.Type;

@Log
public class ApiLogDecoder implements Decoder {

    private ApiLogService apiLogService;

    public ApiLogDecoder(ApiLogService apiLogService){
        this.apiLogService = apiLogService;
    }

    public Object decode(Response response, Type type) throws IOException {
        ApiLog apiLog = ApiLogThreadLocal.get();
        apiLog.setStatus(response.status());
        apiLog.setResponse(response.body().asInputStream().toString());
        apiLogService.set(apiLog);
        log.info("ApiLog: " + apiLog);
        return new JacksonDecoder().decode(response, type);
    }
}

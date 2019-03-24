package ru.selket.photofinish.api;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import ru.selket.photofinish.api.geo.GeoData;
import ru.selket.photofinish.api.photo.PhotoRequestPost;
import ru.selket.photofinish.api.photo.PhotoRequestResponse;
import ru.selket.photofinish.api.photo.PhotoRequestResponseData;
import ru.selket.photofinish.api.race.RaceData;
import ru.selket.photofinish.api.race.RaceListData;

public interface Api {

    @RequestLine("GET /geo")
    GeoData geoList();

    @RequestLine("GET /{chat}")
    String command(@Param("chat") String command);

    @RequestLine("GET /race/{id}")
    RaceData race(@Param("id") long raceId);

    @RequestLine("GET /race?page={page}")
    RaceListData raceList(@Param("page") int page);

    @RequestLine("GET /race?name={query}")
    RaceListData raceList(@Param("query") String query);

    @RequestLine("POST /request")
    @Headers("Content-Type: application/json")
    PhotoRequestResponseData setRequest(PhotoRequestPost photoRequestPost);

    @RequestLine("GET /request/{id}")
    PhotoRequestResponseData getRequest(@Param("id") Long requestId);
}

package com.example.catalogfilms.utils;

public class UrlUtils {
    public static String getGenerateUrl(String objectType, Long id) {
        String baseUrl = "https://192.168.0.101:45455/api";
        if (id == null) {
            return baseUrl.concat("/genre");
        }

        switch (objectType) {
            case "DetailGenre": {
                return baseUrl.concat("/genre/").concat(id.toString());
            }
            case "Movie": {
                return baseUrl.concat("/movie/").concat(id.toString());
            }
            default:
                return baseUrl.concat("/genre/").concat(id.toString());
        }
    }
}

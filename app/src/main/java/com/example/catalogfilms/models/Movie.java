package com.example.catalogfilms.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "director",
        "actors",
        "rating",
        "year",
        "genreId",
        "genre",
        "poster"
})
@Data
public class Movie {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("director")
    private String director;
    @JsonProperty("actors")
    private String actors;
    @JsonProperty("rating")
    private Double rating;
    @JsonProperty("year")
    private Integer year;
    @JsonProperty("genreId")
    private Long genreId;
    @JsonProperty("genre")
    private String genre;
    @JsonProperty("poster")
    private String poster;
}
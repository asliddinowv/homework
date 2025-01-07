package com.company;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class Post {
    private int id;
    private int userId;
    private String title;
    private String body;
}

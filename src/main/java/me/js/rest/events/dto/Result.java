package me.js.rest.events.dto;

import lombok.Data;

import javax.persistence.Id;

@Data
public class Result {
    @Id
    private Long id;

    private String name;
}

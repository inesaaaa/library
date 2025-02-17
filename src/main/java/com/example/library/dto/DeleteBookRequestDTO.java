package com.example.library.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteBookRequestDTO {
    private String title;
    private String author;
}

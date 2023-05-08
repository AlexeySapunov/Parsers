package ru.sapunov.postParser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Post {

        private String title;
        private String detailsLink;
        private String author;
        private String authorDetailsLink;
        private String dateOfCreated;
}

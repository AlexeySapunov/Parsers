package ru.sapunov.postParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class App {

        public static void main(String[] args) throws IOException {
                List<Post> posts = new ArrayList<>();

                System.out.println("Connected to main page...");

                Document doc = Jsoup.connect("https://4pda.to/").get();
                Elements titleElements = doc.getElementsByAttributeValue("itemprop", "url");

                for (Element element : titleElements) {
                        String detailsLink = element.attr("href");
                        Post post = new Post();
                        post.setDetailsLink(detailsLink);
                        post.setTitle(element.attr("title"));

                        System.out.println("Connected to post details: " + detailsLink);

                        Document detailDoc = Jsoup.connect(detailsLink).get();

                        try {
                                Element authorElement = Objects.requireNonNull(detailDoc
                                        .getElementsByClass("name").first()).child(0);
                                post.setAuthor(authorElement.text());
                                post.setAuthorDetailsLink(authorElement.attr("href"));
                                post.setDateOfCreated(Objects.requireNonNull(detailDoc
                                        .getElementsByClass("date").first()).text());
                        } catch (NullPointerException e) {
                                post.setAuthor("No author");
                                post.setAuthorDetailsLink("No link");
                                post.setDateOfCreated("No date");
                        }

                        posts.add(post);
                }

                posts.forEach(System.out::println);
        }
}

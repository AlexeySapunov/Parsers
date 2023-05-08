package ru.sapunov.weatherParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

        private static final Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

        public static void main(String[] args) throws IOException {
                Document page = getPage();
                Element tableWeather = page.select("table[class=wt]").first();

                assert tableWeather != null;

                Elements names = tableWeather.select("tr[class=wth]");
                Elements values = tableWeather.select("tr[valign=top]");
                int index = 0;

                for (Element name: names) {
                        String dateString = name.select("th[id=dt]").text();
                        String date = getDataFromString(dateString);
                        System.out.println();
                        System.out.println(date + "             Явления                  t`C     Давл    Влажн       Ветер");
                        int iterationCount = printPartValues(values, index);
                        index += iterationCount;
                }
        }

        private static int printPartValues(Elements values, int index) {
                int iterationCount = 4;
                if (index == 0) {
                        Element valueLn = values.get(0);
                        boolean isMorning = valueLn.text().contains("Утро");
                        
                        if (isMorning) {
                                iterationCount = 3;
                        }
                } else if (index == 16) {
                        iterationCount = 3;
                }

                for (int i = 0; i < iterationCount; i++) {

                        Element valueLine = values.get(index + i);

                        for (Element td : valueLine.select("td")) {
                                System.out.print(td.text() + "     ");
                        }

                        System.out.println();
                }

                return iterationCount;
        }

        private static Document getPage() throws IOException {
                String url = "https://www.pogoda.spb.ru/";

                return Jsoup.parse(new URL(url), 3000);
        }

        private static String getDataFromString(String stringData) {
                Matcher matcher = pattern.matcher(stringData);

                if (matcher.find()) {
                        return matcher.group();
                }

                throw new RuntimeException("Can not extract date from string!");
        }
}

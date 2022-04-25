package com.api.testBot.parser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Day {

    private String date;
    private List<Lesson> lessons;
    private boolean isToday;

    public static Day build(Element element, boolean isToday){
        Element day = element.getElementsByClass("day-header").first();
        List<Element> elementsLesson = element.getElementsByClass("day-lesson");
        List<Lesson> lessons = new ArrayList<>();
        for (Element i : elementsLesson){
            if(!i.getElementsByClass("lesson-hour").text().isEmpty()){
                lessons.add(Lesson.build(i));
            }
        }
        return new Day(day.select("span").first().text() + " " + day.select("span").first().nextSibling().toString(), lessons , isToday);
    }

    public String getStr(){
        StringBuilder stringBuilder = new StringBuilder();
        lessons.forEach(e->stringBuilder.append(e.getStr()));
        String str = null;
        if(this.isToday){
            str = "~~~~~✅ " + this.date  + "~~~~~~\n\r"+  stringBuilder + "\r\n";
        }else {
            str = "~~~~~~" + this.date + "~~~~~~~\n\r" +stringBuilder + "\r\n ";
        }

        return str;
    }



}

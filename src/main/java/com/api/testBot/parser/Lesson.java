package com.api.testBot.parser;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jsoup.nodes.Element;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Lesson {


    private String time;
    private String lessonName;
    private String lessonType;
    private String lessonRoom;
    private String teacher;


    public static Lesson build(Element element) {
        return new Lesson(
                element.getElementsByClass("lesson-hour").text(),
                element.getElementsByClass("lesson-name").text(),
                element.getElementsByClass("lesson-type").text(),
                element.getElementsByClass("lesson-room").text(),
                element.getElementsByClass("lesson-teacher").select("a").text());
    }

    public String getStr(){
        return "\n            " + "<b>" +this.time + "</b> " + this.lessonType + "\n" +
                " " + this.lessonName + " " + "\n" +
                "                      " + this.lessonRoom + "\n" +
                " <b><u>" + this.teacher + "</u></b>\r\n";

    }


}

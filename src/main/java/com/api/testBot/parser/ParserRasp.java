package com.api.testBot.parser;


import org.checkerframework.checker.units.qual.A;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParserRasp {


    public Set<String> findFacultetsByInst(String inst) {
        try {
            Document document = Jsoup.connect("https://rasp.sstu.ru/").get();
            List<Element> element = document.getElementsByClass("card");

            for (Element e : element) {
                if (e.getElementsByClass("institute collapsed").text().equals(inst)) {
                    return e.getElementsByClass("col-auto group-start d-none d-md-block")
                            .stream()
                            .map(Element::text)
                            .collect(Collectors.toSet());
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    public List<String> getGroupsByFacultret(String facultet) {
        try {

            Document document = Jsoup.connect("https://rasp.sstu.ru/").get();
            List<Element> element = document.getElementsByClass("row groups");
            List<String> groups = new ArrayList<>();
            for (Element e : element) {
                for (Element j : e.getElementsByClass("col-auto group-start d-none d-md-block")) {
                    if (j.text().equals(facultet)) {
                        for (Element i : e.getElementsByClass("col-auto group").select("a")) {
                            groups.add(i.text());
                        }
                    }
                }
            }
            return groups;
        } catch (IOException e) {
            System.out.println("****   " + e.getMessage());
            return null;
        }
    }


    public List<String> findByGroup(String group, boolean isTwo) {
        try {
            Document document = Jsoup.connect("https://rasp.sstu.ru/").get();
            Element element = document.getElementsByClass("col-auto group").select("a:contains(" + group + ")").first();
            if (element == null) {
                return null;
            }
            Document getRasp = Jsoup.connect("https://rasp.sstu.ru/" + element.attr("href")).get();
            List<Element> weeks = getRasp.getElementsByClass("week");
            List<Element> days = new ArrayList<>();

            for (Element e : weeks) {
                for (Element j : e.getElementsByClass("day")) {
                    if (!j.hasClass("day-header-color-blue")) {
                        days.add(j);
                    }
                }
            }
            if (days.size() != 0) {
                if (isTwo) {
                    return getTwoDays(days);
                } else {
                    return getSevenDays(days);
                }
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }


    public List<String> getTwoDays(List<Element> elements) {
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).hasClass("day-current") && i < elements.size() - 1) {
                return Arrays.asList(Day.build(elements.get(i), true).getStr(), Day.build(elements.get(i + 1), false).getStr());
            } else if (elements.get(i).hasClass("day-current")) {
                return List.of(Day.build(elements.get(i), true).getStr());
            }
        }
        return null;
    }

    public List<String> getSevenDays(List<Element> elements) {
        List<String> strDays = new ArrayList<>();
        boolean flag = false;
        int counter = 0;
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).hasClass("day-current")) {
                strDays.add(Day.build(elements.get(i), true).getStr());
                flag = true;
                i++;
            }
            if (flag && (i < elements.size() && counter != 6)) {
                counter++;
                strDays.add(Day.build(elements.get(i), false).getStr());
            }

        }
        return strDays;
    }

}
package com.collegeninja.college.model;

import org.json.JSONArray;

public class CourseColleges {

    private  String id, name, thumb_img, domain;
    private JSONArray jsonArrayColleges;

    public CourseColleges(){
    }

    public CourseColleges(String id, String name, String thumb_img, String domain, JSONArray jsonArrayColleges){
        this.id = id;
        this.name = name;
        this.thumb_img = thumb_img;
        this.domain = domain;
        this.jsonArrayColleges = jsonArrayColleges;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb_img() {
        return thumb_img;
    }

    public void setThumb_img(String thumb_img) {
        this.thumb_img = thumb_img;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public JSONArray getJsonArrayColleges() {
        return jsonArrayColleges;
    }

    public void setJsonArrayColleges(JSONArray jsonArrayColleges) {
        this.jsonArrayColleges = jsonArrayColleges;
    }
}

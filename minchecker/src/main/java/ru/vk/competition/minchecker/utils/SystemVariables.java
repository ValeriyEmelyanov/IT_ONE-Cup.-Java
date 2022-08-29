package ru.vk.competition.minchecker.utils;

public class SystemVariables {

    private String api = System.getenv("rs.endpoint");

    public SystemVariables() {

    }

    public String apiRoot() {        
        return api + "/api/";
    }
}

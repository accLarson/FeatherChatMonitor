package dev.zerek.featherchatmonitor.data;

public class DistinguishGroup {

    private final Integer priority;

    private final String color;


    public DistinguishGroup(Integer priority, String color) {
        this.priority = priority;
        this.color = color;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getColor() {
        return color;
    }

}
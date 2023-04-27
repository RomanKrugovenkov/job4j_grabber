package ru.job4j.utils;

import org.quartz.SchedulerException;

public interface Grab {
    void init() throws SchedulerException;
}

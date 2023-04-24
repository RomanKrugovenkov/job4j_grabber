package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbitSQL {

    public static void main(String[] args) {
        try {
            Connection cn = connection();
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("cn", cn);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(5)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    private static Connection connection() {
        Connection cn;
        try (InputStream in = AlertRabbitSQL.class.getClassLoader().
                getResourceAsStream("rabbit.properties")) {
            Properties pr = new Properties();
            pr.load(in);
            Class.forName(pr.getProperty("driver-class-name"));
            cn = DriverManager.getConnection(
                    pr.getProperty("url"),
                    pr.getProperty("username"),
                    pr.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return cn;
    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println("Производится запись в БД...");
        }

        @Override
        public void execute(JobExecutionContext context) {
            Connection cn = (Connection) context.getJobDetail().getJobDataMap().get("cn");
            try (PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO rabbit(created_date) VALUES (?)")) {
                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                ps.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

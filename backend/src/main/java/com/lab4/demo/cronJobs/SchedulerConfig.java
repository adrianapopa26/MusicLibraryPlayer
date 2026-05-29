package com.lab4.demo.cronJobs;

import com.lab4.demo.email.EmailService;
import com.lab4.demo.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
@AllArgsConstructor
public class SchedulerConfig {
//* "0 0 * * * *" = the top of every hour of every day.
//            * "*/10 * * * * *" = every ten seconds.
//* "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
//            * "0 0 8,10 * * *" = 8 and 10 o'clock of every day.
//            * "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
//            * "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
//* "0 0 0 25 12 ?" = every Christmas Day at midnight

    private EmailService emailService;
    private UserService userService;


    @Component
    public class Scheduler {
        //@Scheduled(cron = "0 9 9 1 * ?")
        @Scheduled(cron = "0 10 2 19 5 ?")
        public void cronJobSch() throws MessagingException {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date now = new Date();
            String strDate = sdf.format(now);
            userService.allUsersForList().forEach(user -> {
                try {
                    emailService.sendScheduledEmail(user.getEmail());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
           // emailService.sendScheduledEmail("popaadriana2606@gmail.com");
        }
    }

//    @Scheduled(fixedDelay = 1000)
//    public void scheduleFixedDelayTask() {
//        System.out.println(
//                "Fixed delay task - " + System.currentTimeMillis() / 1000);
//    }
//
//    @Scheduled(fixedRate = 1000)
//    public void scheduleFixedRateTask() {
//        System.out.println(
//                "Fixed rate task - " + System.currentTimeMillis() / 1000);
//    }

//    @EnableAsync
//    public class ScheduledFixedRateExample {
//        @Async
//        @Scheduled(fixedRate = 1000)
//        public void scheduleFixedRateTaskAsync() throws InterruptedException {
//            System.out.println(
//                    "Fixed rate task async - " + System.currentTimeMillis() / 1000);
//            Thread.sleep(2000);
//        }
//    }

}
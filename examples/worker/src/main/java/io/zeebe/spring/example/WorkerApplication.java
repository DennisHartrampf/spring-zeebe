package io.zeebe.spring.example;

import io.zeebe.client.api.clients.JobClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.spring.client.EnableZeebeClient;
import io.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableZeebeClient
@Slf4j
public class WorkerApplication {

  public static void main(final String... args) {
    SpringApplication.run(WorkerApplication.class, args);
  }

  private static void logJob(final ActivatedJob job) {
    log.info(
      "complete job\n>>> [type: {}, key: {}]\n{deadline; {}]\n[headers: {}]\n[payload: {}]",
      job.getType(),
      job.getKey(),
      job.getDeadline().toString(),
      job.getHeaders(),
      job.getPayload());
  }

  @ZeebeWorker(type = "foo")
  public void handleFooJob(final JobClient client, final ActivatedJob job) {
    logJob(job);
    client.newCompleteCommand(job.getKey()).payload("{\"foo\": 1}").send().join();
  }

  @ZeebeWorker(type = "bar")
  public void handleBarJob(final JobClient client, final ActivatedJob job) {
    logJob(job);
    client.newCompleteCommand(job.getKey()).send().join();
  }
}

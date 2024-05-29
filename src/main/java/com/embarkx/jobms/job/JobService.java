package com.embarkx.jobms.job;

import com.embarkx.jobms.job.dto.JobDTO;

import java.util.List;

public interface JobService {
List<JobDTO> findAll();
void createJob(Job job);

    JobDTO findById(Long id);

  boolean deleteJobById(Long id);


 boolean  updatedJob(Long id,Job updatejob);

}

package com.embarkx.jobms.job.impl;


import com.embarkx.jobms.job.Job;
import com.embarkx.jobms.job.JobRepository;
import com.embarkx.jobms.job.JobService;
import com.embarkx.jobms.job.clients.CompanyClient;
import com.embarkx.jobms.job.clients.ReviewClient;
import com.embarkx.jobms.job.dto.JobDTO;
import com.embarkx.jobms.job.external.Company;
import com.embarkx.jobms.job.external.Review;
import com.embarkx.jobms.job.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    //private List<Job> jobs = new ArrayList<>();
@Autowired
    JobRepository jobRepository;

private CompanyClient companyClient;

private ReviewClient reviewClient;

@Autowired
RestTemplate restTemplate;

    //job repository isa bean which is managed by spring and because of this constructor we autowired at runtime
    public JobServiceImpl(JobRepository jobRepository,CompanyClient companyClient,ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient=companyClient;
        this.reviewClient=reviewClient;
    }

    //private Long nextId=1L;
    @Override
    public List<JobDTO> findAll(){
        List<Job> jobs=jobRepository.findAll();//getting all the jobs
        //List<JobWithCompanyDTO> jobWithCompanyDTOS=new ArrayList<>();//creating empty ArrayList to store DTO Objects and which we are going to return

        return jobs.stream().map(this::covertToDto).collect(Collectors.toList()) ;
    }

    private JobDTO covertToDto(Job job){

        //RestTemplate restTemplate=new RestTemplate();
//            Company company=restTemplate.getForObject(
//                    "http://COMPANY-SERVICE:8081/companies/" +job.getCompanyId(), Company.class);

        Company company=companyClient.getCompany(job.getCompanyId());


//        ResponseEntity <List<Review>> reviewResponse=restTemplate.exchange(
//                "http://REVIEW-SERVICE:8083/reviews?companyId=" + job.getCompanyId(),
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<Review>>() {
//                });
//        List<Review> reviews=reviewResponse.getBody();

        List<Review> reviews=reviewClient.getReviews(job.getCompanyId());


        JobDTO jobDTO= JobMapper.mapToJobWithCompanyDto(job,company,reviews);


        return jobDTO;
    }
    @Override
    public void createJob( Job job) {
       // job.setId(nextId++);
        jobRepository.save(job);
    }
@Override
    public JobDTO findById(Long id){
   Job job=  jobRepository.findById(id).orElse(null);
  return covertToDto(job);

    }

    @Override
    public boolean deleteJobById(Long id){
        try {
            jobRepository.deleteById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }
@Override
    public boolean updatedJob(Long id,Job updatedJob ){
    Optional<Job> jobOptional=jobRepository.findById(id);

     if(jobOptional.isPresent()){
            Job job=jobOptional.get();
         job.setTitle(updatedJob.getTitle());
         job.setDescription(updatedJob.getDescription());
         job.setLocation(updatedJob.getLocation());
         job.setMaxSalary(updatedJob.getMaxSalary());
         job.setMinSalary(updatedJob.getMinSalary());
         job.setLocation(updatedJob.getLocation());
              jobRepository.save(job);
        return true;
     }

        return false;
    }

}

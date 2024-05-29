package com.embarkx.jobms.job;

import com.embarkx.jobms.job.dto.JobDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/jobs")//it works at method level and class level at class level it works as a base URL
public class JobController {
private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<JobDTO>> findAll(){
      return ResponseEntity.ok(jobService.findAll());
    //or use this  //return new ResponseEntity<>(jobService.findAll(),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createJob(@RequestBody Job job){
    jobService.createJob(job);
        return new ResponseEntity<>("Job created Successfully",HttpStatus.OK) ;
    }


    @GetMapping("{id}")
    public ResponseEntity<JobDTO> findById(@PathVariable Long id){
        JobDTO jobDTO = jobService.findById(id);
       if( jobDTO !=null)
         return new ResponseEntity<>(jobDTO, HttpStatus.OK);//returns object with status
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        boolean delete= jobService.deleteJobById(id);
        if(delete)
        return new ResponseEntity<>("Job Deleted SuccessFully",HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping ("{id}")
    //@RequestMapping(value="/jobs/{id}",method= RequestMethod.PUT)  //at method level we can use it this way
    public ResponseEntity<String> updateJobs(@PathVariable Long id, @RequestBody Job updateJob){
 boolean updated= jobService.updatedJob(id,updateJob);//which job to update and what to update
 if(updated)
 return new ResponseEntity("Job updated SuccessFully",HttpStatus.OK);
 return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}

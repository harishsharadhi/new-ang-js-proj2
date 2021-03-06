package com.niit.controllers;
 

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.niit.dao.JobDao;
import com.niit.dao.UserDao;
import com.niit.dao.UserDaoImpl;
import com.niit.model.ErrorClass;
import com.niit.model.Job;
import com.niit.model.User;



public class JobController {
	@Autowired
	private JobDao jobDao;
	@Autowired
	private UserDao userDao;
	
	
@RequestMapping(value="/addjob",method=RequestMethod.POST)	
public ResponseEntity<?>addJob(@RequestBody Job job,HttpSession session)
{
     //Authentication or rite String email="johnsmith@xyz.com
	
	String email=(String)session.getAttribute("loginId");
	    if(email==null){
	    	ErrorClass error=new ErrorClass(4,"Unauthorised access...");
	    	return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
	    }
	//User is Authenticated.
	    UserDaoImpl UserDao;
		User user=userDao.getUser(email);
	    if(!user.getRole().equals("ADMIN")){   //if role is not ADMIN,authorised to post Job
	    	ErrorClass error=new ErrorClass(5,"Access Denied");
	    	return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
	    }
	    try{
	    job.setPostedOn(new Date());
	    jobDao.addJob(job);
	    return new ResponseEntity<Job>(job,HttpStatus.OK);
	    	  
	    }
	    catch(Exception e){
	    	ErrorClass error=new ErrorClass(6,"Unable to post job details..."+e.getMessage());
	    	return new ResponseEntity<ErrorClass>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	    }
}
@RequestMapping(value="/alljobs",method=RequestMethod.GET)
public ResponseEntity<?>getAllJobs(HttpSession session){
	//Authentication
	String email=(String)session.getAttribute("loginId");
	if (email==null){
		ErrorClass error=new ErrorClass(4,"Unauthorised access....");
		return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
	}
		 List<Job>jobs=jobDao.getAllJObs();
		 return new ResponseEntity<List<Job>>(jobs,HttpStatus.OK);
}
@RequestMapping(value="/getjob/{id}",method=RequestMethod.GET)
public ResponseEntity<?> getJob(@PathVariable int id,HttpSession session)
{//Authentication
	String email=(String)session.getAttribute("loginId");
	if(email==null){
		ErrorClass error=new ErrorClass(4,"unauthorised access...");
		return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		
	}
	Job job=jobDao.getJob(id);
	return new ResponseEntity<Job>(job,HttpStatus.OK);
	}
	
}


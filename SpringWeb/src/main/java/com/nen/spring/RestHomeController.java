package com.nen.spring;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nen.model.Student;
import com.nen.services.StudentService;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestHomeController {

    private StudentService ss;

    public StudentService getSs() {
        return ss;
    }

    @Autowired
    public void setSs(StudentService ss) {
    System.out.println("I am in resthomecontroller setting studentservice -------------");
        this.ss = ss;
    }

    @GetMapping(value = "/getAllStudents", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Student> allStudentsPage(Model model){

    System.out.println("api/getallstudent method is called --------------");
//        ResponseEntity<List<Student>>
//        ResponseEntity<String> responseEntity = new ResponseEntity(studentList, HttpStatus.OK);
        List<Student> studentList = getSs().serviceGetAllStudents();
//        new ResponseEntity(studentList, HttpStatus.OK)
        return studentList;

    }

    @GetMapping(value = "/student/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Student> getStudentPage(@PathVariable("id") Integer studentId, Model model) {

        Student student = getSs().serviceGetStudent(studentId);

        return new ResponseEntity(student, HttpStatus.OK);

    }

    @PostMapping(path = "/success",consumes = {MediaType.APPLICATION_JSON_VALUE } ,produces = {MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity registerPage(@Valid @RequestBody Student student, BindingResult br, HttpServletRequest request){

    System.out.println("these are request post request headers --------" + request.getHeader("headerkuchbhi"));

        if(br.hasErrors()){
            List<FieldError> errorsList = br.getFieldErrors();
            System.out.println("errors list is here haserrors api/success if ---------------------");
            System.out.println(errorsList);

            ResponseEntity<List<FieldError>> responseEntity = new ResponseEntity(errorsList, HttpStatus.FORBIDDEN);

            System.out.println("This is the body of post request api/success else ----------" + responseEntity.getBody() + "  response entity ------" + responseEntity);

            return responseEntity;
        }else{
            System.out.println("The saving method is called from homecontroller -------------");
            this.getSs().serviceSaveStudent(student);

//            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> responseEntity = new ResponseEntity(student, HttpStatus.CREATED);

      System.out.println("This is the body of post request ----------" + responseEntity.getBody()  + "  response entity ------" + responseEntity);

            return responseEntity;
        }
    }


    @PutMapping(value = "/update", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateStudentPage(@Valid @RequestBody Student student, BindingResult br, Model model){

        System.out.println("Student in put mapping : --------------" + student );
        if(br.hasErrors()){

            List<FieldError> errorsList = br.getFieldErrors();
            System.out.println(errorsList);

            ResponseEntity<List<FieldError>> responseEntity = new ResponseEntity(errorsList, HttpStatus.FORBIDDEN);
            System.out.println("api/update if This is update error list by responseentity.getbody() --------------------" + responseEntity.getBody() + " error list bro-----   " + errorsList);

            return responseEntity;
        }else{

            System.out.println("The saving method is called from api/update else -------------");
            getSs().serviceUpdateStudent(student);
            ResponseEntity<Student> responseEntity = new ResponseEntity(student, HttpStatus.OK);
            System.out.println("api/update else This is update error list by responseentity.getbody() --------------------" + responseEntity.getBody() + " reponseEntity bro-----   " + responseEntity);

            return responseEntity;
        }

    }
}

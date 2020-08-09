package com.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class AlphaController {

    @GetMapping(value = "/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        System.out.println("====request====");
        System.out.println("method : "+request.getMethod());
        System.out.println("servletPath : "+request.getServletPath());
        System.out.println("protocol : "+request.getProtocol());
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            System.out.println("name : "+ name);
            System.out.println("value : "+ request.getHeader(name));
        }
        System.out.println("====response====");
        System.out.println("status code : "+response.getStatus());
        Collection<String> headerNames1 = response.getHeaderNames();
        for (String name : headerNames1) {
            System.out.println("name : " + name);
            System.out.println("value : " + response.getHeader(name));
        }
    }




    @GetMapping(value = "/get/student")
    @ResponseBody
    public String getStudent(String name, int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    @GetMapping(value = "/get/param/student")
    @ResponseBody
    public String getParamStudent(@RequestParam(value = "name", required = true, defaultValue = "name") String name
            , @RequestParam(value = "age", required = false, defaultValue = "18")int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    @GetMapping(value = "/get/path/student/{name}/{age}")
    @ResponseBody
    public String getPathStudent(@PathVariable(value = "name") String name, @PathVariable(value = "age") int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    @PostMapping(value = "/post/student")
    @ResponseBody
    public String postStudent(String name, int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    @GetMapping(value = {"/teacher"})
    public ModelAndView teacher(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("name", "xx");
        mav.addObject("age", 30);
        mav.setViewName("/view/teacher");
        return mav;
    }

    @GetMapping(value = {"/school"})
    public String school(Model model){
        model.addAttribute("name", "xx");
        model.addAttribute("age", 18);
        return "/school";
    }

    @GetMapping(value = {"/emp"})
    @ResponseBody
    public Map<String, Object> emp(){
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "xx");
        emp.put("age", 18);
        return emp;
    }

    @GetMapping(value = {"/emps"})
    @ResponseBody
    public List<Map<String, Object>> emps(){
        List<Map<String, Object>> emps = new ArrayList<>();
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "xx");
        emp.put("age", 18);
        emps.add(emp);
        emp.put("name", "yy");
        emp.put("age", 19);
        emps.add(emp);
        emp.put("name", "zz");
        emp.put("age", 20);
        emps.add(emp);
        return emps;
    }
}

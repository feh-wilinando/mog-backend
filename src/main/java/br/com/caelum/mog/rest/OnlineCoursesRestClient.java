package br.com.caelum.mog.rest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.caelum.mog.domains.models.Platform;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.caelum.mog.domains.dtos.CourseDTO;
import br.com.caelum.mog.domains.models.Course;
import br.com.caelum.mog.rest.representations.CourseRepresentation;

@Component
public class OnlineCoursesRestClient implements CoursesRestClient {


    private final String onlineAllCoursesURL;
    private final RestTemplate restTemplate;
	private String onlineSingleCourseURL;

    public OnlineCoursesRestClient(	    @Value("${mog.online.all-courses.url}") String onlineAllCoursesURL,
    									@Value("${mog.online.single-courses.url}") String onlineSingleCourseURL,
    									RestTemplate restTemplate) {
        this.onlineAllCoursesURL = onlineAllCoursesURL;
        this.onlineSingleCourseURL = onlineSingleCourseURL;
        this.restTemplate = restTemplate;
    }

    @Override
    public Course getCourseByCode(String code) {
    		return restTemplate.getForObject(onlineSingleCourseURL, Course.class, code);
    }

    @Override
    public List<CourseDTO> getAllSimplesCourse() {
        CourseRepresentation[] courses = restTemplate.getForObject(onlineAllCoursesURL, CourseRepresentation[].class);
        return Stream.of(courses).map(this::toOnlineCourseDTO).collect(Collectors.toList());
    }

    private CourseDTO toOnlineCourseDTO(CourseRepresentation representation){
        return representation.toCourseDTO(Platform.ONLINE);
    }
}
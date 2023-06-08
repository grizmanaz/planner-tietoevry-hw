package com.nd.planner;

import com.nd.planner.model.Request;
import com.nd.planner.model.Response;
import com.nd.planner.service.StudyPlannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private StudyPlannerService studyPlannerService;

    @PostMapping("/planner")
    public ResponseEntity<Response> calculateStudySchedule(@RequestBody Request request) {
        Response response = studyPlannerService.calculateStudySchedule(request);
        return ResponseEntity.ok(response);
    }
}

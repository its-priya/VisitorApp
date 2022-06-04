package io.bootify.visitor_app.rest;

import io.bootify.visitor_app.model.VisitDTO;
import io.bootify.visitor_app.model.VisitStatus;
import io.bootify.visitor_app.service.VisitService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-panel")
public class UserPanelController {
    @Autowired
    private VisitService visitService;

    @GetMapping
    public ResponseEntity<List<VisitDTO>> getPendingVisits(@RequestHeader final Long userId){
        return ResponseEntity.ok((List<VisitDTO>) visitService.getAllVisitsByUserId(userId)
                .stream().filter(visitDTO -> visitDTO.getStatus().equals(VisitStatus.PENDING))
                .collect(Collectors.toList()));

    }

    @GetMapping("/visits")
    public ResponseEntity<java.util.List<VisitDTO>> getAllVisits(@RequestHeader final Long userId){
        return ResponseEntity.ok(visitService.getAllVisitsByUserId(userId));
    }

    @PostMapping("/approve/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<String> approveVisit(@PathVariable final Long visitId, @RequestHeader final Long userId){
        visitService.updateVisitStatus(userId, visitId, VisitStatus.APPROVED);
        return new ResponseEntity<>("Approved", HttpStatus.OK);
    }

    @PostMapping("/reject/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<String> rejectVisit(@PathVariable final Long visitId, @RequestHeader final Long userId){
        visitService.updateVisitStatus(userId, visitId, VisitStatus.REJECTED);
        return new ResponseEntity<>("Rejected", HttpStatus.OK);
    }
}

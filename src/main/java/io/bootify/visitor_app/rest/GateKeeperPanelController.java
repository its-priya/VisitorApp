package io.bootify.visitor_app.rest;

import io.bootify.visitor_app.model.CreateVisitorDTO;
import io.bootify.visitor_app.model.VisitDTO;
import io.bootify.visitor_app.model.VisitorDTO;
import io.bootify.visitor_app.service.GateKeeperPanelService;
import io.bootify.visitor_app.service.VisitService;
import io.bootify.visitor_app.service.VisitorService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api/gk-panel")
public class GateKeeperPanelController {
    static private Logger LOGGER= LoggerFactory.getLogger(GateKeeperPanelController.class);
    static final String basePath= "/static/";
    private final String relativePath= "/vms";

    @Autowired
    private VisitorService visitorService;
    @Autowired
    private GateKeeperPanelService gkPanelService;
    @Autowired
    private VisitService visitService;

    @GetMapping
    public ResponseEntity<List<VisitDTO>> getAllApprovedVisits(){
        return ResponseEntity.ok(visitService.getAllApprovedVisits());
    }

    //Visitors
    @GetMapping("/visitors")
    public ResponseEntity<List<VisitorDTO>> getAllVisitors() {
        return ResponseEntity.ok(visitorService.findAll());
    }

    @GetMapping("/visitors/{id}")
    public ResponseEntity<VisitorDTO> getVisitor(@PathVariable final Long id) {
        return ResponseEntity.ok(visitorService.get(id));
    }

    @PostMapping("/visitors")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVisitor(@RequestBody @Valid final CreateVisitorDTO visitorDTO) {
        return new ResponseEntity<>(gkPanelService.createVisitor(visitorDTO), HttpStatus.CREATED);
    }

    @PutMapping("/visitors/{id}")
    public ResponseEntity<Void> updateVisitor(@PathVariable final Long id,
                                              @RequestBody @Valid final VisitorDTO visitorDTO) {
        visitorService.update(id, visitorDTO);
        return ResponseEntity.ok().build();
    }


    //Visits
    @GetMapping("/visits")
    public ResponseEntity<List<VisitDTO>> getAllVisits() {
        return ResponseEntity.ok(visitService.findAll());
    }

    @GetMapping("/visits/{id}")
    public ResponseEntity<VisitDTO> getVisit(@PathVariable final Long id) {
        return ResponseEntity.ok(visitService.get(id));
    }

    @PostMapping("/visits")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVisit(@RequestBody @Valid final VisitDTO visitDTO) {
        return new ResponseEntity<>(visitService.create(visitDTO), HttpStatus.CREATED);
    }

    @PutMapping("/visits/{id}")
    public ResponseEntity<Void> updateVisit(@PathVariable final Long id,
                                            @RequestBody @Valid final VisitDTO visitDTO) {
        visitService.update(id, visitDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/entry/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<String> markVisitEntry(@PathVariable final Long visitId) {
        visitService.markEntry(visitId);
        return new ResponseEntity<>("Updated entry.", HttpStatus.CREATED);
    }

    @PostMapping("/exit/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<String> markVisitExit(@PathVariable final Long visitId) {
        visitService.markExit(visitId);
        return new ResponseEntity<>("Updated exit.", HttpStatus.CREATED);
    }

    //Upload Image
    public ResponseEntity<String> uploadImg(@RequestParam("file")MultipartFile file){
        String message= "";
        try{
            String path= relativePath + "testfile_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String uploadPath= basePath + path;
            file.transferTo(new File(uploadPath));
            message= "Image URL: "+path;
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }catch(Exception exception){
            LOGGER.error("Exception occurred: {}", exception);
            message= "Couldn't upload the file: "+file.getOriginalFilename()+"!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }
}

package io.bootify.visitor_app.rest;

import io.bootify.visitor_app.model.UserDTO;
import io.bootify.visitor_app.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@RestController
@RequestMapping("/api/admin-panel")
public class AdminPanelController {
    static private Logger LOGGER = LoggerFactory.getLogger(AdminPanelController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping("/user")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createUser(@RequestBody @Valid final UserDTO userDTO) {
        return new ResponseEntity<>(userService.create(userDTO), HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable final Long id) {
        return ResponseEntity.ok(userService.get(id));
    }
    
    @PutMapping("/user/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable final Long id,
                                           @RequestBody @Valid final UserDTO userDTO) {
        userService.update(id, userDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/csv-upload")
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file")MultipartFile file){
        String message= "";
        try{
            BufferedReader fileReader= new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
            CSVParser csvParser= new CSVParser(fileReader,
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            Iterable<CSVRecord> csvRecords= csvParser.getRecords();
            for(CSVRecord record: csvRecords){
                UserDTO userDTO= new UserDTO();
                userDTO.setName(record.get("name"));
                userDTO.setEmail(record.get("email"));
                userDTO.setPhone(record.get("phone"));
                userDTO.setRoleId(Long.parseLong(record.get("roleId")));
                userDTO.setAddressId(Long.parseLong(record.get("addressId")));
                userDTO.setFlatId(Long.parseLong(record.get("flatId")));
                userService.create(userDTO);
                LOGGER.info("User created: {}", userDTO.getId() + " -> " + userDTO.getName());
            }
            message= "File Uploaded successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }catch(Exception e){
            LOGGER.error("Exception occurred: {}",e);
            message = "Upload failed: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @DeleteMapping("/user/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUser(@PathVariable final Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

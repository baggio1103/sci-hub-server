package javajedi.com.controller;

import javajedi.com.data.HttpResponse;
import javajedi.com.data.InstituteData;
import javajedi.com.exception.ExceptionHandling;
import javajedi.com.service.InstituteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static javajedi.com.constant.InstituteConstant.INSTITUTE_DELETED_SUCCESSFULLY;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/institutes")
public class InstituteController extends ExceptionHandling {

    private final InstituteService instituteService;

    @GetMapping("")
    public ResponseEntity<List<InstituteData>> findAllInstitutes() {
        List<InstituteData> institutes = instituteService.findAllInstitutes();
        return new ResponseEntity<>(institutes, OK);
    }

    @GetMapping("/{instituteId}")
    public ResponseEntity<InstituteData> findOneInstitute(@PathVariable("instituteId") String instituteId) {
        InstituteData institute = instituteService.findInstituteById(instituteId);
        return new ResponseEntity<>(institute, OK);
    }

    @PostMapping("")
    public ResponseEntity<InstituteData> saveInstitute(@RequestParam("country") String country,
                                                       @RequestParam("name") String name) {
        InstituteData institute = instituteService.saveInstitute(country, name);
        return new ResponseEntity<>(institute, OK);
    }

    @DeleteMapping("/{instituteId}")
    public ResponseEntity<HttpResponse> deleteInstitute(@PathVariable("instituteId") String instituteId) {
        instituteService.deleteInstitute(instituteId);
        return createHttpResponse(OK, INSTITUTE_DELETED_SUCCESSFULLY + instituteId);
    }

    @PutMapping("{instituteId}")
    public ResponseEntity<InstituteData> updateInstitute(@PathVariable("instituteId") String instituteId,
                                                         @RequestParam("country") String country,
                                                         @RequestParam("name") String name) {
        InstituteData institute = instituteService.updateInstitute(instituteId, country, name);
        return new ResponseEntity<>(institute, OK);
    }

}

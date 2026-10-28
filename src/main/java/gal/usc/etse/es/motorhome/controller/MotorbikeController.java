package gal.usc.etse.es.motorhome.controller;

import gal.usc.etse.es.motorhome.exception.ResourceNotFoundException;
import gal.usc.etse.es.motorhome.model.dto.MotorbikeRequest;
import gal.usc.etse.es.motorhome.model.dto.MotorbikeResponse;
import gal.usc.etse.es.motorhome.service.MotorbikeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping("/motorbikes")
public class MotorbikeController {
    private final MotorbikeService motorbikes;

    public MotorbikeController(MotorbikeService motorbikes) {
        this.motorbikes = motorbikes;
    }

    @PostMapping
    public ResponseEntity<MotorbikeResponse> create(
            @Valid @RequestBody MotorbikeRequest request) {
        MotorbikeResponse response = motorbikes.create(request);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public Page<MotorbikeResponse> list(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return motorbikes.list(pageable);
    }

    @GetMapping("/{id}")
    public MotorbikeResponse findById(@PathVariable Long id) {
        return motorbikes.findById(id);
    }

    @PutMapping("/{id}")
    public MotorbikeResponse update(@PathVariable Long id,
            @Valid @RequestBody MotorbikeRequest request) {
        return motorbikes.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        motorbikes.delete(id);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail notFound(ResourceNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }
}

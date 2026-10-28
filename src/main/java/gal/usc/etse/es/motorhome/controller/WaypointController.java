package gal.usc.etse.es.motorhome.controller;

import gal.usc.etse.es.motorhome.exception.ResourceNotFoundException;
import gal.usc.etse.es.motorhome.model.dto.WaypointRequest;
import gal.usc.etse.es.motorhome.model.dto.WaypointResponse;
import gal.usc.etse.es.motorhome.service.WaypointService;
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
@RequestMapping("/waypoints")
public class WaypointController {
    private final WaypointService service;

    public WaypointController(WaypointService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<WaypointResponse> create(@Valid @RequestBody WaypointRequest request) {
        WaypointResponse response = service.create(request);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public Page<WaypointResponse> list(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public WaypointResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public WaypointResponse update(@PathVariable Long id, @Valid @RequestBody WaypointRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail notFound(ResourceNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }
}

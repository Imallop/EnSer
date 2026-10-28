package gal.usc.etse.es.motorhome.controller;

import gal.usc.etse.es.motorhome.exception.ResourceNotFoundException;
import gal.usc.etse.es.motorhome.model.dto.RouteRequest;
import gal.usc.etse.es.motorhome.model.dto.RouteResponse;
import gal.usc.etse.es.motorhome.service.RouteService;
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
@RequestMapping("/routes")
public class RouteController {
    private final RouteService service;

    public RouteController(RouteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RouteResponse> create(@Valid @RequestBody RouteRequest request) {
        RouteResponse response = service.create(request);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public Page<RouteResponse> list(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public RouteResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public RouteResponse update(@PathVariable Long id, @Valid @RequestBody RouteRequest request) {
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

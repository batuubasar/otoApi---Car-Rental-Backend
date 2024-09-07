package com.otocom.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otocom.dto.OtoDto;
import com.otocom.dto.OtoPageResponse;
import com.otocom.exceptions.EmptyFileException;
import com.otocom.service.OtoService;
import com.otocom.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/oto")
@CrossOrigin(origins = "http://localhost:4200") // Angular uygulamanızın URL'sini buraya ekleyin


public class OtoController {

    private final OtoService otoService;

    public OtoController(OtoService otoService) {
        this.otoService = otoService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add-oto")
    public ResponseEntity<OtoDto> addOtoHandler(@RequestPart MultipartFile file,
                                                    @RequestPart String otoDto) throws IOException, EmptyFileException {
        if (file.isEmpty()){
            throw new EmptyFileException("File is empty. Please send another file.");
        }

        OtoDto dto = convertToOtoDto(otoDto);
        return new ResponseEntity<>(otoService.addOto(dto, file), HttpStatus.CREATED);
    }

    @GetMapping("/{otoId}")
    public ResponseEntity<OtoDto> getOtoHandler(@PathVariable Integer otoId) {
        return ResponseEntity.ok(otoService.getOto(otoId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OtoDto>> getAllOtosHandler() {
        return ResponseEntity.ok(otoService.getAllOtos());
    }

    @PutMapping("/update/{otoId}")
    public ResponseEntity<OtoDto> updateOtoHandler(@PathVariable Integer otoId,
                                                       @RequestPart MultipartFile file,
                                                       @RequestPart String otoDtoObj) throws IOException {
        if (file.isEmpty()) file = null;
        OtoDto otoDto = convertToOtoDto(otoDtoObj);
        return ResponseEntity.ok(otoService.updateOto(otoId, otoDto, file));
    }

    @DeleteMapping("/delete/{otoId}")
    public ResponseEntity<String> deleteOtoHandler(@PathVariable Integer otoId) throws IOException {
        return ResponseEntity.ok(otoService.deleteOto(otoId));
    }

    @GetMapping("/allOtosPage")
    public ResponseEntity<OtoPageResponse> getOtosWithPagination(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ){
        return ResponseEntity.ok(otoService.getAllOtosWithPagination(pageNumber, pageSize));
    }

    @GetMapping("/allOtosPageSort")
    public ResponseEntity<OtoPageResponse> getOtosWithPaginationAndSorting(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String dir
    ){
        return ResponseEntity.ok(otoService.getAllOtosWithPaginationAndSorting(pageNumber, pageSize,sortBy,dir));
    }

    private OtoDto convertToOtoDto(String otoDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(otoDtoObj, OtoDto.class);
    }

}

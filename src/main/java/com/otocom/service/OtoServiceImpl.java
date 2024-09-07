package com.otocom.service;

import com.otocom.dto.OtoDto;
import com.otocom.dto.OtoPageResponse;
import com.otocom.entities.Oto;
import com.otocom.exceptions.FileExistsException;
import com.otocom.exceptions.OtoNotFoundException;
import com.otocom.repositories.OtoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class OtoServiceImpl implements OtoService {

    private final OtoRepository otoRepository;

    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public OtoServiceImpl(OtoRepository otoRepository, FileService fileService) {
        this.otoRepository = otoRepository;
        this.fileService = fileService;
    }

    @Override
    public OtoDto addOto(OtoDto otoDto, MultipartFile file) throws IOException {

        // upload the file
        if(Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw new FileExistsException("File already exists. Please choose another file name.");
        }
        String uploadedFileName = fileService.uploadFile(path, file);

        // set the value of field 'photo' as filename
        otoDto.setPhoto(uploadedFileName);

        // map dto to Oto object
        Oto oto = new Oto(
                null,
                otoDto.getModel(),
                otoDto.getBrand(),
                otoDto.getMileage(),
                otoDto.getFuel(),
                otoDto.getTransmission(),
                otoDto.getYear(),
                otoDto.getPrice(),
                otoDto.getColor(),
                otoDto.getPhoto()
        );

        // save the oto object -> saved Oto object
        Oto savedOto = otoRepository.save(oto);

        // generate the photoUrl
        String photoUrl = baseUrl + "/file/" + uploadedFileName;

        // map Oto object to DTO object and return it
        OtoDto response = new OtoDto(
                savedOto.getOtoId(),
                savedOto.getModel(),
                savedOto.getBrand(),
                savedOto.getMileage(),
                savedOto.getFuel(),
                savedOto.getTransmission(),
                savedOto.getYear(),
                savedOto.getPrice(),
                savedOto.getColor(),
                savedOto.getPhoto(),
                photoUrl
        );

        return response;
    }

    @Override
    public OtoDto getOto(Integer otoId) {
        // check the data in DB and if exists, fetch the data of given ID
        Oto oto = otoRepository.findById(otoId).orElseThrow(() -> new OtoNotFoundException("Car not found with id: " + otoId));

        // generate photoUrl
        String photoUrl = baseUrl + "/file/" + oto.getPhoto();

        // map to OtoDto object and return it
        OtoDto response = new OtoDto(
                oto.getOtoId(),
                oto.getModel(),
                oto.getBrand(),
                oto.getMileage(),
                oto.getFuel(),
                oto.getTransmission(),
                oto.getYear(),
                oto.getPrice(),
                oto.getColor(),
                oto.getPhoto(),
                photoUrl
        );

        return response;
    }

    @Override
    public List<OtoDto> getAllOtos() {
        // fetch all data from DB
        List<Oto> otos = otoRepository.findAll();

        List<OtoDto> otoDtos = new ArrayList<>();

        // iterate through the list, generate photoUrl for each oto obj
        // and map to otoDto obj
        for(Oto oto : otos){
            String photoUrl = baseUrl + "/file/" + oto.getPhoto();
            OtoDto otoDto = new OtoDto(
                    oto.getOtoId(),
                    oto.getModel(),
                    oto.getBrand(),
                    oto.getMileage(),
                    oto.getFuel(),
                    oto.getTransmission(),
                    oto.getYear(),
                    oto.getPrice(),
                    oto.getColor(),
                    oto.getPhoto(),
                    photoUrl
            );
            otoDtos.add(otoDto);
        }

        return otoDtos;
    }

    @Override
    public OtoDto updateOto(Integer otoId, OtoDto otoDto, MultipartFile file) throws IOException {
        // check if oto obj exists with given otoId
        Oto oto_ = otoRepository.findById(otoId).orElseThrow(() -> new OtoNotFoundException("Car not found with id: " + otoId));

        // if file is null, do nothing
        // if file is not null, then delete existing file associated with the record,
        // and upload the new file
        String fileName = oto_.getPhoto();
        if (file != null){
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            fileName = fileService.uploadFile(path, file);
        }

        // set otoDto's photo value, according to step2
        otoDto.setPhoto(fileName);

        // map it to Oto obj
        Oto oto = new Oto(
                oto_.getOtoId(),
                otoDto.getModel(),
                otoDto.getBrand(),
                otoDto.getMileage(),
                otoDto.getFuel(),
                otoDto.getTransmission(),
                otoDto.getYear(),
                otoDto.getPrice(),
                otoDto.getColor(),
                otoDto.getPhoto()
        );

        // save the oto obj -> return saved oto obj
        Oto updatedOto = otoRepository.save(oto);

        // generate photoUrl for it
        String photoUrl = baseUrl + "/file/" + oto.getPhoto();

        // map to OtoDto and return it
        OtoDto response = new OtoDto(
                oto.getOtoId(),
                oto.getModel(),
                oto.getBrand(),
                oto.getMileage(),
                oto.getFuel(),
                oto.getTransmission(),
                oto.getYear(),
                oto.getPrice(),
                oto.getColor(),
                oto.getPhoto(),
                photoUrl
        );

        return response;
    }

    @Override
    public String deleteOto(Integer otoId) throws IOException {
        // check if oto obj exists in DB
        Oto oto_ = otoRepository.findById(otoId).orElseThrow(() -> new OtoNotFoundException("Car not found with id: " + otoId));
        Integer id = oto_.getOtoId();

        // delete the file associated with the obj
        Files.deleteIfExists(Paths.get(path + File.separator + oto_.getPhoto()));

        // delete the oto obj
        otoRepository.delete(oto_);

        return "Car deleted with id: " + id;
    }

    @Override
    public OtoPageResponse getAllOtosWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Oto> otoPages = otoRepository.findAll(pageable);
        List<Oto> otos = otoPages.getContent();

        List<OtoDto> otoDtos = new ArrayList<>();

        // iterate through the list, generate photoUrl for each oto obj
        // and map to otoDto obj
        for(Oto oto : otos) {
            String photoUrl = baseUrl + "/file/" + oto.getPhoto();
            OtoDto otoDto = new OtoDto(
                    oto.getOtoId(),
                    oto.getModel(),
                    oto.getBrand(),
                    oto.getMileage(),
                    oto.getFuel(),
                    oto.getTransmission(),
                    oto.getYear(),
                    oto.getPrice(),
                    oto.getColor(),
                    oto.getPhoto(),
                    photoUrl
            );
            otoDtos.add(otoDto);
        }
        return new OtoPageResponse(otoDtos, pageNumber, pageSize,
                otoPages.getTotalElements(),
                otoPages.getTotalPages(),  otoPages.isLast());
    }

    @Override
    public OtoPageResponse getAllOtosWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {

        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Oto> otoPages = otoRepository.findAll(pageable);
        List<Oto> otos = otoPages.getContent();

        List<OtoDto> otoDtos = new ArrayList<>();

        // iterate through the list, generate photoUrl for each oto obj
        // and map to otoDto obj
        for(Oto oto : otos) {
            String photoUrl = baseUrl + "/file/" + oto.getPhoto();
            OtoDto otoDto = new OtoDto(
                    oto.getOtoId(),
                    oto.getModel(),
                    oto.getBrand(),
                    oto.getMileage(),
                    oto.getFuel(),
                    oto.getTransmission(),
                    oto.getYear(),
                    oto.getPrice(),
                    oto.getColor(),
                    oto.getPhoto(),
                    photoUrl
            );
            otoDtos.add(otoDto);
        }
        return new OtoPageResponse(otoDtos, pageNumber, pageSize,
                otoPages.getTotalElements(),
                otoPages.getTotalPages(),  otoPages.isLast());
    }


}

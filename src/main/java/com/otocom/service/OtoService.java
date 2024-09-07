package com.otocom.service;

import com.otocom.dto.OtoDto;
import com.otocom.dto.OtoPageResponse;
import com.otocom.entities.Oto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OtoService {

    OtoDto addOto(OtoDto otoDto, MultipartFile file) throws IOException;

    OtoDto getOto(Integer otoId);

    List<OtoDto> getAllOtos();

    OtoDto updateOto(Integer otoId, OtoDto otoDto, MultipartFile file) throws IOException;

    String deleteOto(Integer otoId) throws IOException;

    OtoPageResponse getAllOtosWithPagination(Integer pageNumber, Integer pageSize);

    OtoPageResponse getAllOtosWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir);

}

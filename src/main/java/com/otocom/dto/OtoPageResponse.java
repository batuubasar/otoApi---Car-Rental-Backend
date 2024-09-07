package com.otocom.dto;

import java.util.List;

public record OtoPageResponse(List<OtoDto> otoDtos,
                                Integer pageNumber,
                                Integer pageSize,
                                long totalElements,
                                int totalPages,
                                boolean isLast) {
}

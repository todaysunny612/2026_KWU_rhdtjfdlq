package com.rhdtjfdlq_1.Cartalk_BE.service.port;

import com.rhdtjfdlq_1.Cartalk_BE.dto.ResponseSearchDto;

public interface SearchService {

    ResponseSearchDto searchByCarNum(String carNum);
}
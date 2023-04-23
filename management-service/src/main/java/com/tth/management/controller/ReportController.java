package com.tth.management.controller;

import com.tth.common.message.MessageContent;
import com.tth.common.message.ResponseMessage;
import com.tth.common.utils.StringUtil;
import com.tth.management.model.dto.AuthorizationResponseDTO;
import com.tth.management.model.dto.ReportDTO;
import com.tth.management.model.dto.ReportDTOResponse;
import com.tth.management.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ReportController extends BaseController {

    @Autowired
    private EventService eventService;

    public ResponseMessage reportEvent(String requestPath, String requestMethod, String urlParam, Map<String, String> headerParam) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            if (!StringUtil.isNullOrEmpty(urlParam)) {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String filterBy = params.get("filterBy");
                String filterTimeLevel = params.get("filterTimeLevel");
                List<ReportDTO> reportDTOList = new ArrayList<>();
                if(filterBy.equals("object")) {
                    reportDTOList = eventService.reportEventByObject(filterTimeLevel);
                } else if(filterBy.equals("status")) {
                    reportDTOList = eventService.reportEventByStatus(filterTimeLevel);
                }
                response = new ResponseMessage(HttpStatus.OK.value(), "Thống kê sự kiện",
                        new MessageContent(HttpStatus.OK.value(), "Thống kê sự kiện", reportDTOList));
            } else {
                response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                        new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
            }
        }

        return response;
    }

    public ResponseMessage reportGeneral(String requestPath, String requestMethod, String urlParam, Map<String, String> headerParam) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(),
                    new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), eventService.getTotalMoney()));
        }

        return response;
    }

    public ResponseMessage reportEventChart(String requestPath, String requestMethod, String urlParam, Map<String, String> headerParam) throws ParseException {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            if (!StringUtil.isNullOrEmpty(urlParam)) {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String filterBy = params.get("filterBy");
                String filterTimeLevel = params.get("filterTimeLevel");
                List<ReportDTOResponse> reportDTOList = new ArrayList<>();
                if(filterBy.equals("object")) {
                    reportDTOList = eventService.reportEventChartByObject(filterTimeLevel);
                } else if(filterBy.equals("status")) {
                    reportDTOList = eventService.reportEventChartByStatus(filterTimeLevel);
                }
                response = new ResponseMessage(HttpStatus.OK.value(), "Thống kê sự kiện",
                        new MessageContent(HttpStatus.OK.value(), "Thống kê sự kiện", reportDTOList));
            } else {
                response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                        new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
            }
        }

        return response;
    }
}

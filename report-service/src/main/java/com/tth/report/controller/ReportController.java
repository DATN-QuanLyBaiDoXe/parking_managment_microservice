package com.tth.report.controller;

import com.tth.common.message.MessageContent;
import com.tth.common.message.ResponseMessage;
import com.tth.report.dto.AuthorizationResponseDTO;
import com.tth.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class ReportController extends BaseController {

    @Autowired
    private ReportService reportService;

    public ResponseMessage reportEvent(String requestUrl, String method, String urlParam, Map<String, String> headerParam) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ResponseMessage reportDTO = reportService.reportEvent(urlParam, headerParam);
            response = reportDTO;
        }

        return response;
    }

    public ResponseMessage reportGeneral(String requestUrl, String method, String urlParam, Map<String, String> headerParam){
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ResponseMessage reportDTO = reportService.reportGeneral(urlParam, headerParam);
            response = reportDTO;
        }

        return response;
    }

    public ResponseMessage reportEventChart(String requestUrl, String method, String urlParam, Map<String, String> headerParam) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ResponseMessage reportDTO = reportService.reportEventChart(urlParam, headerParam);
            response = reportDTO;
        }

        return response;
    }

    public ResponseMessage reportEventLine(String requestUrl, String method, String urlParam, Map<String, String> headerParam) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ResponseMessage reportDTO = reportService.reportEventLine(urlParam, headerParam);
            response = reportDTO;
        }

        return response;
    }
}

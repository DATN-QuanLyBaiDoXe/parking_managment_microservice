package com.tth.report.service;

import com.tth.common.message.ResponseMessage;

import java.util.Map;

public interface ReportService {

    ResponseMessage reportEvent(String urlParam, Map<String, String> headerParam);

    ResponseMessage reportGeneral(String urlParam, Map<String, String> headerParam);

    ResponseMessage reportEventChart(String urlParam, Map<String, String> headerParam);

    ResponseMessage reportEventLine(String urlParam, Map<String, String> headerParam);
}

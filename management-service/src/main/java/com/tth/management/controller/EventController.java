package com.tth.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tth.common.message.MessageContent;
import com.tth.common.message.ResponseMessage;
import com.tth.management.kafka.KafkaClient;
import com.tth.management.model.Event;
import com.tth.management.model.dto.AuthorizationResponseDTO;
import com.tth.management.model.dto.EventDTO;
import com.tth.management.model.dto.EventPagingDTO;
import com.tth.management.model.dto.EventResponse;
import com.tth.management.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class EventController extends BaseController {

    @Autowired
    private EventService eventService;

    @Autowired
    private KafkaClient kafkaClient;

    public ResponseMessage getAllEvent(String requestUrl, Map<String, String> headerParam, Map<String, Object> bodyParam) throws IOException {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            EventPagingDTO eventPage = eventService.getAllEvent(bodyParam);
            if (eventPage.getTotal() == 0) {
                response = new ResponseMessage(HttpStatus.OK.value(), "Lấy danh sách sự kiện",
                        new MessageContent(HttpStatus.OK.value(), "Lấy danh sách sự kiện", null, 0L));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Lấy danh sách sự kiện",
                        new MessageContent(HttpStatus.OK.value(), "Lấy danh sách sự kiện", eventPage.getEventList(), eventPage.getTotal()));
            }
        }
        return response;
    }

    public ResponseMessage createEvent(String requestUrl, Map<String, String> headerParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            response = new ResponseMessage(HttpStatus.OK.value(), "Tạo sự kiện thành công",
                    new MessageContent(HttpStatus.OK.value(), "Tạo sự kiện thành công", eventService.save(bodyParam, dto.getUuid())));
        }
        return response;
    }

    public ResponseMessage updateEvent(Map<String, Object> bodyParam, Map<String, String> headerParam, String pathParam, String method, String requestUrl) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Event event = eventService.findByUuid(pathParam);
            if(event == null) {
                response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy bản ghi",
                        new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy bản ghi", null));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Cập nhật sự kiện thành công",
                        new MessageContent(HttpStatus.OK.value(), "Cập nhật sự kiện thành công", eventService.update(event, bodyParam, dto.getUuid())));
            }
        }
        return response;
    }

    //xóa
    public ResponseMessage deleteEvent(String requestUrl, Map<String, String> headerParam, Map<String, Object> bodyParam, String method) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            List<String> uuids = (List<String>) bodyParam.get("uuids");
            if (uuids == null || uuids.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "uuids không được bỏ trống hoặc không đúng định dạng",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "uuids không được bỏ trống hoặc không đúng định dạng", null));
            } else {
                List<Event> eventList = eventService.findByUuidIn(uuids);
                if (eventList != null && !eventList.isEmpty()) {
                    eventService.deleteMultiEvents(eventList, dto.getUuid());
                    response = new ResponseMessage(HttpStatus.OK.value(), "Xóa sự kiện thành công",
                            new MessageContent(HttpStatus.OK.value(), "Xóa sự kiện thành công", null));
                } else {
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Sự kiện không tồn tại",
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Sự kiện không tồn tại", null));
                }
            }
        }
        return response;
    }

    //update status: kết thúc = 4, báo sai = 5
    public ResponseMessage updateStatus(Map<String, Object> bodyParam, Map<String, String> headerParam, String pathParam, String methodString, String requestUrl) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            String uuid = (String) bodyParam.get("uuid");
            if (uuid == null || uuid.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "uuid không được bỏ trống",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "uuid không được bỏ trống", null));
            } else {
                Event event = eventService.findByUuid(uuid);
                if (event != null) {
                    EventResponse eventResponse = eventService.updateStatus(event, dto.getUuid(), (Integer) bodyParam.get("status"));
                    response = new ResponseMessage(HttpStatus.OK.value(), "Cập nhật sự kiện thành công",
                            new MessageContent(HttpStatus.OK.value(), "Cập nhật sự kiện thành công", eventResponse));
                } else {
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Sự kiện không tồn tại",
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Sự kiện không tồn tại", null));
                }
            }
        }
        return response;
    }

    public ResponseMessage recognitionEvent(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;
        Event event = transformFromBodyParam(bodyParam);
        String msg = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            msg = mapper.writeValueAsString(event);
        } catch (Exception ex) {
            LOGGER.error("Error to create json >>> {}", ex.toString());
        }
        if(kafkaClient.callKafkaServerWorker("event-request-topic", msg)) {
            responseMessage = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(),
                    new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
        } else {
            responseMessage = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                    new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
        }
        return responseMessage;
    }

    private Event transformFromBodyParam(Map<String, Object> bodyParam) {
        Event event = new Event();
        event.setPlace((String) bodyParam.get("place"));
        event.setImage((String) bodyParam.get("image"));
        event.setCreatedDate(new Date());
        return event;
    }
}

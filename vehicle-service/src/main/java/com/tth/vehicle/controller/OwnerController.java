package com.tth.vehicle.controller;

import com.tth.common.message.MessageContent;
import com.tth.common.message.ResponseMessage;
import com.tth.common.utils.StringUtil;
import com.tth.vehicle.model.Owner;
import com.tth.vehicle.model.dto.AuthorizationResponseDTO;
import com.tth.vehicle.model.dto.OwnerDTO;
import com.tth.vehicle.model.dto.OwnerResponse;
import com.tth.vehicle.service.OwnerService;
import com.tth.vehicle.validation.OwnerValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
public class OwnerController extends BaseController {

    @Autowired
    private OwnerService ownerService;

    public ResponseMessage getAllOwners(String requestUrl, String method, String urlParam, Map<String, String> headerParam) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            String search = "";
            String sort = "";
            Pageable pageable;
            if (!StringUtil.isNullOrEmpty(urlParam)) {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : 0;
                Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : 20;
                sort = !StringUtil.isNullOrEmpty(params.get("sort")) ? params.get("sort") : "fullName";
                search = params.get("search");
                pageable = PageRequest.of(page, size, Sort.by(sort));
            } else {
                pageable = PageRequest.of(0, 20, Sort.by(sort));
            }
            Page<OwnerResponse> userResponsePage = ownerService.getAll(pageable, search);
            if(userResponsePage == null){
                response = new ResponseMessage(HttpStatus.OK.value(), "Lấy danh sách chủ sở hữu",
                        new MessageContent(HttpStatus.OK.value(), "Lấy danh sách chủ sở hữu", null));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Lấy danh sách chủ sở hữu",
                        new MessageContent(HttpStatus.OK.value(), "Lấy danh sách chủ sở hữu", userResponsePage.getContent(), userResponsePage.getTotalElements()));
            }
        }
        return response;
    }

    public ResponseMessage createOwner(String requestUrl, Map<String, String> headerParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            OwnerDTO ownerDTO = getOwnerDTOFromBodyParam(bodyParam);
            String invalidData = new OwnerValidation().validateOwner(ownerDTO);
            if(invalidData != null){
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                Owner existOwner = ownerService.findByPhoneNumber(ownerDTO.getPhoneNumber());
                if(existOwner != null){
                    invalidData = "Đã tồn tại chủ sở hữu ứng với số điện thoại " + ownerDTO.getPhoneNumber();
                    response = new ResponseMessage(HttpStatus.CONFLICT.value(), invalidData,
                            new MessageContent(HttpStatus.CONFLICT.value(), invalidData, null));
                } else {
                    response = new ResponseMessage(HttpStatus.OK.value(), "Tạo chủ sở hữu thành công",
                            new MessageContent(HttpStatus.OK.value(), "Tạo chủ sở hữu thành công", ownerService.save(ownerDTO, dto.getUuid())));
                }
            }
        }
        return response;
    }

    public ResponseMessage updateOwner(Map<String, Object> bodyParam, Map<String, String> headerParam, String pathParam, String method, String requestUrl) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Owner owner = ownerService.findByUuid(pathParam);
            if(owner == null){
                response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy bản ghi",
                        new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy bản ghi", null));
            } else {
                OwnerDTO ownerDTO = getOwnerDTOFromBodyParam(bodyParam);
                String invalidData = new OwnerValidation().validateOwner(ownerDTO);
                if(invalidData != null){
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                } else {
                    Owner existOwner = ownerService.findByPhoneNumber(ownerDTO.getPhoneNumber());
                    if(existOwner != null && !existOwner.getUuid().equals(pathParam)){
                        invalidData = "Đã tồn tại chủ sở hữu ứng với số điện thoại " + ownerDTO.getPhoneNumber();
                        response = new ResponseMessage(HttpStatus.CONFLICT.value(), invalidData,
                                new MessageContent(HttpStatus.CONFLICT.value(), invalidData, null));
                    } else {
                        response = new ResponseMessage(HttpStatus.OK.value(), "Cập nhật chủ sở hữu thành công",
                                new MessageContent(HttpStatus.OK.value(), "Cập nhật chủ sở hữu thành công", ownerService.update(owner, ownerDTO, dto.getUuid())));
                    }
                }
            }
        }
        return response;
    }

    public ResponseMessage deleteOwner(String requestUrl, Map<String, String> headerParam, Map<String, Object> bodyParam, String method) {
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
                List<Owner> ownerList = ownerService.findByUuidIn(uuids);
                if (ownerList != null && !ownerList.isEmpty()) {
                    ownerService.deleteMultiOwner(ownerList);
                    response = new ResponseMessage(HttpStatus.OK.value(), "Xóa tài khoản thành công",
                            new MessageContent(HttpStatus.OK.value(), "Xóa tài khoản thành công", null));
                } else {
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Chủ sở hữu không tồn tại",
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Chủ sở hữu không tồn tại", null));
                }
            }
        }
        return response;
    }

    private OwnerDTO getOwnerDTOFromBodyParam(Map<String, Object> bodyParam) {
        String fullName = (String) bodyParam.get("fullName");
        String phoneNumber = (String) bodyParam.get("phoneNumber");
        String address = (String) bodyParam.get("address");
        OwnerDTO ownerDTO = new OwnerDTO(fullName, phoneNumber, address);
        return ownerDTO;
    }
}

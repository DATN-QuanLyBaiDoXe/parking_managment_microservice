package com.tth.vehicle.controller;

import com.tth.common.message.MessageContent;
import com.tth.common.message.ResponseMessage;
import com.tth.common.utils.StringUtil;
import com.tth.vehicle.model.Owner;
import com.tth.vehicle.model.Vehicle;
import com.tth.vehicle.model.dto.AuthorizationResponseDTO;
import com.tth.vehicle.model.dto.VehicleDTO;
import com.tth.vehicle.service.OwnerService;
import com.tth.vehicle.service.VehicleService;
import com.tth.vehicle.validation.VehicleValidation;
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
public class VehicleController extends BaseController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private OwnerService ownerService;

    public ResponseMessage getAllVehicles(String requestUrl, String method, String urlParam, Map<String, String> headerParam) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
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
                sort = !StringUtil.isNullOrEmpty(params.get("sort")) ? params.get("sort") : "createdDate";
                search = params.get("search");
                pageable = PageRequest.of(page, size, Sort.by(sort));
            } else {
                pageable = PageRequest.of(0, 20, Sort.by(sort));
            }
            Page<VehicleDTO> vehicleDTOPage = vehicleService.getAll(pageable, search);
            if (vehicleDTOPage == null) {
                response = new ResponseMessage(HttpStatus.OK.value(), "Lấy danh sách phương tiện",
                        new MessageContent(HttpStatus.OK.value(), "Lấy danh sách phương tiện", null, 0L));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Lấy danh sách phương tiện",
                        new MessageContent(HttpStatus.OK.value(), "Lấy danh sách phương tiện", vehicleDTOPage.getContent(), vehicleDTOPage.getTotalElements()));
            }
        }
        return response;
    }

    public ResponseMessage createVehicle(String requestUrl, String method, Map<String, String> headerParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            VehicleDTO vehicleDTO = getVehicleDTOFromBodyParam(bodyParam);
            String invalidData = new VehicleValidation().validateVehicle(vehicleDTO);
            if (invalidData != null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                if (!StringUtil.isNullOrEmpty(vehicleDTO.getPlace())) {
                    Vehicle existVehicle = vehicleService.findByPlace(vehicleDTO.getPlace());
                    if (existVehicle != null) {
                        invalidData = "Đã tồn tại phương tiện với biển số " + vehicleDTO.getPlace();
                        response = new ResponseMessage(HttpStatus.CONFLICT.value(), invalidData,
                                new MessageContent(HttpStatus.CONFLICT.value(), invalidData, null));
                    }
                } else {
                    Owner existOwner = ownerService.findByUuid(vehicleDTO.getOwnerName());
                    if (existOwner == null) {
                        invalidData = "Không tìm thấy thông tin người sở hữu trong hệ thống";
                        response = new ResponseMessage(HttpStatus.CONFLICT.value(), invalidData,
                                new MessageContent(HttpStatus.CONFLICT.value(), invalidData, null));
                    } else {
                        response = new ResponseMessage(HttpStatus.OK.value(), "Tạo phương tiện thành công",
                                new MessageContent(HttpStatus.OK.value(), "Tạo phương tiện thành công", vehicleService.save(vehicleDTO, existOwner, dto.getUuid())));
                    }
                }
            }
        }
        return response;
    }

    public ResponseMessage updateVehicle(Map<String, Object> bodyParam, Map<String, String> headerParam, String requestUrl, String method, String requestPath) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Vehicle vehicle = vehicleService.findById(requestUrl);
            if (vehicle == null) {
                response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy bản ghi",
                        new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy bản ghi", null));
            } else {
                VehicleDTO vehicleDTO = getVehicleDTOFromBodyParam(bodyParam);
                String invalidData = new VehicleValidation().validateVehicle(vehicleDTO);
                if (invalidData != null) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                } else {
                    if (!StringUtil.isNullOrEmpty(vehicleDTO.getPlace())) {
                        Vehicle existVehicle = vehicleService.findByPlace(vehicleDTO.getPlace());
                        if (existVehicle != null) {
                            invalidData = "Đã tồn tại phương tiện với biển số " + vehicleDTO.getPlace();
                            response = new ResponseMessage(HttpStatus.CONFLICT.value(), invalidData,
                                    new MessageContent(HttpStatus.CONFLICT.value(), invalidData, null));
                        }
                    } else {
                        Owner existOwner = ownerService.findByUuid(vehicleDTO.getOwnerName());
                        if (existOwner == null) {
                            invalidData = "Không tìm thấy thông tin người sở hữu trong hệ thống";
                            response = new ResponseMessage(HttpStatus.CONFLICT.value(), invalidData,
                                    new MessageContent(HttpStatus.CONFLICT.value(), invalidData, null));
                        } else {
                            vehicleDTO.setUuid(requestUrl);
                            vehicleDTO.setOwnerId(existOwner.getUuid());
                            vehicleDTO.setOwnerName(existOwner.getFullName());
                            response = new ResponseMessage(HttpStatus.OK.value(), "Cập nhật phương tiện thành công",
                                    new MessageContent(HttpStatus.OK.value(), "Cập nhật phương tiện thành công", vehicleService.update(vehicle, vehicleDTO, dto.getUuid(), existOwner)));
                        }
                    }
                }
            }
        }
        return response;
    }

    public ResponseMessage deleteVehicle(String requestUrl, Map<String, String> headerParam, Map<String, Object> bodyParam, String method) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            List<String> uuids = (List<String>) bodyParam.get("uuids");
            if (uuids == null || uuids.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "uuids không được bỏ trống hoặc không đúng định dạng",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "uuids không được bỏ trống hoặc không đúng định dạng", null));
            } else {
                List<Vehicle> vehicleList = vehicleService.findByUuidIn(uuids);
                if (vehicleList != null && !vehicleList.isEmpty()) {
                    vehicleService.deleteMultiVehicle(vehicleList);
                    response = new ResponseMessage(HttpStatus.OK.value(), "Xóa phương tiện thành công",
                            new MessageContent(HttpStatus.OK.value(), "Xóa phương tiện thành công", null));
                } else {
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Phương tiện không tồn tại",
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Phương tiện không tồn tại", null));
                }
            }
        }
        return response;
    }

    private VehicleDTO getVehicleDTOFromBodyParam(Map<String, Object> bodyParam) {
        String vehicleType = (String) bodyParam.get("vehicleType");
        String place = (String) bodyParam.get("place");
        String color = (String) bodyParam.get("color");
        String brand = (String) bodyParam.get("brand");
        String owner = (String) bodyParam.get("owner");

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setVehicleType(vehicleType);
        vehicleDTO.setOwnerName(owner);
        vehicleDTO.setColor(color);
        vehicleDTO.setBrand(brand);
        vehicleDTO.setPlace(place);
        return vehicleDTO;
    }

    public ResponseMessage getVehicleByOwner(String requestUrl, String method, String pathParam, Map<String, String> headerParam) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            List<VehicleDTO> vehicleList = vehicleService.findByOwner(pathParam);
            if (vehicleList == null) {
                response = new ResponseMessage(HttpStatus.OK.value(), "Lấy danh sách phương tiện",
                        new MessageContent(HttpStatus.OK.value(), "Lấy danh sách phương tiện", null));
            } else {
                response = new ResponseMessage(HttpStatus.OK.value(), "Lấy danh sách phương tiện",
                        new MessageContent(HttpStatus.OK.value(), "Lấy danh sách phương tiện", vehicleList));
            }
        }
        return response;
    }

}

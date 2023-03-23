package com.tth.id.controller;

import com.tth.common.message.MessageContent;
import com.tth.common.message.ResponseMessage;
import com.tth.id.auth.CustomUserDetails;
import com.tth.id.auth.jwt.JwtTokenProvider;
import com.tth.id.constant.Constant;
import com.tth.id.model.User;
import com.tth.id.model.dto.AuthorizationResponseDTO;
import com.tth.id.service.AuthService;
import com.tth.id.utils.JwtUtils;
import com.tth.id.validation.UserValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import javax.xml.bind.ValidationException;
import java.util.Map;

@Controller
public class AuthenController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenController.class);

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    public ResponseMessage userLogin(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam) throws ValidationException {
        ResponseMessage response = null;
        if(bodyParam == null || bodyParam.isEmpty()){
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
        } else {
            String username = (String) bodyParam.get("username");
            String password = (String) bodyParam.get("password");

            String invalidData = new UserValidation().validateLogin(username, password);
            if (invalidData != null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                // Xác thực thông tin người dùng Request lên, nếu không xảy ra exception tức là thông tin hợp lệ
                Authentication authentication = null;
                try {
                    authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                } catch (AuthenticationException ex) {
                    LOGGER.error(ex.toString());
                    invalidData = "Tài khoản hoặc mật khẩu không đúng";
                    return new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), invalidData,
                            new MessageContent(HttpStatus.UNAUTHORIZED.value(), invalidData, null));
                }
                // Set thông tin authentication vào Security Context
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                if (userDetails.getUser().getStatus() == 0) {
                    response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), Constant.VALIDATION_ACCOUNT_LOCKED,
                            new MessageContent(HttpStatus.UNAUTHORIZED.value(), Constant.VALIDATION_ACCOUNT_LOCKED, null));
                } else {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    // Trả về jwt cho người dùng.
                    String accessJwt = tokenProvider.generateToken(userDetails);
                    String refreshJwt = JwtUtils.createToken(userDetails.getUser().getUuid());
                    //LoginResponse loginResponse = new LoginResponse(accessJwt, refreshJwt);
                    AuthorizationResponseDTO responseDTO = new AuthorizationResponseDTO(userDetails, accessJwt, refreshJwt);
                    response = new ResponseMessage(new MessageContent(responseDTO));
                }
            }
        }
        return response;
    }

    //Authentication
    public ResponseMessage authorized(String requestPath, Map<String, String> headerParam) {
        ResponseMessage response = null;
        if (headerParam == null || headerParam.isEmpty()) {
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), null));
        } else {
            String bearerToken = headerParam.get("authorization");
            // Kiểm tra xem header Authorization có chứa thông tin jwt không
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                try {
                    long start = System.currentTimeMillis();
                    String jwt = bearerToken.substring(7);
                    LOGGER.info("jwt => " + jwt);
                    String uuid = tokenProvider.getUuidFromJWT(jwt);
                    long end = System.currentTimeMillis();
                    LOGGER.info("Parse token in: {} ms", (end-start), jwt);
                    UserDetails userDetails = authService.loadUserByUuid(uuid);
                    end = System.currentTimeMillis();
                    LOGGER.info("Get UserDetails in: {} ms", (end-start), jwt);
                    if (userDetails != null) {
                        User user = ((CustomUserDetails) userDetails).getUser();
                        if (user.getStatus() == 0) {
                            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), Constant.VALIDATION_ACCOUNT_LOCKED,
                                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), Constant.VALIDATION_ACCOUNT_LOCKED, null));
                        } else {
                            UsernamePasswordAuthenticationToken authentication
                                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            end = System.currentTimeMillis();
                            LOGGER.info("Save authentication for Spring security in: {} ms", (end-start), jwt);
                            AuthorizationResponseDTO responseDTO = new AuthorizationResponseDTO((CustomUserDetails) authentication.getPrincipal(), null, null);
                            end = System.currentTimeMillis();
                            LOGGER.info("Authen JWT Token in: {} ms for {}", (end-start), jwt);
                            response = new ResponseMessage(new MessageContent(responseDTO));
                        }
                    } else {
                        response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),
                                new MessageContent(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null));
                    }
                } catch (Exception ex) {
                    LOGGER.error("Failed on set user authentication", ex);
                    response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                            new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), null));
                }
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), null));
            }
        }
        return response;
    }
}

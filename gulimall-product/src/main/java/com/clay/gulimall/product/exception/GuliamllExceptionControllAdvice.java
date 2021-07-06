package com.clay.gulimall.product.exception;

import com.clay.gulimall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GuliamllExceptionControllAdvice {


    @ExceptionHandler(value = Exception.class)
    public R handleException(Exception e){

        log.error("兄台那个出现异常", e);
        return R.error("系统异常");
    }




    /**
     * 统一处理:参数异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R methodArgumentExceptionHandler(MethodArgumentNotValidException e) {

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, String> errrmsgMap = fieldErrors.stream().collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));

        //已知前端异常 使用info
        log.error("参数异常 异常信息为:{}", errrmsgMap);
        return R.error(errrmsgMap.toString());
    }
}

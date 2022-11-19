package com.github.tangyi.common.config;

import com.github.tangyi.common.constant.ApiMsg;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.JsonMapper;
import com.qiniu.common.QiniuException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 *
 * @author tangyi
 * @date 2019/05/25 15:36
 */
@Slf4j
@RestControllerAdvice
public class CustomGlobalExceptionHandler {

	/**
	 * 处理参数校验异常
	 *
	 * @param ex      ex
	 * @param headers headers
	 * @param status  status
	 * @return ResponseEntity
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> validationBodyException(MethodArgumentNotValidException ex, HttpHeaders headers,
			HttpStatus status) {
		log.error("validationBodyException", ex);
		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
		R<List<String>> responseBean = new R<>(errors, ApiMsg.KEY_ERROR, ex.getMessage());
		return new ResponseEntity<>(responseBean, headers, status);
	}

	/**
	 * 参数类型转换错误
	 */
	@ExceptionHandler(HttpMessageConversionException.class)
	public ResponseEntity<R<String>> parameterTypeException(HttpMessageConversionException e) {
		log.error("parameterTypeException", e);
		return new ResponseEntity<>(R.error(e.getMessage()), HttpStatus.OK);
	}

	/**
	 * 处理CommonException
	 *
	 * @param e e
	 * @return ResponseEntity
	 */
	@ExceptionHandler(CommonException.class)
	public ResponseEntity<R<String>> handleCommonException(Exception e) {
		log.error("handleCommonException", e);
		return new ResponseEntity<>(R.error(e.getMessage()), HttpStatus.OK);
	}

	/**
	 * 捕获@Validate校验抛出的异常
	 *
	 * @param e e
	 * @return ResponseEntity
	 */
	@ExceptionHandler(BindException.class)
	public ResponseEntity<Object> validExceptionHandler(BindException e) {
		log.error("validExceptionHandler", e);
		Exception ex = parseBindingResult(e.getBindingResult());
		return new ResponseEntity<>(R.error(ex.getMessage()), HttpStatus.OK);
	}

	@ExceptionHandler({BadSqlGrammarException.class, SQLSyntaxErrorException.class, SQLException.class})
	public ResponseEntity<Object> handleSQLException(SQLException e) {
		log.error("handleSQLException", e);
		return new ResponseEntity<>(R.error("数据库操作异常"), HttpStatus.OK);
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<R<String>> handleIOException(Exception e) {
		log.error("handleIOException", e);
		return new ResponseEntity<>(R.error(e.getMessage()), HttpStatus.OK);
	}

	@ExceptionHandler(QiniuException.class)
	public ResponseEntity<R<String>> handleQiniuException(QiniuException e) {
		log.error("handleQiniuException", e);
		String msg = "文件操作失败";
		R<String> responseBean = new R<>(null, ApiMsg.KEY_ERROR, msg);
		return new ResponseEntity<>(responseBean, HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<R<String>> handleException(Exception e) {
		log.error("handleException", e);
		return new ResponseEntity<>(R.error(e.getMessage()), HttpStatus.OK);
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<R<String>> handleThrowable(Exception e) {
		log.error("handleThrowable", e);
		return new ResponseEntity<>(R.error(e.getMessage()), HttpStatus.OK);
	}

	/**
	 * 提取Validator产生的异常错误
	 *
	 * @param bindingResult bindingResult
	 * @return Exception
	 */
	private Exception parseBindingResult(BindingResult bindingResult) {
		Map<String, String> map = new HashMap<>();
		for (FieldError error : bindingResult.getFieldErrors()) {
			map.put(error.getField(), error.getDefaultMessage());
		}
		if (map.isEmpty()) {
			return new CommonException(ApiMsg.KEY_PARAM_VALIDATE + "");
		} else {
			return new CommonException(JsonMapper.toJsonString(map));
		}
	}
}

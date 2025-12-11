package fpt.is.bnk.fptis_platform.advice.exception;

import fpt.is.bnk.fptis_platform.advice.base.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Admin 11/25/2025
 *
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AppException extends RuntimeException {

    private ErrorCode errorCode;

}

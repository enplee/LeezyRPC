package github.enplee.exception;

import github.enplee.enums.RpcErrorMessageEnum;

public class RpcException extends RuntimeException {

    public RpcException(RpcErrorMessageEnum errorMessageEnum,String detail){
        super(errorMessageEnum.getMessage()+":" + detail);
    }

    public RpcException(String message, Exception e) {
        super(message,e);
    }
}

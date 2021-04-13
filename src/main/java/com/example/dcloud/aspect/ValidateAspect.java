//package com.example.dcloud.aspect;
//
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.BindingResult;
//
//@Aspect
//@Component
//@Order(2)
//public class ValidateAspect {
//
//        @Pointcut("execution(public * com.example.dcloud.controller.*.*(..))")
//        public void ValidateAspect() {
//        }
//
//        @Around("ValidateAspect()")
//        public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
//            Object[] args = joinPoint.getArgs();
//            for (Object arg : args) {
//                if (arg instanceof BindingResult) {
//                    BindingResult result = (BindingResult) arg;
//                    if (result.hasErrors()) {
//                        FieldError fieldError = result.getFieldError();
//                        if(fieldError!=null){
//                            return CommonResult.validateFailed(fieldError.getDefaultMessage());
//                        }else{
//                            return CommonResult.validateFailed();
//                        }
//                    }
//                }
//            }
//            return joinPoint.proceed();
//        }
//
//}

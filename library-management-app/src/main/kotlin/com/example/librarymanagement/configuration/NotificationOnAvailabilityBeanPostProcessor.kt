//package com.example.librarymanagement.configuration
//
//import com.example.librarymanagementapp.annotation.NotificationOnAvailability
//import com.example.librarymanagement.service.AvailabilityNotificationService
//import org.aopalliance.intercept.MethodInterceptor
//import org.springframework.aop.framework.ProxyFactory
//import org.springframework.beans.factory.config.BeanPostProcessor
//import org.springframework.stereotype.Component
//import java.lang.reflect.Method
//
//@Component
//class NotificationOnAvailabilityBeanPostProcessor(
//    private val availabilityNotificationService: AvailabilityNotificationService
//) : BeanPostProcessor {
//
//    private val methods = mutableMapOf<String, List<Method>>()
//
//    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
//        val targetClass = bean::class.java
//        val targetClassName = targetClass.name
//        val targetMethods = targetClass.methods
//
//        val annotatedMethods = targetMethods.filter { it.isAnnotationPresent(NotificationOnAvailability::class.java) }
//
//        if (annotatedMethods.isNotEmpty()) { methods[targetClassName] = annotatedMethods }
//
//        return bean
//    }
//
//    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
//        val targetClassName = bean::class.java.name
//        val annotatedMethods = methods[targetClassName]
//
//        if (annotatedMethods != null) {
//            val proxyFactory = ProxyFactory(bean)
//            proxyFactory.addAdvice(MethodInterceptor { invocation ->
//                val method = invocation.method
//                val args = invocation.arguments
//
//                val result = invocation.proceed()
//
//                if (annotatedMethods.contains(method)) {
//                    if (result !is Exception) {
//                        val bookId = args.getOrNull(2) as String
//                        val libraryId = args.getOrNull(1) as String
//
//                        if (bookId != null) {
//                            availabilityNotificationService.notifyUserAboutBookAvailability(bookId, libraryId)
//                        }
//                    }
//                }
//                result
//            })
//            return proxyFactory.proxy
//        }
//        return bean
//    }
//}

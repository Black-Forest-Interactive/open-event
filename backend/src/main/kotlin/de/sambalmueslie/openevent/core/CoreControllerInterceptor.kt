package de.sambalmueslie.openevent.core

import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory


@Singleton
@InterceptorBean(CoreAPI::class)
class CoreControllerInterceptor : MethodInterceptor<Any, Any> {

    companion object {
        private val logger = LoggerFactory.getLogger(CoreAPI::class.java)
    }

    override fun intercept(context: MethodInvocationContext<Any, Any>): Any {
        val method = context.methodName
        val isAuthenticated = context.parameters.any { it.value.type == Authentication::class.java }
        if (isAuthenticated) logger.warn("Calling $method of deprecated core API")
        return context.proceed()
    }
}
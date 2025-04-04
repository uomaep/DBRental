package com.uomaep.db.configure

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView

@Component
class SecurityInterceptor: HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val path = request.requestURI

        if(path.startsWith("/user/")) {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate")
            response.setHeader("Pragma", "no-cache")
            response.setHeader("Expires", "0")
        }

        val isIllegalAccess = request.session.getAttribute("user") == null
        if(isIllegalAccess) {
            response.sendRedirect("/user/login")
            return false
        }

        return true
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        modelAndView?.let {
            it.model["user"] = request.session.getAttribute("user")
        }
    }

}
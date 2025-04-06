package com.uomaep.db.configure

import com.uomaep.db.dto.UserDTO
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView

@Component
class SecurityInterceptor: HandlerInterceptor {
    @Value("\${admin.account}") private lateinit var adminAccount: String

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

        val user = request.session.getAttribute("user") as UserDTO
        if(path.startsWith("/admin") && user.account != adminAccount) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
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
package bg.project.petsittingapp.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalTime;

@Component
public class MaintenanceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        if (!requestURI.equals("/maintenance")) {

            LocalTime maintenanceTime = LocalTime.now();

            if (maintenanceTime.isAfter(LocalTime.of(0, 0)) && maintenanceTime.isBefore(LocalTime.of(0, 30))) {
                response.sendRedirect("/maintenance");
                return false;
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}

package com.epam.esm.filter;

import com.epam.esm.dto.ViolationDTO;
import com.epam.esm.util.Translator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * to handle non-Authorized access (@PreAuthorize, @Security)
 *
 * @author Dzmitry Platonov on 2019-10-22.
 * @version 0.0.1
 */
@Log4j2
public class AppAccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exc) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.warn("User: " + auth.getName() + " attempted to access the protected URL: " + request.getRequestURI());
        }

        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(403);
        response.getWriter().write(mapper.writeValueAsString(
                new ViolationDTO(List.of(Translator.toLocale("exception.forbidden",
                        new Object[]{SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request.getRequestURI()})),
                        LocalDateTime.now())));
    }
}

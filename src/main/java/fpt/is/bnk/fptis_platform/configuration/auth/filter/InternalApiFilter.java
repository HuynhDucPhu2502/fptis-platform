package fpt.is.bnk.fptis_platform.configuration.auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Admin 12/1/2025
 *
 **/
@Component
public class InternalApiFilter implements Filter {

    @Value("${remote-federation.secret}")
    private String internalSecret;

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        if (req.getRequestURI().startsWith("/api/internal/")) {
            String header = req.getHeader("X-Internal-Secret");

            if (header == null || !header.equals(internalSecret)) {
                ((HttpServletResponse) servletResponse)
                        .sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid internal secret");
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}

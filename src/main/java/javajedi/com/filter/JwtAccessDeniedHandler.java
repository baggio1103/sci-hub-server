package javajedi.com.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import javajedi.com.data.HttpResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static javajedi.com.constant.SecurityConstant.ACCESS_DENIED_MESSAGE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        HttpResponse httpResponse = new HttpResponse(UNAUTHORIZED.value(), UNAUTHORIZED,
                UNAUTHORIZED.getReasonPhrase().toUpperCase(), ACCESS_DENIED_MESSAGE);
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, httpResponse);
        outputStream.flush();
    }

}

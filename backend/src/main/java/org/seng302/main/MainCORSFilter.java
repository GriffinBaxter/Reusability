/*
 * Created on Mon Feb 15 2021
 *
 * The Unlicense
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute
 * this software, either in source code form or as a compiled binary, for any
 * purpose, commercial or non-commercial, and by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the public
 * domain. We make this dedication for the benefit of the public at large and to
 * the detriment of our heirs and successors. We intend this dedication to be an
 * overt act of relinquishment in perpetuity of all present and future rights to
 * this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */

package org.seng302.main;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class set headers properly for CORS compliance. Global filtering is
 * restrained in {@link Main#corsConfigurer())
 */
@Component
public class MainCORSFilter implements Filter {

  private final String[] allowedOrigins = new String[] {"http://localhost:9500", "https://localhost:9500", "https://csse-s302g4.canterbury.ac.nz"};
  private final String defaultAllowedOrigin = "http://localhost:9500";

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    HttpServletResponse response = (HttpServletResponse) res;
    HttpServletRequest request = (HttpServletRequest) req;
    String allowedOriginValue = defaultAllowedOrigin;

    // ensure nothing went wrong during casting & at least 1 allowed origin has been defined.
    if (request != null && allowedOrigins.length > 0) {
      String requestOrigin = request.getHeader(HttpHeaders.ORIGIN);

      // If the request had the origin header set we can check whether or not we allow this origin.
      // Otherwise the defaultAllowedOrigin will be used!
      if (requestOrigin != null) {

        for (String potentialOrigin : allowedOrigins) {
          if (potentialOrigin.equals(requestOrigin)) {
            allowedOriginValue = potentialOrigin;
            break;
          }
        }

      }
    }

    response.setHeader("Access-Control-Allow-Origin", allowedOriginValue);
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH, HEAD");
    response.setHeader("Access-Control-Max-Age", "6000");
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    // The following header allows front-end scripts to access custom headers (for now just Total-Rows and Total-Pages for searching)
    response.setHeader("Access-Control-Expose-Headers", "Total-Rows, Total-Pages");
    chain.doFilter(req, res);
  }

}

package club.dlblog.datasource.filter;

import club.dlblog.datasource.utils.DynamicDataSourceUtil;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Order(1)
@WebFilter(urlPatterns = "/*")
public class DynamicDataSourceFilter extends   GenericFilter {


    private  final  static String TARGET_ID = "XXX-TARGET-ID";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String targetId = request.getHeader(TARGET_ID);
        //数据源切换
        DynamicDataSourceUtil.checkoutDataSource(targetId);
        filterChain.doFilter(servletRequest,servletResponse);
    }

}

package vm.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharacterEncodingFilter implements Filter {

	@Override
	public void destroy(){}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		// 解决 POST请求中文亂碼問題
    	request.setCharacterEncoding("UTF-8");
		
		// 設置服務器輸出的編碼為UTF-8
		response.setCharacterEncoding("UTF-8");
		// 告訴瀏覽器輸出的內容是html,並且以utf-8的編碼來查看這個內容
		response.setContentType("text/html; charset=utf-8");
		
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0){}

}

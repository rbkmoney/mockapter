package com.rbkmoney.mockapter.servlet;

import com.rbkmoney.damsel.proxy_provider.ProviderProxySrv;
import com.rbkmoney.mockapter.handler.MockapterHandler;
import com.rbkmoney.woody.thrift.impl.http.THServiceBuilder;
import lombok.RequiredArgsConstructor;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@RequiredArgsConstructor
@WebServlet("/adapter/*")
public class MockapterServlet extends GenericServlet {

    private final transient MockapterHandler handler;

    private transient Servlet servlet;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        servlet = new THServiceBuilder().build(ProviderProxySrv.Iface.class, handler);
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        servlet.service(request, response);
    }
}

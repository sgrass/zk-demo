package org.cx.rpc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.String.format;

/**
 * @author grass
 * @date 2018/10/28
 */
public class RpcServer {

    public static final ExecutorService SERVICE = Executors.newCachedThreadPool();

    //注册中心
    private RegisterCenter registerCenter;

    //服务发布地址
    private String serviceAddress;


    // 存放服务名称和服务对象之间的关系
    Map<String,Object> handlerMap=new HashMap<>();

    public RpcServer(RegisterCenter registerCenter, String serviceAddress) {
        this.registerCenter = registerCenter;
        this.serviceAddress = serviceAddress;
    }

    public void bind(Object... services) {
        for (Object service : services) {
            RpcAnnotation rpcAnnotation = service.getClass().getAnnotation(RpcAnnotation.class);
            String serviceName = rpcAnnotation.value().getName();
            String version = rpcAnnotation.version();

            if (version != null && !version.equals("")) {
                serviceName = serviceName + "-" + version;
            }

            //绑定服务接口名称对应的服务
            handlerMap.put(serviceName, service);
        }
    }

    public void publisher() {
        ServerSocket serverSocket = null;
        String[] addres = serviceAddress.split(":");
        try {
            serverSocket = new ServerSocket(Integer.parseInt(addres[1]));

            for (String interfaceName : handlerMap.keySet()) {
                registerCenter.register(interfaceName, serviceAddress);
                System.out.println(format("注册服务成功：interfaceName: %s -> %s", interfaceName, serviceAddress));
            }

            for (;;) {
                Socket socket = serverSocket.accept();
                SERVICE.execute(new ProcessorHanlder(socket, handlerMap));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

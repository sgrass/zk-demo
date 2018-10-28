package org.cx.rpc.server;

import java.io.Serializable;

/**
 * @author grass
 * @date 2018/10/28
 */
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = -404496763627642929L;

    private String className;
    private String methodName;
    private Object[] parameters;
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}

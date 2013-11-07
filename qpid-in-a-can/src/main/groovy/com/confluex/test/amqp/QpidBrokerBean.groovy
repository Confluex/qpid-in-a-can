package com.confluex.test.amqp

import java.util.Map;

import org.apache.qpid.server.Broker;
import org.apache.qpid.server.BrokerOptions;

public class QpidBrokerBean {
    Broker broker;
    String initialConfig;
    Map<String, String> configProperties;

    void init() {
        BrokerOptions options = new BrokerOptions();
        options.setConfigurationStoreType("memory");
        options.setInitialConfigurationLocation(
                QpidBrokerBean.class.getClassLoader().getResource(initialConfig).toExternalForm());
        configProperties.entrySet().each { prop ->
            options.setConfigProperty(prop.key, prop.value)
        }
        broker.startup(options);
    }

    public void destroy() {
        broker.shutdown();
    }
}


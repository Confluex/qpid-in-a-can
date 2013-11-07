package com.confluex.test.amqp

import org.apache.qpid.server.plugin.AuthenticationManagerFactory
import org.apache.qpid.server.security.auth.manager.AuthenticationManager
import org.apache.qpid.server.security.auth.manager.SimpleAuthenticationManager

class TestingAuthenticationManagerFactory implements AuthenticationManagerFactory {

    @Override
    public String getType() {
        "Testing";
    }

    @Override
    public AuthenticationManager createInstance(Map<String, Object> attributes) {
        new SimpleAuthenticationManager("tester", "testing")
    }

    @Override
    public Collection<String> getAttributeNames() {
        [ATTRIBUTE_TYPE]
    }

    @Override
    public Map<String, String> getAttributeDescriptions() {
        [:]
    }

}
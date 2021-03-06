/*
 * Copyright (C) 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.alibaba.nacos.registry;

import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.core.env.Environment;

import java.net.URI;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.client.naming.utils.UtilAndComs;

import static com.alibaba.nacos.api.PropertyKeyConst.*;
/**
 * @author xiaojing
 */
public class NacosRegistration implements Registration, ServiceInstance {

	@Autowired
	private NacosDiscoveryProperties nacosDiscoveryProperties;

	private NamingService nacosNamingService;

	@Autowired
	private Environment environment;

	@PostConstruct
	public void init() {
		nacosDiscoveryProperties.overrideFromEnv(environment);

		Properties properties = new Properties();
		properties.put(SERVER_ADDR, nacosDiscoveryProperties.getServerAddr());
		properties.put(NAMESPACE, nacosDiscoveryProperties.getNamespace());
		properties.put(UtilAndComs.NACOS_NAMING_LOG_NAME, nacosDiscoveryProperties.getLogName());
		properties.put(ENDPOINT, nacosDiscoveryProperties.getEndpoint());
		properties.put(ACCESS_KEY,nacosDiscoveryProperties.getAccessKey());
		properties.put(SECRET_KEY,nacosDiscoveryProperties.getSecretKey());
		properties.put(CLUSTER_NAME,nacosDiscoveryProperties.getClusterName());
		try {
			nacosNamingService = NacosFactory.createNamingService(properties);
		}
		catch (Exception e) {

		}
	}

	@Override
	public String getServiceId() {
		return nacosDiscoveryProperties.getService();
	}

	@Override
	public String getHost() {
		return nacosDiscoveryProperties.getIp();
	}

	@Override
	public int getPort() {
		return nacosDiscoveryProperties.getPort();
	}

	public void setPort(int port) {
		if (nacosDiscoveryProperties.getPort() < 0) {
			this.nacosDiscoveryProperties.setPort(port);
		}
	}

	@Override
	public boolean isSecure() {
		return nacosDiscoveryProperties.isSecure();
	}

	@Override
	public URI getUri() {
		return DefaultServiceInstance.getUri(this);
	}

	@Override
	public Map<String, String> getMetadata() {
		return nacosDiscoveryProperties.getMetadata();
	}

	public boolean isRegisterEnabled() {
		return nacosDiscoveryProperties.isRegisterEnabled();
	}

	public String getCluster() {
		return nacosDiscoveryProperties.getClusterName();
	}

	public float getRegisterWeight() {
		return nacosDiscoveryProperties.getWeight();
	}

	public NacosDiscoveryProperties getNacosDiscoveryProperties() {
		return nacosDiscoveryProperties;
	}

	public NamingService getNacosNamingService() {
		return nacosNamingService;
	}

	public void setNacosNamingService(NamingService nacosNamingService) {
		this.nacosNamingService = nacosNamingService;
	}

	public void setNacosDiscoveryProperties(
		NacosDiscoveryProperties nacosDiscoveryProperties) {
		this.nacosDiscoveryProperties = nacosDiscoveryProperties;
	}

	@Override
	public String toString() {
		return "NacosRegistration{" +
			"nacosDiscoveryProperties=" + nacosDiscoveryProperties +
			", nacosNamingService=" + nacosNamingService +
			'}';
	}
}

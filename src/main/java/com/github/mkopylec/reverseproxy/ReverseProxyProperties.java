package com.github.mkopylec.reverseproxy;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@ConfigurationProperties("reverse-proxy")
public class ReverseProxyProperties {

	private int connectTimeout = 500;
	private int readTimeout = 2000;
	private int filterOrder = LOWEST_PRECEDENCE;
	private List<Mapping> mappings;

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public int getFilterOrder() {
		return filterOrder;
	}

	public void setFilterOrder(int filterOrder) {
		this.filterOrder = filterOrder;
	}

	public List<Mapping> getMappings() {
		return mappings;
	}

	public void setMappings(List<Mapping> mappings) {
		this.mappings = mappings;
	}

	public static class Mapping {

		private String path;
		private List<String> hosts;
		private boolean stripPath = true;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public List<String> getHosts() {
			return hosts;
		}

		public void setHosts(List<String> hosts) {
			this.hosts = hosts;
		}

		public boolean isStripPath() {
			return stripPath;
		}

		public void setStripPath(boolean stripPath) {
			this.stripPath = stripPath;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this)
					.append("path", path)
					.append("hosts", hosts)
					.append("stripPath", stripPath)
					.toString();
		}
	}
}

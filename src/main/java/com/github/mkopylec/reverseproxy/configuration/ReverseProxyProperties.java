package com.github.mkopylec.reverseproxy.configuration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.apache.commons.lang3.builder.ToStringStyle.NO_CLASS_NAME_STYLE;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@ConfigurationProperties("reverse-proxy")
public class ReverseProxyProperties {

	private int filterOrder = LOWEST_PRECEDENCE;
	private Timeout timeout = new Timeout();
	private MappingsUpdate mappingsUpdate = new MappingsUpdate();
	private List<Mapping> mappings = new ArrayList<>();

	public int getFilterOrder() {
		return filterOrder;
	}

	public void setFilterOrder(int filterOrder) {
		this.filterOrder = filterOrder;
	}

	public Timeout getTimeout() {
		return timeout;
	}

	public void setTimeout(Timeout timeout) {
		this.timeout = timeout;
	}

	public MappingsUpdate getMappingsUpdate() {
		return mappingsUpdate;
	}

	public void setMappingsUpdate(MappingsUpdate mappingsUpdate) {
		this.mappingsUpdate = mappingsUpdate;
	}

	public List<Mapping> getMappings() {
		return mappings;
	}

	public void setMappings(List<Mapping> mappings) {
		this.mappings = mappings;
	}

	public static class Timeout {

		private int connect = 500;
		private int read = 2000;

		public int getConnect() {
			return connect;
		}

		public void setConnect(int connect) {
			this.connect = connect;
		}

		public int getRead() {
			return read;
		}

		public void setRead(int read) {
			this.read = read;
		}
	}

	public static class MappingsUpdate {

		private boolean enabled = true;
		private boolean onNetworkError = true;
		private int intervalInMillis = 10000;

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public boolean isOnNetworkError() {
			return onNetworkError;
		}

		public void setOnNetworkError(boolean onNetworkError) {
			this.onNetworkError = onNetworkError;
		}

		public int getIntervalInMillis() {
			return intervalInMillis;
		}

		public void setIntervalInMillis(int intervalInMillis) {
			this.intervalInMillis = intervalInMillis;
		}
	}

	public static class Mapping {

		private String path;
		private List<String> destinations = new ArrayList<>();
		private boolean stripPath = true;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public List<String> getDestinations() {
			return destinations;
		}

		public void setDestinations(List<String> destinations) {
			this.destinations = destinations;
		}

		public boolean isStripPath() {
			return stripPath;
		}

		public void setStripPath(boolean stripPath) {
			this.stripPath = stripPath;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, NO_CLASS_NAME_STYLE)
					.append("path", path)
					.append("destinations", destinations)
					.append("stripPath", stripPath)
					.toString();
		}
	}
}

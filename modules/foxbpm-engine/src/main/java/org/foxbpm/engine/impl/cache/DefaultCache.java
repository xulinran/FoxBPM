/**
 * Copyright 1996-2014 FoxBPM ORG.
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
 * 
 * @author kenshin
 * @author ych
 */
package org.foxbpm.engine.impl.cache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.foxbpm.engine.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultCache<T> implements Cache<T> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultCache.class);

	protected Map<String, T> cache;

	/** Cache with no limit */
	public DefaultCache() {
		this.cache = new HashMap<String, T>();
	}

	/**
	 * Cache which has a hard limit: no more elements will be cached than the
	 * limit.
	 */
	public DefaultCache(final int limit) {
	    this.cache = new LinkedHashMap<String, T>(limit + 1, 0.75f, true) {
	    	// +1 is needed, because the entry is inserted first, before it is removed
	        // 0.75 is the default (see javadocs)
	        // true will keep the 'access-order', which is needed to have a real LRU cache
			private static final long serialVersionUID = 1L;

			protected boolean removeEldestEntry(java.util.Map.Entry<String, T> eldest) {
				boolean removeEldest = size() > limit;
				if (removeEldest) {
					logger.trace("Cache limit is reached, {} will be evicted", eldest.getKey());
				}
				return removeEldest;
			}

		};
	}

	public T get(String id) {
		return cache.get(id);
	}

	public void add(String id, T obj) {
		cache.put(id, obj);
	}

	public void remove(String id) {
		cache.remove(id);
	}

	public void clear() {
		cache.clear();
	}

	// For testing purposes only
	public int size() {
		return cache.size();
	}
}

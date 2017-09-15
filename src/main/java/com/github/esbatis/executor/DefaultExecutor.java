/**
 * Copyright 2009-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.esbatis.executor;

import com.github.esbatis.mapper.MapperFactory;
import com.github.esbatis.client.RestClient;
import com.github.esbatis.handler.ResultHandler;
import com.github.esbatis.proxy.MapperMethod;
import com.github.esbatis.mapper.*;
import com.github.esbatis.handler.DefaultResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 *
 * @author jinzhong.zhang
 */
public class DefaultExecutor implements Executor {

    private static final Logger logger = LoggerFactory.getLogger(DefaultExecutor.class);

    private final MapperFactory mapperFactory;
    private final RestClient restClient;

    public DefaultExecutor(MapperFactory mapperFactory) {
        this.mapperFactory = mapperFactory;
        this.restClient = new RestClient(mapperFactory.getHttpHosts());
    }

    @Override
    public <T> T execute(MappedStatement ms, Object[] args) {
        MapperMethod mapperMethod = ms.getMapperMethod();
        Map<String, Object> parameterMap = mapperMethod.convertArgsToParam(args);

        String httpUrl = ms.renderHttpUrl(parameterMap);
        String httpBody = ms.renderHttpBody(parameterMap);

        String resp;
        executeBefore(ms, parameterMap);
        try {
            resp = restClient.send(httpUrl, ms.getHttpMethod(), httpBody);
        } catch (Exception e) {
            executeException(ms, parameterMap, e);
            throw e;
        }
        executeAfter(ms, parameterMap, resp);

        ResultHandler<?> handler = mapperMethod.getResultHandler();
        if (handler == null) {
            handler = new DefaultResultHandler(ms);
        } else {
            logger.debug("ResultHandler[{}] used for statement[{}].", handler.getClass(), ms.getStatement());
        }

        return (T) handler.handleResult(resp);
    }

    private void executeBefore(MappedStatement ms, Map<String, Object> parameterMap) {
        List<ExecutorFilter> executorFilters = mapperFactory.getExecutorFilters();
        for (ExecutorFilter filter : executorFilters) {
            filter.before(ms, parameterMap);
        }
    }

    private void executeAfter(MappedStatement ms, Map<String, Object> parameterMap, String result) {
        List<ExecutorFilter> executorFilters = mapperFactory.getExecutorFilters();
        for (ExecutorFilter filter : executorFilters) {
            filter.after(ms, parameterMap, result);
        }
    }

    private void executeException(MappedStatement ms, Map<String, Object> parameterMap, Exception e) {
        List<ExecutorFilter> executorFilters = mapperFactory.getExecutorFilters();
        for (ExecutorFilter filter : executorFilters) {
            filter.exception(ms, parameterMap, e);
        }
    }
}

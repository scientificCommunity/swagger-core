package io.swagger.v3.jaxrs2;

import java.util.Collection;

/**
 * @author: tk (soulmate.tangk at gmail dot com)
 * @date: 2021/3/23
 */
public interface ReaderConfiguration {
    /**
     * ignore some param in class which read by {@link Reader#read(Class)}
     *
     * @return be ignored class
     */
    Collection<Class<?>> getIgnoreParamTypes();
}

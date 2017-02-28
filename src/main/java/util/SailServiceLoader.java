package util;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.ServiceLoader;

/**
 * Created by lenovo on 2017/2/24.
 */
public class SailServiceLoader {

    public static <S> S loadFirst(Class<S> serviceClass){return ServiceLoader.load(serviceClass).iterator().next();}

    public static <S> List<S> loadAll(Class<S> serviceClass){
        return Lists.newArrayList(ServiceLoader.load(serviceClass).iterator());

    }



}

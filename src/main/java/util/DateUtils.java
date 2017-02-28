package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by lenovo on 2017/1/12.
 * 如果是 JDK8 的应用，可以使用 instant 代替 Date，Localdatetime 代替 Calendar，
 Datetimeformatter 代替 Simpledateformatter，官方给出的解释：simple beautiful strong
 immutable thread-safe。
 *
 *
 *
 */
public class DateUtils {

    private static final ThreadLocal<DateFormat> df=new ThreadLocal<DateFormat>(){

        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

}

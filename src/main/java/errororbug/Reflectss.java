package errororbug;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by lenovo on 2017/3/17.
 */
public class Reflectss {

    public static void main(String args[]){

        try {
            Constructor co=DemoTT.class.getDeclaredConstructor();
            co.setAccessible(true);
            co.newInstance();
           DemoTT.class.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }


}

class DemoTT{
    private DemoTT() {
    }
}

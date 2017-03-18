package proto.future;

import java.util.Arrays;

/**
 * Created by lenovo on 2017/3/6.
 */
public class DefaultListeners<V> {

    private SailListener<V>[] listeners;
    private int size;

    static <T> DefaultListeners<T> with(SailListener<T> first,SailListener<T> second){
        return new DefaultListeners<T>(first,second);
    }

    @SuppressWarnings("unchecked")
    private DefaultListeners(SailListener<V>first,SailListener<V>second){
        listeners=new SailListener[2];
        listeners[0]=first;
        listeners[1]=second;
        size=2;
    }
    public void add(SailListener<V> listener){
        SailListener<V>[] listeners=this.listeners;
        final int size=this.size;
        if(size==listeners.length){
            this.listeners=listeners= Arrays.copyOf(listeners,size<<1);
        }
        listeners[size]=listener;
        this.size=size+1;
    }

    public void remove(SailListener<V> listener){
        final SailListener<V>[] listeners=this.listeners;
        int size=this.size;
        for(int i=0;i<size;i++){
            if(listeners[i]==listener){
                int length=size-i-1;
                if(length>0){
                    System.arraycopy(listeners,i+1,listeners,i,length);
                }
            }
        }
    }

    public SailListener<V>[] listeners(){
        return listeners;
    }
    public int size(){
        return size;
    }

}

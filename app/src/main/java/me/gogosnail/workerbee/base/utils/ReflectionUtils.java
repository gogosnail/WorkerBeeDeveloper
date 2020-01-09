package me.gogosnail.workerbee.base.utils;

import android.view.ViewTreeObserver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by max on 2019/3/9.<br/>
 */
public class ReflectionUtils {
    public static void removeOnComputeInternalInsetsListener(ViewTreeObserver viewTree) {
        if(viewTree == null) {
            return;
        }
        try {
            Class<?> clazz = Class.forName("android.view.ViewTreeObserver");
            Field field = viewTree.getClass().getDeclaredField("mOnComputeInternalInsetsListeners");
            field.setAccessible(true);
            Object listenerList = field.get(viewTree);
            Method method = listenerList.getClass().getDeclaredMethod("getArray");
            method.setAccessible(true);
            ArrayList<Object> arraylist = (ArrayList<Object>) method.invoke(listenerList);
            Class<?> classes[] = new Class[1];
            classes[0] = Class.forName("android.view.ViewTreeObserver$OnComputeInternalInsetsListener");
            if (arraylist != null && arraylist.size() > 0) {
                clazz.getDeclaredMethod("removeOnComputeInternalInsetsListener", classes).invoke(viewTree,
                        arraylist.get(0));
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public static void addOnComputeInternalInsetsListener(ViewTreeObserver viewTree, Object object) {
        if(viewTree == null) {
            return;
        }
        try {
            Class<?> clazz = Class.forName("android.view.ViewTreeObserver");
            Class<?> classes[] = new Class[1];
            classes[0] = Class.forName("android.view.ViewTreeObserver$OnComputeInternalInsetsListener");
            clazz.getDeclaredMethod("addOnComputeInternalInsetsListener", classes).invoke(viewTree,
                    object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

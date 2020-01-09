package me.gogosnail.workerbee.base.widget;

import android.graphics.Region;
import android.inputmethodservice.InputMethodService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by max on 2018/9/16.<br/>
 */
public class OnComputeInternalInsetsListener implements InvocationHandler {

    private Region touchRegion = null;
    private int touchableInsets = InputMethodService.Insets.TOUCHABLE_INSETS_FRAME;

    public Object bind() {
        Object target = null;
        try {
            Class class1 = Class.forName("android.view.ViewTreeObserver$OnComputeInternalInsetsListener");
            target = Proxy.newProxyInstance(OnComputeInternalInsetsListener.class.getClassLoader(),
                    new Class[]{class1}, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }

    public Region getTouchRegion() {
        return touchRegion;
    }

    public void setTouchRegion(Region touchRegion) {
        this.touchRegion = touchRegion;
    }

    public void setTouchableInsets(int insets) {
        touchableInsets = insets;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            Field touchableRegionField = args[0].getClass()
                    .getDeclaredField("touchableRegion");
            touchableRegionField.setAccessible(true);
            Field touchableInsetsField = args[0].getClass()
                    .getDeclaredField("mTouchableInsets");
            touchableInsetsField.setAccessible(true);
            if (touchableInsets == InputMethodService.Insets.TOUCHABLE_INSETS_REGION && touchRegion != null) {
                Region region = (Region) touchableRegionField.get(args[0]);
                region.set(touchRegion);
                touchableInsetsField.set(args[0], InputMethodService.Insets.TOUCHABLE_INSETS_REGION);
            } else {
                touchableInsetsField.set(args[0], InputMethodService.Insets.TOUCHABLE_INSETS_FRAME);
            }
        } catch (Exception e) {

        }
        return null;
    }
}

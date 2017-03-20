package com.example.nerd.rx_retrofit_okhttp.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.nerd.rx_retrofit_okhttp.utils.DensityUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * BaseActivity中统一处理 Sub
 * Created by nerd on 2017/3/1.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder mUnbinder;

    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//此FLAG可使状态栏透明，且当前视图在绘制时，从屏幕顶端开始即top = 0开始绘制，这也是实现沉浸效果的基础
            //this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//可不加
        }
        setContentView(getContentViewId());
        mUnbinder = ButterKnife.bind(this);
        if (mCompositeSubscription == null || mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription = new CompositeSubscription();
        }
        initSet();
    }

    /**
     * add subscription
     *
     * @param subscription
     */
    public void addSubscription(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    public abstract int getContentViewId();

    protected abstract void initSet();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }
    public int getStatusBarHeight(Context context) {
        //方法1：
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return getResources().getDimensionPixelSize(resourceId);
        } else {
            return DensityUtil.dip2px(this, 22);
        }


        /*//方法2：通过反射来获取
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            return getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            //获取不到就来个通用值咯
            return DensityUtil.dip2px(this,22);
        }*/
    }
}

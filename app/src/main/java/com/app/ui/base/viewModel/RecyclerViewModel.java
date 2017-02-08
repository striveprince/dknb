package com.app.ui.base.viewModel;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.databinding.ObservableBoolean;
import android.databinding.adapters.ListenerUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.R;
import com.app.data.entity.BaseEntity;
import com.app.ui.base.adapter.recycler.RecyclerWrapper;

import rx.subscriptions.CompositeSubscription;

/**
 * project：cutv_ningbo
 * description：
 * create developer： admin
 * create time：9:08
 * modify developer：  admin
 * modify time：9:08
 * modify remark：VM module, business logic to deal with this module; 业务逻辑在这里处理
 *
 * @version 2.0
 */
@InverseBindingMethods({
    @InverseBindingMethod(
            type = SwipeRefreshLayout.class,
            attribute = "refreshing",
            event = "refreshingAttrChanged",
            method = "isRefreshing")
})
public class RecyclerViewModel<Entity extends BaseEntity>
{
    protected final CompositeSubscription compositeSubscription = new CompositeSubscription();
    public final ObservableBoolean loading = new ObservableBoolean(false);
    private RecyclerWrapper<Entity> adapter = new RecyclerWrapper<>();
    private boolean pageFlag = false;
//    public abstract Observable<InfoEntity<T>> httpApi(int offset);
//};

//    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = ()->onHttp(0);

//    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
//        return onRefreshListener;
//    }

//    public  RecyclerView.LayoutManager getManager(){
//        return new LinearLayoutManager(getContext());
//    };
//    protected boolean getPageFlag() {
//        return true;
//    }
//    public RecyclerView.OnScrollListener getOnScrollListener() {
//        return scrollListener;
//    }
//    protected void onScrollBottom() {}
//    public void onHttp(int offset) {
//        onHttp(offset, httpApi(offset));
//    }
//
//
//    private void onHttp(int offset, Observable<InfoEntity<T>> observable) {
//        loading.set(true);
//        if (observable == null) return;
//        RestfulSubscriber<T> subscriber = new RestfulSubscriber<>(getContext(),
//                t -> {
//                    if (offset == 0) adapter.clear();
//                    adapter.addRangItemList(getRespond().transform(t));
//                },
//                e -> Timber.e(e, "load failed"),this::onCompleted);
//        compositeSubscription.add(observable.compose(new RestfulTransformer<>()).subscribe(subscriber));
//    }

//    @CallSuper
//    protected void onCompleted(T t,Throwable e) {
//        loading.set(false);
//        getRespond().onCompleted(e, t, adapter.getRealItemCount());
//    }
//
//    @Override
//    public void detachView() {
//        super.detachView();
//        compositeSubscription.clear();
//    }

    public void setPageFlag(boolean pageFlag) {
        this.pageFlag = pageFlag;
    }

    private void onHttp(int offset){

    }

    public void onScrollBottom(){}

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        int lastVisibleItem = 0;
        private int dy = 0;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 >= adapter.getItemCount()
                    && !loading.get()) {
                if (pageFlag&&dy>0) onHttp(adapter.getRealItemCount());
                onScrollBottom();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            this.dy = dy;
        }
    };

    @BindingAdapter({"setAdapter"})
    public static void setAdapter(RecyclerView layout, RecyclerWrapper adapter) {
        layout.setAdapter(adapter);
    }

    @BindingAdapter({"setScroll"})
    public static void setOnScroll(RecyclerView view, RecyclerView.OnScrollListener listener) {
        view.addOnScrollListener(listener);
    }

    @BindingAdapter({"refreshing"})
    public static void setRefreshing(SwipeRefreshLayout view, boolean refreshing) {
        if (refreshing != view.isRefreshing()) {
            view.setRefreshing(refreshing);
        }
    }

    @BindingAdapter(value = {"onRefreshListener", "refreshingAttrChanged"}, requireAll = false)
    public static void setOnRefreshListener(SwipeRefreshLayout view,SwipeRefreshLayout.OnRefreshListener listener,InverseBindingListener refreshingAttrChanged) {
        SwipeRefreshLayout.OnRefreshListener newValue = () -> {
            if (refreshingAttrChanged != null) refreshingAttrChanged.onChange();
            if (listener != null) listener.onRefresh();
        };
        SwipeRefreshLayout.OnRefreshListener oldValue = ListenerUtil.trackListener(view, newValue, R.id.swipe_refresh_layout);
        if (oldValue != null) view.setOnRefreshListener(null);
        view.setOnRefreshListener(newValue);
        view.setColorSchemeResources(
                R.color.swipe_color1,
                R.color.swipe_color2,
                R.color.swipe_color3,
                R.color.swipe_color4);
    }
}

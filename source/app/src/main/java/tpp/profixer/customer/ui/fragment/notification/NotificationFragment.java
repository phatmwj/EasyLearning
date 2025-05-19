package tpp.profixer.customer.ui.fragment.notification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import tpp.profixer.customer.BR;
import tpp.profixer.customer.R;
import tpp.profixer.customer.data.model.api.response.Course;
import tpp.profixer.customer.data.model.api.response.Notification;
import tpp.profixer.customer.databinding.FragmentNotificationBinding;
import tpp.profixer.customer.di.component.FragmentComponent;
import tpp.profixer.customer.ui.base.fragment.BaseFragment;
import tpp.profixer.customer.ui.fragment.notification.adpater.NotificationAdapter;
import tpp.profixer.customer.ui.fragment.study.adpater.MyCourseAdapter;
import tpp.profixer.customer.ui.lesson.LessonActivity;

public class NotificationFragment extends BaseFragment<FragmentNotificationBinding, NotificationFragmentViewModel>{

    NotificationAdapter notificationAdapter;

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_notification;
    }

    @Override
    protected void performDataBinding() {

    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLayoutNotification();
        viewModel.notification.observe(this, courses -> {
            notificationAdapter.setData(courses);
        });
        viewModel.getNotification();
    }

    private void setLayoutNotification(){
        notificationAdapter = new NotificationAdapter(getContext(), new ArrayList<>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rcNotification.setLayoutManager(linearLayoutManager);
        binding.rcNotification.setAdapter(notificationAdapter);
        notificationAdapter.setListener(new NotificationAdapter.NotificationListener() {
            @Override
            public void onNotificationClick(Notification Notification) {

            }
        });
    }

    public void setState(Integer state){
        viewModel.state.set(state);
        viewModel.getNotification();
    }

    public void setStateNull(){
        setState(null);
    }

}

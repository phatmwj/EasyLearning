package tpp.easy.learning.ui.fragment.notification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import tpp.easy.learning.BR;
import tpp.easy.learning.R;
import tpp.easy.learning.data.model.api.response.Course;
import tpp.easy.learning.data.model.api.response.Notification;
import tpp.easy.learning.databinding.FragmentNotificationBinding;
import tpp.easy.learning.di.component.FragmentComponent;
import tpp.easy.learning.ui.base.fragment.BaseFragment;
import tpp.easy.learning.ui.dialog.ConfirmDialog;
import tpp.easy.learning.ui.fragment.notification.adpater.NotificationAdapter;
import tpp.easy.learning.ui.fragment.study.adpater.MyCourseAdapter;
import tpp.easy.learning.ui.lesson.LessonActivity;

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

    public void confirmDeleteAll(){
        ConfirmDialog confirmDialog = new ConfirmDialog(getContext());
        confirmDialog.title.set("Xóa tất cả thông báo?");
        confirmDialog.titleRightButton.set("Xóa");
        confirmDialog.setListener(new ConfirmDialog.ConfirmListener() {
            @Override
            public void confirm() {
                confirmDialog.dismiss();
                viewModel.deleteAll();
            }
        });
        confirmDialog.show();
    }

    public void setState(Integer state){
        viewModel.state.set(state);
        viewModel.getNotification();
    }

    public void setStateNull(){
        setState(null);
    }

}

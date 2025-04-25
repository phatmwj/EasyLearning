package tpp.profixer.customer.ui.cart;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import tpp.profixer.customer.R;
import tpp.profixer.customer.data.model.api.response.CartItem;
import tpp.profixer.customer.data.model.api.response.Course;
import tpp.profixer.customer.databinding.ActivityCartBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;
import tpp.profixer.customer.ui.cart.adapter.CartAdapter;
import tpp.profixer.customer.ui.course.CourseActivity;
import tpp.profixer.customer.ui.dialog.ConfirmDialog;
import tpp.profixer.customer.ui.fragment.home.adapter.CourseAdapter;
import tpp.profixer.customer.ui.login.LoginActivity;

public class CartActivity extends BaseActivity<ActivityCartBinding, CartViewModel> {
    private CartAdapter cartAdapter;
    private CourseAdapter courseAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_cart;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutCart();
        setLayoutRelatedCourses();
        viewModel.cartInfo.observe(this, cartInfo -> {
            if (cartInfo != null) {
                viewModel.cartInfoF.set(cartInfo);
                cartAdapter.setData(cartInfo.getContent().getCartItems());
                if(cartInfo.getContent().getCartItems() != null){
                    List<Long> categoryIds = new ArrayList<>();
                    List<Long> courseIds = new ArrayList<>();
                    int price = 0;
                    int oldPrice = 0;
                    for(CartItem cartItem : cartInfo.getContent().getCartItems()){
                        courseIds.add(cartItem.getCourse().getId());
                        price = price + cartItem.getCourse().getMoney();
                        oldPrice = oldPrice + cartItem.getCourse().getPrice();
                    }
                    viewModel.price.set(price);
                    viewModel.oldPrice.set(oldPrice);
                    categoryIds.add(7050199255187456L);
                    viewModel.getRelatedCourses(courseIds, categoryIds);
                }
            }
        });

        viewModel.relatedCourses.observe(this, relatedCourses -> {
            courseAdapter.setData(relatedCourses);
        });
        viewModel.getCart();
    }

    private void setLayoutCart(){
        cartAdapter = new CartAdapter(this, new ArrayList<>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        viewBinding.rvCart.setLayoutManager(linearLayoutManager);
        viewBinding.rvCart.setAdapter(cartAdapter);
        cartAdapter.setListener(new CartAdapter.CartListener() {
            @Override
            public void onItemClick(CartItem cartItem) {
                Intent it = new Intent(getApplicationContext(), CourseActivity.class);
                it.putExtra("course_id", cartItem.getCourse().getId());
//                it.putExtra("category_id", viewModel.categoryId);
                startActivity(it);
            }

            @Override
            public void onItemDelete(CartItem cartItem) {
                confirmDelete(cartItem.getId());
            }
        });
    }

    private void setLayoutRelatedCourses(){
        courseAdapter = new CourseAdapter(this, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        viewBinding.rvRelatedCourse.setLayoutManager(layoutManager);
        viewBinding.rvRelatedCourse.setAdapter(courseAdapter);
        courseAdapter.setListener(new CourseAdapter.CourseListener() {
            @Override
            public void onCourseClick(Course course) {
                Intent it = new Intent(getApplicationContext(), CourseActivity.class);
                it.putExtra("course_id", course.getId());
//                it.putExtra("category_id", viewModel.categoryId);
                startActivity(it);
            }
        });
    }

    public void confirmDelete(Long cartItemId){
        ConfirmDialog confirmDialog = new ConfirmDialog(this);
        confirmDialog.title.set("Bạn muốn xóa khóa học ra khỏi giỏ hàng?");
        confirmDialog.titleRightButton.set("Xóa");
        confirmDialog.setListener(new ConfirmDialog.ConfirmListener() {
            @Override
            public void confirm() {
                confirmDialog.dismiss();
                viewModel.deleteCartItem(cartItemId);
            }
        });
        confirmDialog.show();
    }

    public void confirmDeleteAll(){
        ConfirmDialog confirmDialog = new ConfirmDialog(this);
        confirmDialog.title.set("Bạn muốn xóa tất cả khóa học?");
        confirmDialog.titleRightButton.set("Xóa");
        confirmDialog.setListener(new ConfirmDialog.ConfirmListener() {
            @Override
            public void confirm() {
                confirmDialog.dismiss();
                viewModel.deleteAllCartItem();
            }
        });
        confirmDialog.show();
    }
}

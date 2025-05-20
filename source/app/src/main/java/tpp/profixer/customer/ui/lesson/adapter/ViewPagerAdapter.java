package tpp.profixer.customer.ui.lesson.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import tpp.profixer.customer.ui.lesson.fragment.ContentFragment;
import tpp.profixer.customer.ui.lesson.fragment.IntroduceFragment;
import tpp.profixer.customer.ui.lesson.fragment.ReviewFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private Long courseId;
    private Long expertId;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, Long courseId, Long expertId) {
        super(fragmentActivity);
        this.courseId = courseId;
        this.expertId = expertId;
    }

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new IntroduceFragment(courseId);
        }
        else if (position == 1)
        {
            fragment = new ContentFragment(courseId);
        }
        else if (position == 2)
        {
            fragment = new ReviewFragment(courseId, expertId);
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

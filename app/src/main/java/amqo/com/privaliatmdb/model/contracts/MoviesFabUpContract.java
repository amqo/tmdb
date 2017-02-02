package amqo.com.privaliatmdb.model.contracts;

import android.support.design.widget.FloatingActionButton;

public interface MoviesFabUpContract {

    interface View {

        void scrollUp();
    }

    interface Presenter {

        boolean isUpFABVisible();

        void setShownUpFAB(boolean show);

        void setUpFab(FloatingActionButton upFab);
    }
}

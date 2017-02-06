package amqo.com.privaliatmdb.model.contracts;

import android.support.design.widget.FloatingActionButton;

public interface MoviesFabUpContract {

    interface Presenter {

        void scrollUp();
    }

    interface View {

        boolean isUpFABVisible();

        void setShownUpFAB(boolean show);

        void setUpFab(FloatingActionButton upFab);
    }
}

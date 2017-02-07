package amqo.com.privaliatmdb.views.utils;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import amqo.com.privaliatmdb.R;
import amqo.com.privaliatmdb.model.MoviesContext;
import amqo.com.privaliatmdb.model.contracts.MoviesContract;

public abstract class NotificationsHelper {

    public static Snackbar showSnackConnectivity(final MoviesContract.View moviesView) {
        if (moviesView == null || moviesView.getMoviesContext() == null) return null;

        final MoviesContext moviesContext = moviesView.getMoviesContext();
        if (moviesContext.view == null) return null;

        String message = moviesContext.context.getString(R.string.Toast_Connection_Off);
        int color = Color.WHITE;

        Snackbar snackbar = Snackbar.make(moviesContext.view, message, Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction(moviesContext.context.getString(R.string.Notification_Action_Connect)
                .toUpperCase(Locale.getDefault()), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSettings = new Intent(android.provider.Settings.ACTION_SETTINGS);
                intentSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                moviesContext.context.startActivity(intentSettings);
            }
        });

        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(
                android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
        return snackbar;
    }
}

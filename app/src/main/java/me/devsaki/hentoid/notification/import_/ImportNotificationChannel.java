package me.devsaki.hentoid.notification.import_;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;

import java.util.Objects;

import me.devsaki.hentoid.R;

public class ImportNotificationChannel {

    private ImportNotificationChannel() {
        throw new IllegalStateException("Utility class");
    }

    private static final String ID_OLD = "import";
    static final String ID = "import2";

    public static void init(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = context.getResources().getString(R.string.notification_library_imports);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(ID, name, importance);
            channel.setSound(null, null);
            channel.setVibrationPattern(null);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            // Mandatory; it is not possible to change the sound of an existing channel after its initial creation
            Objects.requireNonNull(notificationManager, "notificationManager must not be null");
            notificationManager.deleteNotificationChannel(ID_OLD);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

package me.devsaki.hentoid.parsers.images;

import android.webkit.URLUtil;

import androidx.annotation.NonNull;

import com.annimon.stream.Optional;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Map;

import me.devsaki.hentoid.database.domains.Content;
import me.devsaki.hentoid.database.domains.ImageFile;
import me.devsaki.hentoid.enums.StatusContent;
import me.devsaki.hentoid.events.DownloadEvent;
import me.devsaki.hentoid.parsers.ParseHelper;
import timber.log.Timber;

public abstract class BaseImageListParser implements ImageListParser {

    private final ParseProgress progress = new ParseProgress();
    protected boolean processHalted = false;

    protected abstract List<String> parseImages(@NonNull Content content) throws Exception;

    public List<ImageFile> parseImageList(@NonNull Content content) throws Exception {
        String readerUrl = content.getReaderUrl();

        if (!URLUtil.isValidUrl(readerUrl))
            throw new IllegalArgumentException("Invalid gallery URL : " + readerUrl);

        Timber.d("Gallery URL: %s", readerUrl);

        EventBus.getDefault().register(this);

        List<ImageFile> result;
        try {
            List<String> imgUrls = parseImages(content);
            result = ParseHelper.urlsToImageFiles(imgUrls, content.getCoverImageUrl(), StatusContent.SAVED);
        } finally {
            EventBus.getDefault().unregister(this);
        }

        Timber.d("%s", result);

        return result;
    }

    public Optional<ImageFile> parseBackupUrl(@NonNull String url, @NonNull Map<String, String> requestHeaders, int order, int maxPages) {
        return Optional.of(new ImageFile(order, url, StatusContent.SAVED, maxPages));
    }

    void progressStart(@NonNull final String url, int maxSteps) {
        progress.start(url, maxSteps);
    }

    void progressPlus() {
        progress.advance();
    }

    void progressComplete() {
        progress.complete();
    }

    /**
     * Download event handler called by the event bus
     *
     * @param event Download event
     */
    @Subscribe
    public void onDownloadEvent(DownloadEvent event) {
        switch (event.eventType) {
            case DownloadEvent.EV_PAUSE:
            case DownloadEvent.EV_CANCEL:
            case DownloadEvent.EV_SKIP:
                processHalted = true;
                break;
            default:
                // Other events aren't handled here
        }
    }
}

package me.devsaki.hentoid.activities.sources;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.devsaki.hentoid.enums.Site;
import me.devsaki.hentoid.util.network.HttpHelper;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;

/**
 * Created by Shiro on 1/20/2016.
 * Implements Hitomi.la source
 */
public class HitomiActivity extends BaseWebActivity {

    private static final String DOMAIN_FILTER = "hitomi.la";
    private static final String[] GALLERY_FILTER = {"//hitomi.la/[\\w%\\-]+/[^/]+-[0-9]{2,}.html$"};
    private static final String[] RESULTS_FILTER = {"//hitomi.la[/]{0,1}$", "//hitomi.la[/]{0,1}\\?", "//hitomi.la/search.html", "//hitomi.la/index-[\\w%\\-\\.\\?]+", "//hitomi.la/(series|artist|tag|character)/[\\w%\\-\\.\\?]+"};
    private static final String[] BLOCKED_CONTENT = {"hitomi-horizontal.js", "hitomi-vertical.js", "invoke.js", "ion.sound"};
    private static final String[] JS_WHITELIST = {"galleries/[\\w%\\-]+.js$", "jquery", "filesaver", "common", "date", "download", "gallery", "jquery", "cookie", "jszip", "limitlists", "moment-with-locales", "moveimage", "pagination", "search", "searchlib", "yall", "reader", "decode_webp", "bootstrap"};
    private static final String[] BLOCKED_JS_CONTENTS = {"exoloader", "popunder"};

    private static final List<Pattern> whitelistUrlPattern = new ArrayList<>();
    private static final List<String> jsBlacklistCache = new ArrayList<>();

    static {
        for (String s : JS_WHITELIST) whitelistUrlPattern.add(Pattern.compile(s));
    }

    Site getStartSite() {
        return Site.HITOMI;
    }

    @Override
    protected CustomWebViewClient getWebClient() {
        addContentBlockFilter(BLOCKED_CONTENT);
        CustomWebViewClient client = new CustomWebViewClient(GALLERY_FILTER, this);
        client.restrictTo(DOMAIN_FILTER);
        client.setResultsUrlPatterns(RESULTS_FILTER);
        client.setResultUrlRewriter(this::rewriteResultsUrl);
        return client;
    }

    private String rewriteResultsUrl(@NonNull Uri resultsUri, int page) {
        Uri.Builder builder = resultsUri.buildUpon();

        if (resultsUri.toString().contains("search"))
            builder.fragment(page + ""); // https://hitomi.la/search.html?<searchTerm>#<page>
        else {
            Map<String, String> params = HttpHelper.extractParameters(resultsUri);
            params.put("page", page + "");

            builder.clearQuery();
            for (Map.Entry<String, String> param : params.entrySet())
                builder.appendQueryParameter(param.getKey(), param.getValue());
        }

        return builder.toString();
    }

    /**
     * Specific implementation to get rid of Hitomi's ad js files
     * that have random names
     */
    @Override
    protected boolean isUrlForbidden(@NonNull String url) {
        // 1- Process usual blacklist and cached dynamic blacklist
        if (super.isUrlForbidden(url)) return true;
        if (jsBlacklistCache.contains(url)) return true;

        // 2- Accept non-JS files
        if (!url.toLowerCase().endsWith(".js")) return false;

        // 3- Accept JS files defined in the whitelist
        for (Pattern p : whitelistUrlPattern) {
            Matcher matcher = p.matcher(url.toLowerCase());
            if (matcher.find()) return false;
        }

        // 4- For the others (gray list), block them if they _contain_ keywords
        Timber.d(">> examining grey file %s", url);
        try {
            Response response = HttpHelper.getOnlineResource(url, null, getStartSite().useMobileAgent(), getStartSite().useHentoidAgent());
            ResponseBody body = response.body();
            if (null == body) throw new IOException("Empty body");

            String jsBody = body.string().toLowerCase();
            for (String s : BLOCKED_JS_CONTENTS)
                if (jsBody.contains(s)) {
                    jsBlacklistCache.add(url);
                    return true;
                }
        } catch (IOException e) {
            Timber.e(e);
        }

        // Accept non-blocked grey JS files
        return false;
    }
}

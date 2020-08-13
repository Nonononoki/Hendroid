package me.devsaki.hentoid.enums;

import io.objectbox.converter.PropertyConverter;
import me.devsaki.hentoid.R;
import timber.log.Timber;

/**
 * Created by neko on 20/06/2015.
 * Site enumerator
 */
public enum Site {

    // NOTE : to maintain compatiblity with saved JSON files and prefs, do _not_ edit either existing names or codes
    //FAKKU(0, "Fakku", "https://www.fakku.net", "fakku", R.drawable.ic_menu_fakku, true, false, false), // Legacy support for old fakku archives
    PURURIN(1, "Pururin", "https://pururin.io", "pururin", R.drawable.ic_menu_pururin, true, false, false),
    HITOMI(2, "hitomi", "https://hitomi.la", "hitomi", R.drawable.ic_menu_hitomi, false, false, false),
    NHENTAI(3, "nhentai", "https://nhentai.net", "nhentai", R.drawable.ic_menu_nhentai, true, false, false),
    TSUMINO(4, "tsumino", "https://www.tsumino.com", "tsumino", R.drawable.ic_menu_tsumino, true, false, false),
    HENTAICAFE(5, "hentaicafe", "https://hentai.cafe", "hentai.cafe", R.drawable.ic_menu_hentaicafe, true, false, false),
    ASMHENTAI(6, "asmhentai", "https://asmhentai.com", "/asmhentai", R.drawable.ic_menu_asmhentai, true, false, false),
    ASMHENTAI_COMICS(7, "asmhentai comics", "https://comics.asmhentai.com", "comics.asmhentai", R.drawable.ic_menu_asmcomics, true, false, false),
    EHENTAI(8, "e-hentai", "https://e-hentai.org", "e-hentai", R.drawable.ic_menu_ehentai, true, false, true),
    //FAKKU2(9, "Fakku", "https://www.fakku.net", "fakku2", R.drawable.ic_menu_fakku, false, true, false),
    NEXUS(10, "Hentai Nexus", "https://hentainexus.com", "nexus", R.drawable.ic_menu_nexus, false, false, false),
    MUSES(11, "8Muses", "https://www.8muses.com", "8muses", R.drawable.ic_menu_8muses, false, false, false),
    DOUJINS(12, "doujins.com", "https://doujins.com/", "doujins", R.drawable.ic_menu_doujins, false, false, false),
    LUSCIOUS(13, "luscious.net", "https://members.luscious.net/manga/", "luscious", R.drawable.ic_menu_luscious, false, false, false),
    EXHENTAI(14, "exhentai", "https://exhentai.org", "exhentai", R.drawable.ic_menu_exhentai, false, false, true),
    PORNCOMIX(15, "porncomixonline", "https://www.porncomixonline.net/", "porncomixonline", R.drawable.ic_menu_porncomix, false, false, false),
    HBROWSE(16, "Hbrowse", "https://www.hbrowse.com/", "hbrowse", R.drawable.ic_menu_hbrowse, false, false, false),
    HENTAI2READ(17, "Hentai2Read", "https://hentai2read.com/", "h2r", R.drawable.ic_menu_hentai2read, false, false, false),
    HENTAIFOX(18, "Hentaifox", "https://hentaifox.com", "hentaifox", R.drawable.ic_menu_hentaifox, false, false, false),
    NONE(98, "none", "", "none", R.drawable.ic_external_library, true, false, false), // External library; fallback site
    PANDA(99, "panda", "https://www.mangapanda.com", "mangapanda", R.drawable.ic_menu_panda, true, false, false); // Safe-for-work/wife/gf option; not used anymore and kept here for retrocompatibility


    private final int code;
    private final String description;
    private final String url;
    private final String uniqueKeyword;
    private final int ico;
    private final boolean canKnowHentoidAgent;
    private final boolean hasImageProcessing;
    private final boolean hasBackupURLs;

    Site(int code,
         String description,
         String url,
         String uniqueKeyword,
         int ico,
         boolean canKnowHentoidAgent,
         boolean hasImageProcessing,
         boolean hasBackupURLs) {
        this.code = code;
        this.description = description;
        this.url = url;
        this.uniqueKeyword = uniqueKeyword;
        this.ico = ico;
        this.canKnowHentoidAgent = canKnowHentoidAgent;
        this.hasImageProcessing = hasImageProcessing;
        this.hasBackupURLs = hasBackupURLs;
    }

    public static Site searchByCode(long code) {
        for (Site s : values())
            if (s.getCode() == code) return s;

        return NONE;
    }

    // Same as ValueOf with a fallback to NONE
    // (vital for forward compatibility)
    public static Site searchByName(String name) {
        for (Site s : values())
            if (s.name().equalsIgnoreCase(name)) return s;

        return NONE;
    }

    public static Site searchByUrl(String url) {
        if (null == url || url.isEmpty()) {
            Timber.w("Invalid url");
            return null;
        }
        for (Site s : Site.values()) {
            if (url.contains(s.getUniqueKeyword()))
                return s;
        }
        return Site.NONE;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    private String getUniqueKeyword() {
        return uniqueKeyword;
    }

    public String getUrl() {
        return url;
    }

    public int getIco() {
        return ico;
    }


    public boolean canKnowHentoidAgent() {
        return canKnowHentoidAgent;
    }

    public boolean hasImageProcessing() {
        return hasImageProcessing;
    }

    public boolean hasBackupURLs() {
        return hasBackupURLs;
    }

    public String getFolder() {
            return description;
    }

    public static class SiteConverter implements PropertyConverter<Site, Long> {
        @Override
        public Site convertToEntityProperty(Long databaseValue) {
            if (databaseValue == null) {
                return Site.NONE;
            }
            for (Site site : Site.values()) {
                if (site.getCode() == databaseValue) {
                    return site;
                }
            }
            return Site.NONE;
        }

        @Override
        public Long convertToDatabaseValue(Site entityProperty) {
            return entityProperty == null ? null : (long) entityProperty.getCode();
        }
    }
}

package me.devsaki.hentoid.json.sources;

import androidx.annotation.NonNull;

import java.util.List;

import javax.annotation.Nonnull;

import me.devsaki.hentoid.database.domains.Attribute;
import me.devsaki.hentoid.database.domains.Content;
import me.devsaki.hentoid.enums.AttributeType;
import me.devsaki.hentoid.enums.Site;
import me.devsaki.hentoid.enums.StatusContent;
import me.devsaki.hentoid.util.AttributeMap;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class EHentaiGalleriesMetadata {
    private List<EHentaiGalleryMetadata> gmetadata;

    public Content toContent(@Nonnull String url, @NonNull Site site) {
        return (gmetadata != null && !gmetadata.isEmpty()) ? gmetadata.get(0).toContent(url, site) : new Content();
    }


    public static class EHentaiGalleryMetadata {

        private String gid;
        private String token;
        private String title;
        private String thumb;
        private String filecount;
        private List<String> tags;


        public Content toContent(@Nonnull String url, @NonNull Site site) {
            Content result = new Content();

            result.setSite(site);

            result.setUrl("/" + gid + "/" + token) // The rest will not be useful anyway because of temporary keys
                    .setCoverImageUrl(thumb)
                    .setTitle(title)
                    .setQtyPages(Integer.parseInt(filecount))
                    .setStatus(StatusContent.SAVED);

            AttributeMap attributes = new AttributeMap();
            String[] tagParts;
            AttributeType type;
            String name;

            for (String s : tags) {
                tagParts = s.split(":");
                if (1 == tagParts.length) {
                    type = AttributeType.TAG;
                    name = s;
                } else {
                    name = tagParts[1];
                    switch (tagParts[0]) {
                        case "parody":
                            type = AttributeType.SERIE;
                            break;
                        case "character":
                            type = AttributeType.CHARACTER;
                            break;
                        case "language":
                            type = AttributeType.LANGUAGE;
                            break;
                        case "artist":
                            type = AttributeType.ARTIST;
                            break;
                        default:
                            type = AttributeType.TAG;
                            name = s;
                            break;
                    }
                }

                attributes.add(new Attribute(type, name, type.name() + "/" + name, site));
            }
            result.addAttributes(attributes);

            return result;
        }
    }

}

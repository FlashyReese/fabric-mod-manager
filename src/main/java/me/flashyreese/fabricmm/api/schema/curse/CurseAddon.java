package me.flashyreese.fabricmm.api.schema.curse;

import java.util.List;

public class CurseAddon {
    private long id;
    private String name;
    private List<CurseAuthor> authors;
    private List<CurseAttachment> attachments;
    private String websiteUrl;
    private String summary;
    private float downloadCount;
    private String slug;
    private List<CurseFile> latestFiles;

    private List<CurseFile> files;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CurseAuthor> getAuthors() {
        return authors;
    }

    public List<CurseAttachment> getAttachments() {
        return attachments;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getSummary() {
        return summary;
    }

    public float getDownloadCount() {
        return downloadCount;
    }

    public String getSlug() {
        return slug;
    }

    public List<CurseFile> getFiles() {
        return files;
    }

    public void setFiles(List<CurseFile> files) {
        this.files = files;
    }

    public List<CurseFile> getLatestFiles() {
        return latestFiles;
    }

    public CurseAttachment getDefaultCurseAttachment() {
        if (getAttachments() != null) {
            for (CurseAttachment curseAttachment : getAttachments()) {
                if (curseAttachment.isDefault()) {
                    return curseAttachment;
                }
            }
        }
        return null;
    }
}

package me.flashyreese.fabricmm.schema;

import java.util.ArrayList;

public class CurseAddon {
    private long id;
    private String name;
    private ArrayList<CurseAuthor> authors;
    private ArrayList<CurseAttachment> attachments;
    private String websiteUrl;
    private String summary;
    private float downloadCount;
    private String slug;

    private ArrayList<CurseFile> files;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<CurseAuthor> getAuthors() {
        return authors;
    }

    public ArrayList<CurseAttachment> getAttachments() {
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

    public ArrayList<CurseFile> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<CurseFile> files) {
        this.files = files;
    }

    public CurseAttachment getDefaultCurseAttachment(){
        for (CurseAttachment curseAttachment: getAttachments()){
            if (curseAttachment.isDefault()){
                return curseAttachment;
            }
        }
        return null;
    }
}

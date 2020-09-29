package me.flashyreese.fabricmm.api.schema;

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

    public static class CurseAuthor {
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }

    public static class CurseAttachment {
        private boolean isDefault;
        private String thumbnailUrl;

        public boolean isDefault() {
            return isDefault;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }
    }

    public static class CurseFile {

        private String displayName;
        private String fileName;
        private String fileDate;
        private int fileLength;
        private String downloadUrl;
        private List<CurseDependency> dependencies;
        private List<String> gameVersion;
        private List<CurseModule> modules;
        private String gameVersionDateReleased;

        public String getDisplayName() {
            return displayName;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFileDate() {
            return fileDate;
        }

        public int getFileLength() {
            return fileLength;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public List<CurseDependency> getDependencies() {
            return dependencies;
        }

        public List<String> getGameVersion() {
            return gameVersion;
        }

        public List<CurseModule> getModules() {
            return modules;
        }

        public String getGameVersionDateReleased() {
            return gameVersionDateReleased;
        }

        public void removeFabricFromGameVersion() {
            getGameVersion().remove("Forge");
        }

        public boolean isFabricModFile() {
            for (CurseModule curseModule : getModules()) {
                if (curseModule.isFabricMod()) {
                    return true;
                }
            }
            return false;
        }

        public static class CurseDependency {
            private long addonId;
            private int type;// 1 = Embedded, 2 = Required, 3 = Optional
        }

        public static class CurseModule {
            private String foldername;

            public boolean isFabricMod() {
                return foldername.equals("fabric.mod.json");
            }
        }
    }
}

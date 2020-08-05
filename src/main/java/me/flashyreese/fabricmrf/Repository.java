package me.flashyreese.fabricmrf;

import me.flashyreese.fabricmrf.schema.repository.Author;
import me.flashyreese.common.util.URLUtil;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class Repository {
    private final String repositoryUrl;
    private ArrayList<Author> localAuthors;

    public Repository(String repositoryUrl){
        this.repositoryUrl = repositoryUrl;
    }

    public ArrayList<Author> getLocalAuthors(){
        return localAuthors;
    }

    public void setLocalAuthors(ArrayList<Author> localAuthors) {
        this.localAuthors = localAuthors;
    }

    public String getRepositoryName() throws MalformedURLException {
        return URLUtil.getFileName(this.repositoryUrl);
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }
}

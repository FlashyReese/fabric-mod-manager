package me.flashyreese.fabricmrf;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.flashyreese.fabricmrf.schema.repository.Author;
import me.flashyreese.fabricmrf.schema.repository.Mod;
import me.flashyreese.common.util.FileUtil;
import me.flashyreese.common.util.URLUtil;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RepositoryManager {
    private final File directory;
    private ArrayList<Repository> repositories;

    public RepositoryManager(File directory, ArrayList<String> repos) throws Exception {
        if(directory.isFile()){
            throw new Exception("Invalid directory, this is a file");
        }
        this.directory = directory;
        this.repositories = new ArrayList<Repository>();
        if(!directory.exists()){
            directory.mkdirs();
        }
        loadRepositories(repos);
    }

    public void addRepository(Repository repository) throws IOException {
        File repositoryDirectory = new File(directory, repository.getRepositoryName());
        if(repositoryDirectory.exists()){
            loadLocalRepository(repository);
        }else{
            createLocalRepository(repository);
        }
        repositories.add(repository);
    }

    private void createLocalRepository(Repository repository) throws IOException {
        File repositoryDirectory = new File(directory, repository.getRepositoryName());
        repositoryDirectory.mkdirs();
        updateLocalRepository(repository);
    }

    private void loadLocalRepository(Repository repository) throws MalformedURLException {
        File repositoryDirectory = new File(directory, repository.getRepositoryName());
        ArrayList<Author> authors = new ArrayList<Author>();
        for(File authorFile: Objects.requireNonNull(repositoryDirectory.listFiles())){
            if(authorFile.isFile()){
                Author author = getAuthorFromFile(authorFile);
                if(author != null){
                    authors.add(author);
                }
            }
        }
        repository.setLocalAuthors(authors);
    }

    public void updateLocalRepository(Repository repository) throws IOException {
        URL url = new URL(repository.getRepositoryUrl());
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String json = in.lines().collect(Collectors.joining());
        in.close();
        List<String> listOfAuthors = new Gson().fromJson(json, new TypeToken<List<String>>(){}.getType());
        for(String authorUrl: listOfAuthors){
            File authorFile = new File(directory.getAbsolutePath() + File.separator + repository.getRepositoryName(), URLUtil.getFileNameWithExtension(authorUrl));
            if(authorFile.exists()){
                if(!isAuthorUpToDate(authorUrl, authorFile)){
                    downloadAuthor(authorUrl, authorFile);
                }
            }else{
                downloadAuthor(authorUrl, authorFile);
            }
        }
        loadLocalRepository(repository);
    }

    public void loadRepositories(ArrayList<String> repositories) throws Exception {
        for(String url: repositories){
            addRepository(new Repository(url));
        }
    }
    public List<Mod> getModList() {
        List<Mod> modList = new ArrayList<Mod>();
        for(Repository repository: this.repositories){
            for(Author author:  repository.getLocalAuthors()){
                for(Mod mod: author.getMods()){
                    mod.setAuthor(author);
                    modList.add(mod);
                }
            }
        }
        return modList;
    }

    private void downloadAuthor(String authorUrl, File authorFile) throws IOException {
        URL url = new URL(authorUrl);
        BufferedReader in2 = new BufferedReader(new InputStreamReader(url.openStream()));
        String json = in2.lines().collect(Collectors.joining());
        Author author = new Gson().fromJson(json, Author.class);
        if (author != null){
            FileUtil.writeJson(authorFile, author);
        }
    }

    public boolean isAuthorUpToDate(String url, File author) throws MalformedURLException {
        return author.length() == URLUtil.getFileSize(new URL(url));
    }

    private Author getAuthorFromFile(File file){
        return FileUtil.readJson(file, new TypeToken<Author>(){}.getType());
    }

    public ArrayList<Repository> getRepositories() {
        return repositories;
    }

    public File getDirectory(){
        return directory;
    }
}

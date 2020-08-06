package me.flashyreese.fabricmm.util;

import me.flashyreese.fabricmm.schema.CurseAddon;
import me.flashyreese.fabricmrf.schema.repository.Author;
import me.flashyreese.fabricmrf.schema.repository.Mod;

import java.io.IOException;
import java.util.ArrayList;

public class CurseUtils {

    public static long[] projectIDs = new long[]{/*248787, 394468, 372124, 360438, 354231, 393442*/};
    public static ArrayList<Author> authors = new ArrayList<>();

    public static ArrayList<Author> getAuthors() throws IOException {
        for (long id: projectIDs){
            CurseAddon test = ModUtils.getCurseAddonFromProjectID(id, true);
            Author authorTest = ModUtils.convertCurseAddonToAuthor(test);
            if(containsAuthor(authorTest)){
                Author existingAuthor = getAuthor(authorTest.getName());
                for (Mod mod: authorTest.getMods()){
                    existingAuthor.getMods().add(mod);
                }
            }else{
                authors.add(authorTest);
            }
        }
        //System.out.println(new Gson().toJson(authors));
        return authors;
    }

    public static boolean containsAuthor(Author author){
        for (Author author1 : authors){
            if (author1.getName().equalsIgnoreCase(author.getName())){
                return true;
            }
        }
        return false;
    }

    public static Author getAuthor(String author){
        for (Author author1 : authors){
            if (author1.getName().equalsIgnoreCase(author)){
                return author1;
            }
        }
        return null;
    }
}
